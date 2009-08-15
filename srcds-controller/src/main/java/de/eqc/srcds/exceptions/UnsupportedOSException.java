package de.eqc.srcds.exceptions;

public class UnsupportedOSException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263271934736639145L;
	
	public UnsupportedOSException(final String msg) {
		
		super(msg);
	}

	public UnsupportedOSException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}

}
