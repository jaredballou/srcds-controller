package de.eqc.srcds.xmlbeans.impl;

import de.eqc.srcds.xmlbeans.XmlBean;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;


public class ControllerResponse extends XmlBean {

    /**
     * 
     */
    private static final long serialVersionUID = -6455374990668467104L;
    protected ResponseCode code;
    protected String message;
    
    public ControllerResponse(ResponseCode code, String message) {

	this.code = code;
	this.message = message;
    }

    @Override
    public String toXml() {

	StringBuilder sb = new StringBuilder(header());
	sb.append(indent(String.format("<ResponseCode>%s</ResponseCode>\n", code), 1));
	sb.append(indent(String.format("<Message>%s</Message>\n", message), 1));
	return sb.append(footer()).toString();
    }

}
