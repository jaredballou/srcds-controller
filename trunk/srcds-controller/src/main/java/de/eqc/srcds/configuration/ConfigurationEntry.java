package de.eqc.srcds.configuration;

import java.util.Collection;
import java.util.LinkedList;



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
    
    public boolean isEnumerationType() {
	
	return defaultValue.getClass().isEnum();
    }
    
    public Collection<String> getEnumValues() {
	
	if (!isEnumerationType()) {
	    throw new IllegalStateException("Data type is not an enumeration");
	}
	
	Collection<String> enumValues = new LinkedList<String>();
	for (Object constants : defaultValue.getClass().getEnumConstants()) {
	    enumValues.add(constants.toString());
	}
	return enumValues;
    }
    
    public T getDefaultValue() {

	return defaultValue;
    }
    
    public final String getDescription() {

	return description;
    }    

}