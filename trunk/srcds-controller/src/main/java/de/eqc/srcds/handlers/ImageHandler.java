package de.eqc.srcds.handlers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.core.Utils;
import de.eqc.srcds.enums.ImageType;

/**
 * @author Hannes
 */
public class ImageHandler extends AbstractCacheControlRegisteredHandler implements
	RegisteredHandler {

    public static final String HANDLER_PATH = "/image";

    /*
     * @see
     * de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun
     * .net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(final HttpExchange httpExchange) throws IOException {

	final String imageName = getParameter("name");
	if (imageName.indexOf('/') > -1 || imageName.indexOf('\\') > -1) {
	    throw new IllegalArgumentException("Only plain file names are allowed as parameter value");
	}
	

	final String resource = String.format("/images/%s", imageName);

	final URL resourceUrl = getClass().getResource(resource);
	final String mimeType = ImageType.getMimeTypeForImageFile(imageName);

	if (resourceUrl == null) {
	    if (imageName == null) {
		throw new IllegalArgumentException("Parameter specify the parameter 'name'");
	    } else {
		throw new IllegalArgumentException(String.format("Cannot find image for name %s", imageName));
	    }
	} else {
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    InputStream input = null;
	    try {
		input = resourceUrl.openStream();

		final byte[] buffer = new byte[1024];
		for (int len = 0; (len = input.read(buffer)) != -1;) {
		    baos.write(buffer, 0, len);
		}
		baos.flush();
		outputContent(baos.toByteArray(), mimeType);
	    } finally {
		Utils.closeQuietly(input);
		Utils.closeQuietly(baos);
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
