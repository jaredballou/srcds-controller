package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.handlers.utils.SimpleTemplate;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

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
	String resource = String.format("/xslt/%s.xsl", beanName);
	
	SimpleTemplate template = new SimpleTemplate(resource);
	template.setAttribute("hostname", InetAddress.getLocalHost().getHostName());

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
