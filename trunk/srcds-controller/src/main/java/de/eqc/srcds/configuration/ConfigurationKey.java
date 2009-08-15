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
package de.eqc.srcds.configuration;

import java.util.Collection;
import java.util.LinkedList;



public class ConfigurationKey<T> implements Comparable<ConfigurationKey<T>> {

    private final String key;
    private final String description;
    private final T defaultValue;
    private final Integer order;

    public ConfigurationKey(final String key, final T defaultValue, final String description, final int order) {

	this.key = key;
	this.defaultValue = defaultValue;
	this.description = description;
	this.order = order;
    }
    
    public final String getKey() {

	return key;
    }

    public final Class<?> getDataType() {

	return defaultValue.getClass();
    }
    
    public final boolean isEnumerationType() {
	
	return defaultValue.getClass().isEnum();
    }
    
    public final Collection<String> getEnumValues() {
	
	if (!isEnumerationType()) {
	    throw new IllegalStateException("Data type is not an enumeration");
	}
	
	final Collection<String> enumValues = new LinkedList<String>();
	for (Object constants : defaultValue.getClass().getEnumConstants()) {
	    enumValues.add(constants.toString());
	}
	return enumValues;
    }
    
    public T getDefaultValue() {

	return defaultValue;
    }
    
    public final String getDescription() {

	return description;
    }    

    
    public final int getOrder() {

	return order;
    }

    @Override
    public int compareTo(final ConfigurationKey<T> entry) {

	return order.compareTo(entry.order);
    }
}