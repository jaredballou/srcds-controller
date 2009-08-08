package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.enums.ServerState;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class RestartHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/restart";
    }

    @Override
    public void handleRequest(HttpExchange httpExchange) throws IOException {

	ResponseCode code = ResponseCode.OK;
	Message message = new Message();
	
	if (getServerController().getServerState() == ServerState.RUNNING) {
		try {
		    getServerController().stopServer();
		    message.addMessage("Server stopped successfully");
		} catch (Exception e) {
		    throw new IllegalStateException(e);
		}	
	}

	try {
	    getServerController().startServer();
	    message.addMessage("Server started successfully");
	} catch (Exception e) {
	    throw new IllegalStateException(e);
	}

	outputXmlContent(new ControllerResponse(code, message).toXml());
    }
}