package de.eqc.srcds.handlers;


import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_EXECUTABLE;

import java.io.IOException;
import java.net.InetAddress;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.core.VersionUtil;
import de.eqc.srcds.handlers.utils.SimpleTemplate;

/**
 * @author Hannes
 */
public class XsltHandler extends AbstractCacheControlRegisteredHandler implements
	RegisteredHandler {

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
	
	final String resource = String.format("/xslt/%s", name);
	
	final SimpleTemplate template = new SimpleTemplate(resource);
	template.setAttribute("hostname", InetAddress.getLocalHost().getHostName());
	template.setAttribute("version", VersionUtil.getProjectVersion());
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
