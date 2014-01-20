/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.config;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.openhab.binding.knx.config.KNXBindingProvider;
import org.openhab.binding.knx.internal.dpt.KNXCoreTypeMapper;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.types.Type;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;

import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.CommandDP;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.datapoint.DatapointMap;
import tuwien.auto.calimero.datapoint.StateDP;
import tuwien.auto.calimero.exception.KNXFormatException;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

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
public class KNXGenericBindingProvider extends AbstractGenericBindingProvider implements KNXBindingProvider, AutoUpdateBindingProvider {

	/** the binding type to register for as a binding config reader */
	public static final String KNX_BINDING_TYPE = "knx";
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return KNX_BINDING_TYPE;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// all types of items are valid ...
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {

		super.processBindingConfiguration(context, item, bindingConfig);
		
		addBindingConfig(item, parseBindingConfigString(item, bindingConfig));
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Datapoint> getDatapoints(final String itemName, final GroupAddress groupAddress) {
		synchronized(bindingConfigs) {
			try {
				Iterable<KNXBindingConfig> configList = Iterables.filter(Iterables.concat(bindingConfigs.values()), KNXBindingConfig.class);
				Iterable<KNXBindingConfigItem> configItemList = Iterables.filter(Iterables.concat(configList), KNXBindingConfigItem.class);
				
				Iterable<KNXBindingConfigItem> bindingConfigs = Iterables.filter(configItemList,
						new Predicate<KNXBindingConfigItem>() {
							public boolean apply(KNXBindingConfigItem input) {
								return input.itemName.equals(itemName) && input.allDataPoints.contains(groupAddress);
							}
						});
				
				Iterable<Datapoint> datapoints = Iterables.transform(bindingConfigs, 
					new Function<KNXBindingConfigItem, Datapoint>() {
						public Datapoint apply(KNXBindingConfigItem configItem) {
							return configItem.mainDataPoint;
						}
					});
				
				return datapoints;
			}
			catch(NoSuchElementException e) {
				return null;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Datapoint> getDatapoints(final String itemName, final Class<? extends Type> typeClass) {
		synchronized(bindingConfigs) {
			try {
				Iterable<KNXBindingConfig> configList = Iterables.filter(Iterables.concat(bindingConfigs.values()), KNXBindingConfig.class);
				Iterable<KNXBindingConfigItem> configItemList = Iterables.filter(Iterables.concat(configList), KNXBindingConfigItem.class);
				Iterable<KNXBindingConfigItem> bindingConfigs = Iterables.filter(configItemList,
						new Predicate<KNXBindingConfigItem>() {
							public boolean apply(KNXBindingConfigItem input) {
								if(input==null) {
									return false;
								}
								return input.itemName.equals(itemName)
										&& KNXCoreTypeMapper.toTypeClass(input.mainDataPoint.getDPT()).equals(typeClass);
							}
						});
				
				Iterable<Datapoint> datapoints = Iterables.transform(bindingConfigs,
					new Function<KNXBindingConfigItem, Datapoint>() {
						public Datapoint apply(KNXBindingConfigItem configItem) {
							return configItem.mainDataPoint;
						}
					});
				
				return Lists.newArrayList(datapoints);
			}
			catch (NoSuchElementException e) {
				// ignore and return null
				return null;
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Iterable<String> getListeningItemNames(final GroupAddress groupAddress) {
		synchronized(bindingConfigs) {
			Iterable<KNXBindingConfig> configList = Iterables.filter(Iterables.concat(bindingConfigs.values()), KNXBindingConfig.class);
			Iterable<KNXBindingConfigItem> configItemList = Iterables.filter(Iterables.concat(configList), KNXBindingConfigItem.class);
			Iterable<KNXBindingConfigItem> filteredBindingConfigs = Iterables.filter(configItemList,
					new Predicate<KNXBindingConfigItem>() {
						public boolean apply(KNXBindingConfigItem input) {
							if(input==null) {
								return false;
							}
							return input.allDataPoints.contains(groupAddress);
						}
					});
			return Iterables.transform(filteredBindingConfigs, new Function<KNXBindingConfigItem, String>() {
				public String apply(KNXBindingConfigItem from) {
					if(from==null) {
						return null;
					}
					return from.itemName;
				}
			});
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isCommandGA(final GroupAddress groupAddress) {
		synchronized(bindingConfigs) {
			for (BindingConfig config : bindingConfigs.values()) {
				KNXBindingConfig knxConfig = (KNXBindingConfig) config;
				for (KNXBindingConfigItem configItem : knxConfig) {
					if (configItem.allDataPoints.contains(groupAddress)) {
						if(configItem.mainDataPoint instanceof CommandDP) {
							if(configItem.mainDataPoint.getMainAddress().equals(groupAddress)) {
								// the first GA in a CommandDP is always a command GA
								return true;
							} else {
								return false;
							}
						} else {
							// it is a StateDP, so the GA cannot be a command GA
							return false;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public Iterable<Datapoint> getReadableDatapoints() {
		synchronized(bindingConfigs) {
			Iterable<KNXBindingConfig> configList = Iterables.filter(Iterables.concat(bindingConfigs.values()), KNXBindingConfig.class);
			Iterable<KNXBindingConfigItem> configItemList = Iterables.filter(Iterables.concat(configList), KNXBindingConfigItem.class);
			Iterable<KNXBindingConfigItem> filteredBindingConfigs = Iterables.filter(configItemList,
					new Predicate<KNXBindingConfigItem>() {
						public boolean apply(KNXBindingConfigItem input) {
							if(input == null) {
								return false;
							}
							return input.readableDataPoint != null;
						}
					});
			return Iterables.transform(filteredBindingConfigs, new Function<KNXBindingConfigItem, Datapoint>() {
				public Datapoint apply(KNXBindingConfigItem from) {
					return from.readableDataPoint;
				}
			});
		}
	}

	@Override
	public Boolean autoUpdate(String itemName) {
		BindingConfig config = bindingConfigs.get(itemName);
		if(config instanceof KNXBindingConfig) {
			KNXBindingConfig knxConfig = (KNXBindingConfig) config;
			Iterator<KNXBindingConfigItem> it = knxConfig.iterator();
			while(it.hasNext()) {
				KNXBindingConfigItem item = it.next();
				if(item.allDataPoints.getDatapoints().size()>1) {
					// If the datapoint is a CommandDP, the first GA is the command GA, all other are listening GAs.
					// If the datapoint is a StateDP, all GAs are listening GAs.
					// If we have a single DPT configured with a command GA and at least one listening GA,
					// we deactivate the auto-update as we assume that status updates after a command
					// will come from KNX.
					return false;
				}
			}
		}
		return null;
	}

	/**
	 * This is the main method that takes care of parsing a binding configuration
	 * string for a given item. It returns a collection of {@link BindingConfig}
	 * instances, which hold all relevant data about the binding to KNX of an item.
	 * 
	 * @param item the item for which the binding configuration string is provided
	 * @param bindingConfig a string which holds the binding information
	 * @return a knx binding config, a collection of {@link KNXBindingConfigItem} 
	 *   instances, which hold all relevant data about the binding 
	 * @throws BindingConfigParseException if the configuration string has no valid syntax
	 */
	protected KNXBindingConfig parseBindingConfigString(Item item, String bindingConfig) throws BindingConfigParseException {
		KNXBindingConfig config = new KNXBindingConfig();
		String[] datapointConfigs = bindingConfig.trim().split(",");

		// we can have one datapoint per accepted command type of this item
		for (int i = 0; i < datapointConfigs.length; i++) {
			try {
				String datapointConfig = datapointConfigs[i].trim();
				KNXBindingConfigItem configItem = new KNXBindingConfigItem();
				configItem.itemName = item.getName();
				
				if (datapointConfig.split("<").length > 2) {
					throw new BindingConfigParseException("Only one readable GA allowed.");
				}

				Class<? extends Type> typeClass = item.getAcceptedCommandTypes().size() > 0 ?
						item.getAcceptedCommandTypes().get(i) : item.getAcceptedDataTypes().size() > 1 ? 
								item.getAcceptedDataTypes().get(i) : item.getAcceptedDataTypes().get(0);
				
				String[] dataPoints = datapointConfig.split("\\+");
				for (int j = 0; j < dataPoints.length; ++j) {
					String dataPoint = dataPoints[j].trim();
					
					// If dataPoint is empty, we most likely have "pure" listening DP (+x/y/z).
					// Just skip it, it will be handle in the next iteration.
					if (dataPoint.isEmpty()) {
						continue;
					}
					
					// check for the readable flag
					boolean isReadable = false;
					if (dataPoint.startsWith("<")) {
						isReadable = true;
						dataPoint = dataPoint.substring(1);
					}
					
					// find the DPT for this entry
					String[] segments = dataPoint.split(":");
					String dptID = (segments.length == 1) ? getDefaultDPTId(typeClass) : segments[0];
					if (dptID == null || dptID.trim().isEmpty()) {
						throw new BindingConfigParseException(
							"No DPT could be determined for the type '"	+ typeClass.getSimpleName() + "'.");
					}
				
					String ga = (segments.length == 1) ? segments[0].trim() : segments[1].trim();
					
					// create group address and datapoint
					GroupAddress groupAddress = new GroupAddress(ga);
					Datapoint dp;
					if (j != 0 || item.getAcceptedCommandTypes().size() == 0) {
						dp = new StateDP(groupAddress, item.getName(), 0, dptID);
					} else {
						dp = new CommandDP(groupAddress, item.getName(), 0, dptID);
					}
					
					// assign datapoint to configuration item
					if (configItem.mainDataPoint == null) {
						configItem.mainDataPoint = dp;
					}
					if (isReadable) {
						configItem.readableDataPoint = dp;
					}
					configItem.allDataPoints.add(dp);
				}
				
				config.add(configItem);
				
			} catch (IndexOutOfBoundsException e) {
				throw new BindingConfigParseException(
					"No more than " + i	+ " datapoint definitions are allowed for this item.");
			} catch (KNXFormatException e) {
				throw new BindingConfigParseException(e.getMessage());
			}
		}
		return config;
	}

	/** 
	 * Returns a default datapoint type id for a type class.
	 * 
	 * @param typeClass the type class
	 * @return the default datapoint type id
	 */
	private String getDefaultDPTId(Class<? extends Type> typeClass) {
		return KNXCoreTypeMapper.toDPTid(typeClass);
	}

	/**
	 * This is an internal container to gather all config items for one opeHAB item.
	 * 
	 * @author Kai Kreuzer
	 * 
	 */
	@SuppressWarnings("serial")
	/* default */ static class KNXBindingConfig extends LinkedList<KNXBindingConfigItem> implements BindingConfig {}
	
	/**
	 * This is an internal data structure to store information from the binding config strings and use it to answer the
	 * requests to the KNX binding provider.
	 * 
	 * @author Kai Kreuzer
	 * 
	 */
	/* default */ static class KNXBindingConfigItem {
		public String itemName;
		public Datapoint mainDataPoint = null;
		public Datapoint readableDataPoint = null;
		public DatapointMap allDataPoints = new DatapointMap();
	}
}
