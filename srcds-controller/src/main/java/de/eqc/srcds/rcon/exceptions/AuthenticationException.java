package de.eqc.srcds.rcon.exceptions;

public class AuthenticationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263271934736639145L;
	
	public AuthenticationException(final String msg) {
		
		super(msg);
	}

	public AuthenticationException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}

}
