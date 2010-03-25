/* 
* openHAB, the open Home Automation Bus.
* Copyright 2010, openHAB.org
*
* See the contributors.txt file in the distribution for a
* full listing of individual contributors.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as
* published by the Free Software Foundation, either version 3 of the
* License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package org.openhab.core.items;

/**
 * This is a listener interface which should be implemented where ever item providers are used in order
 * to be notified of any dynamic changes in the provided items.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public interface ItemChangeListener {

	/**
	 * Notifies the listener that all items of a provider have changed and thus should be reloaded.
	 * 
	 * @param provider the concerned item provider 
	 */
	public void allItemsChanged(ItemProvider provider);

	/**
	 * Notifies the listener that a single item has been added
	 * 
	 * @param provider the concerned item provider 
	 * @param item the item that has been added
	 */
	public void itemAdded(ItemProvider provider, GenericItem item);
	
	/**
	 * Notifies the listener that a single item has been removed
	 * 
	 * @param provider the concerned item provider 
	 * @param item the item that has been removed
	 */
	public void itemRemoved(ItemProvider provider, GenericItem item);
	
}
