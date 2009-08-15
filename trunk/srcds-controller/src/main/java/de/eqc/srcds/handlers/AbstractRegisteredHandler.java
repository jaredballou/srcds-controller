/**
 * This file is part of the Source Dedicated Server Controller project.
 * It is distributed under GPL 3 license.
 *
 * The srcds-controller is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You should have received a copy of the GNU General Public License
 * along with the srcds-controller. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://www.earthquake-clan.de/srcds/>
 *    <http://code.google.com/p/srcds-controller/>
 */
package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.SourceDServerController;
import de.eqc.srcds.core.Utils;
import de.eqc.srcds.core.logging.LogFactory;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

/**
 * @author Holger Cremer
 */
public abstract class AbstractRegisteredHandler implements HttpHandler, RegisteredHandler {

    private static final String UTF_8 = "utf-8";

    private static Logger log = LogFactory.getLogger(AbstractRegisteredHandler.class);

    private Configuration config;
    private SourceDServerController serverController;

    // created on demand
    private Map<String, String> parsedRequestParameter = null;
    // created on demand
    private Map<String, String> parsedPostParameter = null;

    // for the current request
    private HttpExchange httpExchange;

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getHttpHandler()
     */
    @Override
    public HttpHandler getHttpHandler() {

	return this;
    }

    /*
     * @seede.eqc.srcds.handlers.RegisteredHandler#init(de.eqc.srcds.core.
     * ServerController, de.eqc.srcds.configuration.Configuration)
     */
    @Override
    public void init(final SourceDServerController controller, final Configuration config) {

	this.serverController = controller;
	this.config = config;
    }

    /**
     * @return the config
     */
    protected Configuration getConfig() {

	return this.config;
    }

    /**
     * @return the serverController
     */
    protected SourceDServerController getServerController() {

	return this.serverController;
    }

    private void parseRequestQuery() throws UnsupportedEncodingException {

	if (this.parsedRequestParameter == null) {
	    this.parsedRequestParameter = new HashMap<String, String>();

	    final String requestQuery = this.httpExchange.getRequestURI().getRawQuery();
	    if (requestQuery == null || requestQuery.isEmpty()) {
		return;
	    }
	    final String[] params = requestQuery.split("&");
	    for (String param : params) {
		final String[] parts = param.split("=");
		if (parts.length != 2) {
		    continue;
		}
		this.parsedRequestParameter.put(URLDecoder.decode(parts[0], UTF_8), URLDecoder.decode(parts[1],
			UTF_8));
	    }
	}
    }

    /**
     * @throws IOException
     * 
     */
    private void parsePostRequest() throws IOException {

	if (this.parsedPostParameter == null) {
	    this.parsedPostParameter = new HashMap<String, String>();

	    final String requestBody = Utils.getInputStreamContent(this.httpExchange.getRequestBody());

	    final String[] params = requestBody.split("&");
	    for (String param : params) {
		final String[] parts = param.split("=");
		if (parts.length != 2) {
		    continue;
		}
		this.parsedPostParameter
			.put(URLDecoder.decode(parts[0], UTF_8), URLDecoder.decode(parts[1], UTF_8));
	    }
	}
    }

    protected Map<String, String> getParameters() throws UnsupportedEncodingException {

	parseRequestQuery();
	return this.parsedRequestParameter;
    }

    protected String getParameter(final String getKey) throws UnsupportedEncodingException {

	parseRequestQuery();
	return this.parsedRequestParameter.get(getKey);
    }

    protected String getPostParameter(final String postKey) throws IOException {

	parsePostRequest();
	return this.parsedPostParameter.get(postKey);
    }

    protected boolean isPost() {

	return this.httpExchange.getRequestMethod().equalsIgnoreCase("POST");
    }

    /**
     * Set the content-type to "text/html" and writes the content to the stream.
     * The stream is closed at the end, so don't call this method twice!
     * 
     * @param content
     * @throws IOException
     */
    protected void outputHtmlContent(final String content) throws IOException {

	outputContent(content, "text/html");
    }

    /**
     * Set the content-type to "text/xml" and writes the content to the stream.
     * The stream is closed at the end, so don't call this method twice!
     * 
     * @param content
     * @throws IOException
     */
    protected void outputXmlContent(final String content) throws IOException {

	outputContent(content, "text/xml");
    }

    /**
     * Set the content-type to "text/plain" and writes the content to the
     * stream. The stream is closed at the end, so don't call this method twice!
     * 
     * @param content
     * @throws IOException
     */
    protected void outputTextContent(final String content) throws IOException {

	outputContent(content, "text/plain");
    }

    /**
     * Set the content-type to "text/css" and writes the content to the stream.
     * The stream is closed at the end, so don't call this method twice!
     * 
     * @param content
     * @throws IOException
     */
    protected void outputCssContent(final String content) throws IOException {

	outputContent(content, "text/css");
    }

    /**
     * Writes the content to the stream. The stream is closed at the end, so
     * don't call this method twice!
     * 
     * @param content
     * @param contentType
     * @throws IOException
     */
    protected void outputContent(final String content, final String contentType) throws IOException {

	outputContent(content.getBytes(), contentType);
    }

    /**
     * Writes the content to the stream. The stream is closed at the end, so
     * don't call this method twice!
     * 
     * @param content
     * @param contentType
     * @throws IOException
     */
    protected void outputContent(final byte[] bytes, final String contentType) throws IOException {

	this.httpExchange.getResponseHeaders().add("Content-type", contentType);

	httpExchange.sendResponseHeaders(200, bytes.length);
	final OutputStream os = httpExchange.getResponseBody();
	os.write(bytes);
	os.flush();
	os.close();
    }
    
    /*
     * @see
     * com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange
     * )
     */
    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {

	this.httpExchange = httpExchange;
	this.parsedRequestParameter = null;
	this.parsedPostParameter = null;

	try {
	    this.handleRequest(httpExchange);
	} catch (Exception e) {
	    log.log(Level.WARNING, String.format("Exception in request handler '%s': %s", this.getPath(), e
		    .getMessage()));
	    log.log(Level.FINE, "Stacktrace: ", e);
	    
	    final Message message = new Message();
	    message.addLine(e.getLocalizedMessage());
	    outputXmlContent(new ControllerResponse(ResponseCode.ERROR, message).toXml());
	}
    }

    public abstract void handleRequest(final HttpExchange httpExchange) throws Exception;
}
