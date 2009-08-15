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
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.core.Utils;
import de.eqc.srcds.core.logging.LogFactory;

/**
 * @author Holger Cremer
 */
public abstract class AbstractCacheControlRegisteredHandler extends AbstractRegisteredHandler {

    private static Logger log = LogFactory.getLogger(AbstractCacheControlRegisteredHandler.class);

    /**
     * 86400 seconds = one day
     */
    private static final long EXPIRES_IN = 86400000;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);

    /*
     * @see
     * de.eqc.srcds.handlers.AbstractRegisteredHandler#handle(com.sun.net.httpserver
     * .HttpExchange)
     */
    @Override
    public void handle(final HttpExchange httpExchange) throws IOException {

	final long lastModified = (Utils.getLastModifiedDate() / 1000) * 1000;

	// send cache control headers
	httpExchange.getResponseHeaders().add("Cache-Control", "public, max-age=" + EXPIRES_IN + ", must-revalidate");
	httpExchange.getResponseHeaders().add("Expires",
		this.dateFormat.format(new Date(System.currentTimeMillis() + EXPIRES_IN)));
	httpExchange.getResponseHeaders().add("Last-Modified", this.dateFormat.format(new Date(lastModified)));
	
	// looking for "If-modified-since" to send a 304 status
	for (Entry<String, List<String>> entry : httpExchange.getRequestHeaders().entrySet()) {
	    if (entry.getKey().equalsIgnoreCase("If-modified-since")) {
		final Date parsedDate = parseHeaderDate(entry.getValue());
		if (parsedDate != null && lastModified <= parsedDate.getTime()) {
		    // skip the request and send a http 304
		    httpExchange.sendResponseHeaders(304, 0);
		    final OutputStream os = httpExchange.getResponseBody();
		    os.flush();
		    os.close();
		    return;
		}
	    }
	}

	// handle the request normal
	super.handle(httpExchange);
    }

    private Date parseHeaderDate(final List<String> valueList) {

	Date ret = null;
	if (valueList.size() != 1) {
	    log.warning("Expected size of values is 1 but was: " + valueList.size());
	} else {
	    try {
		ret = dateFormat.parse(valueList.get(0));
	    } catch (ParseException excp) {
		log.log(Level.WARNING, "Can't parse the if-modified-since date '" + valueList.get(0) + "': "
			+ excp.getMessage(), excp);
	    }
	}
	return ret;
    }
}
