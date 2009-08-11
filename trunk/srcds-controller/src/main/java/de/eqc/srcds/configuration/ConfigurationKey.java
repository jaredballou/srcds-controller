package de.eqc.srcds.configuration;

import java.util.Collection;
import java.util.LinkedList;



public class ConfigurationKey<T> implements Comparable<ConfigurationKey<T>> {

    private final String key;
    private final String description;
    private final T defaultValue;
    private final Integer order;

    public ConfigurationKey(String key, T defaultValue, String description, int order) {

	this.key = key;
	this.defaultValue = defaultValue;
	this.description = description;
	this.order = order;
    }
    
    public final String getKey() {

	return key;
    }

    public final Class<?> getDataType() {

	return defaultValue.getClass();
    }
    
    public final boolean isEnumerationType() {
	
	return defaultValue.getClass().isEnum();
    }
    
    public final Collection<String> getEnumValues() {
	
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

    
    public final int getOrder() {

	return order;
    }

    @Override
    public int compareTo(ConfigurationKey<T> entry) {

	return order.compareTo(entry.order);
    }
}