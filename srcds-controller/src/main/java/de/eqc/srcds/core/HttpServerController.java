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
package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.ConfigurationRegistry.HTTP_SERVER_PASSWORD;
import static de.eqc.srcds.configuration.ConfigurationRegistry.HTTP_SERVER_PORT;
import static de.eqc.srcds.configuration.ConfigurationRegistry.HTTP_SERVER_USERNAME;
import static de.eqc.srcds.core.Constants.HTTP_SERVER_SHUTDOWN_DELAY_SECS;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.datatypes.Password;
import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.enums.ServerState;
import de.eqc.srcds.exceptions.InitializationException;
import de.eqc.srcds.handlers.RegisteredHandler;
import de.eqc.srcds.handlers.utils.HandlerUtil;

public class HttpServerController extends AbstractServerController<HttpServer> {

    private final transient SourceDServerController srcdsController;
    private final transient List<HttpContext> contextList;

    public HttpServerController(final Configuration config,
	    final SourceDServerController srcdsController)
	    throws InitializationException {

	super("HTTP server", config);
	setAutostart(true);
	this.contextList = new LinkedList<HttpContext>();
	this.srcdsController = srcdsController;

	try {
	    final int port = config.getValue(HTTP_SERVER_PORT, Integer.class);
	    this.server = HttpServer.create(new InetSocketAddress(port), 0);
	    log.info(String.format("Bound to TCP port %d.", port));
	} catch (Exception e) {
	    throw new InitializationException(String.format(
		    "HTTP server startup failed: %s", e.getLocalizedMessage()), e);
	}
    }

    private List<HttpContext> bindHandlers()
	    throws UnsupportedEncodingException, ClassNotFoundException,
	    InstantiationException, IllegalAccessException,
	    ConfigurationException {

	final String username = config.getValue(HTTP_SERVER_USERNAME, String.class);
	final String password = config.getValue(HTTP_SERVER_PASSWORD, Password.class).toString();
	final HttpAuthenticator defaultAuthenticator = new HttpAuthenticator(
		username, password);

	final List<HttpContext> localContextList = new LinkedList<HttpContext>();
	final Collection<RegisteredHandler> classes = HandlerUtil
		.getRegisteredHandlerImplementations();
	for (RegisteredHandler classByReflection : classes) {
	    classByReflection.init(this.srcdsController, this.config);
	    final HttpContext context = server.createContext(classByReflection
		    .getPath(), classByReflection.getHttpHandler());
	    context.setAuthenticator(defaultAuthenticator);
	    localContextList.add(context);
	    log.info(String.format("Registered handler at context %s",
		    classByReflection.getPath()));
	}
	return localContextList;
    }

    @Override
    public void startServer() {

	synchronized (getMutex()) {
	    contextList.clear();
	    try {
		contextList.addAll(bindHandlers());
	    } catch (Exception e) {
		log.warning(String.format(
			"Error occured while registering handlers: %s", e
				.getLocalizedMessage()));
	    }
	    server.start();
	}
    }

    @Override
    public void stopServer() {

	synchronized (getMutex()) {
	    for (HttpContext context : contextList) {
		server.removeContext(context);
		log.info(String.format("Unregistered handler at context %s",
			context.getPath()));
	    }
	    server.stop(HTTP_SERVER_SHUTDOWN_DELAY_SECS);
	}
    }

    @Override
    public ServerState getServerState() {

	return running ? ServerState.RUNNING : ServerState.STOPPED;
    }
}