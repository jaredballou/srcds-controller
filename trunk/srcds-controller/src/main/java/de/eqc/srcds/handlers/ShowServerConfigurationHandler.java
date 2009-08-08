package de.eqc.srcds.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.enums.GameType;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class ShowServerConfigurationHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

    @Override
    public String getPath() {
	return "/showServerConfig";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {

	ResponseCode code = ResponseCode.OK;
	Message message = new Message();
	try {
	    GameType gameType = getServerController().getGameType();
	    File srcdsPath = getServerController().getSrcdsPath();

	    File configFile = new File(String.format("%s%s%s%s%s%s%s",
		    srcdsPath.getPath(), File.separator, gameType
			    .getImplementation().getDirectory(),
		    File.separator, "cfg", File.separator, "server.cfg"));
	    if (configFile.exists()) {
		message.addMessage(String.format(
			"Server configuration file: %s\n\n", configFile
				.getPath()));
		BufferedReader br = new BufferedReader(new FileReader(
			configFile));
		String line;
		while ((line = br.readLine()) != null) {
		    message.addMessage(line + "\n");
		}
	    } else {
		code = ResponseCode.FAILED;
		message.addMessage(String.format(
			"Unable to find server configuration file: %s",
			configFile.getPath()));
	    }
	} catch (Exception e) {
	    code = ResponseCode.FAILED;
	    message.addMessage(e.getLocalizedMessage());
	}

	if (code == ResponseCode.OK) {
	    outputContent(message.toString(), "text/plain");
	} else {
	    outputXmlContent(new ControllerResponse(code, message).toXml());
	}
    }
}