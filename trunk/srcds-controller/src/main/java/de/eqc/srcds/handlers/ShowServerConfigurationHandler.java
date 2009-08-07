package de.eqc.srcds.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.ServerController;
import de.eqc.srcds.enums.GameType;

public class ShowServerConfigurationHandler implements HttpHandler, RegisterHandlerByReflection {

	private ServerController controller;

  @Override
  public void init(ServerController controller, Configuration config) {
    this.controller = controller;
  }

  @Override
  public String getPath() {
    return "/showServerConfig";
  }

  @Override
  public HttpHandler getHttpHandler() {
    return this;
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