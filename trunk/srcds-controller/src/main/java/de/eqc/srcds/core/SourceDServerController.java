package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.ConfigurationRegistry.AUTOSTART;
import static de.eqc.srcds.configuration.ConfigurationRegistry.FORBIDDEN_USER_PARAMETERS;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_EXECUTABLE;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_GAMETYPE;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_PARAMETERS;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_PATH;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_SERVER_PORT;
import static de.eqc.srcds.core.Constants.STARTUP_WAIT_TIME_MILLIS;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.enums.GameType;
import de.eqc.srcds.enums.OperatingSystem;
import de.eqc.srcds.enums.ServerState;
import de.eqc.srcds.exceptions.AlreadyRunningException;
import de.eqc.srcds.exceptions.NotRunningException;
import de.eqc.srcds.exceptions.StartupFailedException;

/**
 * Controls the server and provides therefore functions like start/stop/status
 * etc.
 * 
 * @author Hannes
 * @author Holger
 */
public class SourceDServerController extends AbstractServerController<Process> {

    private ServerOutputReader serverOutputReader;

    public SourceDServerController(Configuration config)
	    throws ConfigurationException {

	super("SRCDS server", config);

	try {
	    if (config.getValue(AUTOSTART, Boolean.class)) {
		setAutostart(config.getValue(AUTOSTART, Boolean.class));
	    }
	} catch (ConfigurationException e) {
	    log.warning(String.format("Autostart configuration is missing: %s",
		    e.getLocalizedMessage()));
	}
    }

    public ServerState getServerState() {

	ServerState state = ServerState.RUNNING;
	synchronized (getMutex()) {
	    if (server != null) {
		int exitValue = -1;
		try {
		    exitValue = server.exitValue();
		    log.info("Server was terminated");
		} catch (Exception e) {
		}

		if (exitValue > -1) {
		    state = ServerState.TERMINATED;
		}
	    } else {
		state = ServerState.STOPPED;
	    }
	}
	return state;
    }

    private List<String> parseCommandLine() throws ConfigurationException {

	String executable = config.getValue(SRCDS_EXECUTABLE, String.class);

	File srcdsPath = new File(config.getValue(SRCDS_PATH, String.class));
	File srcdsExecutable = new File(srcdsPath.getPath() + File.separator
		+ executable);
	if (!srcdsPath.exists()) {
	    throw new ConfigurationException(String.format(
		    "Unable to find SRCDS path: %s", srcdsPath.getPath()));
	} else if (!srcdsPath.isDirectory()) {
	    throw new ConfigurationException(String.format(
		    "Configured SRCDS path %s is not a directory", srcdsPath
			    .getPath()));
	} else if (!srcdsExecutable.exists()) {
	    throw new ConfigurationException(String.format(
		    "Configured SRCDS executable %s does not exist",
		    srcdsExecutable.getPath()));
	} else if (srcdsExecutable.isDirectory()) {
	    throw new ConfigurationException(String.format(
		    "Configured SRCDS executable %s refers to a directory",
		    srcdsExecutable.getPath()));
	}

	GameType gameType = getGameType();
	log.info(String.format("Game type is %s", gameType));
	LinkedList<String> parameters = gameType.getImplementation()
		.getParametersAsList();
	
	int srcdsPort = config.getValue(SRCDS_SERVER_PORT, Integer.class);
	parameters.add(String.format("+hostport %d", srcdsPort));

	List<String> userParameters = parseUserParameters();
	for (int i = userParameters.size() - 1; i >= 0; i--) {
	    String userParameter = userParameters.get(i).split(" ")[0];
	    if (FORBIDDEN_USER_PARAMETERS.contains(userParameter)) {
		userParameters.remove(i);
		log.warning(String.format("Forbidden user parameter %s ignored", userParameter));
	    }
	}
	
	parameters.addAll(userParameters);
	parameters.addFirst(srcdsExecutable.getAbsolutePath());

	log.info(String.format("Process: %s", parameters.toString()));

	return parameters;
    }

    public GameType getGameType() throws ConfigurationException {

	return config.getValue(SRCDS_GAMETYPE, GameType.class);
    }

    private List<String> parseUserParameters() throws ConfigurationException {

	String userParametersString = config.getValue(SRCDS_PARAMETERS,
		String.class).trim();
	List<String> userParameters = new LinkedList<String>();

	List<String> plusParameterNames = new LinkedList<String>();
	for (int i = 0; i < userParametersString.length(); i++) {
	    char c = userParametersString.charAt(i);
	    if (c == '+' && userParametersString.lastIndexOf(' ') > i) {
		int pNameLength = userParametersString.substring(i + 1)
			.indexOf(' ');
		int startOffset = i + 1;
		String pName = userParametersString.substring(startOffset,
			startOffset + pNameLength);
		plusParameterNames.add(pName);
	    }
	}

	userParametersString = userParametersString.replaceAll("\\+", "-");
	String[] parts = userParametersString.split("-");
	for (String part : parts) {
	    if (!"".equals(part)) {
		String prefix = plusParameterNames.contains(part.split(" ")[0]) ? "+"
			: "-";
		userParameters.add(prefix + part.trim());
	    }
	}

	return userParameters;
    }

    @Override
    public void startServer() throws AlreadyRunningException,
	    StartupFailedException, ConfigurationException {

	synchronized (getMutex()) {
	    if (getServerState() != ServerState.RUNNING) {
		try {
		    File srcdsPath = getSrcdsPath();

		    ProcessBuilder pb = new ProcessBuilder(parseCommandLine());
		    pb.redirectErrorStream(true);
		    pb.directory(srcdsPath);
		    server = pb.start();

		    serverOutputReader = new ServerOutputReader(server
			    .getInputStream());
		    serverOutputReader.start();

		    Thread.sleep(STARTUP_WAIT_TIME_MILLIS);

		    if (getServerState() != ServerState.RUNNING) {
			throw new StartupFailedException(
				"Process was terminated during startup phase");
		    }
		} catch (Exception e) {
		    throw new StartupFailedException(String.format(
			    "Unable to start server: %s", e
				    .getLocalizedMessage()));
		}
	    } else {
		throw new AlreadyRunningException("Server is already running");
	    }
	}
    }

    public File getSrcdsPath() throws ConfigurationException {

	File srcdsPath = new File(config.getValue(SRCDS_PATH, String.class));
	if (srcdsPath.exists()) {
	    log.info(String.format("SRCDS path is %s", srcdsPath.getPath()));
	} else {
	    throw new ConfigurationException(String.format(
		    "%s refers to the non-existent path %s", SRCDS_PATH,
		    srcdsPath.getPath()));
	}
	return srcdsPath;
    }

    @Override
    public void stopServer() throws NotRunningException {

	synchronized (getMutex()) {
	    if (getServerState() != ServerState.RUNNING) {
		throw new NotRunningException("Server is not running");
	    } else {
		serverOutputReader.stopGraceful();
		try {
		    server.getOutputStream().write(3);
		    server.getOutputStream().flush();
		    log.info("SIGTERM sent to process");
		} catch (Exception e) {
		    e.printStackTrace();
		}
		log.info("Destroying reference to process");
		server.destroy();
		server = null;
	    }
	}
    }
}