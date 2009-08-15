package de.eqc.srcds.core;

import java.io.InputStream;
import java.util.Properties;

public final class VersionUtil {

    private static final String PROJECT_NAME = "srcds-controller";
    private static final String ROOT_PACKAGE = "de.eqc.srcds";    
    private static final String POM_PROPERTIES_LOCATION = "/META-INF/maven/${root-package}/${project-name}/pom.properties";

    /** Hides the constructor of the utility class. */
    private VersionUtil() {

	throw new UnsupportedOperationException();
    }

    public static String getProjectVersion() {

	String projectVersion;
	try {
	    String pomPropertiesLocation = POM_PROPERTIES_LOCATION;
	    pomPropertiesLocation = pomPropertiesLocation.replaceAll("\\$\\{root-package\\}", ROOT_PACKAGE);
	    pomPropertiesLocation = pomPropertiesLocation.replaceAll("\\$\\{project-name\\}", PROJECT_NAME);
	    final InputStream intputStream = VersionUtil.class.getResourceAsStream(pomPropertiesLocation);
	    final Properties props = new Properties();
	    props.load(intputStream);
	    projectVersion = props.getProperty("version");
	} catch (Exception e) {
	    projectVersion = "(unknown)";
	}
	return projectVersion;
    }

}