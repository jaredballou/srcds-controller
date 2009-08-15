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

import java.util.Map.Entry;

import de.eqc.srcds.configuration.Configuration;
import de.eqc.srcds.configuration.ConfigurationKey;
import de.eqc.srcds.xmlbeans.AbstractXmlBean;


public class ControllerConfiguration extends AbstractXmlBean {

    private final Configuration config;
    
    public ControllerConfiguration(final Configuration config) {

	super(true);
	this.config = config;
    }

    /**
     * 
     */
    private static final long serialVersionUID = -1655029232522729190L;

    @Override
    protected String toXml(final int indent) {

	final StringBuilder sbEntries = new StringBuilder(header(indent));
	final StringBuilder sbEnums = new StringBuilder(indent("<Metadata>\n", indent + 1));

	boolean enums = false;
	for (Entry<ConfigurationKey<?>, String> entry : config.getData().entrySet()) {
	    if (entry.getKey().isEnumerationType()) {
		enums = true;
		sbEnums.append(indent(String.format("<Enumeration name=\"%s\">\n", entry.getKey().getDataType().getSimpleName()), indent + 2));
		for (String enumValue : entry.getKey().getEnumValues()) {
		    sbEnums.append(indent(String.format("<Value>%s</Value>\n", enumValue), indent + 3));
		}
		sbEnums.append(indent("</Enumeration>\n", indent + 2));
	    }

	    sbEntries.append(indent(String.format("<Entry type=\"%s\" description=\"%s\" enumeration=\"%s\">\n", entry.getKey().getDataType().getSimpleName(), entry.getKey().getDescription(), entry.getKey().isEnumerationType()), indent + 1));
	    sbEntries.append(indent(String.format("<Key>%s</Key>\n", entry.getKey().getKey()), indent + 2));
	    sbEntries.append(indent(String.format("<Value>%s</Value>\n", entry.getValue()), indent + 2));
	    sbEntries.append(indent("</Entry>\n", indent + 1));
	}

	if (enums) {
	    sbEnums.append(indent("</Metadata>\n", indent + 1));
	    sbEntries.append(sbEnums);
	}
	
	return sbEntries.append(footer(indent)).toString();
    }

}
