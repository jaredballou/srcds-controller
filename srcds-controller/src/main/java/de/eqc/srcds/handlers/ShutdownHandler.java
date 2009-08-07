package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class ShutdownHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/shutdown";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {
	System.out.println("Controller is going down...");
	System.exit(0);
    }
}
