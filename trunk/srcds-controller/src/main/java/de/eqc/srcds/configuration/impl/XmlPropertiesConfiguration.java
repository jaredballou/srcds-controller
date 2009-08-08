package de.eqc.srcds.configuration.impl;

import static de.eqc.srcds.configuration.Constants.HTTP_SERVER_PORT;
import static de.eqc.srcds.configuration.Constants.SRCDS_EXECUTABLE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.logging.Logger;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.SourceDServerController;
import de.eqc.srcds.exceptions.ConfigurationException;
import de.eqc.srcds.xmlbeans.impl.ControllerConfig;

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
		
		if (!SRCDS_EXECUTABLE.equals(key)) {
			properties.setProperty(key, value.toString());
			store();

			log.info(String.format("%s = %s", key, value));
		} else {
			throw new ConfigurationException(String.format("Runtime modification of parameter %s is prohibited", key));
		}
	}

	private void loadConfiguration() throws ConfigurationException {
		
		properties = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream(propertiesFile);
			this.properties.loadFromXML(fis);
			fis.close();
		} catch (InvalidPropertiesFormatException e) {
			log.warning("Configuration file seems to be corrupted - creating default");
			createDefaultConfiguration();
		} catch (Exception e) {
			throw new ConfigurationException("Unable to load configuration file");
		}
	}

	private void createDefaultConfiguration() throws ConfigurationException {
		
		if (propertiesFile.exists()) {
			propertiesFile.delete();
		}

		properties = new Properties();
		properties.setProperty(HTTP_SERVER_PORT, "8888");
		properties.setProperty(SRCDS_EXECUTABLE, "srcds_run");
		
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
		
		log.info("Configuration stored to file.");
	}
	
	@Override
	public String toXml() {
		
		return new ControllerConfig(this).toXml();
	}

	@Override
	public Map<String, String> getData() {

	    Map<String, String> data = new HashMap<String, String>();
	    
	    for (Entry<Object, Object> entry : properties.entrySet()) {
		data.put(entry.getKey().toString(), entry.getValue().toString());
	    }
	    
	    return data;
	}
}