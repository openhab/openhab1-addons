/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.ihc.internal;

import org.openhab.binding.ihc.IhcBindingProvider;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
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
		public int resourceId;
		public int refreshInterval;
		public boolean outBindingOnly;
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
		logger.debug("Validate item type for item {}", item.getName());

		if (!(item instanceof NumberItem || item instanceof SwitchItem
				|| item instanceof ContactItem || item instanceof StringItem || item instanceof DateTimeItem)) {
			throw new BindingConfigParseException(
					"Item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only NumberItems, SwitchItems, ContactItems, DateTimeItem or StringItem are allowed - please check your *.items configuration");

		}

	}

	@Override
	public Boolean autoUpdate(String itemName) {
		logger.debug("AutoUpdate for item {} canceled", itemName);
		return false;
	}

}
