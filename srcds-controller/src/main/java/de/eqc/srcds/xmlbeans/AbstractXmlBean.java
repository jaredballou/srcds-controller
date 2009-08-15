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
package de.eqc.srcds.xmlbeans;

import java.io.Serializable;


public abstract class AbstractXmlBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6007738341162785134L;
    private static final int INDENT_WIDTH = 2;
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"; 
    private static final String XSLT_HEADER = "<?xml-stylesheet type=\"text/xsl\" href=\"/xslt?name=%CLASSNAME%.xsl\" ?>\n";
    protected final boolean stylesheet;
    
    
    public AbstractXmlBean(final boolean stylesheet) {

	this.stylesheet = stylesheet;
    }
    
    public String indent(final String line, final int level) {

	final StringBuilder sb = new StringBuilder();
	for (int i = 0; i < INDENT_WIDTH * level; i++) {
	    sb.append(" ");
	}
	return sb.append(line).toString();
    }
    
    public String header(final int indent) {

	final StringBuilder sb = new StringBuilder();
	sb.append(indent(String.format("<" + getClass().getSimpleName() + ">\n"), indent));
	return sb.toString();
    }

    public String footer(final int indent) {

	final StringBuilder sb = new StringBuilder();
	sb.append(indent(String.format("</" + getClass().getSimpleName() + ">\n"), indent));
	return sb.toString();
    }
    
    public String toXml() {
	
	final StringBuilder sb = new StringBuilder(XML_HEADER);
	if (stylesheet) {
	    sb.append(XSLT_HEADER.replaceAll("%CLASSNAME%", getClass().getSimpleName()));
	}
	sb.append(toXml(0));
	return sb.toString();
    }

    protected abstract String toXml(int indent);
}
