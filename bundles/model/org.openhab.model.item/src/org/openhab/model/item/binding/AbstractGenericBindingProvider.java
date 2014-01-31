/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.item.binding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger logger = LoggerFactory.getLogger(AbstractGenericBindingProvider.class);

	private Set<BindingChangeListener> listeners = new CopyOnWriteArraySet<BindingChangeListener>();

	/** caches binding configurations. maps itemNames to {@link BindingConfig}s */
	protected Map<String, BindingConfig> bindingConfigs = new ConcurrentHashMap<String, BindingConfig>(new WeakHashMap<String, BindingConfig>());

	/** 
	 * stores information about the context of items. The map has this content
	 * structure: context -> Set of Items
	 */ 
	protected Map<String, Set<Item>> contextMap = new ConcurrentHashMap<String, Set<Item>>();
	

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
            try {
                listener.bindingChanged(this, item.getName());
            } catch (Exception e) {
                logger.error("Binding " + listener.getClass().getName() + " threw an exception: ", e);
            }
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
	
	/**
	 * {@inheritDoc}
	 */
	public Collection<String> getItemNames() {
		return new ArrayList<String>(bindingConfigs.keySet());
	}	

}