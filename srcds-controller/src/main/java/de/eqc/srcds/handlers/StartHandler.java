package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class StartHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/start";
    }

    @Override
    public void handleRequest(HttpExchange httpExchange) throws IOException {

	ResponseCode code = ResponseCode.OK;
	Message message = new Message();
	try {
	    getServerController().startServer();
	    message.addMessage("Server started successfully");
	} catch (Exception e) {
	    code = ResponseCode.FAILED;
	    message.addMessage(e.getMessage());
	}

	outputXmlContent(new ControllerResponse(code, message).toXml());
    }
}