package de.eqc.srcds.rcon.exceptions;

public class ConnectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263271934736639145L;
	
	public ConnectException(final String msg) {
		
		super(msg);
	}

	public ConnectException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}

}
