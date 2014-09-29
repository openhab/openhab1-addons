/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeheatpump.internal;

import org.openhab.binding.nibeheatpump.NibeHeatPumpBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * Nibe heat pump binding information from it.
 * 
 * Value identifiers on binding configuration are MODBUS coil addresses.
 * 
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>nibeheatpump="43005"</code></li>
 * <li><code>nibeheatpump="40004"</code></li>
 * <li><code>nibeheatpump="40008"</code></li>
 * </ul>
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class NibeHeatPumpGenericBindingProvider extends
		AbstractGenericBindingProvider implements NibeHeatPumpBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "nibeheatpump";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException(
					"item '" + item.getName() + "' is of type '"
						+ item.getClass().getSimpleName()
						+ "', only NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		NibeHeatpumpBindingConfig config = new NibeHeatpumpBindingConfig();

		String itemId = bindingConfig.trim();
		if (itemId.startsWith("0x")) {
			config.itemId = Integer.parseInt(itemId.replace("0x", ""), 16);
		} else {
			config.itemId = Integer.parseInt(itemId);
		}

		addBindingConfig(item, config);
	}
	
	
	@Override
	public int getItemId(String itemName) {
		NibeHeatpumpBindingConfig config = (NibeHeatpumpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemId : null;
	}


	class NibeHeatpumpBindingConfig implements BindingConfig {
		public int itemId;
	}

}
