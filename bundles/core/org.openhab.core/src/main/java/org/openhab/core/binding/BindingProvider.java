/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.core.binding;


/**
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 0.6.0
 */
public interface BindingProvider {

	/**
	 * Adds a binding change listener, which gets notified whenever there 
	 * are changes in the binding configuration
	 * 
	 * @param listener the binding change listener to add
	 */
	public void addBindingChangeListener(BindingChangeListener listener);

	/**
	 * Removes a binding change listener again.
	 * Does nothing, if this listener has not been added before.
	 * 
	 * @param listener the binding listener to remove
	 */
	public void removeBindingChangeListener(BindingChangeListener listener);
	
	/**
	 * Indicates whether this binding provider contains a binding for the given
	 * <code>itemName</code>
	 * 
	 * @param itemName the itemName to check
	 * 
	 * @return <code>true</code> if this provider contains an adequate mapping
	 * for <code>itemName</code> and <code>false</code> otherwise.
	 */
	boolean providesBindingFor(String itemName);

	/**
	 * Indicates whether this binding provider contains any binding
	 * 
	 * @return <code>true</code> if this provider contains any binding 
	 * configuration and <code>false</code> otherwise 
	 */
	public boolean providesBinding();

}
