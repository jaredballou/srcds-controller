/**
 * This file is part of the Source Dedicated Server Controller project.
 * It is distributed under GPL 3 license.
 *
 * The srcds-controller is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You should have received a copy of the GNU General Public License
 * along with the srcds-controller. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://www.earthquake-clan.de/srcds/>
 *    <http://code.google.com/p/srcds-controller/>
 */
package de.eqc.srcds.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.channels.Channels;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.ProcessOutputObserver;
import de.eqc.srcds.core.ServerOutput;
import de.eqc.srcds.core.SourceDServerController;
import de.eqc.srcds.core.Utils;
import de.eqc.srcds.core.logging.LogFactory;
import de.eqc.srcds.enums.ServerState;

/**
 * @author Holger Cremer
 */
public class ProcessOutputHandler implements HttpHandler, RegisteredHandler {

    private static Logger log = LogFactory.getLogger(ProcessOutputHandler.class);
    
    private SourceDServerController serverController;

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {

	return "/processOutput";
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getHttpHandler()
     */
    @Override
    public HttpHandler getHttpHandler() {

	return this;
    }

    /*
     * @seede.eqc.srcds.handlers.RegisteredHandler#init(de.eqc.srcds.core.
     * SourceDServerController, de.eqc.srcds.configuration.Configuration)
     */
    @Override
    public void init(final SourceDServerController controller, final Configuration config) {

	this.serverController = controller;
    }

    /*
     * @see
     * com.sun.net.httpserver.HttpHandler#handle(com.sun.net.httpserver.HttpExchange
     * )
     */
    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {

	httpExchange.getResponseHeaders().add("Content-type", "text/html");
	httpExchange.sendResponseHeaders(200, 0);
	PrintStream printStream = null;
	
	final ServerOutput serverOutput = this.serverController.getServerOutput();
	try {
	    final OutputStream os = httpExchange.getResponseBody();
	    
	    // set autoflush on in constructor
	    printStream = new PrintStream(os, true);

	    // output the log history
	    printStream.println("### Output history:<br/>");
	    if (serverOutput == null) {
		printStream.println("### Process never run!<br/>");
		Utils.closeQuietly(printStream);
		return;
	    } else {
		for (String logLine : serverOutput.getLastLog()) {
		    printStream.println(logLine + "<br/>");
		}

		if (this.serverController.getServerState() != ServerState.RUNNING) {
		    printStream.println("### Server isn't running.<br/>");
		    Utils.closeQuietly(printStream);
		} else {
		    printStream.println("### Live output:<br/>");
		    Channels.newChannel(printStream);
		    final WriteLogToStreamThread streamLogger = new WriteLogToStreamThread(printStream, serverOutput);
		    streamLogger.start();
		}
	    }
	} catch (Exception e) {
	    // every Exception is interesting here because the PrintStream should omit any IOExceptions
	    log.log(Level.WARNING, "Exception during output sending: " + e.getMessage());
	    log.log(Level.FINE, "Detailled exception: ", e);
	    Utils.closeQuietly(printStream);
	} 
	
    }

    /**
     * Monitors the output stream and prints new process output lines on it.
     * 
     * @author Holger Cremer
     */
    class WriteLogToStreamThread extends Thread implements ProcessOutputObserver {

	private final PrintStream printStream;
	private final ServerOutput serverOutput;

	/**
	 * @param printStream
	 * @param serverOutput 
	 */
	public WriteLogToStreamThread(final PrintStream printStream, final ServerOutput serverOutput) {

	    this.setName("WriteLogToStreamThread");
	    this.printStream = printStream;
	    this.serverOutput = serverOutput;
	}

	/*
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

	    serverOutput.registerOnLogObserver(this);
	    try {
	    /*
	     * If the browser disconnects 'checkError()' returns true and we
	     * stop this thread. (PrintStream omit any IOExceptions !)
	     */
	    while (!this.printStream.checkError()) {
		try {
		    // sleeping 1 sec. seem to be a good compromise between cpu
		    // usage and actuality
		    Thread.sleep(1000);
		} catch (InterruptedException excp) {
		    // ignore
		}
	    }
	    } finally {
		serverOutput.unRegisterOnLogObserver(this);
		Utils.closeQuietly(this.printStream);
	    }
	}

	/*
	 * @see
	 * de.eqc.srcds.core.ProcessOutputObserver#outputHasChanged(java.lang
	 * .String)
	 */
	@Override
	public void outputHasChanged(final String newLine) {

	    this.printStream.println(newLine + "<br />");
	}
    }
}
