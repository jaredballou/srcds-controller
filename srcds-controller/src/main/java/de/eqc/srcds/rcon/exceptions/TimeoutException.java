package de.eqc.srcds.rcon.exceptions;

public class TimeoutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263271934736639145L;
	
	public TimeoutException(final String msg) {
		
		super(msg);
	}

	public TimeoutException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}

}
