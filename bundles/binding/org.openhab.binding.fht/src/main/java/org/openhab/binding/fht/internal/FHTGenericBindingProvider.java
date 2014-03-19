/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fht.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.fht.FHTBindingConfig;
import org.openhab.binding.fht.FHTBindingConfig.Datapoint;
import org.openhab.binding.fht.FHTBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class FHTGenericBindingProvider extends AbstractGenericBindingProvider
		implements FHTBindingProvider {

	private final static Logger logger = LoggerFactory
			.getLogger(FHTGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "fht";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem || item instanceof ContactItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Switch-, Contact- and NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * Binding in the type of
	 * {fht="housecode=<housecode>;address=<optional>;datapoint=<optional>"}
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		String[] configParts = bindingConfig.split(";");

		String housecode = null;
		String address = null;
		Datapoint datapoint = null;
		for (String s : configParts) {
			String[] entryParts = s.split("=");
			if ("housecode".equals(entryParts[0])) {
				housecode = entryParts[1];
			} else if ("address".equals(entryParts[0])) {
				address = entryParts[1];
			} else if ("datapoint".equals(entryParts[0])) {
				datapoint = Datapoint.valueOf(entryParts[1]);
			}
		}

		if (housecode == null) {
			throw new BindingConfigParseException("housecode mustn't be null");
		}
		if (datapoint == null) {
			throw new BindingConfigParseException(
					"datapoint must be one of MEASURED_TEMP, DESIRED_TEMP, BATTERY, WINDOW or VALVE");
		}

		if ((datapoint == Datapoint.WINDOW || datapoint == Datapoint.VALVE)
				&& address == null) {
			throw new BindingConfigParseException(
					"Address of window contact needed");
		}
		FHTBindingConfig config = new FHTBindingConfig(item, housecode,
				address, datapoint);
		addBindingConfig(item, config);
	}

	@Override
	public FHTBindingConfig getConfigByItemName(String itemName) {
		return (FHTBindingConfig) bindingConfigs.get(itemName);
	}

	@Override
	public FHTBindingConfig getConfigByFullAddress(String fullAddress,
			Datapoint datapoint) {
		for (BindingConfig c : bindingConfigs.values()) {
			FHTBindingConfig fhtConfig = (FHTBindingConfig) c;
			if (fhtConfig.getFullAddress().equals(fullAddress)
					&& fhtConfig.getDatapoint() == datapoint) {
				return fhtConfig;
			}
		}
		return null;
	}

	@Override
	public List<FHTBindingConfig> getAllFHT80bBindingConfigs() {
		List<FHTBindingConfig> configs = new ArrayList<FHTBindingConfig>();
		for (BindingConfig c : bindingConfigs.values()) {
			FHTBindingConfig config = (FHTBindingConfig) c;
			if (config.getFullAddress().length() == 4
					&& !configs.contains(config)) {
				configs.add(config);
			}
		}
		return configs;
	}

}
