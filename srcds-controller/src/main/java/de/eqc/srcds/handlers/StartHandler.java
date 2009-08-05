package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.core.Controller;

public class StartHandler implements HttpHandler {
	
	private final Controller controller;
	
	public StartHandler(Controller controller) {
	
		this.controller = controller;
	}
	
	public void handle(HttpExchange httpExchange) throws IOException {
		httpExchange.getResponseHeaders().add("Content-type", "text/html");
		String response = "<pre>Server started successfully</pre>";

		try {
			controller.startServer();
		} catch (Exception e) {
			response = "<pre>" + e.getMessage() + "</pre>";
		}

		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
