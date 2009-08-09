package de.eqc.srcds.configuration;

import java.util.LinkedList;
import java.util.List;

import de.eqc.srcds.enums.GameType;

public final class ConfigurationRegistry {

    /** Hides the constructor of the utility class. */
    private ConfigurationRegistry() {

	throw new UnsupportedOperationException();
    }
    
    public static final String HTTP_SERVER_PORT = "srcds.controller.networking.httpserver.port";

    public static final String SYNC_URL = "srcds.controller.sync.url";
    
    public static final String SYNC_USERNAME = "srcds.controller.sync.username";
    
    public static final String SYNC_PASSWORD = "srcds.controller.sync.password";

    public static final String AUTOSTART = "srcds.controller.srcds.autostart";

    public static final String SRCDS_PATH = "srcds.controller.srcds.path";

    public static final String SRCDS_EXECUTABLE = "srcds.controller.srcds.executable";

    public static final String SRCDS_PARAMETERS = "srcds.controller.srcds.parameters";

    public static final String SRCDS_GAMETYPE = "srcds.controller.srcds.gametype";

    private static List<ConfigurationEntry<?>> entries;
    static {
	entries = new LinkedList<ConfigurationEntry<?>>();
	entries.add(new ConfigurationEntry<Integer>(HTTP_SERVER_PORT, 8888, "HTTP Server Port"));
	entries.add(new ConfigurationEntry<Boolean>(AUTOSTART, false, "SRCDS Autostart"));
	entries.add(new ConfigurationEntry<String>(SRCDS_PATH, "./", "SRCDS Path"));
	entries.add(new ConfigurationEntry<String>(SRCDS_EXECUTABLE, "srcds_run", "SRCDS Executable"));
	entries.add(new ConfigurationEntry<String>(SRCDS_PARAMETERS, "", "SRCDS Parameters"));
	entries.add(new ConfigurationEntry<GameType>(SRCDS_GAMETYPE, GameType.LEFT4DEAD, "SRCDS Game Type"));
    }    
    
    public static ConfigurationEntry<?> getEntryByKey(String key) {
	
	ConfigurationEntry<?> match = null;
	for (ConfigurationEntry<?> entry : entries) {
	    if (entry.getKey().equals(key)) {
		match = entry;
		break;
	    }
	}
	return match;
    }
    
    public static List<ConfigurationEntry<?>> getEntries() {
	
	return entries;
    }

}