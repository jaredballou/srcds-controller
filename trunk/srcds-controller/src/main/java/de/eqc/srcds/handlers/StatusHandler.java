package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.ServerController;

public class StatusHandler implements HttpHandler, RegisterHandlerByReflection {
	
	private ServerController controller;
	
  @Override
  public void init(ServerController controller, Configuration config) {
    this.controller = controller;
  }

  @Override
  public String getPath() {
    return "/status";
  }

  @Override
  public HttpHandler getHttpHandler() {
    return this;
  }
	
	public void handle(HttpExchange httpExchange) throws IOException {

		httpExchange.getResponseHeaders().add("Content-type", "text/html");
		String response = "<pre>" + controller.getServerState() + "</pre>";

		httpExchange.sendResponseHeaders(200, response.length());
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
