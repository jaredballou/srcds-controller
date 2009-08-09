package de.eqc.srcds.core;

import java.util.TimerTask;
import java.util.logging.Logger;

import de.eqc.srcds.core.logging.LogFactory;

public class ShutdownTimer extends TimerTask {

    private static Logger log = LogFactory.getLogger(ShutdownTimer.class);

    public ShutdownTimer(long delayInSecs) {

	log.warning(String.format("Controller is going down in %d seconds",
		delayInSecs));
    }

    @Override
    public void run() {

	System.exit(0);
    }
}