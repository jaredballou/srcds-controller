package de.eqc.srcds.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.core.Controller;
import de.eqc.srcds.enums.GameType;
import de.eqc.srcds.exceptions.ConfigurationException;

public class ShowServerConfigurationHandler implements HttpHandler {

	private final Controller controller;

	public ShowServerConfigurationHandler(Controller controller) {

		this.controller = controller;
	}

	public void handle(HttpExchange httpExchange) throws IOException {

		httpExchange.getResponseHeaders().add("Content-type", "text/plain; charset=utf-8");
		StringBuilder response = new StringBuilder();

		try {
			GameType gameType = controller.getGameType();
			File srcdsPath = controller.getSrcdsPath();

			File configFile = new File(String.format("%s%s%s%s%s%s%s",
					srcdsPath.getPath(), File.separator, gameType
							.getImplementation().getDirectory(),
					File.separator, "cfg", File.separator, "server.cfg"));
			if (configFile.exists()) {
				response.append(String.format("Server configuration file: %s\n\n", configFile.getPath()));
				BufferedReader br = new BufferedReader(new FileReader(configFile));
				String line;
				while ((line = br.readLine()) != null) {
					response.append(line + "\n");
				}				
			} else {
				response.append(String.format("Unable to find server configuration file: %s", configFile.getPath()));
			}
		} catch (Exception e) {
			response.append(e.getLocalizedMessage());
		}

		httpExchange.sendResponseHeaders(200, response.toString().getBytes().length);
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.toString().getBytes());
		os.close();

	}
}