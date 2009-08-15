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
package de.eqc.srcds.enums;

import de.eqc.srcds.games.AbstractGame;
import de.eqc.srcds.games.impl.CounterStrikeSource;
import de.eqc.srcds.games.impl.Left4Dead;

public enum GameType {

    LEFT4DEAD(new Left4Dead()),
    COUNTERSTRIKE_SOURCE(new CounterStrikeSource());

    private final AbstractGame implementation;

    private GameType(final AbstractGame implementation) {

	this.implementation = implementation;
    }

    public AbstractGame getImplementation() {

	return implementation;
    }

}