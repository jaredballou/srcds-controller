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
import de.eqc.srcds.handlers.RegisteredHandler;
import de.eqc.srcds.handlers.SetConfigurationValueHandler;
import de.eqc.srcds.handlers.ShowConfigurationHandler;
import de.eqc.srcds.handlers.ShowServerConfigurationHandler;
import de.eqc.srcds.handlers.ShutdownHandler;
import de.eqc.srcds.handlers.StartHandler;
import de.eqc.srcds.handlers.StopHandler;

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

	RegisteredHandler[] clazzes = new RegisteredHandler[] {
		new SetConfigurationValueHandler(),
		new ShowConfigurationHandler(),
		new ShowServerConfigurationHandler(), // 
		new ShutdownHandler(), //
		new StartHandler(), //
		new StopHandler() //
	};
	DefaultAuthenticator defaultAuthenticator = new DefaultAuthenticator();
	for (RegisteredHandler clazzByReflection : clazzes) {
	    clazzByReflection.init(this.serverController, this.config);
	    HttpContext startContext = httpServer.createContext(
		    clazzByReflection.getPath(), clazzByReflection
			    .getHttpHandler());
	    startContext.setAuthenticator(defaultAuthenticator);
	}

	httpServer.start();
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
