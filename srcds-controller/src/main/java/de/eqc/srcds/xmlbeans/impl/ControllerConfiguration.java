package de.eqc.srcds.xmlbeans.impl;

import java.util.Map.Entry;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.ConfigurationKey;
import de.eqc.srcds.configuration.ConfigurationRegistry;
import de.eqc.srcds.xmlbeans.XmlBean;


public class ControllerConfiguration extends XmlBean {

    private final Configuration config;
    
    public ControllerConfiguration(Configuration config) {

	super(true);
	this.config = config;
    }

    /**
     * 
     */
    private static final long serialVersionUID = -1655029232522729190L;

    @Override
    protected String toXml(int indent) {

	StringBuilder sbEntries = new StringBuilder(header(indent));
	StringBuilder sbEnums = new StringBuilder(indent("<Metadata>\n", indent + 1));

	boolean enums = false;
	for (Entry<ConfigurationKey<?>, String> entry : config.getData().entrySet()) {
	    if (entry.getKey().isEnumerationType()) {
		enums = true;
		sbEnums.append(indent(String.format("<Enumeration name=\"%s\">\n", entry.getKey().getDataType().getSimpleName()), indent + 2));
		for (String enumValue : entry.getKey().getEnumValues()) {
		    sbEnums.append(indent(String.format("<Value>%s</Value>\n", enumValue), indent + 3));
		}
		sbEnums.append(indent("</Enumeration>\n", indent + 2));
	    }

	    sbEntries.append(indent(String.format("<Entry type=\"%s\" description=\"%s\" enumeration=\"%s\">\n", entry.getKey().getDataType().getSimpleName(), entry.getKey().getDescription(), entry.getKey().isEnumerationType()), indent + 1));
	    sbEntries.append(indent(String.format("<Key>%s</Key>\n", entry.getKey().getKey()), indent + 2));
	    sbEntries.append(indent(String.format("<Value>%s</Value>\n", entry.getValue()), indent + 2));
	    sbEntries.append(indent("</Entry>\n", indent + 1));
	}

	if (enums) {
	    sbEnums.append(indent("</Metadata>\n", indent + 1));
	    sbEntries.append(sbEnums);
	}
	
	return sbEntries.append(footer(indent)).toString();
    }

}
