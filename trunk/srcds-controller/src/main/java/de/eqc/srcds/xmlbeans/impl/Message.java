package de.eqc.srcds.xmlbeans.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.eqc.srcds.xmlbeans.AbstractXmlBean;

public class Message extends AbstractXmlBean {

    /**
     * 
     */
    private static final long serialVersionUID = -3453607805613176580L;
    private final List<String> items;

    public Message() {

	super(false);
	this.items = new LinkedList<String>();
    }

    public Message(final String... messages) {

	super(false);
	if (messages == null) {
	    this.items = new LinkedList<String>();
	} else {
	    this.items = Arrays.asList(messages);
	}
    }

    public void addLine(final String message) {

	items.add(message);
    }

    @Override
    protected String toXml(final int indent) {

	final StringBuilder sb = new StringBuilder(header(indent));

	if (items.isEmpty()) {
	    addLine("(no message specified)");
	}

	for (String message : items) {
	    sb.append(indent(String.format("<Item><![CDATA[%s]]></Item>\n", message),
		    indent + 1));
	}

	return sb.append(footer(indent)).toString();
    }

    @Override
    public String toString() {

	final StringBuilder sb = new StringBuilder();
	for (String message : items) {
	    sb.append(message + "\n");
	}
	return sb.toString();
    }

}