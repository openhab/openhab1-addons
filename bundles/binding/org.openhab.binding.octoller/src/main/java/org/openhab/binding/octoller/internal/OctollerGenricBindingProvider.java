/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.octoller.internal;

import org.openhab.binding.octoller.OctollerBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration. The binding
 * must have the format octoller=
 * "<IP-Address of octoller>@<IP-Address of octoller.Gateway>#'<ID of block>'"
 * Example: Rollershutter Shutter_SF_Office "Shutter Office" <rollershutter>
 * (SF_Office, gShutters) { octoller="10.158.2.33@localhost#'Shutter.DG.FZ'" }
 * 
 * @author JPlenert
 * @since 1.8.0
 */
public class OctollerGenricBindingProvider extends
		AbstractGenericBindingProvider implements OctollerBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "octoller";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		String[] configParts = bindingConfig.trim().split("#");
		if (configParts.length != 2)
			throw new BindingConfigParseException(
					"Two parameters needed (Host#Block)");

		// Right now a gateway must be given!
		String[] hostParts = configParts[0].trim().split("@");
		if (hostParts.length != 2)
			throw new BindingConfigParseException(
					"Host paramter must contain device and gateway (deviceHost@gatewayHost)");

		OctollerBindingConfig config = new OctollerBindingConfig();
		config.DeviceHost = hostParts[0].trim();
		config.GatewayHost = hostParts[1].trim();
		// Check for BlockName
		if (configParts[1].length() > 2 && configParts[1].startsWith("'")
				&& configParts[1].endsWith("'"))
			config.BlockName = configParts[1].substring(1,
					configParts[1].length() - 1).trim();
		else {
			try {
				config.BlockID = Integer.parseInt(configParts[1]);
			} catch (Exception ex) {
				throw new BindingConfigParseException(
						"Block parameter must be nummeric or the block name in single quotation marks");
			}

		}

		addBindingConfig(item, config);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public OctollerBindingConfig getConfig(String itemName) {
		return (OctollerBindingConfig) bindingConfigs.get(itemName);
	}
}
