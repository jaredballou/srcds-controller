package de.eqc.srcds.handlers;


import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.handlers.utils.SimpleTemplate;

/**
 * @author Hannes
 */
public class CssHandler extends AbstractCacheControlRegisteredHandler implements
	RegisteredHandler {

    public static final String HANDLER_PATH = "/css";

    /*
     * @see
     * de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun
     * .net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(final HttpExchange httpExchange) throws IOException {

	final String name = getParameter("name");
	if (name.indexOf('/') > -1 || name.indexOf('\\') > -1) {
	    throw new IllegalArgumentException("Only plain file names are allowed as parameter value");
	}
	
	final String resource = String.format("/css/%s", name);
	
	final SimpleTemplate template = new SimpleTemplate(resource);
	outputCssContent(template.renderTemplate());
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return HANDLER_PATH;
    }
}
