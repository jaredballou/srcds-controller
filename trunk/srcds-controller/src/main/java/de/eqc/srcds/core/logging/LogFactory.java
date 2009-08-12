package de.eqc.srcds.core.logging;

import static de.eqc.srcds.core.Constants.BUILTIN_LOGGING_FILENAME;
import static de.eqc.srcds.core.Constants.FS_LOGGING_FILENAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Holger Cremer
 */
public class LogFactory {

    static {

	File fsLoggingConfig = new File(FS_LOGGING_FILENAME);
	InputStream builtinLoggingConfig = LogFactory.class.getResourceAsStream(BUILTIN_LOGGING_FILENAME);

	if (fsLoggingConfig.exists()) {
	    try {
		LogManager.getLogManager().readConfiguration(
			new FileInputStream(fsLoggingConfig));
	    } catch (Exception e) {
		System.out
			.println("Unable to read logging configuration file - using built-in default");
	    }
	} else {
	    System.out.println("Using builtin logging configuration file");
	    try {
		LogManager.getLogManager().readConfiguration(builtinLoggingConfig);
	    } catch (Exception e) {
		System.out
			.println("Unable to built-in logging configuration file - using default");
	    }
	}
	// find the global log level
	Level level = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getLevel();
	if (level == null) {
	    level = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getParent().getLevel();
	}
	System.out.println("Loggin with global level: " + level);
    }

    public static Logger getLogger(Class<?> clazz) {

	return Logger.getLogger(clazz.getName());
    }
}
