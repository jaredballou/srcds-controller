package de.eqc.srcds.enums;

public enum OperatingSystem {
	
	WINDOWS("^Windows.*$"),
	LINUX("^.*Linux.*$"),
	UNSUPPORTED(null);
	
	private static final String OS_NAME_PROPERTY = "os.name"; 
	private final String pattern;
	
	private OperatingSystem(final String pattern) {
		
		this.pattern = pattern;
	}
	
	public String getPattern() {
		return pattern;
	}
	
	public static OperatingSystem getCurrent() {
		OperatingSystem ret = UNSUPPORTED;
		final String currentOS = System.getProperty(OS_NAME_PROPERTY);
		for (OperatingSystem os : values()) {
			if (os != UNSUPPORTED && currentOS.matches(os.getPattern())) {
				ret = os;
				break;
			}
		}
		return ret;
	}
}
