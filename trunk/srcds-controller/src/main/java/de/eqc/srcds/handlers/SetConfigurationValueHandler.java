package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.exceptions.ConfigurationException;

public class SetConfigurationValueHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

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

	outputHtmlContent(response.toString());
    }
}