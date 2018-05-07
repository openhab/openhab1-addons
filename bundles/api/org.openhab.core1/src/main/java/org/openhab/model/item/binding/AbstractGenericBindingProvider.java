/**
 * Copyright (c) 2015-2018 by the respective copyright holders.
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This abstract class serves as a basis for implementations of binding providers that retrieve binding
 * information from the items configuration file(s), i.e. they register as {@link BindingConfigReader}s.
 * </p>
 *
 * <p>
 * This class takes care of tracking all changes in the binding config strings and makes sure that all
 * listeners are correctly notified of any change.
 * <p>
 *
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public abstract class AbstractGenericBindingProvider implements BindingConfigReader, BindingProvider {

    private static final Logger logger = LoggerFactory.getLogger(AbstractGenericBindingProvider.class);

    private Set<BindingChangeListener> listeners = new CopyOnWriteArraySet<>();

    /** caches binding configurations. maps itemNames to {@link BindingConfig}s */
    protected Map<String, BindingConfig> bindingConfigs = new ConcurrentHashMap<>();

    /**
     * stores information about the context of items. The map has this content
     * structure: context -> Set of Items
     */
    protected Map<String, Set<Item>> contextMap = new ConcurrentHashMap<>();

    public AbstractGenericBindingProvider() {
        super();
    }

    @Override
    public void addBindingChangeListener(BindingChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeBindingChangeListener(BindingChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        if (context == null) {
            throw new BindingConfigParseException("null context is not permitted for item " + item.getName());
        }
        synchronized (contextMap) {
            Set<Item> items = contextMap.get(context);
            if (items == null) {
                items = new HashSet<>();
                contextMap.put(context, items);
            }
            items.add(item);
        }
    }

    @Override
    public void removeConfigurations(String context) {
        Set<Item> items = null;
        synchronized (contextMap) {
            items = contextMap.get(context);
            if (items != null) {
                contextMap.remove(context);
            }
        }
        if (items != null) {
            for (Item item : items) {
                // we remove all binding configurations for all items
                bindingConfigs.remove(item.getName());
                notifyListeners(item);
            }
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
                logger.error("Binding {} threw an exception: ", listener.getClass().getName(), e);
            }
        }
    }

    @Override
    public boolean providesBindingFor(String itemName) {
        return bindingConfigs.get(itemName) != null;
    }

    @Override
    public boolean providesBinding() {
        return !bindingConfigs.isEmpty();
    }

    @Override
    public Collection<String> getItemNames() {
        return new ArrayList<>(bindingConfigs.keySet());
    }

}