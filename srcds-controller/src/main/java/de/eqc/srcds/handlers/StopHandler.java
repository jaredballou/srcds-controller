package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.core.Controller;

public class StopHandler implements HttpHandler {
	
	private final Controller controller;
	
	public StopHandler(Controller controller) {
	
		this.controller = controller;
	}	
	
	public void handle(HttpExchange httpExchange) throws IOException {

		httpExchange.getResponseHeaders().add("Content-type", "text/html");
		String response = "<pre>Server stopped successfully</pre>";

		try {
			controller.stopServer();
		} catch (Exception e) {
			response = "<pre>" + e.getMessage() + "</pre>";
		}		

		httpExchange.sendResponseHeaders(200, response.length());		
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.getBytes());
		os.flush();
		os.close();
	}
}
