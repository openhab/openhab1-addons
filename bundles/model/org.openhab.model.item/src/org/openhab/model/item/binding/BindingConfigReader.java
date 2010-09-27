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

package org.openhab.model.item.binding;

import org.openhab.core.items.Item;

/**
 * This interface must be implemented by services, which can parse the generic 
 * binding configuration string used in the {@link GenericItemProvider}.
 *  
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public interface BindingConfigReader {

	/**
	 * This defines the type of binding this reader will process, e.g. "knx".
	 * 
	 * @return the type of the binding
	 */
	public String getBindingType();
	
	/**
	 * This method is called by the {@link GenericItemProvider} whenever it comes
	 * across a binding configuration string for an item.
	 * 
	 * @param context a string of the context from where this item comes from. Usually the file name of the config file
	 * @param item the item for which the binding is defined
	 * @param bindingConfig the configuration string that must be processed
	 * 
	 * @throws BindingConfigParseException if the configuration string is not valid
	 */
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException;
	
	/**
	 * Removes all configuration information for a given context. This is usually called if a config file is reloaded,
	 * so that the old values are removed, before the new ones are processed.
	 * 
	 * @param context the context of the configurations that should be removed
	 */
	public void removeConfigurations(String context);
}
