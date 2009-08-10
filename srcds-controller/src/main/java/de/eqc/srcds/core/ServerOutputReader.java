package de.eqc.srcds.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import de.eqc.srcds.core.logging.LogFactory;

public class ServerOutputReader extends Thread {

    private static Logger log = LogFactory.getLogger(ServerOutputReader.class);

    private final InputStream inputStream;
    private boolean running;

    public ServerOutputReader(InputStream inputStream) {

	setName(getClass().getSimpleName());
	this.running = false;
	this.inputStream = inputStream;
    }

    @Override
    public void run() {

	BufferedReader br = new BufferedReader(new InputStreamReader(
		inputStream));
	running = true;
	log.info("Reading server output started");
	try {
	    String line;
	    while ((line = br.readLine()) != null && running) {
		if (line.contains("connected, address")
			|| line.contains("disconnect")) {
		    log.info(line.trim());
		}
	    }
	} catch (IOException e) {
	    log.info(String.format("Error while reading server output: %s", e
		    .getLocalizedMessage()));
	} finally {
	    log.info("Reading server output stopped");
	}
    }

    public void stopGraceful() {

	running = false;
    }

    public boolean isRunning() {

	return running;
    }
}