package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class StopHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/stop";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {

	ResponseCode code = ResponseCode.OK;
	Message message = new Message();
	try {
	    getServerController().stopServer();
	    message.addLine("Server stopped successfully");
	} catch (Exception e) {
	    throw new IllegalStateException(e);
	}

	outputXmlContent(new ControllerResponse(code, message).toXml());
    }
}