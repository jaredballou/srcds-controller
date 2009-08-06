package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ShutdownHandler implements HttpHandler {
	
	public void handle(HttpExchange httpExchange) throws IOException {

		System.out.println("Controller is going down...");
		System.exit(0);
	}
}
