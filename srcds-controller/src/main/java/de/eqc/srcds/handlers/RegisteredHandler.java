package de.eqc.srcds.handlers;

import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.SourceDServerController;

/**
 * 
 * @author Holger Cremer
 */
public interface RegisteredHandler {
    void init(SourceDServerController controller, Configuration config);

    String getPath();

    HttpHandler getHttpHandler();
}
