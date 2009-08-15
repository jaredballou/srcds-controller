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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sun.net.httpserver.HttpExchange;

import de.eqc.srcds.core.Utils;
import de.eqc.srcds.enums.ImageType;

/**
 * @author Hannes
 */
public class ImageHandler extends AbstractCacheControlRegisteredHandler implements
	RegisteredHandler {

    public static final String HANDLER_PATH = "/image";

    /*
     * @see
     * de.eqc.srcds.handlers.AbstractRegisteredHandler#handleRequest(com.sun
     * .net.httpserver.HttpExchange)
     */
    @Override
    public void handleRequest(final HttpExchange httpExchange) throws IOException {

	final String imageName = getParameter("name");
	if (imageName.indexOf('/') > -1 || imageName.indexOf('\\') > -1) {
	    throw new IllegalArgumentException("Only plain file names are allowed as parameter value");
	}
	

	final String resource = String.format("/images/%s", imageName);

	final URL resourceUrl = getClass().getResource(resource);
	final String mimeType = ImageType.getMimeTypeForImageFile(imageName);

	if (resourceUrl == null) {
	    if (imageName == null) {
		throw new IllegalArgumentException("Parameter specify the parameter 'name'");
	    } else {
		throw new IllegalArgumentException(String.format("Cannot find image for name %s", imageName));
	    }
	} else {
	    final ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    InputStream input = null;
	    try {
		input = resourceUrl.openStream();

		final byte[] buffer = new byte[1024];
		for (int len = 0; (len = input.read(buffer)) != -1;) {
		    baos.write(buffer, 0, len);
		}
		baos.flush();
		outputContent(baos.toByteArray(), mimeType);
	    } finally {
		Utils.closeQuietly(input);
		Utils.closeQuietly(baos);
	    }
	}
    }

    /*
     * @see de.eqc.srcds.handlers.RegisteredHandler#getPath()
     */
    @Override
    public String getPath() {
	return HANDLER_PATH;
    }
}
