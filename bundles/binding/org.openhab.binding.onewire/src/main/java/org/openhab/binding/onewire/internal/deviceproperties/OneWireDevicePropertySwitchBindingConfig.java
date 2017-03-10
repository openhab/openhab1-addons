/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.deviceproperties;

import org.openhab.binding.onewire.internal.deviceproperties.modifier.OneWireOnOffTypeInvertModifier;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This Class is a specialized writable BindingConfig. It connects openHab Switch-Items to 1-Wire device properties.
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
public class OneWireDevicePropertySwitchBindingConfig extends AbstractOneWireDevicePropertyWritableBindingConfig {

    public OneWireDevicePropertySwitchBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
        super(pvBindingConfig);
        parseBindingConfig(pvBindingConfig);
    }

    protected void parseBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
        String[] lvConfigParts = pvBindingConfig.trim().split(";");

        for (String lvConfigPart : lvConfigParts) {
            parseInvertModifier(lvConfigPart);
        }
    }

    private void parseInvertModifier(String pvConfigPart) {
        String lvConfigProperty = null;

        lvConfigProperty = "invert";
        if (pvConfigPart.equals(lvConfigProperty)) {
            this.getTypeModifieryList().add(new OneWireOnOffTypeInvertModifier());
        }
    }

    @Override
    public Type convertReadValueToUnmodifiedType(String pvReadValue) {
        Type lvType = UnDefType.UNDEF;

        if (pvReadValue != null) {
            lvType = pvReadValue.trim().equals("1") ? OnOffType.ON : OnOffType.OFF;
        }

        return lvType;
    }

    @Override
    public String convertTypeToUnmodifiedString(Type pvType) {
        if (pvType == null) {
            return null;
        } else if (pvType.equals(OnOffType.ON)) {
            return "1";
        } else if (pvType.equals(OnOffType.OFF)) {
            return "0";
        } else {
            throw new IllegalStateException("recevice unknown command for OneWireSwitchBinding = " + pvType.toString());
        }
    }

    @Override
    public String toString() {
        final int maxLen = 20;
        return "OneWireDevicePropertySwitchBindingConfig [getDeviceId()=" + getDeviceId() + ", getPropertyName()="
                + getPropertyName() + ", getAutoRefreshInSecs()=" + getAutoRefreshInSecs()
                + ", getDevicePropertyPath()=" + getDevicePropertyPath() + ", getTypeModifieryList()="
                + (getTypeModifieryList() != null
                        ? getTypeModifieryList().subList(0, Math.min(getTypeModifieryList().size(), maxLen)) : null)
                + "]";
    }

}
