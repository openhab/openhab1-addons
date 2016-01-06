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

import org.openhab.binding.onewire.internal.deviceproperties.modifier.OneWireDecimalTypeAddModifier;
import org.openhab.binding.onewire.internal.deviceproperties.modifier.OneWireDecimalTypeMultiplyModifier;
import org.openhab.binding.onewire.internal.deviceproperties.modifier.OneWireDecimalTypeTukeyFilterModifier;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This Class is a specialized writable BindingConfig. It connects openHab Number-Items to 1-Wire device properties.  
 * 
 * For Basic Configuration of the binding, see AbstractOneWireDevicePropertyBindingConfig.java 
 * 
 * Optional modifiers:
 * <ul>
 * <li>add-modifier - Adds a given value to read value: <code>add=<i>value</i></code></li>
 * <li>multiply-modifier - Multiplies a given value with read value: <code>multiply=<i>value</i></code></li>
 * </ul>
 * 
 * Examples:
 * <ul>
 * <li><code>onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;add=0.8""</code></li>
 * <li><code>onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;multiply=1.2""</code></li>
 * <li><code>onewire="deviceId=28.67C6697351FF;propertyName=temperature;refreshinterval=10;add=0.8;multiply=1.2""</code></li>
 * </ul>
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public class OneWireDevicePropertyNumberBindingConfig extends AbstractOneWireDevicePropertyWritableBindingConfig {

	public OneWireDevicePropertyNumberBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		super(pvBindingConfig);
		parseBindingConfig(pvBindingConfig);
	}

	private void parseBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		String[] lvConfigParts = pvBindingConfig.trim().split(";");

		for (String lvConfigPart : lvConfigParts) {
			parseAddModifier(lvConfigPart);
			parseMultiplyModifier(lvConfigPart);
			parseTukeyFilterModifier(lvConfigPart);
		}
	}

	private void parseAddModifier(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "add=";
		if (pvConfigPart.startsWith(lvConfigProperty)) {
			String lvConfigValue = pvConfigPart.substring(lvConfigProperty.length());
			this.getTypeModifieryList().add(new OneWireDecimalTypeAddModifier(new BigDecimal(Double.parseDouble(lvConfigValue))));
		}
	}

	private void parseMultiplyModifier(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "multiply=";
		if (pvConfigPart.startsWith(lvConfigProperty)) {
			String lvConfigValue = pvConfigPart.substring(lvConfigProperty.length());
			this.getTypeModifieryList().add(new OneWireDecimalTypeMultiplyModifier(new BigDecimal(Double.parseDouble(lvConfigValue))));
		}
	}
	
	private void parseTukeyFilterModifier(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "tukeyfilter";
		if (pvConfigPart.startsWith(lvConfigProperty)) {
			this.getTypeModifieryList().add(new OneWireDecimalTypeTukeyFilterModifier());
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
	public String convertTypeToUnmodifiedString(Type pvType) {
		if (pvType == null) {
			return null;
		} else {
			try {
				Double.parseDouble(pvType.toString());
			} catch (Exception e) {
				throw new IllegalStateException("command is not a parsable number: " + pvType.toString());
			}
			return pvType.toString();
		}
	}

	@Override
	public String toString() {
		final int maxLen = 20;
		return "OneWireDevicePropertyNumberBindingConfig [getDeviceId()=" + getDeviceId() + ", getPropertyName()=" + getPropertyName() + ", getAutoRefreshInSecs()=" + getAutoRefreshInSecs()
				+ ", getDevicePropertyPath()=" + getDevicePropertyPath() + ", getTypeModifieryList()="
				+ (getTypeModifieryList() != null ? getTypeModifieryList().subList(0, Math.min(getTypeModifieryList().size(), maxLen)) : null) + "]";
	}

}
