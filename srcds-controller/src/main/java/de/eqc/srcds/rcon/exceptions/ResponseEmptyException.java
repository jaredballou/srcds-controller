package de.eqc.srcds.rcon.exceptions;

public class ResponseEmptyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263271934736639145L;
	
	public ResponseEmptyException(final String msg) {
		
		super(msg);
	}

	public ResponseEmptyException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}

}
