/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ihc.internal;

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
 * @since 1.1.0
 */
public class IhcGenericBindingProvider extends AbstractGenericBindingProvider
		implements IhcBindingProvider, AutoUpdateBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(IhcGenericBindingProvider.class);

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
		
		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length > 2) {
			throw new BindingConfigParseException(
					"IHC / ELKO LS binding must contain of max two two parts separated by ':'");
		}

		if (bindingConfig.startsWith(">")) {

			if (configParts.length == 1) {
				config.outBindingOnly = true;
				config.resourceId = Integer.parseInt(configParts[0].replace(
						">", ""));
			} else {
				throw new BindingConfigParseException(
						"When configuration start with '>', refresh interval is not supported ");

			}

		} else {

			String resourceId = configParts[0];

			if (resourceId.startsWith("0x"))
				config.resourceId = Integer.parseInt(
						resourceId.replace("0x", ""), 16);
			else
				config.resourceId = Integer.parseInt(resourceId);

			if (configParts.length == 2)
				config.refreshInterval = Integer.parseInt(configParts[1]);

		}

		addBindingConfig(item, config);
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
		public boolean outBindingOnly;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}
	
	@Override
	public int getResourceId(String itemName) {
		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.resourceId : null;
	}

	@Override
	public int getRefreshInterval(String itemName) {
		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.refreshInterval : null;
	}

	@Override
	public boolean isOutBindingOnly(String itemName) {
		IhcBindingConfig config = (IhcBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.outBindingOnly : null;
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

		if (!(item instanceof NumberItem || item instanceof SwitchItem
				|| item instanceof ContactItem || item instanceof StringItem
				|| item instanceof DateTimeItem || item instanceof DimmerItem 
				|| item instanceof RollershutterItem)) {
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

		// Cancel auto update functionality for items, which are handled on this binding

		if (providesBindingFor(itemName)) {

			if (isOutBindingOnly(itemName) == false) {
				
				// Cancel auto update functionality only if item is not 'out binding only'

				logger.debug("AutoUpdate for item {} canceled", itemName);
				return false;
			}
		}

		return null;
	}

}
