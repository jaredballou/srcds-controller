package de.eqc.srcds.core;

/**
 * An observer for the process output stream. The observer is notified for every
 * new line of process output.
 * 
 * @author Holger Cremer
 */
public interface ProcessOutputObserver {

    /**
     * Called when the process has produced an output line.
     * 
     * @param newLine
     */
    void outputHasChanged(String newLine);
}
