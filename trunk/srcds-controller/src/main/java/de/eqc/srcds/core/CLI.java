package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.Constants.DEFAULT_CONFIG_FILENAME;
import static de.eqc.srcds.configuration.Constants.HTTP_SERVER_PORT;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.impl.XmlPropertiesConfiguration;
import de.eqc.srcds.enums.OperatingSystem;
import de.eqc.srcds.exceptions.ConfigurationException;
import de.eqc.srcds.exceptions.UnsupportedOSException;
import de.eqc.srcds.handlers.IndexHandler;
import de.eqc.srcds.handlers.RegisteredHandler;

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

	Collection<RegisteredHandler> classes = getRegisterHandlerByReflectionClasses();
	DefaultAuthenticator defaultAuthenticator = new DefaultAuthenticator();
	for (RegisteredHandler classByReflection : classes) {
	    classByReflection.init(this.serverController, this.config);
	    HttpContext startContext = httpServer.createContext(
		    classByReflection.getPath(), classByReflection
			    .getHttpHandler());
	    startContext.setAuthenticator(defaultAuthenticator);
	    log.info(String.format("Registered handler at context %s",
		    classByReflection.getPath()));
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
    public Collection<RegisteredHandler> getRegisterHandlerByReflectionClasses()
	    throws ClassNotFoundException, InstantiationException,
	    IllegalAccessException, UnsupportedEncodingException {

	String pckgname = RegisteredHandler.class.getPackage().getName();
	Collection<RegisteredHandler> handlers = new ArrayList<RegisteredHandler>();
	List<String> classNames = null;
	try {
	    String path = '/' + pckgname.replace('.', '/');
	    URL resource = getClass().getResource(path);
	    if (resource == null) {
		throw new ClassNotFoundException("No resource for " + path);
	    }

	    classNames = findHandlerClassNames(resource.getPath().toString(),
		    pckgname);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new ClassNotFoundException(pckgname + " (" + pckgname
		    + ") does not appear to be a valid package");
	}

	for (String className : classNames) {
	    Class<?> aClass = Class.forName(className);
	    if (!aClass.isInterface()
		    && !Modifier.isAbstract(aClass.getModifiers())) {
		boolean implementsRemoteServiceCall = false;
		for (Class<?> interfaceClass : aClass.getInterfaces()) {
		    if (interfaceClass == RegisteredHandler.class) {
			implementsRemoteServiceCall = true;
			break;
		    }
		}
		if (implementsRemoteServiceCall) {
		    handlers.add((RegisteredHandler) aClass.newInstance());
		}
	    }
	}

	return handlers;
    }

    public List<String> findHandlerClassNames(String path, String packageName)
	    throws URISyntaxException, IOException {

	List<String> classNames = new ArrayList<String>();

	if (path.contains(".jar!/")) {
	    packageName = packageName.replaceAll("\\.", "/");
	    path = path.split("!")[0];
	    FileInputStream fis = new FileInputStream(new File(new URI(path)));
	    JarInputStream jarFile = new JarInputStream(fis);
	    JarEntry jarEntry;
	    while ((jarEntry = jarFile.getNextJarEntry()) != null) {
		if ((jarEntry.getName().startsWith(packageName))
			&& (jarEntry.getName().endsWith(".class"))) {

		    String className = jarEntry.getName()
			    .replaceAll("/", "\\.").substring(0,
				    jarEntry.getName().length() - 6);
		    classNames.add(className);
		}
	    }
	} else {
	    File directory = new File(path);
	    if (directory.exists()) {
		// Get the list of the files contained in the package
		String[] files = directory.list();
		for (int i = 0; i < files.length; i++) {
		    // we are only interested in .class files
		    if (files[i].endsWith(".class")) {
			String className = packageName + '.'
				+ files[i].substring(0, files[i].length() - 6);
			classNames.add(className);
		    }
		}
	    }
	}

	return classNames;
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
