package de.eqc.srcds.configuration;



public class ConfigurationEntry<T> {

    private final String key;
    private final String description;
    private final T defaultValue;

    public ConfigurationEntry(String key, T defaultValue, String description) {

	this.key = key;
	this.defaultValue = defaultValue;
	this.description = description;
    }
    
    public final String getKey() {

	return key;
    }

    public final Class<?> getDataType() {

	return defaultValue.getClass();
    }
    
    public T getDefaultValue() {

	return defaultValue;
    }
    
    public final String getDescription() {

	return description;
    }    

}