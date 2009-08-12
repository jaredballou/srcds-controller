package de.eqc.srcds.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.handlers.utils.HandlerUtil;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class ListHandlersHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

    @Override
    public String getPath() {
	return "/usage";
    }

    public void handleRequest(HttpExchange httpExchange) throws IOException {
	
	try {
	    Collection<RegisteredHandler> handlers = HandlerUtil.getRegisteredHandlerImplementations();
	    List<String> lines = new LinkedList<String>();
	    for (RegisteredHandler handler : handlers) {
		lines.add(handler.getPath());
	    }
	    String[] linesAsArray = lines.toArray(new String[0]); 
	    ControllerResponse cr = new ControllerResponse(ResponseCode.INFORMATION, new Message(linesAsArray));
	    outputXmlContent(cr.toXml());
	} catch (Exception e) {
	    throw new IOException(String.format("Unable to register handler %s", getClass()));
	}
	
    }
}