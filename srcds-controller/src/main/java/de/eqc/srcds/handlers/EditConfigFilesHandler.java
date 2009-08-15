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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.core.Utils;
import de.eqc.srcds.xmlbeans.impl.GameConfiguration;


/**
 * @author Holger Cremer
 */
public class EditConfigFilesHandler extends AbstractRegisteredHandler {

    /*
     * @see de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun.net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(final HttpExchange httpExchange) throws Exception {
	final List<String> filesForEdit = getServerController().getGameType().getImplementation().getFilesForEdit();

	if (isPost()) {
	    final String fileIdParam = getPostParameter("id");
	    final String newContent = getPostParameter("content");
	    if (fileIdParam == null || newContent == null) {
		throw new IllegalArgumentException("id or content was null");
	    }
	    final int fileId = Integer.parseInt(fileIdParam);
	    saveFile(fileId, filesForEdit.get(fileId), newContent);
	} else {
	    final String fileIdParam = getParameter("id");
	    int fileId = 0;
	    if (fileIdParam != null) {
		fileId = Integer.parseInt(fileIdParam);
	    }
	    showFile(fileId, filesForEdit.get(fileId));
	}
    }

    /**
     * @param string
     * @param newContent
     * @throws IOException 
     * @throws ConfigurationException 
     */
    private void saveFile(final int fileId, final String file, final String newContent) throws IOException, ConfigurationException {
	final File fileToEdit = new File(getConfig().getValue("srcds.controller.srcds.path",String.class), file);
	
	// TODO: unescape content!
	Utils.saveToFile(fileToEdit, newContent);

	showFile(fileId, file);
//	SimpleTemplate template = new SimpleTemplate("/html/editConfigFiles/fileSaved.html");
//	template.setAttribute("url", this.getPath());
//	outputHtmlContent(template.renderTemplate());
    }

    /**
     * @param string
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws ConfigurationException 
     */
    private void showFile(final int fileId, final String file) throws FileNotFoundException, IOException, ConfigurationException {
	final File fileToEdit = new File(getConfig().getValue("srcds.controller.srcds.path",String.class), file);
	
	if (!fileToEdit.exists()) {
	    throw new FileNotFoundException(String.format("Cannot find file %s", file));
	}

	final GameConfiguration gameConfiguration = new GameConfiguration(getConfig(), fileId);
	outputXmlContent(gameConfiguration.toXml());
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return "/editConfigFiles";
    }
}