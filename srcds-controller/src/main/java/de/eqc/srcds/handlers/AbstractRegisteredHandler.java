package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.LogFactory;
import de.eqc.srcds.core.SourceDServerController;
import de.eqc.srcds.core.Utils;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

/**
 * @author Holger Cremer
 */
public abstract class AbstractRegisteredHandler implements HttpHandler,
	RegisteredHandler {

    private static Logger log = LogFactory
	    .getLogger(AbstractRegisteredHandler.class);

    private Configuration config;
    private SourceDServerController serverController;

    // created on demand
    private Map<String, String> parsedRequestParameter = null;
    // created on demand
    private Map<String, String> parsedPostParameter = null;

    // for the current request
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
    public void init(SourceDServerController controller, Configuration config) {

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
    protected SourceDServerController getServerController() {

	return this.serverController;
    }

    private void parseRequestQuery() throws UnsupportedEncodingException {

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
		this.parsedRequestParameter.put(URLDecoder.decode(parts[0], "utf-8"), URLDecoder.decode(parts[1], "utf-8"));
	    }
	}
    }

    /**
     * @throws IOException
     * 
     */
    private void parsePostRequest() throws IOException {

	if (this.parsedPostParameter == null) {
	    this.parsedPostParameter = new HashMap<String, String>();

	    String requestBody = Utils.getInputStreamContent(this.httpExchange
		    .getRequestBody());

	    String[] params = requestBody.split("&");
	    for (String param : params) {
		String[] parts = param.split("=");
		if (parts.length != 2) {
		    continue;
		}
		this.parsedPostParameter.put(URLDecoder.decode(parts[0], "utf-8"), URLDecoder.decode(parts[1], "utf-8"));
	    }
	}
    }

    protected String getParameter(String getKey) throws UnsupportedEncodingException {
	parseRequestQuery();
	return this.parsedRequestParameter.get(getKey);
    }

    protected String getPostParameter(String postKey) throws IOException {

	parsePostRequest();
	return this.parsedPostParameter.get(postKey);
    }

    protected boolean isPost() {

	return this.httpExchange.getRequestMethod().equalsIgnoreCase("POST");
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

	outputContent(content.getBytes(), contentType);
    }

    /**
     * Writes the content to the stream. The stream is closed at the end, so
     * don't call this method twice!
     * 
     * @param content
     * @param contentType
     * @throws IOException
     */
    protected void outputContent(byte[] bytes, String contentType)
	    throws IOException {

	this.httpExchange.getResponseHeaders().add("Content-type", contentType);

	httpExchange.sendResponseHeaders(200, bytes.length);
	OutputStream os = httpExchange.getResponseBody();
	os.write(bytes);
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
	this.parsedPostParameter = null;

	try {
	    this.handleRequest(httpExchange);
	} catch (Exception e) {
	    log.log(Level.WARNING, String.format(
		    "Exception in request handler '%s': %s", this.getPath(), e
			    .getMessage()), e);

	    Message message = new Message();
	    message.addMessage(e.getMessage());
	    outputXmlContent(new ControllerResponse(ResponseCode.FAILED,
		    message).toXml());
	}
    }

    public abstract void handleRequest(HttpExchange httpExchange)
	    throws Exception;
}
