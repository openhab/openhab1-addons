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
package org.openhab.model.item.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.GroupFunction;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemFactory;
import org.openhab.core.items.ItemProvider;
import org.openhab.core.items.ItemsChangeListener;
import org.openhab.core.library.types.ArithmeticGroupFunction;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.model.ItemsStandaloneSetup;
import org.openhab.model.core.EventType;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;
import org.openhab.model.items.ItemModel;
import org.openhab.model.items.ModelBinding;
import org.openhab.model.items.ModelGroupFunction;
import org.openhab.model.items.ModelGroupItem;
import org.openhab.model.items.ModelItem;
import org.openhab.model.items.ModelNormalItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericItemProvider implements ItemProvider,
		ModelRepositoryChangeListener {

	public GenericItemProvider() {
		// make sure that the DSL is correctly registered with EMF before we
		// start
		new ItemsStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	private static final Logger logger = 
		LoggerFactory.getLogger(GenericItemProvider.class);

	/** to keep track of all item change listeners */
	private Collection<ItemsChangeListener> listeners = new HashSet<ItemsChangeListener>();

	/** to keep track of all binding config readers */
	private Map<String, BindingConfigReader> bindingConfigReaders = new HashMap<String, BindingConfigReader>();

	private ModelRepository modelRepository = null;
	
	private Collection<ItemFactory> itemFactorys = new ArrayList<ItemFactory>();
	
	
	public void addItemFactory(ItemFactory factory) {
		itemFactorys.add(factory);
	}
	
	public void removeItemFactory(ItemFactory factory) {
		itemFactorys.remove(factory);
	}
	

	public Collection<Item> getItems() {
		List<Item> items = new ArrayList<Item>();
		if (modelRepository != null) {
			for (String modelName : modelRepository.getAllModelNamesOfType("items")) {
				ItemModel model = (ItemModel) modelRepository.getModel(modelName);
				if (model == null) {
					continue;
				}

				// clear the old binding information
				for (BindingConfigReader reader : bindingConfigReaders.values()) {
					reader.removeConfigurations(modelName);
				}

				for (ModelItem modelItem : model.getItems()) {
					Item item = null;
					if (modelItem instanceof ModelGroupItem) {
						ModelGroupItem modelGroupItem = (ModelGroupItem) modelItem;
						String baseItemType = modelGroupItem.getType();
						GenericItem baseItem = getItemOfType(baseItemType, modelGroupItem.getName());
						if (baseItem != null) {
							ModelGroupFunction function = modelGroupItem.getFunction();
							if (function == null) {
								item = new GroupItem(modelGroupItem.getName(), baseItem);
							} else {
								item = applyGroupFunction(baseItem, modelGroupItem, function);
							}
						} else {
							item = new GroupItem(modelGroupItem.getName());
						}
					} else {
						ModelNormalItem normalItem = (ModelNormalItem) modelItem;
						String itemName = normalItem.getName();
						item = getItemOfType(normalItem.getType(), itemName);
					}

					if (item != null) {
						for (String groupName : modelItem.getGroups()) {
							item.getGroupNames().add(groupName);
						}
						items.add(item);
						dispatchBindings(modelName, item, modelItem.getBindings());
					}
				}
			}
		}
		return items;
	}
	
	private GroupItem applyGroupFunction(GenericItem baseItem, ModelGroupItem modelGroupItem, ModelGroupFunction function) {
		List<State> args = new ArrayList<State>();
		for (String arg : modelGroupItem.getArgs()) {
			State state = TypeParser.parseState(baseItem.getAcceptedDataTypes(), arg);
			if (state == null) {
				logger.warn("State '" + arg	+ "' is not valid for group item '"
						+ modelGroupItem.getName() + "' with base type '"
						+ modelGroupItem.getType() + "'");
				args.clear();
				break;
			} else {
				args.add(state);
			}
		}
		
		GroupFunction groupFunction = null;
		switch (function) {
			case AND:
				if (args.size() == 2) {
					groupFunction = new ArithmeticGroupFunction.And(args.get(0), args.get(1));
					break;
				} else {
					logger.error("Group function 'AND' requires two arguments. Using Equality instead.");
				}
			case OR:
				if (args.size() == 2) {
					groupFunction = new ArithmeticGroupFunction.Or(args.get(0), args.get(1));
					break;
				} else {
					logger.error("Group function 'OR' requires two arguments. Using Equality instead.");
				}
			case NAND:
				if (args.size() == 2) {
					groupFunction = new ArithmeticGroupFunction.NAnd(args.get(0), args.get(1));
					break;
				} else {
					logger.error("Group function 'NOT AND' requires two arguments. Using Equality instead.");
				}
				break;
			case NOR:
				if (args.size() == 2) {
					groupFunction = new ArithmeticGroupFunction.NOr(args.get(0), args.get(1));
					break;
				} else {
					logger.error("Group function 'NOT OR' requires two arguments. Using Equality instead.");
				}
			case AVG:
				groupFunction = new ArithmeticGroupFunction.Avg();
				break;
			case SUM:
				groupFunction = new ArithmeticGroupFunction.Sum();
				break;
			case MIN:
				groupFunction = new ArithmeticGroupFunction.Min();
				break;
			case MAX:
				groupFunction = new ArithmeticGroupFunction.Max();
				break;
			default:
				logger.error("Unknown group function '"
					+ function.getName() + "'. Using Equality instead.");
		}
		
		if (groupFunction == null) {
			groupFunction = new GroupFunction.Equality();
		}
		
		return new GroupItem(modelGroupItem.getName(), baseItem, groupFunction);
	}

	private void dispatchBindings(String modelName, Item item,
			EList<ModelBinding> bindings) {
		for (ModelBinding binding : bindings) {
			String bindingType = binding.getType();
			String config = binding.getConfiguration();
			BindingConfigReader reader = bindingConfigReaders.get(bindingType);
			if (reader != null) {
				try {
					reader.validateItemType(item, config);
					reader.processBindingConfiguration(modelName, item, config);
				} catch (BindingConfigParseException e) {
					logger.error("Binding information of type '" + bindingType
							+ "' for item ‘" + item.getName()
							+ "‘ could not be parsed correctly.", e);
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
		if (!bindingConfigReaders.containsKey(reader.getBindingType())) {
			bindingConfigReaders.put(reader.getBindingType(), reader);
			
			// Re-read items after adding the new config reader ...
			modelChanged(".items",null);
		} else {
			logger.warn("There are two binding configuration readers registered. "
					+ "Only one of them will be active!");
		}
	}

	public void removeBindingConfigReader(BindingConfigReader reader) {
		if (bindingConfigReaders.get(reader.getBindingType()).equals(reader)) {
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

	public void modelChanged(String modelName, EventType type) {
		if (modelName.endsWith("items")) {
			for (ItemsChangeListener listener : listeners) {
				listener.allItemsChanged(this, null);
			}
		}
	}

	protected GenericItem getItemOfType(String itemType, String itemName) {
		if (itemType == null) {
			return null;
		}
		
		for (ItemFactory factory : itemFactorys) {
			GenericItem item = factory.createItem(itemType, itemName);
			if (item != null) {
				return item;
			}
		}
		
		return null;
	}

}
