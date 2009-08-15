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

import java.io.InputStream;
import java.util.Properties;

public final class VersionUtil {

    private static final String PROJECT_NAME = "srcds-controller";
    private static final String ROOT_PACKAGE = "de.eqc.srcds";    
    private static final String POM_PROPERTIES_LOCATION = "/META-INF/maven/${root-package}/${project-name}/pom.properties";

    /** Hides the constructor of the utility class. */
    private VersionUtil() {

	throw new UnsupportedOperationException();
    }

    public static String getProjectVersion() {

	String projectVersion;
	try {
	    String pomPropertiesLocation = POM_PROPERTIES_LOCATION;
	    pomPropertiesLocation = pomPropertiesLocation.replaceAll("\\$\\{root-package\\}", ROOT_PACKAGE);
	    pomPropertiesLocation = pomPropertiesLocation.replaceAll("\\$\\{project-name\\}", PROJECT_NAME);
	    final InputStream intputStream = VersionUtil.class.getResourceAsStream(pomPropertiesLocation);
	    final Properties props = new Properties();
	    props.load(intputStream);
	    projectVersion = props.getProperty("version");
	} catch (Exception e) {
	    projectVersion = "(unknown)";
	}
	return projectVersion;
    }

}