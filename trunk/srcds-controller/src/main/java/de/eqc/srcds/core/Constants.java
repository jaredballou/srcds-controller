package de.eqc.srcds.core;


public final class Constants {

    /** Hides the constructor of the utility class. */
    private Constants() {

	throw new UnsupportedOperationException();
    }

    public static final int MILLIS_PER_SEC = 1000;

    public static final int STARTUP_WAIT_TIME_MILLIS = 2000;

    public static final int SHUTDOWN_DELAY_MILLIS = 3000;
    
    public static final int HTTP_SERVER_SHUTDOWN_DELAY_MILLIS = 2000;

    public static final int SERVER_POLL_INTERVAL_MILLIS = 1000;

    public static final String LOGGING_FILENAME = "srcds-controller-logging.properties";

    public static final String DEFAULT_CONFIG_FILENAME = "srcds-controller-config.xml";
    
}
