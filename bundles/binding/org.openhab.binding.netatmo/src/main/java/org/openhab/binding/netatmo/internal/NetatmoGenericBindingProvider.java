/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.netatmo.internal;

import org.openhab.binding.netatmo.NetatmoBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.LocationItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * <p>
 * Valid bindings for the main device are:
 * <ul>
 * <li><code>{ netatmo="&lt;device_id&gt;#Measurement" }</code></li>
 * <ul>
 * <li><code>{ netatmo="00:00:00:00:00:00#Temperature" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Humidity" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Co2" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Pressure" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Noise" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#WifiStatus" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Altitude" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Latitude" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#Longitude" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#TimeStamp" }</code></li>
 * </ul>
 * </li> </ul>
 * <p>
 * Valid bindings for a module are:
 * <ul>
 * <li>
 * <code>{ netatmo="&lt;device_id&gt;#&lt;module_id&gt;#Measurement" }</code></li>
 * <ul>
 * <li>
 * <code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Temperature" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Humidity" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Co2" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#Rain" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#RfStatus" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#BatteryVp" }</code></li>
 * <li><code>{ netatmo="00:00:00:00:00:00#00:00:00:00:00:00#TimeStamp" }</code></li>
 * </ul>
 * </li> </ul>
 *
 * @author Andreas Brenk
 * @author Thomas.Eichstaedt-Engelen
 * @author Gaël L'hopital
 * @author Rob Nielsen
 * @since 1.4.0
 */
public class NetatmoGenericBindingProvider extends
		AbstractGenericBindingProvider implements NetatmoBindingProvider {

	private static Logger logger = LoggerFactory
			.getLogger(NetatmoGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "netatmo";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof DateTimeItem
				|| item instanceof LocationItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only NumberItems, DateTimeItems, StringItems and LocationItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUserid(final String itemName) {
		final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.userid : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDeviceId(final String itemName) {
		final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.deviceId : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NetatmoMeasureType getMeasureType(String itemName) {
		final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.measureType : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getModuleId(final String itemName) {
		final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.moduleId : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NetatmoScale getNetatmoScale(String itemName) {
		final NetatmoBindingConfig config = (NetatmoBindingConfig) this.bindingConfigs
				.get(itemName);
		return config != null ? config.netatmoScale : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(final String context,
			final Item item, final String bindingConfig)
			throws BindingConfigParseException {
		logger.debug("Processing binding configuration: '{}'", bindingConfig);

		super.processBindingConfiguration(context, item, bindingConfig);

		final NetatmoBindingConfig config = new NetatmoBindingConfig();

		final String[] configParts = bindingConfig.split("#");
		String measureTypeString;
		switch (configParts.length) {
		case 2:
			config.deviceId = configParts[0];
			measureTypeString = configParts[1];
			break;
		case 3:
			config.deviceId = configParts[0];
			config.moduleId = configParts[1];
			measureTypeString = configParts[2];
			break;
		case 4:
			config.userid = configParts[0];
			config.deviceId = configParts[1];
			config.moduleId = configParts[2];
			measureTypeString = configParts[3];
			break;
		default:
			throw new BindingConfigParseException(
					"A Netatmo binding configuration must consist of two, three or four parts - please verify your *.items file");
		}

		/*
		 * use a ',' when including scale so that it does not break backwards
		 * compatibility with case 4 above.
		 */
		final String[] measureTypeParts = measureTypeString.split(",");
		switch (measureTypeParts.length) {
		case 1:
			config.measureType = NetatmoMeasureType.fromString(measureTypeParts[0]);
			config.netatmoScale = config.measureType.getDefaultScale();
			break;
		case 2:
			config.measureType = NetatmoMeasureType.fromString(measureTypeParts[0]);
			config.netatmoScale = NetatmoScale.fromString(measureTypeParts[1]);
			break;
		default:
			throw new BindingConfigParseException(
					"The last part of the Netatmo binding configuration must be 'type' or 'type,scale'"
							+ " - please verify your *.items file");
		}

		logger.debug("Adding binding: {}", config);

		addBindingConfig(item, config);
	}

	private static class NetatmoBindingConfig implements BindingConfig {

		String userid;
		String deviceId;
		String moduleId;
		NetatmoMeasureType measureType;
		NetatmoScale netatmoScale;

		@Override
		public String toString() {
			return "NetatmoBindingConfig [userid=" + this.userid
					+ ", deviceId=" + this.deviceId + ", moduleId="
					+ this.moduleId + ", measure="
					+ this.measureType.getMeasure() + "]";
		}
	}

}
