package de.eqc.srcds.configuration;

import java.util.Map;

import de.eqc.srcds.configuration.impl.ConfigurationKey;
import de.eqc.srcds.exceptions.ConfigurationException;

public interface Configuration {

	public <T> T getValue(String key, Class<T> dataType) throws ConfigurationException;
	
	public <T> void setValue(String key, T value) throws ConfigurationException;
	
	public void removeValue(String key) throws ConfigurationException;
	
	public String toXml();
	
	public <T> Map<ConfigurationKey<?>, String> getData();
	
}