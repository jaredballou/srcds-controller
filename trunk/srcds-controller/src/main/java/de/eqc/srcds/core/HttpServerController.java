package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.Constants.HTTP_SERVER_PORT;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Collection;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.enums.ServerState;
import de.eqc.srcds.exceptions.InitializationException;
import de.eqc.srcds.handlers.RegisteredHandler;
import de.eqc.srcds.handlers.utils.HandlerUtil;

public class HttpServerController extends Controller<HttpServer> {

    private final SourceDServerController srcdsController;
    
    public HttpServerController(Configuration config, SourceDServerController srcdsController) throws InitializationException {

	super("HTTP server", config);
	this.srcdsController = srcdsController;

	try {
	    int port = config.getValue(HTTP_SERVER_PORT, Integer.class);
	    server = HttpServer.create(new InetSocketAddress(port), 0);
	    log.info(String.format("Bound to TCP port %d.", port));
	    bindHandlers();
	} catch (Exception e) {
	    throw new InitializationException(String.format("HTTP server startup failed: %s", e.getLocalizedMessage()));
	}
    }
    
    private void bindHandlers() throws UnsupportedEncodingException, ClassNotFoundException, InstantiationException, IllegalAccessException {

	Collection<RegisteredHandler> classes = HandlerUtil
		.getRegisteredHandlerImplementations();
	DefaultAuthenticator defaultAuthenticator = new DefaultAuthenticator();
	for (RegisteredHandler classByReflection : classes) {
	    classByReflection.init(this.srcdsController, this.config);
	    HttpContext startContext = server.createContext(
		    classByReflection.getPath(), classByReflection
			    .getHttpHandler());
	    startContext.setAuthenticator(defaultAuthenticator);
	    log.info(String.format("Registered handler at context %s",
		    classByReflection.getPath()));
	}
    }

    @Override
    public void startServer() {

	server.start();
    }

    @Override
    public void stopServer() {

        server.stop(0);
    }

    @Override
    public ServerState getServerState() {

	return running ? ServerState.RUNNING : ServerState.STOPPED;
    }
}