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
package de.eqc.srcds.core.logging;

import static de.eqc.srcds.core.Constants.BUILTIN_LOGGING_FILENAME;
import static de.eqc.srcds.core.Constants.FS_LOGGING_FILENAME;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author Holger Cremer
 */
public final class LogFactory {

    /** Hides the constructor of the utility class. */
    private LogFactory() {

	throw new UnsupportedOperationException();
    }
    
    static {

	final File fsLoggingConfig = new File(FS_LOGGING_FILENAME);
	final InputStream builtinLoggingConfig = LogFactory.class.getResourceAsStream(BUILTIN_LOGGING_FILENAME);

	if (fsLoggingConfig.exists()) {
	    try {
		LogManager.getLogManager().readConfiguration(
			new FileInputStream(fsLoggingConfig));
	    } catch (Exception e) {
		System.out
			.println("Unable to read logging configuration file - using built-in default");
	    }
	} else {
	    System.out.println("Using builtin logging configuration file");
	    try {
		LogManager.getLogManager().readConfiguration(builtinLoggingConfig);
	    } catch (Exception e) {
		System.out
			.println("Unable to built-in logging configuration file - using default");
	    }
	}
	// find the global log level
	Level level = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getLevel();
	if (level == null) {
	    level = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).getParent().getLevel();
	}
	System.out.println("Logging with global level: " + level);
    }

    public static Logger getLogger(final Class<?> clazz) {

	return Logger.getLogger(clazz.getName());
    }
}
