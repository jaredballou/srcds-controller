package de.eqc.srcds.core;

public final class ProcessUtil {

    private static final int SIG_ABORT = 3;
    
    /** Hides the constructor of the utility class. */
    private ProcessUtil() {

	throw new UnsupportedOperationException();
    }

    public static int sendSigAbort(final Process process, final long timeout) {

	int ret = 0;
	try {
	    process.getOutputStream().write(SIG_ABORT);
	    process.getOutputStream().flush();
	    Thread.sleep(timeout);
	} catch (Exception e) {
	    ret = -1;
	}
	return ret;
    }
}
