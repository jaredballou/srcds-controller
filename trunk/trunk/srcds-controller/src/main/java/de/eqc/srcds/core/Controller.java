package de.eqc.srcds.core;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import de.eqc.srcds.enums.ServerState;
import de.eqc.srcds.exceptions.AlreadyRunningException;
import de.eqc.srcds.exceptions.NotRunningException;
import de.eqc.srcds.exceptions.StartupFailedException;
import de.eqc.srcds.handlers.ShutdownHandler;
import de.eqc.srcds.handlers.StartHandler;
import de.eqc.srcds.handlers.StatusHandler;
import de.eqc.srcds.handlers.StopHandler;

public class Controller {
	
	private final HttpServer httpServer;
	private Process server;
	
	public static void main(String[] args) throws IOException {
		
		new Controller();
	}
	
	public Controller() throws IOException {
		
		int port = 8080;
		httpServer = HttpServer.create(new InetSocketAddress(port), 0);
		System.out.println("Bound to port " + port + ".");
		
		HttpContext startContext = httpServer.createContext("/start", new StartHandler(this));
		startContext.setAuthenticator(new DefaultAuthenticator());
		
		HttpContext stopContext = httpServer.createContext("/stop", new StopHandler(this));
		stopContext.setAuthenticator(new DefaultAuthenticator());
		
		HttpContext statusContext = httpServer.createContext("/status", new StatusHandler(this));
		statusContext.setAuthenticator(new DefaultAuthenticator());		
		
		HttpContext shutdownContext = httpServer.createContext("/shutdown", new ShutdownHandler());
		shutdownContext.setAuthenticator(new DefaultAuthenticator());
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook(this));
		
		httpServer.start();
	}
	
	public void init() throws IOException {
		
		new Controller();
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
				System.out.println("Server was terminated.");
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
	
	public void startServer() throws AlreadyRunningException, StartupFailedException {
		
		if (getServerState() != ServerState.RUNNING) {
			try {
				server = Runtime.getRuntime().exec("notepad.exe");
			} catch (IOException e) {
				throw new StartupFailedException("Unable to start server.");
			}
			
		} else {
			throw new AlreadyRunningException("Server is already running.");
		}
	}
	
	public void stopServer() throws NotRunningException {

		if (getServerState() != ServerState.RUNNING) {
			throw new NotRunningException("Server is not running.");
		} else {
			server.destroy();
			server = null;
		}
	}
	
	public HttpServer getHttpServer() {
		
		return httpServer;
	}
}