package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.ConfigurationRegistry.HTTP_SERVER_PORT;

import java.net.InetAddress;
import java.net.UnknownHostException;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.exceptions.ConfigurationException;

public final class NetworkUtil {

    /** Hides the constructor of the utility class. */
    private NetworkUtil() {

	throw new UnsupportedOperationException();
    }

    public static String getLocalHostname() throws UnknownHostException {

	return InetAddress.getLocalHost().getHostName();
    }
    
    public static String getHomeUrl(Configuration config) throws UnknownHostException, ConfigurationException {
	
	int port = config.getValue(HTTP_SERVER_PORT, Integer.class);
	return String.format("http://%s:%d", getLocalHostname(), port);
    }
}
