package de.eqc.srcds.configuration.impl;

import static de.eqc.srcds.configuration.Constants.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Logger;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.ServerController;
import de.eqc.srcds.enums.GameType;
import de.eqc.srcds.exceptions.ConfigurationException;

public class XmlPropertiesConfiguration implements Configuration {

	private static Logger log = Logger.getLogger(ServerController.class.getSimpleName());
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
		
		/*
		 * This block is for testing purposes only.
		 */
		setValue(AUTOSTART, true);
		setValue(SRCDS_PATH, "l4d");
		setValue(SRCDS_PARAMETERS, "+hostport 27015 +maxplayers 14 -tickrate 100 +exec server_unrest.cfg +map l4d_vs_airport01_greenhouse.bsp");
		setValue(SRCDS_GAMETYPE, GameType.LEFT4DEAD);
		
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
		
		String ret = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			properties.storeToXML(baos, null);
			baos.flush();
			baos.close();
			ret = new String(baos.toByteArray());
		} catch (IOException e) {
			ret = "Unable to create XML output for configuration";
		}
		return ret;
	}
}