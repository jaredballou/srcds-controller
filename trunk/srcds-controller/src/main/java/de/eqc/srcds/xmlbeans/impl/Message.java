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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.eqc.srcds.xmlbeans.AbstractXmlBean;

public class Message extends AbstractXmlBean {

    /**
     * 
     */
    private static final long serialVersionUID = -3453607805613176580L;
    private final List<String> items;

    public Message() {

	super(false);
	this.items = new LinkedList<String>();
    }

    public Message(final String... messages) {

	super(false);
	if (messages == null) {
	    this.items = new LinkedList<String>();
	} else {
	    this.items = Arrays.asList(messages);
	}
    }

    public void addLine(final String message) {

	items.add(message);
    }

    @Override
    protected String toXml(final int indent) {

	final StringBuilder sb = new StringBuilder(header(indent));

	if (items.isEmpty()) {
	    addLine("(no message specified)");
	}

	for (String message : items) {
	    sb.append(indent(String.format("<Item><![CDATA[%s]]></Item>\n", message),
		    indent + 1));
	}

	return sb.append(footer(indent)).toString();
    }

    @Override
    public String toString() {

	final StringBuilder sb = new StringBuilder();
	for (String message : items) {
	    sb.append(message + "\n");
	}
	return sb.toString();
    }

}