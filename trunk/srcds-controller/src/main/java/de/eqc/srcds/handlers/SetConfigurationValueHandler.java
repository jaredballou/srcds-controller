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

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class SetConfigurationValueHandler extends AbstractRegisteredHandler
	implements RegisteredHandler {

    @Override
    public String getPath() {
	return "/setConfig";
    }

    public void handleRequest(final HttpExchange httpExchange) throws IOException {

	final String key = getParameter("key");
	final String value = getParameter("value");

	ResponseCode code = ResponseCode.INFORMATION;
	final Message message = new Message();
	if (key == null || value == null) {
	    code = ResponseCode.ERROR;
	    message.addLine("Either key or value parameter is missing");
	} else {
	    try {
		getConfig().setValue(key, value);
		message.addLine(String.format("Set key %s to value %s", key, value));
	    } catch (ConfigurationException e) {
		code = ResponseCode.ERROR;
		message.addLine(e.getLocalizedMessage());
	    }
	}

	outputXmlContent(new ControllerResponse(code, message).toXml());
    }
}