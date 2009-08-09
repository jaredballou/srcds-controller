package de.eqc.srcds.xmlbeans.impl;

import java.util.Map.Entry;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.ConfigurationEntry;
import de.eqc.srcds.configuration.ConfigurationRegistry;
import de.eqc.srcds.xmlbeans.XmlBean;


public class ControllerConfig extends XmlBean {

    private final Configuration config;
    
    public ControllerConfig(Configuration config) {

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
	for (Entry<String, String> entry : config.getData().entrySet()) {
	    ConfigurationEntry<?> registryEntry = ConfigurationRegistry.getEntryByKey(entry.getKey());
	    if (registryEntry.isEnumerationType()) {
		enums = true;
		sbEnums.append(indent(String.format("<Enumeration name=\"%s\">\n", registryEntry.getDataType().getSimpleName()), indent + 2));
		for (String enumValue : registryEntry.getEnumValues()) {
		    sbEnums.append(indent(String.format("<Value>%s</Value>\n", enumValue), indent + 3));
		}
		sbEnums.append(indent("</Enumeration>\n", indent + 2));
	    }

	    sbEntries.append(indent(String.format("<Entry type=\"%s\" description=\"%s\" enumeration=\"%s\">\n", registryEntry.getDataType().getSimpleName(), registryEntry.getDescription(), registryEntry.isEnumerationType()), indent + 1));
	    sbEntries.append(indent(String.format("<Key>%s</Key>\n", entry.getKey()), indent + 2));
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
