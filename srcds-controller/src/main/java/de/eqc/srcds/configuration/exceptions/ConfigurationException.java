package de.eqc.srcds.configuration.exceptions;

public class ConfigurationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263271934736639145L;
	
	public ConfigurationException(final String msg) {
		
		super(msg);
	}

	public ConfigurationException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}	
}
