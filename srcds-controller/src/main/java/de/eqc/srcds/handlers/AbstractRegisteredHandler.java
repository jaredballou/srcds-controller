package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.LogFactory;
import de.eqc.srcds.core.ServerController;

/**
 * @author Holger Cremer
 */
public abstract class AbstractRegisteredHandler implements HttpHandler,
	RegisteredHandler {

    private static Logger log = LogFactory.getLogger(AbstractRegisteredHandler.class);
    
    private Configuration config;
    private ServerController serverController;

    // created on demand
    private Map<String, String> parsedRequestParameter = null;
    private HttpExchange httpExchange;

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

	    String requestQuery = this.httpExchange.getRequestURI().getQuery();
	    if (requestQuery == null || requestQuery.isEmpty()) {
		return;
	    }
	    String[] params = requestQuery.split("&");
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

    /**
     * Set the content-type to "text/html" and writes the content to the stream.
     * The stream is closed at the end, so don't call this method twice!
     * 
     * @param content
     * @throws IOException
     */
    protected void outputHtmlContent(String content) throws IOException {
	outputContent(content, "text/html");
    }

    /**
     * Set the content-type to "text/xml" and writes the content to the stream.
     * The stream is closed at the end, so don't call this method twice!
     * 
     * @param content
     * @throws IOException
     */
    protected void outputXmlContent(String content) throws IOException {
	outputContent(content, "text/xml");
    }    
    
    /**
     * Writes the content to the stream. The stream is closed at the end, so
     * don't call this method twice!
     * 
     * @param content
     * @param contentType
     * @throws IOException
     */
    protected void outputContent(String content, String contentType)
	    throws IOException {
	this.httpExchange.getResponseHeaders().add("Content-type", contentType);

	httpExchange.sendResponseHeaders(200, content.getBytes().length);
	OutputStream os = httpExchange.getResponseBody();
	os.write(content.getBytes());
	os.close();
    }

    /*
     * @see
     * com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange
     * )
     */
    @Override
    public final void handle(HttpExchange httpExchange) throws IOException {
	this.httpExchange = httpExchange;
	this.parsedRequestParameter = null;

	try {
	    this.handleRequest(httpExchange);
	} catch (Exception e) {
	    log.log(Level.WARNING, String.format("Exception in request handler '%s': %s",this.getPath(), e.getMessage()) , e);
	}
    }

    public abstract void handleRequest(HttpExchange httpExchange)
	    throws Exception;
}
