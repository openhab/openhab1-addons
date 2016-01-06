/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.deviceproperties;

import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This Class is a specialized writable BindingConfig. It connects openHab String-Items to 1-Wire device properties (example: DS2408 with LCD connected) 
 * 
 * For Basic Configuration of the binding, see AbstractOneWireDevicePropertyBindingConfig.java 
 * 
 * Optional modifiers: none
 * 
 * Examples:
 * <ul>
  * <li><code>onewire="deviceId=29.44C80E000000;propertyName=LCD_H/message;refreshinterval=-1"</code></li>
 * </ul>
 * 
 * @author Dennis Riegelbauer
 * @since 1.7.0
 * 
 */
public class OneWireDevicePropertyStringBindingConfig extends AbstractOneWireDevicePropertyWritableBindingConfig {

	public OneWireDevicePropertyStringBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
		super(pvBindingConfig);
	}

	@Override
	public Type convertReadValueToUnmodifiedType(String pvReadValue) {
		Type lvType = UnDefType.UNDEF;

		if (pvReadValue != null) {
			lvType = new StringType(pvReadValue);
		}

		return lvType;
	}

	@Override
	public String convertTypeToUnmodifiedString(Type pvType) {
		if (pvType == null) {
			return null;
		} else {
			return pvType.toString();
		}
	}

	@Override
	public String toString() {
		final int maxLen = 20;
		return "OneWireDevicePropertyStringBindingConfig [getDeviceId()=" + getDeviceId() + ", getPropertyName()=" + getPropertyName() + ", getAutoRefreshInSecs()=" + getAutoRefreshInSecs()
				+ ", getDevicePropertyPath()=" + getDevicePropertyPath() + ", getTypeModifieryList()="
				+ (getTypeModifieryList() != null ? getTypeModifieryList().subList(0, Math.min(getTypeModifieryList().size(), maxLen)) : null) + "]";
	}

}
