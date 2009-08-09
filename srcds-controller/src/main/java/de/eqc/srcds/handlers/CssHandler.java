package de.eqc.srcds.handlers;


import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_EXECUTABLE;

import java.io.IOException;
import java.net.InetAddress;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.handlers.utils.SimpleTemplate;

/**
 * @author Hannes
 */
public class CssHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    /*
     * @see
     * de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun
     * .net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(HttpExchange httpExchange) throws IOException {

	String beanName = getParameter("bean");
	if (beanName.indexOf('/') > -1 || beanName.indexOf('\\') > -1) {
	    throw new IllegalArgumentException("Only plain file names are allowed as parameter value");
	}
	
	String resource = String.format("/css/%s.css", beanName);
	
	SimpleTemplate template = new SimpleTemplate(resource);
	outputTextContent(template.renderTemplate());
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return "/css";
    }
}
