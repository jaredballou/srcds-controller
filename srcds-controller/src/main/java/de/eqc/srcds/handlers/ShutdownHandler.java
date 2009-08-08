package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class ShutdownHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/shutdown";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {

	ResponseCode code = ResponseCode.OK;
	Message message = new Message("Server is going down...");
	outputXmlContent(new ControllerResponse(code, message).toXml());
	
	System.exit(0);
    }
}
