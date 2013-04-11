/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.homematic;

import org.openhab.binding.homematic.internal.config.AdminItem;
import org.openhab.binding.homematic.internal.config.ParameterAddress;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * The interface to implement to provide a binding for Homematic.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public interface HomematicBindingProvider extends BindingProvider {

    /**
     * @return the parameter address to the given <code>itemName</code>
     */
    ParameterAddress getParameterAddress(String itemName);

    /**
     * @return the admin item to the given <code>itemName</code>
     */
    AdminItem getAdminItem(String itemName);

    /**
     * @return true if the item is an ADMIN item
     */
    boolean isAdminItem(String itemName);

    /**
     * @param itemName The name of the item
     * @return The item with the given name
     */
    Item getItem(String itemName);

}
