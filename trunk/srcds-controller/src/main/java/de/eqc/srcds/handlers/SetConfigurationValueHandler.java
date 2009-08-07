package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.ServerController;
import de.eqc.srcds.exceptions.ConfigurationException;

public class SetConfigurationValueHandler extends AbstractRegisteredHandler
	implements HttpHandler, RegisteredHandler {

    @Override
    public String getPath() {
	return "/setConfig";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {
	StringBuilder response = new StringBuilder("<pre>");

	String key = getParameter("key");
	String value = getParameter("value");

	if (key == null || value == null) {
	    response.append("Either key or value parameter is missing<br/>");
	} else {
	    try {
		getConfig().setValue(key, value);
		response.append("Set key " + key + " to value " + value);
	    } catch (ConfigurationException e) {
		response.append(e.getLocalizedMessage());
	    }
	}
	response.append("<pre>");

	httpExchange.sendResponseHeaders(200,
		response.toString().getBytes().length);
	OutputStream os = httpExchange.getResponseBody();
	os.write(response.toString().getBytes());
	os.close();

    }
}