/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.binding.networkhealth;



/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and NetworkHealth items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public interface NetworkHealthBindingProvider {

	/**
	 * @return the corresponding hostname of the given <code>itemName</code>
	 */
	public String getHostname(String itemName);
	
	/**
	 * @return the corresponding port of the given <code>itemName</code>
	 */
	public int getPort(String itemName);
	
	/**
	 * @return the corresponding timeout of the given <code>itemName</code>
	 */
	public int getTimeout(String itemName);
	
	/**
	 * @return all itemNames with a network health binding 
	 */
	public Iterable<String> getItemNames();

	/**
	 * @return <code>true</code> if this provider contains any binding configuration 
	 */
	public boolean containsBinding();

}
