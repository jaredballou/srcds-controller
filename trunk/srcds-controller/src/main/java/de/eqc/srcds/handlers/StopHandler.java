package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

public class StopHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/stop";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {

	String response = "<pre>Server stopped successfully</pre>";

	try {
	    getServerController().stopServer();
	} catch (Exception e) {
	    response = "<pre>" + e.getMessage() + "</pre>";
	}

	outputHtmlContent(response);
    }
}
