package de.eqc.srcds.exceptions;

public class StartupFailedException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4638694991556457399L;

	public StartupFailedException(final String msg) {
		
		super(msg);
	}

	public StartupFailedException(final String msg, final Exception exception) {
		
		super(msg, exception);
	}

}
