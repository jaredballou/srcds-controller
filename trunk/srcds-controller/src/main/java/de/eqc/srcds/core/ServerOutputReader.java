package de.eqc.srcds.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import de.eqc.srcds.core.logging.LogFactory;

public class ServerOutputReader extends Thread implements ServerOutput {

    private static final int SAVE_LAST_LINES = 20;

    private static Logger log = LogFactory.getLogger(ServerOutputReader.class);

    private final transient InputStream inputStream;
    private final transient AtomicBoolean running = new AtomicBoolean(false);

    // we use a concurrent version of the deque because many threads may access
    // this object
    private final transient LinkedBlockingDeque<String> savedLogLines = new LinkedBlockingDeque<String>(SAVE_LAST_LINES);
    // same here
    private final transient List<ProcessOutputObserver> outputObservers = new ArrayList<ProcessOutputObserver>(3);

    public ServerOutputReader(final InputStream inputStream) {

	setName(getClass().getSimpleName());
	this.running.set(false);
	this.inputStream = inputStream;
    }

    @Override
    public void run() {

	final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	running.set(true);
	log.info("Reading server output started");
	try {
	    String line;
	    while ((line = reader.readLine()) != null && running.get()) {
		// TODO: apply a filter here?
		this.saveLogLine(line);
		if (line.matches("^.*STEAM.*connected.*$")) {
		    log.info(line.replaceAll("[^\\p{ASCII}]", "").trim());
		}
	    }
	} catch (IOException e) {
	    log.info(String.format("Error while reading server output: %s", e.getLocalizedMessage()));
	} finally {
	    log.info("Reading server output stopped");
	}
    }

    /**
     * Saves the given output to the history (up to {@link #SAVE_LAST_LINES})
     * and notifies all observers.
     * 
     * @param line
     */
    private void saveLogLine(final String line) {

	if (this.savedLogLines.size() == SAVE_LAST_LINES) {
	    this.savedLogLines.removeFirst();
	}
	this.savedLogLines.add(line);
	this.notifyObservers(line);
    }

    public void stopGraceful() {

	running.set(false);
    }

    public boolean isRunning() {

	return running.get();
    }

    /**
     * Notifies the registered observers about the new line of output.
     * 
     * @param newLine
     */
    private void notifyObservers(final String newLine) {

	synchronized (this.outputObservers) {
	    for (ProcessOutputObserver observer : this.outputObservers) {
		observer.outputHasChanged(newLine);
	    }
	}
    }

    /*
     * ServerOutput implementation
     */

    @Override
    public Collection<String> getLastLog() {

	return Collections.unmodifiableCollection(this.savedLogLines);
    }

    @Override
    public void registerOnLogObserver(final ProcessOutputObserver observer) {

	this.outputObservers.add(observer);
    }

    @Override
    public void unRegisterOnLogObserver(final ProcessOutputObserver observer) {

	this.outputObservers.remove(observer);
    }

    /*
     * @see de.eqc.srcds.core.ServerOutput#getMaxHistorySize()
     */
    @Override
    public int getMaxHistorySize() {

	return SAVE_LAST_LINES;
    }
}