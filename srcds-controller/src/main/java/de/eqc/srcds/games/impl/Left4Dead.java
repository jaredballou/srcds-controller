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
package de.eqc.srcds.games.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.eqc.srcds.games.AbstractGame;

public class Left4Dead extends AbstractGame {

    public Left4Dead() {

	super("left4dead");

	addParameter("game", "left4dead");
    }

    @Override
    public List<String> getFilesForEdit() {

	final List<String> files = new ArrayList<String>();
	Collections.addAll(files, "left4dead/gameinfo.txt",
		"left4dead/missioncycle.txt", "left4dead/motd.txt",
		"left4dead/cfg/private_server.cfg", "left4dead/cfg/server.cfg");
	return files;
    }

    @Override
    public List<String> getFilesForSync() {

	final List<String> files = new ArrayList<String>();
	Collections.addAll(files, "left4dead/gameinfo.txt",
		"left4dead/missioncycle.txt", "left4dead/motd.txt",
		"left4dead/cfg/server.cfg");
	return files;
    }
}
