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


import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_RCON_PASSWORD;
import static de.eqc.srcds.configuration.ConfigurationRegistry.SRCDS_SERVER_PORT;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.configuration.datatypes.Password;
import de.eqc.srcds.core.NetworkUtil;
import de.eqc.srcds.rcon.RconConnection;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class RconHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/rcon";
    }

    public void handleRequest(final HttpExchange httpExchange) throws IOException {
	
	final String command = getParameter("command");
	if (command != null && !"".equals(command)) {
	    try {
		final String rconAddress = NetworkUtil.getLocalHostname();
		final int rconPort = getConfig().getValue(SRCDS_SERVER_PORT, Integer.class);
		final String rconPassword = getConfig().getValue(SRCDS_RCON_PASSWORD, Password.class).toString();

		final RconConnection rc = new RconConnection(rconAddress, rconPort, rconPassword);
		final String response = rc.send(command);
		final ResponseCode code = ResponseCode.RCON_RESPONSE;
		final String lines[] = response.split("\n");
		final Message message = new Message();
		for (String line : lines) {
		    message.addLine(line);
		}
		outputXmlContent(new ControllerResponse(code, message).toXml());
	    } catch (Exception e) {
		throw new IllegalStateException(e);
	    }
	} else {
	    throw new IllegalArgumentException("Please specify parameter 'command'");
	}
    }
}