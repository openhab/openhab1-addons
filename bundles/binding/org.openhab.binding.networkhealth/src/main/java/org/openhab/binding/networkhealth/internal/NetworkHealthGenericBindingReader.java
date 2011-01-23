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

package org.openhab.binding.networkhealth.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openhab.binding.networkhealth.NetworkHealthBindingProvider;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides NetworkHealth binding information from it. It registers as a 
 * {@link NetworkHealthBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ nh="192.168.1.100" }</code> - which checks if the given host allows connections on port 80 with a default timeout of 5000ms</li>
 * 	<li><code>{ nh="imap.email.com:993" }</code> - which checks if the given host allows connections on port 993 with a default timeout of 5000ms</li>
 * 	<li><code>{ nh="ssh.secureserver.com:22:10000" } -  - which checks if the given host allows connections on port 22 with a timeout of 10000ms</code></li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class NetworkHealthGenericBindingReader implements BindingConfigReader, NetworkHealthBindingProvider {

	/** caches binding configurations. maps itemNames to {@link BindingConfig}s */
	private Map<String, BindingConfig> bindingConfigs = new HashMap<String, BindingConfig>();

	/** 
	 * stores information about the context of items. The map has this content
	 * structure: context -> Set of itemNames
	 */ 
	private Map<String, Set<Item>> contextMap = new HashMap<String, Set<Item>>();

	private Set<BindingChangeListener> listeners = new HashSet<BindingChangeListener>();
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "nh";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		
		if (item instanceof SwitchItem) {
			
			String[] configParts = bindingConfig.trim().split(":");
			if (configParts.length > 3) {
				throw new BindingConfigParseException("NetworkHealth configuration can contain three parts at max");
			}
			
			BindingConfig config = new BindingConfig();
			
			item.getName();
			config.hostname = configParts[0];
			if (configParts.length > 1) {
				config.port = Integer.valueOf(configParts[1]);
			}
			if (configParts.length > 2) {
				config.timeout = Integer.valueOf(configParts[2]);
			}
										
			bindingConfigs.put(item.getName(), config);
			notifyListeners(item);
		}
					
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
	@Override
	public void removeConfigurations(String context) {
		Set<Item> items = contextMap.get(context);
		if(items!=null) {
			for(Item item : items) {
				// we remove all information in the serial devices
				BindingConfig config = bindingConfigs.get(item.getName());
				bindingConfigs.remove(config);
				notifyListeners(item);
			}
			contextMap.remove(context);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getHostname(String itemName) {
		BindingConfig config = bindingConfigs.get(itemName);
		return config != null ? bindingConfigs.get(itemName).hostname : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPort(String itemName) {
		BindingConfig config = bindingConfigs.get(itemName);
		return config != null ? bindingConfigs.get(itemName).port : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getTimeout(String itemName) {
		BindingConfig config = bindingConfigs.get(itemName);
		return config != null ? bindingConfigs.get(itemName).timeout : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<String> getItemNames() {
		return bindingConfigs.keySet();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsBinding() {
		return !bindingConfigs.isEmpty();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void addBindingChangeListener(BindingChangeListener listener) {
		listeners.add(listener);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void removeBindingChangeListener(BindingChangeListener listener) {
		listeners.remove(listener);
	}

	private void notifyListeners(Item item) {
		for (BindingChangeListener listener : listeners) {
			listener.bindingChanged(this, item.getName());
		}
	}

	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the NetworkHealth
	 * binding provider.
	 * 
	 * @author thomasee
	 */
	private class BindingConfig {
		public String hostname;
		public int port;
		public int timeout;
	}


}
