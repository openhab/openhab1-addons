/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.deviceproperties;

import java.math.BigDecimal;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class is a specialized readonly BindingConfig. It connects openHab Switch-Items to 1-Wire numeric device
 * properties.
 * 
 * For Basic Configuration of the binding, see AbstractOneWireDevicePropertyBindingConfig.java
 * 
 * For this Binding, maxWarning or minWarning must be set. If readvalue is greater than maxWarningValue or readvalue is
 * less than minWarningValue, the Switch turns on
 * 
 * Required: <code>maxWarning=<i>value</i>"</code> or <code>minWarning=<i>value</i>"</code>
 * 
 * Optional modifiers:
 * <ul>
 * <li>add-modifier - Adds a given value to read value: <code>add=<i>value</i></code></li>
 * <li>multiply-modifier - Multiplies a given value with read value: <code>multiply=<i>value</i></code></li>
 * </ul>
 * 
 * Examples:
 * <ul>
 * <li>
 * <code>onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;maxWarning=30;add=0.8""</code></li>
 * <li>
 * <code>onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;minWarning=10;multiply=1.2""</code>
 * </li>
 * <li>
 * <code>onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;maxWarning=30;minWarning=10;add=0.8;multiply=1.2""</code>
 * </li>
 * </ul>
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public class OneWireDevicePropertySwitchMinMaxNumberWarningBindingConfig extends OneWireDevicePropertyNumberBindingConfig {

	private static final Logger logger = LoggerFactory.getLogger(OneWireDevicePropertySwitchMinMaxNumberWarningBindingConfig.class);

	private BigDecimal ivMaxWarningValue = null;
	private BigDecimal ivMinWarningValue = null;

	public OneWireDevicePropertySwitchMinMaxNumberWarningBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		super(pvBindingConfig);
		parseBindingConfig(pvBindingConfig);
	}

	private void parseBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		String[] lvConfigParts = pvBindingConfig.trim().split(";");

		for (String lvConfigPart : lvConfigParts) {
			parseMaxWarning(lvConfigPart);
			parseMinWarning(lvConfigPart);
		}

		if (!(ivMaxWarningValue != null || ivMinWarningValue != null)) {
			logger.error("maxWarning or minWarning must be set in config!");
			throw new BindingConfigParseException("Onewire sensor configuration must contain maxWarning or minWarning Value!");
		}
	}

	private void parseMaxWarning(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "maxWarning=";
		if (pvConfigPart.startsWith(lvConfigProperty)) {
			String lvConfigValue = pvConfigPart.substring(lvConfigProperty.length());
			ivMaxWarningValue = new BigDecimal(Double.parseDouble(lvConfigValue));
		}
	}

	private void parseMinWarning(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "minWarning=";
		if (pvConfigPart.startsWith(lvConfigProperty)) {
			String lvConfigValue = pvConfigPart.substring(lvConfigProperty.length());
			ivMinWarningValue = new BigDecimal(Double.parseDouble(lvConfigValue));
		}
	}

	@Override
	public Type convertReadValueToUnmodifiedType(String pvReadValue) {
		Type lvType = UnDefType.UNDEF;

		if (pvReadValue != null) {
			lvType = new DecimalType(Double.valueOf(pvReadValue));
		}

		return lvType;
	}

	@Override
	public Type convertReadValueToType(String pvReadValue) {
		Type lvType = super.convertReadValueToType(pvReadValue);

		// Must be Number Tyoe
		DecimalType lvDecimalType = (DecimalType) lvType;

		// Check if readValue greater then MaxWarningValue or less then
		// MinWarningValue
		if (ivMaxWarningValue != null && lvDecimalType.toBigDecimal().compareTo(ivMaxWarningValue) == 1) {
			return OnOffType.ON;
		} else if (ivMinWarningValue != null && lvDecimalType.toBigDecimal().compareTo(ivMinWarningValue) == -1) {
			return OnOffType.ON;
		} else {
			return OnOffType.OFF;
		}

	}

	/**
	 * @param pvItem
	 * @param pvBindingConfig
	 * @return true, when Item is a Switch and bindigConfig contains min- oder maxWarning
	 */
	public static boolean isBindingConfigToCreate(Item pvItem, String pvBindingConfig) {
		return (pvItem instanceof SwitchItem && (pvBindingConfig.contains("maxWarning") || pvBindingConfig.contains("minWarning")));
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		return "OneWireDevicePropertyNumberWarningBindingConfig [ivMaxWarningValue=" + ivMaxWarningValue + ", ivMinWarningValue=" + ivMinWarningValue + ", getDeviceId()=" + getDeviceId()
				+ ", getPropertyName()=" + getPropertyName() + ", getAutoRefreshInSecs()=" + getAutoRefreshInSecs() + ", getDevicePropertyPath()=" + getDevicePropertyPath()
				+ ", getTypeModifieryList()=" + (getTypeModifieryList() != null ? getTypeModifieryList().subList(0, Math.min(getTypeModifieryList().size(), maxLen)) : null) + "]";
	}

}
