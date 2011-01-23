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

package org.openhab.config.misterhouse.internal.items;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.knx.config.KNXBindingProvider;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemProvider;
import org.openhab.core.items.ItemsChangeListener;
import org.openhab.core.types.Type;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIProvider;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.Datapoint;

/**
 * This class provides items by parsing a Misterhouse mht file.
 * 
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public class MhItemProvider implements ItemProvider, ItemUIProvider, ManagedService, KNXBindingProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(MhItemProvider.class);
	
	/** to keep track of all item change listeners */
	private Collection<ItemsChangeListener> listeners = new HashSet<ItemsChangeListener>();

	/** to keep track of all binding change listeners */
	private Collection<BindingChangeListener> bindingListeners = new HashSet<BindingChangeListener>();

	/** the URL to the misterhouse config file */
	private URL configFileURL;
	
	
	public void activate() {
		if(configFileURL!=null) {
			try {
				InputStream is = configFileURL.openStream();
				MhtFileParser.parse(is);
			} catch (IOException e) {
				logger.error("Cannot read config file: " + e.getMessage());
			} catch (ParserException e) {
				logger.error("Cannot parse config file: " + e.getMessage());
			}
		} else {
			logger.debug("No config file has been set yet.");
		}		
	}
	
	public void deactivate() {
	}
	
	public synchronized Collection<Item> getItems() {
		if(configFileURL!=null) {
			try {
				InputStream is = configFileURL.openStream();
				return MhtFileParser.parse(is);
			} catch (IOException e) {
				logger.error("Cannot read config file: " + e.getMessage());
			} catch (ParserException e) {
				logger.error("Cannot parse config file: " + e.getMessage());
			}
		} else {
			logger.debug("No config file has been set yet.");
		}
		return Collections.emptySet();
	}

	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		if(config!=null) {
			try {
				configFileURL = new URL("file", "", (String) config.get("mhtFile"));
			} catch (MalformedURLException e) {
				logger.error("Cannot locate config file", e);
			}
		}
		// we run getItems() once here as this initializes all maps that store relevant data
		getItems();
		notifyListeners();
	}

	/** 
	 * notifies all listeners that they should reload the complete set of items 
	 */
	private void notifyListeners() {
		for(ItemsChangeListener listener : listeners) {
			Set<String> emptySet = Collections.emptySet();
			listener.allItemsChanged(this, emptySet);
		}
		for(BindingChangeListener listener : bindingListeners) {
			listener.allBindingsChanged(this);
		}
	}

	public void addItemChangeListener(ItemsChangeListener listener) {
		listeners.add(listener);
	}

	public void removeItemChangeListener(ItemsChangeListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void addBindingChangeListener(BindingChangeListener listener) {
		bindingListeners.add(listener);
	}

	@Override
	public void removeBindingChangeListener(BindingChangeListener listener) {
		bindingListeners.remove(listener);
	}

	@Override
	public String getIcon(String itemName) {
		return MhtFileParser.iconMap.get(itemName);
	}

	@Override
	public String getLabel(String itemName) {
		return MhtFileParser.labelMap.get(itemName);
	}

	@Override
	public Widget getDefaultWidget(Class<? extends Item> itemType, String itemName) {
		
		// we only want to react to items that we provide ourselves
		if(!MhtFileParser.allItems.contains(itemName)) return null;

		// currently, we have no special treatment defined
		return null;
	}

	@Override
	public Datapoint getDatapoint(String itemName, Class<? extends Type> typeClass) {
		return MhtFileParser.datapointMap.get(itemName+","+typeClass.getSimpleName());
	}

	@Override
	public Collection<String> getListeningItemNames(GroupAddress groupAddress) {
		List<String> itemNames = new ArrayList<String>();
		for(Entry<String, GroupAddress[]> entry : MhtFileParser.listeningGroupAddressMap.entrySet()) {
			if(ArrayUtils.contains(entry.getValue(),groupAddress)) {
				itemNames.add(entry.getKey());
			}
		}
		return itemNames;
	}

	@Override
	public Datapoint getDatapoint(String itemName, GroupAddress groupAddress) {
		Class<? extends Type> typeClass = MhtFileParser.typeMap.get(groupAddress);
		if(typeClass!=null) {
			return MhtFileParser.datapointMap.get(itemName+","+typeClass.getSimpleName());
		} else {
			return null;
		}
	}

	@Override
	public Collection<Datapoint> getReadableDatapoints() {
		List<Datapoint> datapoints = new ArrayList<Datapoint>();
		for(Entry<String, Datapoint> entry : MhtFileParser.datapointMap.entrySet()) {
			String[] parts = entry.getKey().split(",");
			if(MhtFileParser.readableItems.contains(parts[0])) {
				datapoints.add(entry.getValue());
			}
		}
		return datapoints;
	}

}
