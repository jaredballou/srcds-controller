package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class ListHandlersHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

    @Override
    public String getPath() {
	return "/usage";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {
	
    }
}