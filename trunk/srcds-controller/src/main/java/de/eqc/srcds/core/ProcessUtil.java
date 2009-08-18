package de.eqc.srcds.core;

public final class ProcessUtil {

    private static final int SIGINT = 3;
    
    /** Hides the constructor of the utility class. */
    private ProcessUtil() {

	throw new UnsupportedOperationException();
    }

    public static int sendSigInt(final Process process, final long timeout) {

	int ret = 0;
	try {
	    process.getOutputStream().write(SIGINT);
	    process.getOutputStream().flush();
	    Thread.sleep(timeout);
	} catch (Exception e) {
	    ret = -1;
	}
	return ret;
    }
}
