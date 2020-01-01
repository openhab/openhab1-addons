/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.pilight.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * openHAB binding config for a single device in pilight
 *
 * @author Jeroen Idserda
 * @since 1.0
 */
public class PilightBindingConfig implements BindingConfig {

    private String itemName;

    private Class<? extends Item> itemType;

    private String instance;

    private String device;

    private String property;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Class<? extends Item> getItemType() {
        return itemType;
    }

    public void setItemType(Class<? extends Item> itemType) {
        this.itemType = itemType;
    }

    public String getInstance() {

        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String value) {
        this.property = value;
    }

}
