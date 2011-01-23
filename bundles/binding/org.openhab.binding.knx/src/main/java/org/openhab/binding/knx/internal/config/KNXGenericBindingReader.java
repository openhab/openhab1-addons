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

package org.openhab.binding.knx.internal.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.commons.lang.ArrayUtils;
import org.openhab.binding.knx.config.KNXBindingProvider;
import org.openhab.binding.knx.internal.dpt.KNXCoreTypeMapper;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.Type;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.dptxlator.DPT;
import tuwien.auto.calimero.exception.KNXFormatException;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * <p>This class can parse information from the generic binding format and provides KNX binding information from it. It
 * registers as a {@link BindingConfigReader} service as well as as a {@link KNXBindingProvider} service.</p>
 * 
 * <p>The syntax of the binding configuration strings accepted is the following:<p>
 * <p><code>
 * 	knx="[&lt;dptId&gt;:][&lt;]&lt;mainGA&gt;[[+&lt;listeningGA&gt;]+&lt;listeningGA&gt;..],
 *  [&lt;dptId&gt;:][&lt;]&lt;mainGA&gt;[[+&lt;listeningGA&gt;]+&lt;listeningGA&gt;..]"
 * </code></p>
 * where parts in brackets [] signify an optional information.
 * 
 * <p>Each comma-separated section corresponds to an KNX datapoint. There is usually one datapoint defined per accepted 
 * command type of an openHAB item. If no datapoint type id is defined for the datapoint, this is automatically derived
 * from the list of accepted command types of the item - i.e. the second datapoint definition is mapped to the second
 * accepted command type of the item.</p>
 * <p> The optional '&lt;' sign tells whether the datapoint accepts read requests on the KNX bus (it does, if the sign is there)</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li> For an SwitchItem:
 * 	<ul>
 * 		<li><code>knx="1/1/10"</code></li>
 * 		<li><code>knx="1.001:1/1/10"</code></li>
 * 		<li><code>knx="<1/1/10"/code></li>
 * 		<li><code>knx="<1/1/10+0/1/13+0/1/14+0/1/15"</code></li>
 *	</ul>
 *	</li>
 *	<li> For a RollershutterItem:
 *	<ul>
 *		<li><code>knx="4/2/10"</code></li>
 *		<li><code>knx="4/2/10, 4/2/11"</code></li>
 *		<li><code>knx="1.008:4/2/10, 5.006:4/2/11"</code></li>
 *		<li><code>knx="<4/2/10+0/2/10, 5.006:4/2/11+0/2/11"</code></li>
 *	</ul>
 *	</li>
 * </ul>
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 * 
 */
public class KNXGenericBindingReader implements BindingConfigReader, KNXBindingProvider {

	/** the binding type to register for as a binding config reader */
	public static final String KNX_BINDING_TYPE = "knx";

	/**
	 * Here we store the results after parsing the binding configurations. We serve requests to the binding provider
	 * from this source
	 */
	private Map<Item, Collection<BindingConfig>> bindingConfigs = Collections.synchronizedMap(new WeakHashMap<Item, Collection<BindingConfig>>());

	/**
	 * Store, which item bindings were provided in which context. We need this information in order to delete outdated
	 * binding configs again
	 */
	private Map<String, Set<Item>> contextMap = new HashMap<String, Set<Item>>();

	private Set<BindingChangeListener> listeners = new HashSet<BindingChangeListener>();
	
