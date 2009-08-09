package de.eqc.srcds.core;

import java.io.InputStream;
import java.util.Properties;

public final class RuntimeUtil {

    private static final String POM_PROPERTIES_LOCATION = "/META-INF/maven/de.eqc.srcds/srcds-controller/pom.properties";

    /** Hides the constructor of the utility class. */
    private RuntimeUtil() {

	throw new UnsupportedOperationException();
    }

    public static String getProjectVersion() {

	String projectVersion;
	try {
	    InputStream is = RuntimeUtil.class.getResourceAsStream(POM_PROPERTIES_LOCATION);
	    Properties props = new Properties();
	    props.load(is);
	    projectVersion = props.getProperty("version");
	} catch (Exception e) {
	    projectVersion = "(unknown)";
	}
	return projectVersion;
    }

}