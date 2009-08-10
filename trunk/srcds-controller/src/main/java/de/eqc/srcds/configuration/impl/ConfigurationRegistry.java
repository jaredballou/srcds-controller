package de.eqc.srcds.configuration.impl;

import java.util.LinkedList;
import java.util.List;

import de.eqc.srcds.configuration.datatypes.Password;
import de.eqc.srcds.enums.GameType;

public final class ConfigurationRegistry {

    /** Hides the constructor of the utility class. */
    private ConfigurationRegistry() {

	throw new UnsupportedOperationException();
    }
    
    public static final String HTTP_SERVER_PORT = "srcds.controller.networking.httpserver.port";

    public static final String HTTP_SERVER_USERNAME = "srcds.controller.networking.httpserver.username";

    public static final String HTTP_SERVER_PASSWORD = "srcds.controller.networking.httpserver.password";

    public static final String SYNC_URL = "srcds.controller.sync.url";
    
    public static final String SYNC_USERNAME = "srcds.controller.srcds.sync.username";
    
    public static final String SYNC_PASSWORD = "srcds.controller.srcds.sync.password";

    public static final String AUTOSTART = "srcds.controller.srcds.autostart";

    public static final String SRCDS_PATH = "srcds.controller.srcds.path";

    public static final String SRCDS_EXECUTABLE = "srcds.controller.srcds.executable";

    public static final String SRCDS_PARAMETERS = "srcds.controller.srcds.parameters";

    public static final String SRCDS_GAMETYPE = "srcds.controller.srcds.gametype";

    private static List<ConfigurationKey<?>> entries;
    static {
	entries = new LinkedList<ConfigurationKey<?>>();
	entries.add(new ConfigurationKey<Integer>(HTTP_SERVER_PORT, 8888, "HTTP Server Port", 0));	
	entries.add(new ConfigurationKey<String>(HTTP_SERVER_USERNAME, "admin", "HTTP Server Username", 1));
	entries.add(new ConfigurationKey<Password>(HTTP_SERVER_PASSWORD, new Password("joshua"), "HTTP Server Password", 2));
	entries.add(new ConfigurationKey<String>(SYNC_URL, "http://", "Synchronization URL", 3));
	entries.add(new ConfigurationKey<String>(SYNC_USERNAME, "admin", "Synchronization Username", 4));
	entries.add(new ConfigurationKey<Password>(SYNC_PASSWORD, new Password("joshua"), "Synchronization Password", 5));	
	entries.add(new ConfigurationKey<Boolean>(AUTOSTART, false, "SRCDS Autostart", 6));
	entries.add(new ConfigurationKey<GameType>(SRCDS_GAMETYPE, GameType.LEFT4DEAD, "SRCDS Game Type", 7));
	entries.add(new ConfigurationKey<String>(SRCDS_PATH, "./", "SRCDS Path", 8));
	entries.add(new ConfigurationKey<String>(SRCDS_EXECUTABLE, "srcds_run", "SRCDS Executable", 9));
	entries.add(new ConfigurationKey<String>(SRCDS_PARAMETERS, "", "SRCDS Parameters", 10));
    }    
    
    public static ConfigurationKey<?> getEntryByKey(String key) {
	
	ConfigurationKey<?> match = null;
	for (ConfigurationKey<?> entry : entries) {
	    if (entry.getKey().equals(key)) {
		match = entry;
		break;
	    }
	}
	return match;
    }
    
    public static List<ConfigurationKey<?>> getEntries() {
	
	return entries;
    }

}