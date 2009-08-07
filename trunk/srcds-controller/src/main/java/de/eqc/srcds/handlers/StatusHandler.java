package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class StatusHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/status";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {

	httpExchange.getResponseHeaders().add("Content-type", "text/html");
	String response = "<pre>" + getServerController().getServerState()
		+ "</pre>";

	outputHtmlContent(response);
    }
}