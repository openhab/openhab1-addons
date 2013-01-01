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
package org.openhab.model.script.internal.engine;

import org.openhab.core.items.ItemRegistry;
import org.openhab.model.script.internal.ScriptActivator;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * This class allows guice-enabled classes to have access to the item registry
 * without going through OSGi declarative services.
 * Though it is very handy, this should be rather seen as a workaround - I am not
 * yet clear on how best to combine guice injection and OSGi DS.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
@Singleton
public class ItemRegistryProvider implements Provider<ItemRegistry> {
	public ItemRegistry get() {
		ItemRegistry itemRegistry = (ItemRegistry) ScriptActivator.itemRegistryTracker.getService();
		return itemRegistry;
	}
}