package de.eqc.srcds.exceptions;

public class AlreadyRunningException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263271934736639145L;
	
	public AlreadyRunningException(final String msg) {
		
		super(msg);
	}

	public AlreadyRunningException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}

}
