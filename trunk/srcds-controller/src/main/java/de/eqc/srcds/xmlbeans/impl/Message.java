package de.eqc.srcds.xmlbeans.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.eqc.srcds.xmlbeans.XmlBean;

public class Message extends XmlBean {

    /**
     * 
     */
    private static final long serialVersionUID = -3453607805613176580L;
    private final List<String> items;

    public Message() {

	super(false);
	this.items = new LinkedList<String>();
    }

    public Message(String... messages) {

	super(false);
	if (messages == null) {
	    this.items = new LinkedList<String>();
	} else {
	    this.items = Arrays.asList(messages);
	}
    }

    public void addLine(String message) {

	items.add(message);
    }

    @Override
    protected String toXml(int indent) {

	StringBuilder sb = new StringBuilder(header(indent));

	if (items.size() == 0) {
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

	StringBuilder sb = new StringBuilder();
	for (String message : items) {
	    sb.append(message + "\n");
	}
	return sb.toString();
    }

}