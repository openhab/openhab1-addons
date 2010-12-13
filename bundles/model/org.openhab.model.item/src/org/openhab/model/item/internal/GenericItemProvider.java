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

package org.openhab.model.item.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemProvider;
import org.openhab.core.items.ItemsChangeListener;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.ItemsStandaloneSetup;
import org.openhab.model.core.EventType;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;
import org.openhab.model.items.Binding;
import org.openhab.model.items.GroupItem;
import org.openhab.model.items.ItemModel;
import org.openhab.model.items.NormalItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericItemProvider implements ItemProvider, ModelRepositoryChangeListener {

	public GenericItemProvider() {
		// make sure that the DSL is correctly registered with EMF before we start
		new ItemsStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
	
	private static final Logger logger = LoggerFactory.getLogger(GenericItemProvider.class);
	
	/** to keep track of all item change listeners */
	private Collection<ItemsChangeListener> listeners = new HashSet<ItemsChangeListener>();

	/** to keep track of all binding config readers */
	private Map<String, BindingConfigReader> bindingConfigReaders = new HashMap<String, BindingConfigReader>();

	private ModelRepository modelRepository = null;
	
	public Collection<Item> getItems() {
		Map<String, Item> items = new HashMap<String, Item>();
		if(modelRepository!=null) {
			for(String modelName : modelRepository.getAllModelNamesOfType("items")) {
				ItemModel model = (ItemModel) modelRepository.getModel(modelName);
				if(model==null) continue;
				
				// clear the old binding information
				for(BindingConfigReader reader : bindingConfigReaders.values()) {
					reader.removeConfigurations(modelName);
				}
				
				for(org.openhab.model.items.Item modelItem : model.getItems()) {
					Item item = null;					
					if(modelItem instanceof GroupItem) {
						GroupItem groupItem = (GroupItem) modelItem;
						item = new org.openhab.core.items.GroupItem(groupItem.getName());
						items.put(groupItem.getName(), item);
					} else {
						NormalItem normalItem = (NormalItem) modelItem;
						String itemName = normalItem.getName(); 
						if(normalItem.getType().equals("Switch")) item = new SwitchItem(itemName);
						if(normalItem.getType().equals("Rollershutter")) item = new RollershutterItem(itemName);
						if(normalItem.getType().equals("Contact")) item = new ContactItem(itemName);
						if(normalItem.getType().equals("String")) item = new StringItem(itemName);
						if(normalItem.getType().equals("Number")) item = new NumberItem(itemName);
						if(normalItem.getType().equals("Dimmer")) item = new DimmerItem(itemName);
						if(item!=null) items.put(itemName, item);
					}
					
					if(item!=null) {
						dispatchBindings(modelName, item, modelItem.getBindings());
					}
				}
				// now process the group contents
				for(org.openhab.model.items.Item modelItem :  model.getItems()) {
					for(GroupItem groupItem : modelItem.getGroups()) {
						org.openhab.core.items.GroupItem group = (org.openhab.core.items.GroupItem) items.get(groupItem.getName());
						group.addMember(items.get(modelItem.getName()));
					}
				}
			}
		}
		return items.values();
	}

	private void dispatchBindings(String modelName, Item item, EList<Binding> bindings) {
		for(Binding binding : bindings) {
			String bindingType = binding.getType();
			String config = binding.getConfiguration();
			BindingConfigReader reader = bindingConfigReaders.get(bindingType);
			if(reader!=null) {
				try {
					reader.processBindingConfiguration(modelName, item, config);
				} catch (BindingConfigParseException e) {
					logger.error("Binding information of type '" + bindingType + "' for item ‘{}‘ could not be parsed correctly.", item.getName() , e);
				}
			}
		}
	}

	public void addItemChangeListener(ItemsChangeListener listener) {
		listeners.add(listener);
	}

	public void removeItemChangeListener(ItemsChangeListener listener) {
		listeners.remove(listener);
	}

	public void addBindingConfigReader(BindingConfigReader reader) {
		if(!bindingConfigReaders.containsKey(reader.getBindingType())) {
			bindingConfigReaders.put(reader.getBindingType(), reader);
		} else {
			logger.warn("There are two binding configuration readers registered. " +
			"Only the one of them will be active!");
		}
	}

	public void removeBindingConfigReader(BindingConfigReader reader) {
		if(bindingConfigReaders.get(reader.getBindingType()).equals(reader)) {
			bindingConfigReaders.remove(reader.getBindingType());
		}
	}

	public void setModelRepository(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
		modelRepository.addModelRepositoryChangeListener(this);
	}

	public void unsetModelRepository(ModelRepository modelRepository) {
		modelRepository.removeModelRepositoryChangeListener(this);
		this.modelRepository = null;
	}

	@Override
	public void modelChanged(String modelName, EventType type) {
		for(ItemsChangeListener listener : listeners) {
			listener.allItemsChanged(this, null);
		}
	}

}
