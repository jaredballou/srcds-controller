package de.eqc.srcds.handlers;

import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.ServerController;

/**
 * 
 * @author Holger Cremer
 */
public interface RegisterHandlerByReflection {
  void init(ServerController controller, Configuration config);
  
  String getPath();
  HttpHandler getHttpHandler();
}
