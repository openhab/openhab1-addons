/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel.internal;

import java.io.InvalidClassException;

import org.openhab.binding.anel.AnelBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * <p>
 * Example configuration:
 * </p>
 * 
 * <pre>
 * ########################## Anel NET-PwrCtrl Binding ###################################
 * #
 * # UDP receive port (optional, defaults to 77)
 * anel:anel1.udpReceivePort=7777
 * 
 * # UDP send port (optional, defaults to 75)
 * anel:anel1.udpSendPort=7775
 * 
 * # IP or network address (optional, defaults to 'net-control')
 * anel:anel1.host=anel1
 * 
 * # User name (optional, defaults to 'user7')
 * anel:anel1.user=user7
 * 
 * # Password (optional, defaults to 'anel')
 * anel:anel1.password=anel
 * 
 * # Global refresh interval in ms (optional, defaults to 60000 = 1min, disable with '0')
 * #anel:refresh=60
 * 
 * # Cache the state for n minutes so only changes are posted (optional, defaults to 0 = disabled)
 * # Example: if period is 60, once per hour all states are posted to the event bus;
 * #          changes are always and immediately posted to the event bus.
 * # The recommended value is 60 minutes.
 * anel:cachePeriod=60
 * </pre>
 * 
 * Example items:
 * 
 * <pre>
 * Switch f1 { anel="anel1:F1" }
 * Switch f2 { anel="anel1:F2" }
 * Switch io7 { anel="anel1:IO7" }
 * Switch io8 { anel="anel1:IO8" }
 * </pre>
 * 
 * Example rules:
 * 
 * <pre>
 * rule "regular switch on Anel1 IO7 for relay 1"
 * when Item io7 changed then
 * 	postUpdate(f1, io7.state)
 * end
 * 
 * rule "push button switch on Anel1 IO8 for relay 2"
 * when Item io8 changed to ON then
 * 	postUpdate(f2, if (f2.state != ON) ON else OFF)
 * end
 * </pre>
 * 
 * @author paphko
 * @since 1.6.0
 */
public class AnelGenericBindingProvider extends AbstractGenericBindingProvider implements AnelBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "anel";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
					+ item.getClass().getSimpleName()
					+ "', only Switch/String/NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		if (bindingConfig == null || bindingConfig.trim().isEmpty())
			return; // empty binding - nothing to do

		final String[] segments = bindingConfig.trim().split(":");
		if (segments.length != 2)
			throw new BindingConfigParseException("Invalid binding format '" + bindingConfig
					+ "', expected: '<anelId>:<property>'");

		final String deviceId = segments[0];
		final String commandType = segments[1];
		try {
			AnelCommandType.validateBinding(commandType, item.getClass());
			final AnelCommandType cmdType = AnelCommandType.getCommandType(commandType);

			// if command type was validated successfully, add binding config
			addBindingConfig(item, new AnelBindingConfig(item.getClass(), cmdType, deviceId));
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("'" + commandType + "' is not a valid Anel property");
		} catch (InvalidClassException e) {
			throw new BindingConfigParseException("Invalid class for Anel property '" + commandType + "'");
		}
	}

	/**
	 * Internal class to represent an openHAB item binding.
	 */
	class AnelBindingConfig implements BindingConfig {
		final Class<? extends Item> itemType;
		final AnelCommandType commandType;
		final String deviceId;

		protected AnelBindingConfig(Class<? extends Item> itemType, AnelCommandType cmdType, String deviceId) {
			this.itemType = itemType;
			this.commandType = cmdType;
			this.deviceId = deviceId;
		}

		@Override
		public String toString() {
			return "AnelBindingConfig [device=" + deviceId + ", itemType=" + itemType + ", property=" + commandType
					+ "]";
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		final AnelBindingConfig config = (AnelBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AnelCommandType getCommandType(String itemName) {
		final AnelBindingConfig config = (AnelBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.commandType : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDeviceId(String itemName) {
		final AnelBindingConfig config = (AnelBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.deviceId : null;
	}
}
