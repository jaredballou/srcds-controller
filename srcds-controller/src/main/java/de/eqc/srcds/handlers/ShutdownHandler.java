package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.ServerController;

public class ShutdownHandler implements HttpHandler, RegisterHandlerByReflection {

  @Override
  public void init(ServerController controller, Configuration config) {
  }

  @Override
  public String getPath() {
    return "/shutdown";
  }

  @Override
  public HttpHandler getHttpHandler() {
    return this;
  }
  
	public void handle(HttpExchange httpExchange) throws IOException {
		System.out.println("Controller is going down...");
		System.exit(0);
	}
}
