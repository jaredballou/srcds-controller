package de.eqc.srcds.games;

import java.util.AbstractSequentialList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractGame {

    private final Map<String, String> parameters;
    private final String directory;

    public AbstractGame(final String directory) {

	this.directory = directory;
	this.parameters = new HashMap<String, String>();
    }

    public Map<String, String> getParameters() {

	return parameters;
    }

    protected void addParameter(final String key, final String value) {

	if (parameters.get(key) == null) {
	    parameters.put(key, value);
	}	
    }

    public AbstractSequentialList<String> getParametersAsList() {

	final LinkedList<String> params = new LinkedList<String>();

	for (String parameter : parameters.keySet()) {
	    params.add("-" + parameter + " " + parameters.get(parameter));
	}

	return params;
    }

    public String getDirectory() {

	return directory;
    }

    public abstract List<String> getFilesForEdit();
    
    public abstract List<String> getFilesForSync();
}


