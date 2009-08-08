package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;

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
	StringBuilder builder = new StringBuilder();

	String beanName = getParameter("bean");	
	URL resourceUrl = getClass().getResource(String.format("/xslt/%s.xsl", beanName));
	if (resourceUrl == null) {
	    if (beanName == null) {
		throw new IllegalArgumentException("Parameter specify the parameter 'bean'");
	    } else {
		throw new IllegalArgumentException(String.format("Cannot find XSLT for name %s", beanName));
	    }
	} else {
        	InputStream input = null;
        	try {
        	    input = resourceUrl.openStream();
        
        	    byte[] buffer = new byte[1024];
        	    for (int len = 0; (len = input.read(buffer)) != -1;) {
        		builder.append(new String(buffer, 0, len));
        	    }
        	} finally {
        	    if (input != null) {
        		input.close();
        	    }
        	}
	}

	outputXmlContent(builder.toString());
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return "/xslt";
    }
}
