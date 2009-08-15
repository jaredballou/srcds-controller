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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.handlers.utils.HandlerUtil;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class ListHandlersHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

    @Override
    public String getPath() {
	return "/usage";
    }

    public void handleRequest(final HttpExchange httpExchange) throws IOException {
	
	try {
	    final Collection<RegisteredHandler> handlers = HandlerUtil.getRegisteredHandlerImplementations();
	    final List<String> lines = new LinkedList<String>();
	    for (RegisteredHandler handler : handlers) {
		lines.add(handler.getPath());
	    }
	    final String[] linesAsArray = lines.toArray(new String[0]); 
	    final ControllerResponse cr = new ControllerResponse(ResponseCode.INFORMATION, new Message(linesAsArray));
	    outputXmlContent(cr.toXml());
	} catch (Exception e) {
	    throw new IOException(String.format("Unable to register handler %s", getClass()), e);
	}
	
    }
}