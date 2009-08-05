package de.eqc.srcds.core;

import de.eqc.srcds.exceptions.NotRunningException;


public class ShutdownHook extends Thread {
	
	private final Controller controller;
	
	public ShutdownHook(Controller controller) {
		
		this.controller = controller;
	}
	
	@Override
	public void run() {
	
		controller.getHttpServer().stop(0);
		try {
			controller.stopServer();
		} catch (NotRunningException e) {
		}
		
		System.out.println("Server is going down...");
	}
}
