package de.eqc.srcds.handlers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.core.Utils;
import de.eqc.srcds.handlers.utils.SimpleTemplate;

/**
 * @author Holger Cremer
 */
public class IndexHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    /**
     * 
     */
    private static final String INDEX_HTML = "/html/index.html";

    /*
     * @see
     * de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun
     * .net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(HttpExchange httpExchange) throws IOException {

	SimpleTemplate template = new SimpleTemplate(INDEX_HTML);
	
	outputHtmlContent(template.renderTemplate());
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return "/";
    }
}
