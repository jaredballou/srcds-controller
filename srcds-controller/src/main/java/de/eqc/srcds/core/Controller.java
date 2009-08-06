package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.Constants.AUTOSTART;
import static de.eqc.srcds.configuration.Constants.DEFAULT_CONFIG_FILENAME;
import static de.eqc.srcds.configuration.Constants.HTTP_SERVER_PORT;
import static de.eqc.srcds.configuration.Constants.LOGGING_FILENAME;
import static de.eqc.srcds.configuration.Constants.SRCDS_EXECUTABLE;
import static de.eqc.srcds.configuration.Constants.SRCDS_GAMETYPE;
import static de.eqc.srcds.configuration.Constants.SRCDS_PARAMETERS;
import static de.eqc.srcds.configuration.Constants.SRCDS_PATH;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.impl.XmlPropertiesConfiguration;
import de.eqc.srcds.enums.GameType;
import de.eqc.srcds.enums.OperatingSystem;
import de.eqc.srcds.enums.ServerState;
import de.eqc.srcds.exceptions.AlreadyRunningException;
import de.eqc.srcds.exceptions.ConfigurationException;
import de.eqc.srcds.exceptions.NotRunningException;
import de.eqc.srcds.exceptions.StartupFailedException;
import de.eqc.srcds.exceptions.UnsupportedOSException;
import de.eqc.srcds.handlers.SetConfigurationValueHandler;
import de.eqc.srcds.handlers.ShowConfigurationHandler;
import de.eqc.srcds.handlers.ShutdownHandler;
import de.eqc.srcds.handlers.StartHandler;
import de.eqc.srcds.handlers.StatusHandler;
import de.eqc.srcds.handlers.StopHandler;

public class Controller {
	
	private static Logger log;
	private final HttpServer httpServer;
	private final OperatingSystem os;
	private final Configuration config;
	private Process server;
	
	static {
		log = Logger.getLogger(Controller.class.getSimpleName());
		
		File loggingConfig = new File(LOGGING_FILENAME);
		if (loggingConfig.exists()) {
			try {
				LogManager.getLogManager().readConfiguration(new FileInputStream(loggingConfig));
			} catch (Exception e) {
				log.warning("Unable to read logging configuration file - using default");
			}
		} else {
			log.info("Unable to find logging configuration file - using default");
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		try {
			new Controller();
		} catch (Exception e) {
			log.warning(e.getLocalizedMessage());
		}
	}
	
	public Controller() throws IOException, UnsupportedOSException, ConfigurationException {
		
		os = OperatingSystem.getCurrent();
		log.info(String.format("Detected %s operating system", os));
		if (os == OperatingSystem.UNSUPPORTED) {
			throw new UnsupportedOSException("Detected operating system is not supported");
		}
		
		File configFile = new File(DEFAULT_CONFIG_FILENAME);
		
		/*
		 * This is for testing purposes only.
		 */
		configFile.delete();

		this.config = new XmlPropertiesConfiguration(configFile);
		
		int port = config.getValue(HTTP_SERVER_PORT, Integer.class);
		httpServer = HttpServer.create(new InetSocketAddress(port), 0);
		log.info(String.format("Bound to TCP port %d.", port));
		
		HttpContext startContext = httpServer.createContext("/start", new StartHandler(this));
		startContext.setAuthenticator(new DefaultAuthenticator());
		
		HttpContext stopContext = httpServer.createContext("/stop", new StopHandler(this));
		stopContext.setAuthenticator(new DefaultAuthenticator());
		
		HttpContext statusContext = httpServer.createContext("/status", new StatusHandler(this));
		statusContext.setAuthenticator(new DefaultAuthenticator());		
		
		HttpContext shutdownContext = httpServer.createContext("/shutdown", new ShutdownHandler());
		shutdownContext.setAuthenticator(new DefaultAuthenticator());
		
		HttpContext showConfigurationContext = httpServer.createContext("/showConfig", new ShowConfigurationHandler(config));
		showConfigurationContext.setAuthenticator(new DefaultAuthenticator());	

		HttpContext setConfigurationContext = httpServer.createContext("/setConfig", new SetConfigurationValueHandler(config));
		setConfigurationContext.setAuthenticator(new DefaultAuthenticator());	
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
		
		httpServer.start();
		
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
				log.info("Server was terminated.");
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
	
	public List<String> parseCommandLine() throws ConfigurationException {

		String executable = "." + File.separator + config.getValue(SRCDS_EXECUTABLE, String.class);
		
		GameType gameType = config.getValue(SRCDS_GAMETYPE, GameType.class);
		log.info(String.format("Game type is set to %s", gameType));
		LinkedList<String> parameters = gameType.getImplementation().getParametersAsList();

		parameters.addAll(parseUserParameters());
		parameters.addFirst(executable);

		log.info(String.format("Process: %s", parameters.toString()));
		
		return parameters;
	}
	
	public List<String> parseUserParameters() throws ConfigurationException {

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
				File srcdsPath = new File(config.getValue(SRCDS_PATH, String.class));
				if (srcdsPath.exists()) {
					log.info(String.format("SRCDS path is set to %s", srcdsPath.getPath()));
				} else {
					throw new ConfigurationException(String.format("%s refers to the non-existent path %s", SRCDS_PATH, srcdsPath.getPath()));
				}
				
				ProcessBuilder pb = new ProcessBuilder(parseCommandLine());
				pb.directory(srcdsPath);
				server = pb.start();
				
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
			server.destroy();
			server = null;
		}
	}
	
	public HttpServer getHttpServer() {
		
		return httpServer;
	}
}