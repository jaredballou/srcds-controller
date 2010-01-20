/**
 * This file is part of the Source Dedicated Server Controller project.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 * 
 * Additional permission under GNU GPL version 3 section 7
 * 
 * If you modify this Program, or any covered work, by linking or
 * combining it with srcds-controller (or a modified version of that library),
 * containing parts covered by the terms of GNU General Public License,
 * the licensors of this Program grant you additional permission to convey
 * the resulting work. {Corresponding Source for a non-source form of such a
 * combination shall include the source code for the parts of srcds-controller
 * used as well as that of the covered work.}
 *
 * For more information, please consult:
 *    <http://www.earthquake-clan.de/srcds/>
 *    <http://code.google.com/p/srcds-controller/>
 */
package de.eqc.srcds.handlers;
import static de.eqc.srcds.core.Constants.SHUTDOWN_DELAY_MILLIS;

import java.io.IOException;
import java.util.Timer;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.core.ShutdownTimer;
import de.eqc.srcds.core.Utils;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class ShutdownHandler extends AbstractRegisteredHandler implements
	RegisteredHandler {

    @Override
    public String getPath() {
	return "/shutdown";
    }

    public void handleRequest(final HttpExchange httpExchange) throws IOException {

	final ResponseCode code = ResponseCode.INFORMATION;
	final Message message = new Message(String.format("Controller is going down in %d seconds...", Utils.millisToSecs(SHUTDOWN_DELAY_MILLIS)));
	outputXmlContent(new ControllerResponse(code, message).toXml());

	final Timer timer = new Timer();
	timer.schedule(new ShutdownTimer(Utils.millisToSecs(SHUTDOWN_DELAY_MILLIS)), SHUTDOWN_DELAY_MILLIS);
    }
    
}
