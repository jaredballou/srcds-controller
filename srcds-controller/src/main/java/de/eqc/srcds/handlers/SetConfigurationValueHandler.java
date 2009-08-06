package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.exceptions.ConfigurationException;

public class SetConfigurationValueHandler implements HttpHandler {
	
	private final Configuration config;
	
	public SetConfigurationValueHandler(Configuration config) {

		this.config = config;
	}

	public void handle(HttpExchange httpExchange) throws IOException {
		
		String query = httpExchange.getRequestURI().getQuery();
		StringBuilder response = new StringBuilder("<pre>");
		
		String key = null;
		String value = null;
		if (query != null && !"".equals(query)) {
			String[] params = httpExchange.getRequestURI().getQuery().split("&");
			for (String param : params) {
				String[] parts = param.split("=");
				if (parts[0].equals("key")) {
					key = parts[1];
				} else if (parts[0].equals("value")) {
					value = parts[1];
				}
			}
		}
		
		if (key == null || value == null){
			response.append("Either key or value parameter is missing<br/>");
		} else {
			response.append("Set key " + key + " to value " + value);
			try {
				config.setValue(key, value);
			} catch (ConfigurationException e) {
				response.append(e.getLocalizedMessage());
			}
		}
		response.append("<pre>");
		
		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.toString().getBytes());
		os.close();

	}
}