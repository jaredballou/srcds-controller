package de.eqc.srcds.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Game {

    private final Map<String, String> parameters;
    private String directory;

    private List<String> significantConfigFiles = new ArrayList<String>();

    public Game(String directory) {

	this.directory = directory;
	this.parameters = new HashMap<String, String>();
    }

    public Map<String, String> getParameters() {

	return parameters;
    }

    protected void addParameter(String key, String value) {

	if (parameters.get(key) != null) {
	    throw new RuntimeException(String.format(
		    "Parameter %s already set", key));
	}
	parameters.put(key, value);
    }

    public LinkedList<String> getParametersAsList() {

	LinkedList<String> params = new LinkedList<String>();

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


