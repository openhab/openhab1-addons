/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.mios.MiosBindingProvider;
import org.openhab.binding.mios.internal.config.DeviceBindingConfig;
import org.openhab.binding.mios.internal.config.MiosBindingConfig;
import org.openhab.binding.mios.internal.config.RoomBindingConfig;
import org.openhab.binding.mios.internal.config.SceneBindingConfig;
import org.openhab.binding.mios.internal.config.SystemBindingConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class MiosBindingProviderImpl extends AbstractGenericBindingProvider
		implements MiosBindingProvider {

	/*
	 * The Binding declaration consists of the following comma-separated
	 * elements of the form <name>:<value>, in a specific order.
	 * 
	 * The order is unit:id, {type}:id, extraStuff
	 * 
	 * "unit" - the name of the MiOS unit, declared in the openHAB
	 * configuration. The value is a case-sensitive MiOS unit "id"
	 * [alphaNumeric] String.
	 * 
	 * "type" - the type of entity bound at the MiOS unit. The value is a
	 * MiOS-specific identifier Integer. Type names include "device", "scene",
	 * "system" and "room".
	 * 
	 * "extraStuff" - type-specific content/formatting options for the binding.
	 * Is an arbitrary value that is passed through for the specific MiOS
	 * Binding type to interpret.
	 */
	// TODO: Fix parsing for system to be tighter. We "opened" the parsing for
	// the others to permit no "id" field, but that's not value
	private static final Pattern BINDING_PATTERN = Pattern
			.compile("(unit:(?<unit>[a-zA-Z]+[a-zA-Z0-9]*),)"
					+ "(?<inThing>[^,]+)"
					+ "(,command:(?<command>[^,]*))?"
					+ "(,update:(?<update>[^,]*))?"
					+ "(,in:(?<inTransform>[a-zA-Z]+[a-zA-Z0-9]*\\([^,]*\\)))?"
					+ "(,out:(?<outTransform>[a-zA-Z]+[a-zA-Z0-9]*\\([^,]*\\)))?");

	private static final Pattern IN_CONFIG_PATTERN = Pattern
			.compile("((?<inType>device|scene|system|room):" + "(?<id>[0-9]*))"
					+ "(/(?<inStuff>.+))?");

	private static final Logger logger = LoggerFactory
			.getLogger(MiosBindingProviderImpl.class);

	// Injected by the OSGi Container through the setItemRegistry and
	// unsetItemRegistry methods.
	private ItemRegistry itemRegistry;

	public void setItemRegistry(ItemRegistry itemRegistry) {
		logger.debug("setItemRegistry: called");
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		logger.debug("unsetItemRegistry: called");
		this.itemRegistry = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public ItemRegistry getItemRegistry() {
		return this.itemRegistry;
	}

	public String getBindingType() {
		return "mios";
	}

	// TODO Add per-BindingConfig validation rule for MiOS devices.
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// Now done at the BindingConfig level, just after parsing the
		// bindingConfig String inside processBindingConfiguration.
		//
		// This method is pointless, since we only have the string form
		// of the Binding Config, and it would require us to parse it anyhow.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		try {
			MiosBindingConfig config = parseBindingConfig(context, item,
					bindingConfig);
			config.validateItemType(item);

			logger.debug(
					"processBindingConfiguration: Adding Item '{}' Binding '{}', from '{}'",
					new Object[] { item.getName(), config, context });
			addBindingConfig(item, config);
		} catch (BindingConfigParseException bcpe) {
			logger.debug(String
					.format("processBindingConfiguration: Exception parsing/validating context '%s', item'%s', bindingConfig '%s'.  Exception is %s.",
							context, item, bindingConfig, bcpe.getMessage()));

			throw bcpe;
		}
	}

	private MiosBindingConfig parseBindingConfig(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		Matcher matcher;

		matcher = BINDING_PATTERN.matcher(bindingConfig);
		if (!matcher.matches()) {
			throw new BindingConfigParseException(
					String.format(
							"Config for item '%s' could not be parsed.  Bad general format '%s'",
							item.getName(), bindingConfig.toString()));
		}

		String unitName = matcher.group("unit");

		String inThing = matcher.group("inThing");
		String commandThing = matcher.group("command");
		String updateThing = matcher.group("update");

		String inTrans = matcher.group("inTransform");
		String outTrans = matcher.group("outTransform");

		logger.trace(
				"parseBindingConfig: unit '{}' thing '{}' inTrans '{}' outTrans '{}'",
				new Object[] { unitName, inThing, inTrans, outTrans });

		// The inbound pattern is mandatory, and enforced by the
		// pattern-matcher.
		matcher = IN_CONFIG_PATTERN.matcher(inThing);
		if (!matcher.matches())
			throw new BindingConfigParseException("Config for item '"
					+ item.getName()
					+ "' could not be parsed.  Bad thing format '"
					+ bindingConfig + "'");

		String inType = matcher.group("inType");
		String inId = matcher.group("id");
		String inStuff = matcher.group("inStuff");

		logger.trace(
				"parseBindingConfig: in: (Type '{}' id '{}' Stuff '{}'), command: ('{}'), update: ('{}')",
				new Object[] { inType, inId, inStuff, commandThing, updateThing });

		// Inline a factory for now...
		// FIXME
		if (inType.equals("device")) {
			return DeviceBindingConfig.create(context, item.getName(),
					unitName, Integer.parseInt(inId), inStuff, item.getClass(),
					commandThing, updateThing, inTrans, outTrans);
		} else if (inType.equals("scene")) {
			return SceneBindingConfig.create(context, item.getName(), unitName,
					Integer.parseInt(inId), inStuff, item.getClass(),
					commandThing, updateThing, inTrans, outTrans);
		} else if (inType.equals("system")) {
			return SystemBindingConfig.create(context, item.getName(),
					unitName, inStuff, item.getClass(), inTrans, outTrans);
		} else if (inType.equals("room")) {
			return RoomBindingConfig.create(context, item.getName(), unitName,
					Integer.parseInt(inId), inStuff, item.getClass(), inTrans,
					outTrans);
		} else
			throw new BindingConfigParseException(
					"Invalid binding type received for item " + item.getName()
							+ " bad type (" + inType + ")");
	}

	public MiosBindingConfig getMiosBindingConfig(String itemName) {
		return (MiosBindingConfig) bindingConfigs.get(itemName);
	}

	@Override
	public String getMiosUnitName(String itemName) {
		MiosBindingConfig config = getMiosBindingConfig(itemName);
		return (config == null) ? null : config.getUnitName();
	}

	public String getProperty(String itemName) {
		MiosBindingConfig config = getMiosBindingConfig(itemName);
		return (config == null) ? null : config.toProperty();
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		MiosBindingConfig config = getMiosBindingConfig(itemName);
		return (config == null) ? null : config.getItemType();
	}

	@Override
	public List<String> getItemsForProperty(String property) {
		ArrayList<String> result = new ArrayList<String>();

		// TODO: Make a reverse map somewhere, and keep it around as this is
		// going to get a lot of calls, and a lot of garbage along the way!!
		for (Entry<String, BindingConfig> bindingConfigEntry : bindingConfigs
				.entrySet()) {
			if (bindingConfigEntry.getValue() instanceof MiosBindingConfig) {
				String p = ((MiosBindingConfig) bindingConfigEntry.getValue())
						.toProperty();

				if (p.equals(property)) {
					logger.trace(
							"getItemsForProperty: MATCH property '{}' against BindingConfig.toProperty '{}'",
							property, p);

					String itemName = bindingConfigEntry.getKey();
					result.add(itemName);
				}
			}
		}

		return result;
	}
}
