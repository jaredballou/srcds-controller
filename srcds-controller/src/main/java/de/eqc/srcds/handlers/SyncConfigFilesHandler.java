package de.eqc.srcds.handlers;

import com.sun.net.httpserver.HttpExchange;


/**
 * @author Holger Cremer
 */
public class SyncConfigFilesHandler extends AbstractRegisteredHandler {

    /*
     * @see de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun.net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(HttpExchange httpExchange) throws Exception {
	String run = getParameter("runSync");
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return "/syncConfigFiles";
    }

}
