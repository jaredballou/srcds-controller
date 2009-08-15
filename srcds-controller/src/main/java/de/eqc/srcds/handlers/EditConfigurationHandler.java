package de.eqc.srcds.handlers;

import java.io.IOException;
import java.util.Map.Entry;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.configuration.exceptions.ConfigurationException;

public class EditConfigurationHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

    @Override
    public String getPath() {
	return "/editConfig";
    }

    public void handleRequest(final HttpExchange httpExchange) throws IOException, ConfigurationException {
	
	for (Entry<String, String> entry : getParameters().entrySet()) {
	    getConfig().setValue(entry.getKey(), entry.getValue());
	}
	
	outputContent(getConfig().toXml(), "text/xml");
    }
}