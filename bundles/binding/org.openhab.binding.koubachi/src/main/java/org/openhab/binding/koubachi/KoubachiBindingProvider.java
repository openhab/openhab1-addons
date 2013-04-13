/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.koubachi;

import org.openhab.binding.koubachi.internal.api.KoubachiResourceType;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Koubachi items.
 * 
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.2.0
 */
public interface KoubachiBindingProvider extends BindingProvider {
	
	/**
	 * Returns the configured Koubachi resource type of the given {@code itemName}.
	 * 
	 * @param itemName the item for which to find a resource type.
	 * @return the type of the Item identified by {@code itemName}
	 */
	KoubachiResourceType getResourceType(String itemName);

	/**
	 * Returns the configured Koubachi resource id of the given {@code itemName}.
	 * 
	 * @param itemName the item for which to find a resource id.
	 * @return the resource id of the {@link Item} identified by {@code itemName}
	 */
	String getResourceId(String itemName);

	/**
	 * Returns the configured Koubachi property name of the given {@code itemName}.
	 * 
	 * @param itemName the item for which to find a property name.
	 * @return the property name of the {@link Item} identified by {@code itemName}
	 */
	String getPropertyName(String itemName);	

}
