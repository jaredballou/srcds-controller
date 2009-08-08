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

    /*
     * @see
     * de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun
     * .net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(HttpExchange httpExchange) throws IOException {

	StringBuilder builder = new StringBuilder();

	String imageName = getParameter("name");
	URL resourceUrl = getClass().getResource(
		String.format("/images/%s", imageName));

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

	return "/image";
    }
}
