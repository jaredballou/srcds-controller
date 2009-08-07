package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class StartHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/start";
    }

    @Override
    public void handleRequest(HttpExchange httpExchange) throws IOException {

	httpExchange.getResponseHeaders().add("Content-type", "text/html");
	String response = "<pre>Server started successfully</pre>";

	try {
	    getServerController().startServer();
	} catch (Exception e) {
	    response = "<pre>" + e.getMessage() + "</pre>";
	}

	outputHtmlContent(response);
    }
}
