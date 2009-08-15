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
package de.eqc.srcds.core;

import static de.eqc.srcds.configuration.ConfigurationRegistry.HTTP_SERVER_PORT;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_EXECUTABLE;
import static de.eqc.srcds.core.Constants.DEFAULT_CONFIG_FILENAME;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.configuration.impl.XmlPropertiesConfiguration;
import de.eqc.srcds.core.logging.LogFactory;
import de.eqc.srcds.enums.OperatingSystem;
import de.eqc.srcds.exceptions.InitializationException;
import de.eqc.srcds.exceptions.UnsupportedOSException;

/**
 * This class starts the srcds-controller.
 * 
 * @author Holger Cremer
 * @author Hannes Scharmann
 */
public class CLI {

    private static Logger log =
	    LogFactory.getLogger(CLI.class);

    private Configuration config;

    public void startup(final String... arguments) throws UnsupportedOSException,
						  ConfigurationException,
						  InitializationException {

	Thread.currentThread()
	      .setName(getClass().getSimpleName());

	checkOS();

	final File configFile =
		new File(DEFAULT_CONFIG_FILENAME);

	this.config =
		new XmlPropertiesConfiguration(configFile);

	if (OperatingSystem.getCurrent() == OperatingSystem.WINDOWS) {
	    new TrayMenu(config);
	}

	try {
	    processCommandlineArguments(arguments);
	} catch (ConfigurationException e) {
	    log.warning(e.getLocalizedMessage());
	    System.exit(0);
	}

	final SourceDServerController srcdsController =
		new SourceDServerController(this.config);
	final HttpServerController httpServerController =
		new HttpServerController(config,
					 srcdsController);

	Runtime.getRuntime()
	       .addShutdownHook(new ShutdownHook(Thread.currentThread(),
						 srcdsController,
						 httpServerController));

	httpServerController.start();
	srcdsController.start();

	try {
	    httpServerController.join();
	} catch (InterruptedException e) {
	    log.warning(e.getLocalizedMessage());
	}

	try {
	    srcdsController.join();
	} catch (InterruptedException e) {
	    log.warning(e.getLocalizedMessage());
	}

	System.out.println("Exiting...");
    }

    private void processCommandlineArguments(final String... arguments) throws ConfigurationException {

	for (int i =
		0; i < arguments.length; i++) {
	    final String argument =
		    arguments[i].trim();
	    if ("--help".equals(argument)) {
		System.out.println("Usage: java -jar <jarfile> [--httpServerPort <port>] [--srcdsExecutable <file>]");
		System.exit(0);
	    } else if ("--httpServerPort".equals(argument) && i < argument.length() - 1) {
		final String value =
			arguments[i + 1];
		config.setValue(HTTP_SERVER_PORT,
				Integer.valueOf(value));
	    } else if ("--srcdsExecutable".equals(argument) && i < argument.length() - 1) {
		final String value =
			arguments[i + 1];
		config.setValue(SRCDS_EXECUTABLE,
				value);
	    }
	}
    }

    private void checkOS() throws UnsupportedOSException {

	final OperatingSystem operatingSystem =
		OperatingSystem.getCurrent();
	log.info(String.format("Detected %s operating system",
			       operatingSystem));
	if (operatingSystem == OperatingSystem.UNSUPPORTED) {
	    throw new UnsupportedOSException("Detected operating system is not supported");
	}
    }

    /**
     * @param args
     */
    public static void main(final String[] args) {

	try {
	    new CLI().startup(args);
	} catch (Exception e) {
	    log.log(Level.WARNING,
		    e.getMessage(),
		    e);
	}
    }
}
