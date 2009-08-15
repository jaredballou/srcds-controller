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
package de.eqc.srcds.games;

import java.util.AbstractSequentialList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractGame {

    private final Map<String, String> parameters;
    private final String directory;

    public AbstractGame(final String directory) {

	this.directory = directory;
	this.parameters = new HashMap<String, String>();
    }

    public Map<String, String> getParameters() {

	return parameters;
    }

    protected void addParameter(final String key, final String value) {

	if (parameters.get(key) == null) {
	    parameters.put(key, value);
	}	
    }

    public AbstractSequentialList<String> getParametersAsList() {

	final LinkedList<String> params = new LinkedList<String>();

	for (String parameter : parameters.keySet()) {
	    params.add("-" + parameter + " " + parameters.get(parameter));
	}

	return params;
    }

    public String getDirectory() {

	return directory;
    }

    public abstract List<String> getFilesForEdit();
    
    public abstract List<String> getFilesForSync();
}


