package de.eqc.srcds.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class SetConfigurationValueHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

    @Override
    public String getPath() {
	return "/setConfig";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {

	String key = getParameter("key");
	String value = getParameter("value");

	ResponseCode code = ResponseCode.OK;
	Message message = new Message();
	if (key == null || value == null) {
	    code = ResponseCode.FAILED;
	    message.addMessage("Either key or value parameter is missing");
	} else {
	    try {
		getConfig().setValue(key, value);
		message.addMessage(String.format("Set key %s to value %s", key, value));
	    } catch (ConfigurationException e) {
		code = ResponseCode.FAILED;
		message.addMessage(e.getLocalizedMessage());
	    }
	}

	outputXmlContent(new ControllerResponse(code, message).toXml());
    }
}