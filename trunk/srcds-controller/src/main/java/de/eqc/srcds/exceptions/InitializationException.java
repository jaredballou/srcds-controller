package de.eqc.srcds.exceptions;

public class InitializationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263271934736639145L;
	
	public InitializationException(final String msg) {
		
		super(msg);
	}

	public InitializationException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}

}
