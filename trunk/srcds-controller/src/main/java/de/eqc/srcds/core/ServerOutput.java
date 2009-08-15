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
package de.eqc.srcds.core;

import java.util.Collection;

/**
 * The started process may produce output on the console. This interface
 * provides access to the history
 * 
 * @author Holger Cremer
 */
public interface ServerOutput {

    /**
     * Get the maximum size of the history.
     * 
     * @return the maximum line count.
     */
    int getMaxHistorySize();

    /**
     * Gets a (unmodifiable) collection of strings with the last (up to
     * {@link #getMaxHistorySize()}) output lines.
     * 
     * @return
     */
    Collection<String> getLastLog();

    /**
     * Adds an observer to get notice about every new line of process output.
     * 
     * @param o
     */
    void registerOnLogObserver(ProcessOutputObserver observer);

    /**
     * Unregisters an observer.
     * 
     * @param o
     */
    void unRegisterOnLogObserver(ProcessOutputObserver observer);
}
