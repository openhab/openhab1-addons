/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.deviceproperties;

import org.openhab.binding.onewire.internal.deviceproperties.modifier.OneWireOpenClosedTypeInvertModifier;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.BindingConfigParseException;



/**
 * This Class is a specialized readOnly BindingConfig. It connects openHab Contact-Items to 1-Wire device properties.  
 * 
 * For Basic Configuration of the binding, see AbstractOneWireDevicePropertyBindingConfig.java
 * 
 * Optional modifiers:
 * <ul>
 * <li>invert-modifier - The read status Open/Closed will be inverted by this modifier. Config: <code>invert</code></li>
 * </ul>
 * 
 * Example:
 * <ul>
 * <li><code>onewire="deviceId=29.66C30E000000;propertyName=sensed.0;refreshinterval=10";invert"</code></li>
 * </ul>
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
public class OneWireDevicePropertyContactBindingConfig extends AbstractOneWireDevicePropertyBindingConfig {

	public OneWireDevicePropertyContactBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		super(pvBindingConfig);
		parseBindingConfig(pvBindingConfig);
	}

	private void parseBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		String[] lvConfigParts = pvBindingConfig.trim().split(";");

		for (String lvConfigPart : lvConfigParts) {
			parseInvertModifier(lvConfigPart);
		}
	}

	private void parseInvertModifier(String pvConfigPart) {
		String lvConfigProperty = null;

		lvConfigProperty = "invert";
		if (pvConfigPart.equals(lvConfigProperty)) {
			this.getTypeModifieryList().add(new OneWireOpenClosedTypeInvertModifier());
		}
	}

	@Override
	public Type convertReadValueToUnmodifiedType(String pvReadValue) {
		Type lvType = UnDefType.UNDEF;

		if (pvReadValue != null) {
			lvType = pvReadValue.trim().equals("1") ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
		}

		return lvType;
	}

	@Override
	public String toString() {
		final int maxLen = 20;
		return "OneWireDevicePropertyContactBindingConfig [getDeviceId()=" + getDeviceId() + ", getPropertyName()=" + getPropertyName() + ", getAutoRefreshInSecs()=" + getAutoRefreshInSecs()
				+ ", getDevicePropertyPath()=" + getDevicePropertyPath() + ", getTypeModifieryList()="
				+ (getTypeModifieryList() != null ? getTypeModifieryList().subList(0, Math.min(getTypeModifieryList().size(), maxLen)) : null) + "]";
	}

}
