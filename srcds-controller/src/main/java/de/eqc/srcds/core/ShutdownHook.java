package de.eqc.srcds.core;

import java.util.logging.Logger;

public class ShutdownHook extends Thread {

    private static Logger log = LogFactory.getLogger(ShutdownHook.class);
    
    private Thread mainThread;
    private HttpServerController httpServerController;
    private SourceDServerController srcdsController;

    public ShutdownHook(Thread mainThread, SourceDServerController srcdsController, HttpServerController httpServerController) {

	this.mainThread = mainThread;
	this.srcdsController = srcdsController;
	this.httpServerController = httpServerController;
    }

    @Override
    public void run() {

	httpServerController.stopGraceful();
	srcdsController.stopGraceful();

	try {
	    mainThread.join();
	} catch (InterruptedException e) {

	    log.info("Shutdown interrupted");
	}
	
	// TODO: flush log
    }
}