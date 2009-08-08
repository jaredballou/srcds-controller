package de.eqc.srcds.handlers;

import java.io.IOException;
import java.util.Map.Entry;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.exceptions.ConfigurationException;

public class EditConfigurationHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

    @Override
    public String getPath() {
	return "/editConfig";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException, ConfigurationException {
	
	for (Entry<String, String> entry : getParameters().entrySet()) {
	    System.out.println(entry.getKey() + " = " + entry.getValue());
	    getConfig().setValue(entry.getKey(), entry.getValue());
	}
	
	outputContent(getConfig().toXml(), "text/xml");
    }
}