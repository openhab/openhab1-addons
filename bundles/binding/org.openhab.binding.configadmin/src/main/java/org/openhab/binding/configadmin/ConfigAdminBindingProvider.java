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
package org.openhab.binding.configadmin;

import java.util.Collection;

import org.openhab.binding.configadmin.internal.ConfigAdminGenericBindingProvider.ConfigAdminBindingConfig;
import org.openhab.core.binding.BindingProvider;


/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and ConfigAdmin items.
 * 
 * Implementing classes should register themselves as a service in order to be 
 * taken into account.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public interface ConfigAdminBindingProvider extends BindingProvider {

	/**
	 * Returns the binding {@link ConfigAdminBindingConfig} corresponding to
	 * the given <code>itemName</code>
	 * 
	 * @param itemName
	 * @return the {@link ConfigAdminBindingConfig} for the given
	 * <code>itemName</code> or <code>null</code> if there is none.
	 */
	ConfigAdminBindingConfig getBindingConfig(String itemName);

	/**
	 * Returns all {@link ConfigAdminBindingConfig}s for the given <code>pid</code>
	 * or an empty Collection if there is none.
	 *   
	 * @param pid
	 * @return a collection of all {@link ConfigAdminBindingConfig}s for the 
	 * given <code>pid</code>. Can be empty if there is no config found.
	 */
	Collection<ConfigAdminBindingConfig> getBindingConfigByPid(String pid);
	
}
