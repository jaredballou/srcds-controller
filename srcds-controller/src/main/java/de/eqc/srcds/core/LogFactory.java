package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.Constants.LOGGING_FILENAME;

import java.io.File;
import java.io.FileInputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Holger Cremer
 */
public class LogFactory {
  
  static {
    File loggingConfig = new File(LOGGING_FILENAME);
    if (loggingConfig.exists()) {
      try {
        LogManager.getLogManager().readConfiguration(new FileInputStream(loggingConfig));
      } catch (Exception e) {
        System.out.println("Unable to read logging configuration file - using default");
      }
    } else {
      System.out.println("Unable to find logging configuration file - using default");
    }
  }
  
  public static Logger getLogger(Class clazz) {
    return  Logger.getLogger(clazz.getName());
  }
}
