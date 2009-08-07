package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class ShowConfigurationHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

    @Override
    public String getPath() {
	return "/showConfig";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {
	outputContent(getConfig().toXml(), "text/xml");
    }
}