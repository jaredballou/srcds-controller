package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.ServerController;

public class ShowConfigurationHandler implements HttpHandler, RegisterHandlerByReflection {
	
	private  Configuration config;
	
  @Override
  public void init(ServerController controller, Configuration config) {
    this.config = config;
  }

  @Override
  public String getPath() {
    return "/showConfig";
  }

  @Override
  public HttpHandler getHttpHandler() {
    return this;
  }

	public void handle(HttpExchange httpExchange) throws IOException {
		
		httpExchange.getResponseHeaders().add("Content-type", "text/xml");
		String response = config.toXml();
		
		httpExchange.sendResponseHeaders(200, response.toString().getBytes().length);
		OutputStream os = httpExchange.getResponseBody();
		os.write(response.toString().getBytes());
		os.close();

	}
}