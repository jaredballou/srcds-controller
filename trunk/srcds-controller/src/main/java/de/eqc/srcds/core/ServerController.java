package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.Constants.AUTOSTART;
import static de.eqc.srcds.configuration.Constants.SRCDS_EXECUTABLE;
import static de.eqc.srcds.configuration.Constants.SRCDS_GAMETYPE;
import static de.eqc.srcds.configuration.Constants.SRCDS_PARAMETERS;
import static de.eqc.srcds.configuration.Constants.SRCDS_PATH;
import static de.eqc.srcds.configuration.Constants.STARTUP_WAIT_TIME_MILLIS;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.enums.GameType;
import de.eqc.srcds.enums.ServerState;
import de.eqc.srcds.exceptions.AlreadyRunningException;
import de.eqc.srcds.exceptions.ConfigurationException;
import de.eqc.srcds.exceptions.NotRunningException;
import de.eqc.srcds.exceptions.StartupFailedException;

/**
 * Controls the server and provides therefore functions like start/stop/status etc.
 * 
 * @author Hannes
 * @author Holger
 */
public class ServerController {

  private static Logger log = LogFactory.getLogger(ServerController.class);

  private Process       server;
  private Configuration config;


  public ServerController(Configuration config) throws ConfigurationException {

    this.config = config;

    if (config.getValue(AUTOSTART, Boolean.class)) {
      try {
        startServer();
      } catch (Exception e) {
        log.warning(String.format("Autostart failed: %s", e.getLocalizedMessage()));
      }
    }
  }

  public Process getServer() {

    return server;
  }

  public ServerState getServerState() {

    ServerState state = ServerState.RUNNING;

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

    return state;
  }

  private List<String> parseCommandLine() throws ConfigurationException {

    String executable = "." + File.separator + config.getValue(SRCDS_EXECUTABLE, String.class);

    GameType gameType = getGameType();
    log.info(String.format("Game type is %s", gameType));
    LinkedList<String> parameters = gameType.getImplementation().getParametersAsList();

    parameters.addAll(parseUserParameters());
    parameters.addFirst(executable);

    log.info(String.format("Process: %s", parameters.toString()));

    return parameters;
  }

  public GameType getGameType() throws ConfigurationException {

    return config.getValue(SRCDS_GAMETYPE, GameType.class);
  }

  private List<String> parseUserParameters() throws ConfigurationException {

    String userParametersString = config.getValue(SRCDS_PARAMETERS, String.class).trim();
    List<String> userParameters = new LinkedList<String>();

    List<String> plusParameterNames = new LinkedList<String>();
    for (int i = 0; i < userParametersString.length(); i++) {
      char c = userParametersString.charAt(i);
      if (c == '+' && userParametersString.lastIndexOf(' ') > i) {
        int pNameLength = userParametersString.substring(i + 1).indexOf(' ');
        int startOffset = i + 1;
        String pName = userParametersString.substring(startOffset, startOffset + pNameLength);
        plusParameterNames.add(pName);
      }
    }

    userParametersString = userParametersString.replaceAll("\\+", "-");
    String[] parts = userParametersString.split("-");
    for (String part : parts) {
      if (!"".equals(part)) {
        String prefix = plusParameterNames.contains(part.split(" ")[0]) ? "+" : "-";
        userParameters.add(prefix + part.trim());
      }
    }

    return userParameters;
  }

  public void startServer() throws AlreadyRunningException, StartupFailedException, ConfigurationException {

    if (getServerState() != ServerState.RUNNING) {
      try {
        File srcdsPath = getSrcdsPath();

        ProcessBuilder pb = new ProcessBuilder(parseCommandLine());
        pb.directory(srcdsPath);
        server = pb.start();

        Thread.sleep(STARTUP_WAIT_TIME_MILLIS);

        if (getServerState() != ServerState.RUNNING) {
          throw new StartupFailedException("Process was terminated during startup phase");
        }

        /*
         * DEBUG
         */
        //				InputStreamReader isr = new InputStreamReader(server.getInputStream());
        //				int i;
        //				while ((i = isr.read()) != -1) {
        //					System.out.print((char) i);
        //				}

      } catch (Exception e) {
        throw new StartupFailedException(String.format("Unable to start server: %s", e.getLocalizedMessage()));
      }

    } else {
      throw new AlreadyRunningException("Server is already running");
    }
  }

  public File getSrcdsPath() throws ConfigurationException {

    File srcdsPath = new File(config.getValue(SRCDS_PATH, String.class));
    if (srcdsPath.exists()) {
      log.info(String.format("SRCDS path is %s", srcdsPath.getPath()));
    } else {
      throw new ConfigurationException(String.format("%s refers to the non-existent path %s", SRCDS_PATH,
              srcdsPath.getPath()));
    }
    return srcdsPath;
  }

  public void stopServer() throws NotRunningException {

    if (getServerState() != ServerState.RUNNING) {
      throw new NotRunningException("Server is not running");
    } else {
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
