package de.eqc.srcds.core;

import java.util.Collection;

/**
 * The started process may produce output on the console. This interface
 * provides access to the history
 * 
 * @author Holger Cremer
 */
public interface ServerOutput {

    /**
     * Get the maximum size of the history.
     * 
     * @return the maximum line count.
     */
    int getMaxHistorySize();

    /**
     * Gets a (unmodifiable) collection of strings with the last (up to
     * {@link #getMaxHistorySize()}) output lines.
     * 
     * @return
     */
    Collection<String> getLastLog();

    /**
     * Adds an observer to get notice about every new line of process output.
     * 
     * @param o
     */
    void registerOnLogObserver(ProcessOutputObserver o);

    /**
     * Unregisters an observer.
     * 
     * @param o
     */
    void unRegisterOnLogObserver(ProcessOutputObserver o);
}
