package de.eqc.srcds.core;

import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

import de.eqc.srcds.exceptions.NotRunningException;


public class ShutdownHook extends Thread {

  private static Logger    log = LogFactory.getLogger(ShutdownHook.class);
  private ServerController controller;
  private HttpServer       httpServer;

  public ShutdownHook(ServerController controller, HttpServer httpServer) {
    this.controller = controller;
    this.httpServer = httpServer;
  }

  @Override
  public void run() {
    log.info("Shutting down HTTP server...");
    this.httpServer.stop(0);
    try {
      log.info("Stopping server process...");
      this.controller.stopServer();
    } catch (NotRunningException e) {
    }

    log.info("Controller is going down...");

    // TODO: flush log
  }
}
