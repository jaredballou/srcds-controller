package de.eqc.srcds.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.enums.ImageType;

/**
 * @author Hannes
 */
public class ImageHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    public static final String HANDLER_PATH = "/image";

    /*
     * @see
     * de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun
     * .net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(HttpExchange httpExchange) throws IOException {

	String imageName = getParameter("name");
	String resource = String.format("/images/%s", imageName);
	if (resource.contains("/")) {
	    throw new IllegalArgumentException("Only plain file names are allowed as parameter value");
	}

	URL resourceUrl = getClass().getResource(resource);
	String mimeType = ImageType.getMimeTypeForImageFile(imageName);

	if (resourceUrl == null) {
	    if (imageName == null) {
		throw new IllegalArgumentException("Parameter specify the parameter 'name'");
	    } else {
		throw new IllegalArgumentException(String.format("Cannot find image for name %s", imageName));
	    }
	} else {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    InputStream input = null;
	    try {
		input = resourceUrl.openStream();

		byte[] buffer = new byte[1024];
		for (int len = 0; (len = input.read(buffer)) != -1;) {
		    baos.write(buffer);
		}
	    } finally {
		if (input != null) {
		    input.close();
		}
		baos.flush();
		baos.close();
		outputContent(baos.toByteArray(), mimeType);
	    }
	}
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return HANDLER_PATH;
    }
}
