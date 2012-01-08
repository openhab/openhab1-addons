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
package org.openhab.model.item.binding;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * <p>This abstract class serves as a basis for implementations of binding providers that retrieve binding
 * information from the items configuration file(s), i.e. they register as {@link BindingConfigReader}s.</p>
 * 
 * <p>This class takes care of tracking all changes in the binding config strings and makes sure that all
 * listeners are correctly notified of any change.<p>
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public abstract class AbstractGenericBindingProvider implements BindingConfigReader, BindingProvider {

	private Set<BindingChangeListener> listeners = Collections.synchronizedSet(new HashSet<BindingChangeListener>());

	/** caches binding configurations. maps itemNames to {@link BindingConfig}s */
	protected Map<String, BindingConfig> bindingConfigs = Collections.synchronizedMap(new WeakHashMap<String, BindingConfig>());

	/** 
	 * stores information about the context of items. The map has this content
	 * structure: context -> Set of itemNames
	 */ 
	private Map<String, Set<Item>> contextMap = Collections.synchronizedMap(new HashMap<String, Set<Item>>());

	public AbstractGenericBindingProvider() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public void addBindingChangeListener(BindingChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeBindingChangeListener(BindingChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		Set<Item> items = contextMap.get(context);
		if (items==null) {
			items = new HashSet<Item>();
			contextMap.put(context, items);
		}
			
		items.add(item);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeConfigurations(String context) {
		Set<Item> items = contextMap.get(context);
		if(items!=null) {
			for(Item item : items) {
				// we remove all binding configurations for all items
				bindingConfigs.remove(item.getName());
				notifyListeners(item);
			}
			contextMap.remove(context);
		}
	}
	
	protected void addBindingConfig(Item item, BindingConfig config) {
		bindingConfigs.put(item.getName(), config);
		notifyListeners(item);
	}

	private void notifyListeners(Item item) {
		for (BindingChangeListener listener : listeners) {
			listener.bindingChanged(this, item.getName());
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	public boolean providesBindingFor(String itemName) {
		return bindingConfigs.get(itemName) != null;
	}
	
	/**
	 * @{inheritDoc}
	 */
	public boolean providesBinding() {
		return !bindingConfigs.isEmpty();
	}
	

}