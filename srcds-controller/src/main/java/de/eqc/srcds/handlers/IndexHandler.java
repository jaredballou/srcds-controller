package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;

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
	StringBuilder builder = new StringBuilder();

	URL indexHtml = getClass().getResource(INDEX_HTML);
	if (indexHtml == null) {
	    throw new IOException(String.format("Cannot find file %s", INDEX_HTML));
	}
	InputStream input = null;
	try {
	    input = indexHtml.openStream();

	    byte[] buffer = new byte[1024];
	    for (int len = 0; (len = input.read(buffer)) != -1;) {
		builder.append(new String(buffer, 0, len));
	    }
	} finally {
	    if (input != null) {
		input.close();
	    }
	}

	outputHtmlContent(builder.toString());
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return "/";
    }
}
