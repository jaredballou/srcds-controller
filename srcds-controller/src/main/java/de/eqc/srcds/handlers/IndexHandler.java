package de.eqc.srcds.handlers;

import java.io.IOException;
import java.net.InetAddress;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.core.VersionUtil;
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
	template.setAttribute("hostname", InetAddress.getLocalHost().getHostName());
	template.setAttribute("version", VersionUtil.getProjectVersion());
	
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
