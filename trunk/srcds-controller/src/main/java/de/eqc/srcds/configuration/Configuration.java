package de.eqc.srcds.configuration;

import de.eqc.srcds.exceptions.ConfigurationException;

public interface Configuration {

	public <T> T getValue(String key, Class<T> dataType) throws ConfigurationException;
	
	public <T> void setValue(String key, T value) throws ConfigurationException;

}