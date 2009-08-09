package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.ConfigurationRegistry.AUTOSTART;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_GAMETYPE;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_PARAMETERS;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_PATH;
import static de.eqc.srcds.core.Constants.DEFAULT_CONFIG_FILENAME;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.impl.XmlPropertiesConfiguration;
import de.eqc.srcds.enums.GameType;
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

    public void startup() throws Exception {

	this.checkOS();

	File configFile = new File(DEFAULT_CONFIG_FILENAME);

	this.config = new XmlPropertiesConfiguration(configFile);
	
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