	@Override
	public String getBindingType() {
		return KNX_BINDING_TYPE;
	}

	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		synchronized(bindingConfigs) {
			bindingConfigs.put(item, parseBindingConfigString(item, bindingConfig));
	
			// now fill the context map for later reference
			Set<Item> itemSet = contextMap.get(context);
			if (itemSet == null) {
				itemSet = new HashSet<Item>();
			}
			itemSet.add(item);
			contextMap.put(context, itemSet);
			notifyListeners(item);
		}
	}

	@Override
	public void removeConfigurations(String context) {
		synchronized(bindingConfigs) {
			if (contextMap.containsKey(context)) {
				for (Item item : contextMap.get(context)) {
					bindingConfigs.remove(item);
					notifyListeners(item);
				}
			}
		}
	}

	@Override
	public Datapoint getDatapoint(final String itemName, final GroupAddress groupAddress) {
		synchronized(bindingConfigs) {
			try {
				BindingConfig bindingConfig = Iterables.find(Iterables.concat(bindingConfigs.values()),
						new Predicate<BindingConfig>() {
							public boolean apply(BindingConfig input) {
								return input.itemName.equals(itemName)
										&& ArrayUtils.contains(input.groupAddresses, groupAddress);
							}
						});
				return bindingConfig.datapoint;
			} catch(NoSuchElementException e) {
				return null;
			}
		}
	}

	@Override
	public Datapoint getDatapoint(final String itemName, final Class<? extends Type> typeClass) {
		synchronized(bindingConfigs) {
			try {
				BindingConfig bindingConfig = Iterables.find(Iterables.concat(bindingConfigs.values()),
						new Predicate<BindingConfig>() {
							public boolean apply(BindingConfig input) {
								return input.itemName.equals(itemName)
										&& KNXCoreTypeMapper.toTypeClass(input.dpt.getID()).equals(typeClass);
							}
						});
				return bindingConfig.datapoint;
			} catch (NoSuchElementException e) {
				// ignore and return null
				return null;
			}
		}
	}

	@Override
	public Iterable<String> getListeningItemNames(final GroupAddress groupAddress) {
		synchronized(bindingConfigs) {
			Iterable<BindingConfig> filteredBindingConfigs = Iterables.filter(Iterables.concat(bindingConfigs.values()),
					new Predicate<BindingConfig>() {
						public boolean apply(BindingConfig input) {
							return ArrayUtils.contains(input.groupAddresses, groupAddress);
						}
					});
			return Iterables.transform(filteredBindingConfigs, new Function<BindingConfig, String>() {
				public String apply(BindingConfig from) {
					return from.itemName;
				}
			});
		}
	}

	@Override
	public Iterable<Datapoint> getReadableDatapoints() {
		synchronized(bindingConfigs) {
			Iterable<BindingConfig> filteredBindingConfigs = Iterables.filter(Iterables.concat(bindingConfigs.values()),
					new Predicate<BindingConfig>() {
						public boolean apply(BindingConfig input) {
							return input.readable;
						}
					});
			return Iterables.transform(filteredBindingConfigs, new Function<BindingConfig, Datapoint>() {
				public Datapoint apply(BindingConfig from) {
					return from.datapoint;
				}
			});
		}
	}

	@Override
	public void addBindingChangeListener(BindingChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeBindingChangeListener(BindingChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * This is the main method that takes care of parsing a binding configuration string for a given item.
	 * It returns a collection of {@link BindingConfig} instances, which hold all relevant data about the binding to
	 * KNX of an item.
	 * 
	 * @param item the item for which the binding configuration string is provided
	 * @param bindingConfig a string which holds the binding information
	 * @return a collection of {@link BindingConfig} instances, which hold all relevant data about the binding 
	 * @throws BindingConfigParseException if the configuration string has no valid syntax
	 */
	private Collection<BindingConfig> parseBindingConfigString(Item item, String bindingConfig)
			throws BindingConfigParseException {
		List<BindingConfig> configs = new ArrayList<BindingConfig>();
		String[] datapointConfigs = bindingConfig.trim().split(",");

		// we can have one datapoint per accepted command type of this item
		for (int i = 0; i < datapointConfigs.length; i++) {
			try {
				String datapointConfig = datapointConfigs[i];
				BindingConfig config = new BindingConfig();
				config.itemName = item.getName();

				// find the DPT for this entry
				String[] segments = datapointConfig.trim().split(":");
				Class<? extends Command> cmdType = item.getAcceptedCommandTypes().get(i);
				String dptId = segments.length == 1 ? getDefaultDPTId(cmdType) : segments[0];
				if (dptId == null || dptId.trim().isEmpty()) {
					throw new BindingConfigParseException("No DPT could be determined for the command type '"
							+ cmdType.getSimpleName() + "'.");
				}
				config.dpt = new DPT(dptId, null, null, null);
				String str = segments.length == 1 ? segments[0] : segments[1];

				// check for the readable flag
				if (str.trim().startsWith("<")) {
					config.readable = true;
					str = str.trim().substring(1);
				}

				// read all group addresses
				if(!str.trim().isEmpty()) {
					List<GroupAddress> gas = new ArrayList<GroupAddress>();
					for (String ga : str.trim().split("\\+")) {
						gas.add(new GroupAddress(ga.trim()));
					}
					config.groupAddresses = gas.toArray(new GroupAddress[gas.size()]);
					config.datapoint = new CommandDP(gas.get(0), item.getName(), 0, dptId);
					configs.add(config);
				}
			} catch (IndexOutOfBoundsException e) {
				throw new BindingConfigParseException("No more than " + i
						+ " datapoint definitions are allowed for this item.");
			} catch (KNXFormatException e) {
				throw new BindingConfigParseException(e.getMessage());
			}
		}
		return configs;
	}

	/** 
	 * Returns a default datapoint type id for a command class.
	 * 
	 * @param commandClass the command class
	 * @return the default datapoint type id
	 */
	private String getDefaultDPTId(Class<? extends Command> commandClass) {
		return KNXCoreTypeMapper.toDPTid(commandClass);
	}

	private void notifyListeners(Item item) {
		for(BindingChangeListener listener : listeners) {
			listener.bindingChanged(this, item.getName());
		}
	}

	/**
	 * This is an internal data structure to store information from the binding config strings and use it to answer the
	 * requests to the KNX binding provider.
	 * 
	 * @author Kai Kreuzer
	 * 
	 */
	private static class BindingConfig {
		public String itemName;
		public DPT dpt;
		public Datapoint datapoint;
		public boolean readable = false;
		public GroupAddress[] groupAddresses;
	}
}
