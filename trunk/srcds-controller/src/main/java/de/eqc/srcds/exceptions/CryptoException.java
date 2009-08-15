package de.eqc.srcds.exceptions;

public class CryptoException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5263271934736639145L;
	
	public CryptoException(final String msg) {
		
		super(msg);
	}

	public CryptoException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}	
}
