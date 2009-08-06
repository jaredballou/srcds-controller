package de.eqc.srcds.core;

import java.util.logging.Logger;

import de.eqc.srcds.enums.ServerState;
import de.eqc.srcds.exceptions.NotRunningException;


public class ShutdownHook extends Thread {
	
	private static Logger log = Logger.getLogger(Controller.class.getSimpleName());
	private final Controller controller;
	
	public ShutdownHook(Controller controller) {
		
		this.controller = controller;
	}
	
	@Override
	public void run() {
	
		log.info("Shutting down HTTP server...");
		controller.getHttpServer().stop(0);
		try {
			log.info("Stopping server process...");
			controller.stopServer();
		} catch (NotRunningException e) {
		}
		
		log.info("Controller is going down...");
		
		// TODO: flush log
	}
}
