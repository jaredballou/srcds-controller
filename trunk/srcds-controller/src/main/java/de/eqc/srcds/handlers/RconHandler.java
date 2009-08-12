package de.eqc.srcds.handlers;


import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_RCON_PASSWORD;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_SERVER_PORT;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.configuration.datatypes.Password;
import de.eqc.srcds.core.NetworkUtil;
import de.eqc.srcds.rcon.RconConnection;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class RconHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/rcon";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {
	
	String command = getParameter("command");
	if (command != null && !"".equals(command)) {
	    try {
		String rconAddress = NetworkUtil.getLocalHostname();
		int rconPort = getConfig().getValue(SRCDS_SERVER_PORT, Integer.class);
		String rconPassword = getConfig().getValue(SRCDS_RCON_PASSWORD, Password.class).toString();

		RconConnection rc = new RconConnection(rconAddress, rconPort, rconPassword);
		String response = rc.send(command);
		ResponseCode code = ResponseCode.OK;
		String lines[] = response.split("\n");
		Message message = new Message();
		for (String line : lines) {
		    message.addLine(line);
		}
		outputXmlContent(new ControllerResponse(code, message).toXml());
	    } catch (Exception e) {
		throw new IllegalStateException(e);
	    }
	} else {
	    throw new IllegalArgumentException("Please specify parameter 'command'");
	}
    }
}