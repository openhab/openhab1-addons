/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wago.internal;

import org.openhab.binding.wago.WagoBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Kaltofen
 * @since 1.7.0
 */
public class WagoGenericBindingProvider extends AbstractGenericBindingProvider
		implements WagoBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(WagoGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "wago";
	}

	@Override
	public WagoBindingConfig getConfig(String name) {
		return (WagoBindingConfig) bindingConfigs.get(name);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
		throws BindingConfigParseException {
			if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
				throw new BindingConfigParseException("item '" + item.getName()
				+ "' is of type '" + item.getClass().getSimpleName()
				+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
			}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			WagoBindingConfig config = new WagoBindingConfig(item,
					bindingConfig);
			addBindingConfig(item, config);
		} else {
			logger.warn("binding configuration is empty -> aborting item configuration.");
		}
	}

	public class WagoBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		private Item item = null;

		String couplerName;
		int module;
		int channel;

		public WagoBindingConfig(Item item, String conf) {
			this.item = item;

			String attributes[] = conf.split(":");

				couplerName = attributes[0];
			try {
				module = Integer.parseInt(attributes[1]) - 1; // -1 so that the user can just use the values from the EA-config.
				channel = Integer.parseInt(attributes[2]) - 1;
			} catch (NumberFormatException e) {
				logger.error("wago binding configuration invalid. Module or channel is not a number.");
			}
		}

		protected State translateBoolean2State(boolean b) {
			Class<? extends State> c = item.getState().getClass();
			Class<? extends Item> itemClass = item.getClass();

			if (c == UnDefType.class && itemClass == SwitchItem.class) {
				return b ? OnOffType.ON : OnOffType.OFF;
			} else if (c == UnDefType.class && itemClass == ContactItem.class) {
				return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			} else if (c == OnOffType.class && itemClass == SwitchItem.class) {
				return b ? OnOffType.ON : OnOffType.OFF;
			} else if (c == OpenClosedType.class
					&& itemClass == SwitchItem.class) {
				return b ? OnOffType.ON : OnOffType.OFF;
			} else if (c == OnOffType.class && itemClass == ContactItem.class) {
				return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			} else if (c == OpenClosedType.class
					&& itemClass == ContactItem.class) {
				return b ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
			} else {
				return UnDefType.UNDEF;
			}
		}

		public Item getItem() {
			return item;
		}

		State getItemState() {
			return item.getState();
		}
	}
	
}
