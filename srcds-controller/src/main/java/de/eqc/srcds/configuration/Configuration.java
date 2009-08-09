package de.eqc.srcds.configuration;

import java.util.Collection;
import java.util.Map;

import de.eqc.srcds.exceptions.ConfigurationException;

public interface Configuration {

	public <T> T getValue(String key, Class<T> dataType) throws ConfigurationException;
	
	public <T> void setValue(String key, T value) throws ConfigurationException;

	public String toXml();
	
	public Map<String, String> getData();
	
//	public Map<String, Collection<String>> getEnumValues();
}