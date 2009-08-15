package de.eqc.srcds.xmlbeans.impl;

import de.eqc.srcds.xmlbeans.AbstractXmlBean;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;


public class ControllerResponse extends AbstractXmlBean {

    /**
     * 
     */
    private static final long serialVersionUID = -6455374990668467104L;
    protected ResponseCode code;
    protected Message message;
    
    public ControllerResponse(final ResponseCode code, final Message message) {

	this(code, message, true);
    }

    public ControllerResponse(final ResponseCode code, final Message message, final boolean stylesheet) {

	super(stylesheet);
	this.code = code;
	this.message = message;
    }
    
    @Override
    protected String toXml(final int indent) {

	final StringBuilder sb = new StringBuilder(header(indent));
	sb.append(indent(String.format("<ResponseCode>%s</ResponseCode>\n", code), indent + 1));
	sb.append(message.toXml(indent + 1));
	return sb.append(footer(indent)).toString();
    }

}
