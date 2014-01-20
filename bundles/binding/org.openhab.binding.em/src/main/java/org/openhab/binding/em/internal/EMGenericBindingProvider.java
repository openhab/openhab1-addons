/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.em.internal;

import org.openhab.binding.em.EMBindingProvider;
import org.openhab.binding.em.internal.EMBindingConfig.Datapoint;
import org.openhab.binding.em.internal.EMBindingConfig.EMType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class EMGenericBindingProvider extends AbstractGenericBindingProvider implements EMBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "em";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
					+ item.getClass().getSimpleName()
					+ "', only NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * Binding config in the style {em=
	 * "type=01|02|03;address=AA;datapoint=CUMULATED_VALUE;correctionFactor=NN"}
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		String[] parts = bindingConfig.split(";");
		String address = null;
		EMType type = null;
		Datapoint datapoint = null;
		double correctionFactor = 0;
		for (String part : parts) {
			String[] keyValue = part.split("=");
			if ("type".equals(keyValue[0])) {
				type = EMType.getFromTypeValue(keyValue[1]);
			} else if ("address".equals(keyValue[0])) {
				address = keyValue[1];
			} else if ("datapoint".equals(keyValue[0])) {
				datapoint = Datapoint.valueOf(keyValue[1]);
			} else if ("correctionFactor".equals(keyValue[0])) {
				correctionFactor = Double.parseDouble(keyValue[1]);
			}
		}
		EMBindingConfig config = new EMBindingConfig(type, address, datapoint, item, correctionFactor);

		addBindingConfig(item, config);
	}

	@Override
	public EMBindingConfig getConfigByTypeAndAddressAndDatapoint(EMType type, String address, Datapoint datapoint) {
		for (BindingConfig config : super.bindingConfigs.values()) {
			EMBindingConfig emConfig = (EMBindingConfig) config;
			if (emConfig.getAddress().equals(address) && emConfig.getType() == type
					&& emConfig.getDatapoint() == datapoint) {
				return emConfig;
			}
		}
		return null;
	}

}
