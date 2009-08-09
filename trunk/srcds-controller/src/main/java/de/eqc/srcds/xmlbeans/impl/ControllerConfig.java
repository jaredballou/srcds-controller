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

	StringBuilder sb = new StringBuilder(header(indent));

	for (Entry<String, String> entry : config.getData().entrySet()) {
	    ConfigurationEntry<?> registryEntry = ConfigurationRegistry.getEntryByKey(entry.getKey());
	    
	    sb.append(indent(String.format("<Entry type=\"%s\" description=\"%s\">\n", registryEntry.getDataType().getSimpleName(), registryEntry.getDescription()), indent + 1));
	    sb.append(indent(String.format("<Key>%s</Key>\n", entry.getKey()), indent + 2));
	    sb.append(indent(String.format("<Value>%s</Value>\n", entry.getValue()), indent + 2));
	    sb.append(indent("</Entry>\n", indent + 1));
	}
	
	return sb.append(footer(indent)).toString();
    }

}
