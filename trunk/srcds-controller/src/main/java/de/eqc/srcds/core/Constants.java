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


public final class Constants {

    /** Hides the constructor of the utility class. */
    private Constants() {

	throw new UnsupportedOperationException();
    }
    
    public static final String PROJECT_NAME = "srcds-controller";
    
    public static final String TRAY_ICON_PATH = "/images/icon_tray.gif";
    
    public static final int MILLIS_PER_SEC = 1000;

    public static final int STARTUP_WAIT_TIME_MILLIS = 2000;

    public static final int SHUTDOWN_DELAY_MILLIS = 3000;
    
    public static final int OUTPUT_READING_DELAY_MILLIS = 500;
    
    public static final int HTTP_SERVER_SHUTDOWN_DELAY_SECS = 2;

    public static final int SERVER_POLL_INTERVAL_MILLIS = 1000;

    public static final String FS_LOGGING_FILENAME = "srcds-controller-logging.properties";

    public static final String BUILTIN_LOGGING_FILENAME = "/configuration/srcds-controller-logging.properties";

    public static final String DEFAULT_CONFIG_FILENAME = "srcds-controller-config.xml";
    
}
