package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.ConfigurationRegistry.HTTP_SERVER_PORT;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_EXECUTABLE;
import static de.eqc.srcds.core.Constants.DEFAULT_CONFIG_FILENAME;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.configuration.impl.XmlPropertiesConfiguration;
import de.eqc.srcds.core.logging.LogFactory;
import de.eqc.srcds.enums.OperatingSystem;
import de.eqc.srcds.exceptions.UnsupportedOSException;

/**
 * This class starts the srcds controller.
 * 
 * @author Holger Cremer
 */
public class CLI {

    private static Logger log = LogFactory.getLogger(CLI.class);

    private Configuration config;

    private HttpServerController httpServerController;

    private SourceDServerController srcdsController;

    public void startup(String ... arguments) throws Exception {

	Thread.currentThread().setName(getClass().getSimpleName());
	
	this.checkOS();

	File configFile = new File(DEFAULT_CONFIG_FILENAME);

	this.config = new XmlPropertiesConfiguration(configFile);

	processCommandlineArguments(arguments);
	
	/*
	 * This block is for testing purposes only.
	 */
//	config.setValue(AUTOSTART, true);
//	config.setValue(SRCDS_PATH, "l4d");
//	config.setValue(SRCDS_PARAMETERS, "+hostport 27015 +maxplayers 14 -tickrate 100 +exec server_unrest.cfg +map l4d_vs_airport01_greenhouse.bsp");
//	config.setValue(SRCDS_GAMETYPE, GameType.LEFT4DEAD);	
	
	this.srcdsController = new SourceDServerController(this.config);
	this.httpServerController = new HttpServerController(config, srcdsController);
	
	// start

	Runtime.getRuntime().addShutdownHook(
		new ShutdownHook(Thread.currentThread(), srcdsController, httpServerController));
	
	httpServerController.start();
	srcdsController.start();
	
	try {
	    httpServerController.join();
	} catch (InterruptedException e) {
	    log.warning(e.getLocalizedMessage());
	}

	try {
	    srcdsController.join();
	} catch (InterruptedException e) {
	    log.warning(e.getLocalizedMessage());
	}

	System.out.println("Exiting...");
    }

    private void processCommandlineArguments(String ... arguments) throws ConfigurationException {

	for (int i = 0; i < arguments.length; i++) {
	    String argument = arguments[i].trim();
	    if (argument.equals("--help")) {
		System.out.println("Usage: java -jar <jarfile> [--httpServerPort <port>] [--srcdsExecutable <file>]");
		System.exit(0);
	    } else if (argument.equals("--httpServerPort") && i < argument.length() - 1) {
		String value = arguments[i + 1];
		config.setValue(HTTP_SERVER_PORT, value);
	    } else if (argument.equals("--srcdsExecutable") && i < argument.length() - 1) {
		String value = arguments[i + 1];
		config.setValue(SRCDS_EXECUTABLE, value);
	    }	    
	}	
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
	    new CLI().startup(args);
	} catch (Exception e) {
	    log.log(Level.WARNING, e.getMessage(), e);
	}
    }
}
