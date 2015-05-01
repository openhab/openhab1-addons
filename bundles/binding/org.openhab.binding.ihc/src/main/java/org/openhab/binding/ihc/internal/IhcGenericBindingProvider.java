/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.internal;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.ihc.IhcBindingProvider;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * Ihc binding information from it. It registers as a {@link IhcBindingProvider}
 * service as well.
 * </p>
 * 
 * <p>
 * The syntax of the binding configuration strings accepted is the following:
 * <p>
 * <p>
 * <code>
 * 	ihc="&lt;[>]ResourceId&gt;[:&lt;refreshintervalinseconds&gt;]"
 * </code>
 * </p>
 * where parts in brackets [] signify an optional information.
 * 
 * The optional '>' sign tells whether resource is only out binding, where
 * internal update from OpenHAB bus is transmitted to the controller.
 * 
 * Binding will automatically enable runtime value notifications from controller
 * for all configured resources.
 * 
 * Refresh interval could be used for forcefully synchronous resource values
 * from controller.
 * 
 * Currently Number, Switch, Contact, String and DateTime items are supported.
 * 
 * <p>
 * Here are some examples for valid binding configuration strings:
 * <ul>
 * <li><code>ihc="12345678"</code></li>
 * <li><code>ihc="87654321:20"</code></li>
 * <li><code>ihc="11111111:0"</code></li>
 * <li><code>ihc=">22222222"</code></li>
 * </ul>
 * 
 * @author Pauli Anttila
 * @author Simon Merschjohann
 * @since 1.1.0
 */
public class IhcGenericBindingProvider extends AbstractGenericBindingProvider
		implements IhcBindingProvider, AutoUpdateBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(IhcGenericBindingProvider.class);

	private final static Pattern commandPattern = Pattern
			.compile("\\[([\\w*]+):(0x[0-9a-fA-F]+|\\d+)(?::(\\d+)){0,1}\\]");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "ihc";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		IhcBindingConfig config = new IhcBindingConfig();
		config.itemType = item.getClass();
		config.outCommandMap = new HashMap<Command, IhcOutCommandConfig>();

		String[] splittedCommands = bindingConfig.split(",");

		for (String split : splittedCommands) {
			if (split.startsWith(">")) {
				try {
					// out binding
					String resourceCommand = split.substring(1);

					Matcher m = commandPattern.matcher(resourceCommand);

					IhcOutCommandConfig outConfig = new IhcOutCommandConfig();

					if (!m.matches()) {
						if (splittedCommands.length < 2) {
							// assume old style out command
							outConfig.command = null; // wildcard
							outConfig.resourceId = getResourceIdFromString(resourceCommand);

						} else {
							throw new BindingConfigParseException("Item '"
									+ item.getName()
									+ "' has invalid out binding config");
						}
					} else {
						// new style out binding
						Command command = TypeParser.parseCommand(
								item.getAcceptedCommandTypes(), m.group(1));
						if (command == null && !m.group(1).equals("*")) {
							throw new BindingConfigParseException("Item '"
									+ item.getName() + " invalid Command: "
									+ m.group(1));
						}

						outConfig.command = command;
						outConfig.resourceId = getResourceIdFromString(m
								.group(2));

						if (m.groupCount() == 3 && m.group(3) != null) {
							outConfig.value = Integer.parseInt(m.group(3));

							if (outConfig.value > 2000) {
								throw new BindingConfigParseException(
										"Item '"
												+ item.getName()
												+ "' exceeds maximum value of 2000 ms for trigger command");
							}

						}
					}

					config.outCommandMap.put(outConfig.command, outConfig);
				} catch (Exception e) {
					logger.warn(
							"Error in output config for item: "
									+ item.getName(), e);
				}
			} else if (split.startsWith("<")) {
				if (config.resourceId < 1) {
					config.resourceId = getResourceIdFromString(split
							.substring(1));
				} else {
					throw new BindingConfigParseException(
							"Multiple In-Bindings found, only one In-Binding allowed.");
				}
			} else {
				if (splittedCommands.length < 2) {
					String[] configParts = bindingConfig.trim().split(":");

					if (configParts.length > 2) {
						throw new BindingConfigParseException(
								"IHC / ELKO LS binding must contain of max two two parts separated by ':'");
					}

					String resourceId = configParts[0];
					config.resourceId = getResourceIdFromString(resourceId);

					IhcOutCommandConfig outConfig = new IhcOutCommandConfig();

					outConfig.command = null; // wildcard
					outConfig.resourceId = config.resourceId;
					config.outCommandMap.put(null, outConfig);

					if (configParts.length == 2)
						config.refreshInterval = Integer
								.parseInt(configParts[1]);
				} else {
					throw new BindingConfigParseException(
							"Only a sum of out bindings (+In-Only Binding) or a normal binding is supported.");
				}
			}
		}

		addBindingConfig(item, config);
	}

	private int getResourceIdFromString(String resourceId) {
		int ret = 0;

		if (resourceId.startsWith("0x")) {
			ret = Integer.parseInt(resourceId.replace("0x", ""), 16);
		} else {
			ret = Integer.parseInt(resourceId);
		}

		return ret;
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the IHC binding
	 * provider.
	 * 
	 */
	static private class IhcBindingConfig implements BindingConfig {
		Class<? extends Item> itemType;
		public int resourceId;
		public int refreshInterval;

		public HashMap<Command, IhcOutCommandConfig> outCommandMap;
	}

	static private class IhcOutCommandConfig {
		public Command command;
		public Integer resourceId;

		public Integer value; // used if it is a function
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.itemType : null;
	}

	@Override
	public int getResourceIdForInBinding(String itemName) {
		int result = 0;

		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);
		if (config != null) {
			result = config.resourceId;
		}

		return result;
	}

	@Override
	public int getResourceId(String itemName, Command cmd) {
		int result = 0;

		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);
		if (config != null) {
			IhcOutCommandConfig outConfig = config.outCommandMap.get(cmd);
			if (outConfig != null) {
				// command matching resource found
				result = outConfig.resourceId;
			} else {
				// check if wildcard resource exists
				outConfig = config.outCommandMap.get(null);
				if (outConfig != null)
					result = outConfig.resourceId;
			}
		}

		return result;
	}

	@Override
	public int getRefreshInterval(String itemName) {
		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.refreshInterval : null;
	}

	@Override
	public boolean isOutBinding(String itemName, int resourceId) {
		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);

		boolean isOutBinding = true;

		if (config.resourceId == resourceId) {
			isOutBinding = false;
		}

		return isOutBinding;
	}

	@Override
	public boolean hasInBinding(String itemName) {
		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);

		return config != null ? config.resourceId > 0 : null;
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

		if (!(item instanceof NumberItem || item instanceof SwitchItem
				|| item instanceof ContactItem || item instanceof StringItem
				|| item instanceof DateTimeItem || item instanceof DimmerItem || item instanceof RollershutterItem)) {
			throw new BindingConfigParseException(
					"Item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only NumberItems, SwitchItems, ContactItems, DateTimeItem, StringItem, DimmerItem or RollershutterItem are allowed - please check your *.items configuration");

		}

	}

	@Override
	public Boolean autoUpdate(String itemName) {
		return null;
	}

	@Override
	public Integer getValue(String itemName, Command cmd) {
		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);

		IhcOutCommandConfig outConfig = (config != null && config.outCommandMap != null) ? config.outCommandMap
				.get(cmd) : null;
		if (outConfig == null)
			outConfig = (config != null && config.outCommandMap != null) ? config.outCommandMap
					.get(null) : null;

		return (outConfig != null) ? outConfig.value : null;
	}

}
