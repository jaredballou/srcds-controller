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
package de.eqc.srcds.xmlbeans.impl;

import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_GAMETYPE;

import java.io.File;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.core.Utils;
import de.eqc.srcds.enums.GameType;
import de.eqc.srcds.games.AbstractGame;
import de.eqc.srcds.xmlbeans.AbstractXmlBean;

public class GameConfiguration extends AbstractXmlBean {

    /**
     * 
     */
    private static final long serialVersionUID = -7008363335234493278L;
    private final Configuration config;
    private final int selectedFileIndex;

    public GameConfiguration(final Configuration config,
	    final int selectedFileIndex) {

	super(true);
	this.config = config;
	this.selectedFileIndex = selectedFileIndex;
    }

    @Override
    protected String toXml(final int indent) {

	final StringBuilder sb = new StringBuilder(header(indent));
	try {
	    final AbstractGame game = config.getValue(SRCDS_GAMETYPE,
		    GameType.class).getImplementation();
	    final File file = new File(config.getValue(
		    "srcds.controller.srcds.path", String.class), game
		    .getFilesForEdit().get(selectedFileIndex));

	    sb.append(indent("<ConfigurationFiles>\n", indent + 1));
	    for (int id = 0; id < game.getFilesForEdit().size(); id++) {
		final String configFile = game.getFilesForEdit().get(id);
		sb.append(indent(String.format(
			"<ConfigurationFile id=\"%d\" name=\"%s\" />\n", id,
			configFile), indent + 2));
	    }
	    sb.append(indent("</ConfigurationFiles>\n", indent + 1));

	    sb.append(indent(String.format("<FileContent id=\"%d\">",
		    selectedFileIndex), indent + 1));
	    sb.append("<![CDATA[");
	    sb.append(Utils.getFileContent(file));
	    sb.append("]]>");
	    sb.append("</FileContent>\n");
	} catch (Exception e) {
	    sb.append("<Error>Unable to load required resources</Error>");
	}

	return sb.append(footer(indent)).toString();
    }

    // public static void main(String[] args) throws ConfigurationException {
    // File configFile = new File(DEFAULT_CONFIG_FILENAME);
    // Configuration config = new XmlPropertiesConfiguration(configFile);
    // System.out.println(new GameConfiguration(config, 0).toXml());
    // }
}