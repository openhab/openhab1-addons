/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.deviceproperties;

import org.openhab.binding.onewire.internal.connection.OneWireConnection;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This Class is a specialized writable BindingConfig with a special execute command.
 * It connects openHab Switch-Items to 1-Wire device properties and simulates a PushButton (ON-wait-OFF or OFF-wait-ON)
 *
 * For Basic Configuration of the binding, see
 * OneWireDevicePropertySwitchBindingConfig.java
 *
 * Example:
 * <ul>
 * <li>
 * <code>onewire="deviceId=29.66C30E000000;propertyName=sensed.0;refreshinterval=10";pushbutton=500;invert",autoupdate="false"</code>
 * </li>
 * </ul>
 *
 * @author Dennis Riegelbauer
 * @since 1.9.0
 *
 */
public class OneWireDevicePropertyPushButtonBindingConfig extends OneWireDevicePropertySwitchBindingConfig
        implements OneWireDevicePropertyExecutableBindingConfig {

    private int waitTime;

    public OneWireDevicePropertyPushButtonBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
        super(pvBindingConfig);
        super.parseBindingConfig(pvBindingConfig);
        this.parsePushButtonConfig(pvBindingConfig);
    }

    @Override
    protected void parseBindingConfig(String pvBindingConfig) throws BindingConfigParseException {
        String[] lvConfigParts = pvBindingConfig.trim().split(";");

        for (String lvConfigPart : lvConfigParts) {
            parsePushButtonConfig(lvConfigPart);
        }
    }

    private void parsePushButtonConfig(String pvConfigPart) {
        String lvConfigProperty = null;

        lvConfigProperty = "pushbutton=";
        if (pvConfigPart.startsWith(lvConfigProperty)) {
            String lvConfigValue = pvConfigPart.substring(lvConfigProperty.length());
            this.waitTime = Integer.parseInt(lvConfigValue);
        }
    }

    /**
     * Checks, if this special binding-type matches to the given pvBindingConfig
     * 
     * @param pvItem
     * @param pvBindingConfig
     * @return boolean
     */
    public static boolean isBindingConfigToCreate(Item pvItem, String pvBindingConfig) {
        return ((pvItem instanceof SwitchItem) && (pvBindingConfig.contains("pushbutton")));
    }

    @Override
    public void execute(Command pvCommand) {
        if (pvCommand.equals(OnOffType.ON)) {
            write(OnOffType.ON);
            sleep();
            write(OnOffType.OFF);
        } else if (pvCommand.equals(OnOffType.OFF)) {
            write(OnOffType.OFF);
            sleep();
            write(OnOffType.ON);
        } else {
            throw new IllegalStateException("Unknown command for this binding:" + pvCommand.toString());
        }
    }

    private void sleep() {
        try {
            Thread.sleep(this.waitTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(Command pvCommand) {
        OneWireConnection.writeToOneWire(this.getDevicePropertyPath(), this.convertTypeToString(pvCommand));
    }

    @Override
    public String toString() {
        final int maxLen = 20;
        return "OneWireDevicePropertyPushButtonBindingConfig [getDeviceId()=" + getDeviceId() + ", getPropertyName()="
                + getPropertyName() + ", getAutoRefreshInSecs()=" + getAutoRefreshInSecs()
                + ", getDevicePropertyPath()=" + getDevicePropertyPath() + ", getTypeModifieryList()="
                + (getTypeModifieryList() != null
                        ? getTypeModifieryList().subList(0, Math.min(getTypeModifieryList().size(), maxLen)) : null)
                + "]";
    }
}
