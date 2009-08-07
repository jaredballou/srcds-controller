package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.Constants.DEFAULT_CONFIG_FILENAME;
import static de.eqc.srcds.configuration.Constants.HTTP_SERVER_PORT;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.impl.XmlPropertiesConfiguration;
import de.eqc.srcds.enums.OperatingSystem;
import de.eqc.srcds.exceptions.ConfigurationException;
import de.eqc.srcds.exceptions.UnsupportedOSException;
import de.eqc.srcds.handlers.RegisterHandlerByReflection;

/**
 * This class starts the srcds controller.
 * 
 * @author Holger Cremer
 */
public class CLI {

    private static Logger log = LogFactory.getLogger(CLI.class);

    private Configuration config;

    private HttpServer httpServer;

    private ServerController serverController;

    public void startup() throws Exception {
	this.checkOS();

	File configFile = new File(DEFAULT_CONFIG_FILENAME);

	/*
	 * This is for testing purposes only.
	 */
	configFile.delete();

	this.config = new XmlPropertiesConfiguration(configFile);
	this.serverController = new ServerController(this.config);

	startHttpServer();

	Runtime.getRuntime().addShutdownHook(
		new ShutdownHook(serverController, this.httpServer));
    }

    private void startHttpServer() throws IOException, ConfigurationException,
	    ClassNotFoundException, InstantiationException,
	    IllegalAccessException {
	int port = config.getValue(HTTP_SERVER_PORT, Integer.class);
	httpServer = HttpServer.create(new InetSocketAddress(port), 0);
	log.info(String.format("Bound to TCP port %d.", port));

	RegisterHandlerByReflection[] clazzes = getRegisterHandlerByReflectionClasses();
	DefaultAuthenticator defaultAuthenticator = new DefaultAuthenticator();
	for (RegisterHandlerByReflection clazzByReflection : clazzes) {
	    clazzByReflection.init(this.serverController, this.config);
	    HttpContext startContext = httpServer.createContext(
		    clazzByReflection.getPath(), clazzByReflection
			    .getHttpHandler());
	    startContext.setAuthenticator(defaultAuthenticator);
	}

	httpServer.start();
    }

    /**
     * This class gets all classes in the same package as
     * RegisterHandlerByReflection which implements the
     * RegisterHandlerByReflection interface.
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     */
    public RegisterHandlerByReflection[] getRegisterHandlerByReflectionClasses()
	    throws ClassNotFoundException, InstantiationException,
	    IllegalAccessException, UnsupportedEncodingException {
	String pckgname = RegisterHandlerByReflection.class.getPackage()
		.getName();
	ArrayList<RegisterHandlerByReflection> serviceCalls = new ArrayList<RegisterHandlerByReflection>();
	// Get a File object for the package
	File directory = null;
	try {
	    String path = '/' + pckgname.replace('.', '/');
	    URL resource = getClass().getResource(path);
	    if (resource == null) {
		throw new ClassNotFoundException("No resource for " + path);
	    }
	    directory = new File(URLDecoder.decode(resource.getFile(), "UTF-8"));
	} catch (NullPointerException excp) {
	    throw new ClassNotFoundException(pckgname + " (" + directory
		    + ") does not appear to be a valid package");
	}

	if (directory.exists()) {
	    // Get the list of the files contained in the package
	    String[] files = directory.list();
	    for (int i = 0; i < files.length; i++) {
		// we are only interested in .class files
		if (files[i].endsWith(".class")) {
		    // removes the .class extension
		    Class<?> aClass = Class.forName(pckgname + '.'
			    + files[i].substring(0, files[i].length() - 6));
		    if (!aClass.isInterface()
			    && !Modifier.isAbstract(aClass.getModifiers())) {
			boolean implementsRemoteServiceCall = false;
			for (Class<?> interfaceClass : aClass.getInterfaces()) {
			    if (interfaceClass == RegisterHandlerByReflection.class) {
				implementsRemoteServiceCall = true;
				break;
			    }
			}
			if (implementsRemoteServiceCall) {
			    serviceCalls
				    .add((RegisterHandlerByReflection) aClass
					    .newInstance());
			}
		    }
		}
	    }
	} else {
	    throw new ClassNotFoundException(pckgname
		    + " does not appear to be a valid package");
	}

	RegisterHandlerByReflection[] array = new RegisterHandlerByReflection[serviceCalls
		.size()];
	return serviceCalls.toArray(array);
    }

    private void checkOS() throws UnsupportedOSException {
	OperatingSystem os = OperatingSystem.getCurrent();
	log.info(String.format("Detected %s operating system", os));
	if (os == OperatingSystem.UNSUPPORTED) {
	    throw new UnsupportedOSException(
		    "Detected operating system is not supported");
	}
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
	try {
	    new CLI().startup();
	} catch (Exception e) {
	    log.log(Level.WARNING, e.getMessage(), e);
	}
    }
}
