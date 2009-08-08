package de.eqc.srcds.xmlbeans.impl;

import de.eqc.srcds.xmlbeans.XmlBean;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;


public class ControllerResponse extends XmlBean {

    /**
     * 
     */
    private static final long serialVersionUID = -6455374990668467104L;
    protected ResponseCode code;
    protected Message message;
    
    public ControllerResponse(ResponseCode code, Message message) {

	this.code = code;
	this.message = message;
    }

    @Override
    public String toXml(int indent) {

	StringBuilder sb = new StringBuilder(header(indent));
	sb.append(indent(String.format("<ResponseCode>%s</ResponseCode>\n", code), indent + 1));
	sb.append(message.toXml(indent + 1));
	return sb.append(footer(indent)).toString();
    }

}
