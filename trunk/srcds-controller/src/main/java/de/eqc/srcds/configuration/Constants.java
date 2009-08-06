package de.eqc.srcds.configuration;

public final class Constants {

    /** Hides the constructor of the utility class. */
    private Constants() {
        throw new UnsupportedOperationException();
    }
    
    public static final String LOGGING_FILENAME = "srcds-controller-logging.properties";
    
    public static final String DEFAULT_CONFIG_FILENAME = "srcds-controller-config.xml";
    
	public static final String HTTP_SERVER_PORT = "srcds.controller.networking.httpserver.port";

	public static final String AUTOSTART = "srcds.controller.autostart";

	public static final String SRCDS_PATH = "srcds.controller.srcds.path";

	public static final String SRCDS_EXECUTABLE = "srcds.controller.srcds.executable";

	public static final String SRCDS_PARAMETERS = "srcds.controller.srcds.parameters";
	
	public static final String SRCDS_GAMETYPE = "srcds.controller.srcds.gametype";
}
