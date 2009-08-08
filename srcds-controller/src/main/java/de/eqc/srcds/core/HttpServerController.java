package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.Constants.HTTP_SERVER_PORT;
import static de.eqc.srcds.core.Constants.HTTP_SERVER_SHUTDOWN_DELAY_SECS;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.enums.ServerState;
import de.eqc.srcds.exceptions.InitializationException;
import de.eqc.srcds.handlers.RegisteredHandler;
import de.eqc.srcds.handlers.utils.HandlerUtil;

public class HttpServerController extends ServerController<HttpServer> {

    private final SourceDServerController srcdsController;
    private final List<HttpContext> contextList;
    
    public HttpServerController(Configuration config, SourceDServerController srcdsController) throws InitializationException {

	super("HTTP server", config);
	this.contextList = new LinkedList<HttpContext>();
	this.srcdsController = srcdsController;

	try {
	    int port = config.getValue(HTTP_SERVER_PORT, Integer.class);
	    server = HttpServer.create(new InetSocketAddress(port), 0);
	    log.info(String.format("Bound to TCP port %d.", port));
	} catch (Exception e) {
	    throw new InitializationException(String.format("HTTP server startup failed: %s", e.getLocalizedMessage()));
	}
    }
    
    private List<HttpContext> bindHandlers() throws UnsupportedEncodingException, ClassNotFoundException, InstantiationException, IllegalAccessException {

	List<HttpContext> localContextList = new LinkedList<HttpContext>();
	Collection<RegisteredHandler> classes = HandlerUtil
		.getRegisteredHandlerImplementations();
	DefaultAuthenticator defaultAuthenticator = new DefaultAuthenticator();
	for (RegisteredHandler classByReflection : classes) {
	    classByReflection.init(this.srcdsController, this.config);
	    HttpContext context = server.createContext(
		    classByReflection.getPath(), classByReflection
			    .getHttpHandler());
	    context.setAuthenticator(defaultAuthenticator);
	    localContextList.add(context);
	    log.info(String.format("Registered handler at context %s",
		    classByReflection.getPath()));
	}
	return localContextList;
    }

    @Override
    public void startServer() {

	contextList.clear();
	try {
	    contextList.addAll(bindHandlers());
	} catch (Exception e) {
	    log.warning(String.format("Error occured while registering handlers: %s", e.getLocalizedMessage()));
	}
	server.start();
    }

    @Override
    public void stopServer() {

	for (HttpContext context : contextList) {
	    server.removeContext(context);
	    log.info(String.format("Unregistered handler at context %s",
		    context.getPath()));	    
	}
        server.stop(HTTP_SERVER_SHUTDOWN_DELAY_SECS);
    }

    @Override
    public ServerState getServerState() {

	return running ? ServerState.RUNNING : ServerState.STOPPED;
    }
}