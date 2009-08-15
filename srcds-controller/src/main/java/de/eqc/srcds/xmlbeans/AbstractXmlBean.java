package de.eqc.srcds.xmlbeans;

import java.io.Serializable;


public abstract class AbstractXmlBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6007738341162785134L;
    private static final int INDENT_WIDTH = 2;
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"; 
    private static final String XSLT_HEADER = "<?xml-stylesheet type=\"text/xsl\" href=\"/xslt?name=%CLASSNAME%.xsl\" ?>\n";
    protected final boolean stylesheet;
    
    
    public AbstractXmlBean(final boolean stylesheet) {

	this.stylesheet = stylesheet;
    }
    
    public String indent(final String line, final int level) {

	final StringBuilder sb = new StringBuilder();
	for (int i = 0; i < INDENT_WIDTH * level; i++) {
	    sb.append(" ");
	}
	return sb.append(line).toString();
    }
    
    public String header(final int indent) {

	final StringBuilder sb = new StringBuilder();
	sb.append(indent(String.format("<" + getClass().getSimpleName() + ">\n"), indent));
	return sb.toString();
    }

    public String footer(final int indent) {

	final StringBuilder sb = new StringBuilder();
	sb.append(indent(String.format("</" + getClass().getSimpleName() + ">\n"), indent));
	return sb.toString();
    }
    
    public String toXml() {
	
	final StringBuilder sb = new StringBuilder(XML_HEADER);
	if (stylesheet) {
	    sb.append(XSLT_HEADER.replaceAll("%CLASSNAME%", getClass().getSimpleName()));
	}
	sb.append(toXml(0));
	return sb.toString();
    }

    protected abstract String toXml(int indent);
}
