package de.eqc.srcds.configuration.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.ConfigurationKey;
import de.eqc.srcds.configuration.ConfigurationRegistry;
import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.core.SourceDServerController;
import de.eqc.srcds.xmlbeans.impl.ControllerConfiguration;

public class XmlPropertiesConfiguration implements Configuration {

	private static Logger log = Logger.getLogger(SourceDServerController.class.getSimpleName());
	private final File propertiesFile;
	private Properties properties;
	
	public XmlPropertiesConfiguration(File propertiesFile) throws ConfigurationException {

		this.propertiesFile = propertiesFile;
		
		if (propertiesFile.exists()) {
			loadConfiguration();
		} else {
			createDefaultConfiguration();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getValue(String key, Class<T> dataType) throws ConfigurationException {
		T value = null;

		if (String.class.isAssignableFrom(dataType)) {
			value = (T) properties.getProperty(key);
		} else {
			try {
				Method conversionMethod = dataType.getDeclaredMethod("valueOf", String.class);
				value = (T) conversionMethod.invoke(dataType, properties.getProperty(key));
			} catch (Exception e) {
				throw new ConfigurationException(String.format("Conversion to datatype %s failed for %s", dataType.getName(), key));
			}
		}
		return value;
	}

	@Override
	public <T> void setValue(String key, T value) throws ConfigurationException {

	    if (isValidKey(key)) {
		properties.setProperty(key, value.toString());
		store();
		log.finest(String.format("%s = %s", key, value));
	    } else {
		throw new ConfigurationException(String.format("Configuration key %s is not a valid key", key));
	    }
	}

	@Override
	public void removeValue(String key) throws ConfigurationException {
	    
	    properties.remove(key);
	    store();
	    log.finest(String.format("Removed key %s", key));
	}
	
	private void loadConfiguration() throws ConfigurationException {
		
		properties = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream(propertiesFile);
			this.properties.loadFromXML(fis);
			fis.close();
			validateConfiguration();
		} catch (InvalidPropertiesFormatException e) {
			log.warning("Configuration file seems to be corrupted - creating default");
			createDefaultConfiguration();
		} catch (Exception e) {
			throw new ConfigurationException("Unable to load configuration file");
		}
	}

	private void validateConfiguration() throws ConfigurationException {
	    
	    List<Object> keysToRemove = new LinkedList<Object>();
	    for (Entry<Object, Object> entry : properties.entrySet()) {
		if (!isValidKey(entry.getKey().toString())) {
		    keysToRemove.add(entry.getKey());
		}
	    }
	    for (Object keyToRemove : keysToRemove) {
		removeValue(keyToRemove.toString());
		log.info(String.format("Configuration entry %s is not an allowed entry - thus removed", keyToRemove.toString()));
	    }
	    for (ConfigurationKey<?> registryEntry : ConfigurationRegistry.getEntries()) {
		if (!containsKey(registryEntry.getKey())) {
		    setValue(registryEntry.getKey(), registryEntry.getDefaultValue());
		    log.info(String.format("Configuration entry %s is missing in configuration - thus added with default value", registryEntry.getKey()));
		}
	    }
	}
	
	private boolean isValidKey(String key) {
	    
	    return ConfigurationRegistry.getEntryByKey(key) != null;
	}
	
	private boolean containsKey(String key) {
	    
	    return properties.get(key) != null;
	}
	
	private void createDefaultConfiguration() throws ConfigurationException {
		
		if (propertiesFile.exists()) {
			propertiesFile.delete();
		}

		properties = new Properties();
		for (ConfigurationKey<?> registryEntry : ConfigurationRegistry.getEntries()) {
			setValue(registryEntry.getKey(), registryEntry.getDefaultValue());
		}
		
		store();	
	}
	
	private void store() throws ConfigurationException {

		try {
			FileOutputStream fos = new FileOutputStream(propertiesFile);
			properties.storeToXML(fos, null);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			throw new ConfigurationException("Unable to store configuration to file");
		}
		
		log.finest("Configuration stored to file.");
	}
	
	@Override
	public String toXml() {
		
		return new ControllerConfiguration(this).toXml();
	}

	@Override
	public Map<ConfigurationKey<?>, String> getData() {

	    Map<ConfigurationKey<?>, String> data = new TreeMap<ConfigurationKey<?>, String>();
	    
	    for (Entry<Object, Object> entry : properties.entrySet()) {
		ConfigurationKey<?> configKey = ConfigurationRegistry.getEntryByKey(entry.getKey().toString());
		data.put(configKey, entry.getValue().toString());
	    }
	    
	    return data;
	}

}