package de.eqc.srcds.handlers;


import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_EXECUTABLE;

import java.io.IOException;
import java.net.InetAddress;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.handlers.utils.SimpleTemplate;

/**
 * @author Hannes
 */
public class XsltHandler extends AbstractRegisteredHandler implements
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
	
	String resource = String.format("/xslt/%s.xsl", beanName);
	
	SimpleTemplate template = new SimpleTemplate(resource);
	template.setAttribute("hostname", InetAddress.getLocalHost().getHostName());
	template.setAttribute("srcds-executable-key", SRCDS_EXECUTABLE);

	outputXmlContent(template.renderTemplate());
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return "/xslt";
    }
}