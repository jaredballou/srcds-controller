package de.eqc.srcds.xmlbeans;

import java.io.Serializable;


public abstract class XmlBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6007738341162785134L;
    private static final int INDENT_WIDTH = 2;
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"; 
    
    public String indent(String line, int level) {

	StringBuilder sb = new StringBuilder();
	for (int i = 0; i < INDENT_WIDTH * level; i++) {
	    sb.append(" ");
	}
	return sb.append(line).toString();
    }
    
    public String header(int indent) {

	StringBuilder sb = new StringBuilder();
	sb.append(indent(String.format("<" + getClass().getSimpleName() + ">\n"), indent));
	return sb.toString();
    }

    public String footer(int indent) {

	StringBuilder sb = new StringBuilder();
	sb.append(indent(String.format("</" + getClass().getSimpleName() + ">\n"), indent));
	return sb.toString();
    }
    
    public String toXml() {
	
	return XML_HEADER + toXml(0);
    }

    public abstract String toXml(int indent);
}
