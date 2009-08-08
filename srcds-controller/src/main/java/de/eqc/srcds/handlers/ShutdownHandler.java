package de.eqc.srcds.handlers;
import static de.eqc.srcds.core.Constants.SHUTDOWN_DELAY_MILLIS;

import java.io.IOException;
import java.util.Timer;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.core.ShutdownTimer;
import de.eqc.srcds.core.Utils;
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
	Message message = new Message(String.format("Controller is going down in %d seconds...", Utils.millisToSecs(SHUTDOWN_DELAY_MILLIS)));
	outputXmlContent(new ControllerResponse(code, message).toXml());

	Timer timer = new Timer();
	timer.schedule(new ShutdownTimer(Utils.millisToSecs(SHUTDOWN_DELAY_MILLIS)), SHUTDOWN_DELAY_MILLIS);
    }
    
}
