package de.eqc.srcds.handlers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.ServerController;

/**
 * @author Holger Cremer
 */
public abstract class AbstractRegisteredHandler implements HttpHandler,
	RegisteredHandler {

    private Configuration config;
    private ServerController serverController;
    private String requestQuery;

    // created on demand
    private Map<String, String> parsedRequestParameter = null;

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getHttpHandler()
     */
    @Override
    public HttpHandler getHttpHandler() {
	return this;
    }

    /*
     * @seede.eqc.srcds.handlers.RegisteredHandler#init(de.eqc.srcds.core.
     * ServerController, de.eqc.srcds.configuration.Configuration)
     */
    @Override
    public void init(ServerController controller, Configuration config) {
	this.serverController = controller;
	this.config = config;
    }

    /**
     * @return the config
     */
    protected Configuration getConfig() {
	return this.config;
    }

    /**
     * @return the serverController
     */
    protected ServerController getServerController() {
	return this.serverController;
    }

    private void parseRequestQuery() {
	if (this.parsedRequestParameter == null) {
	    this.parsedRequestParameter = new HashMap<String, String>();
	    if (this.requestQuery == null || this.requestQuery.isEmpty()) {
		return;
	    }
	    String[] params = this.requestQuery.split("&");
	    for (String param : params) {
		String[] parts = param.split("=");
		if (parts.length != 2) {
		    continue;
		}
		this.parsedRequestParameter.put(parts[0], parts[1]);
	    }
	}
    }

    protected String getParameter(String getKey) {
	parseRequestQuery();
	return this.parsedRequestParameter.get(getKey);
    }

    /*
     * @see
     * com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange
     * )
     */
    @Override
    public final void handle(HttpExchange arg0) throws IOException {
	this.requestQuery = arg0.getRequestURI().getQuery();
	this.parsedRequestParameter = null;
	this.handleRequest(arg0);
    }

    public abstract void handleRequest(HttpExchange arg0) throws IOException;
}
