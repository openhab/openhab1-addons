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
package org.openhab.binding.sallegra.internal;

import org.openhab.core.binding.BindingConfig;

/**
 * Wrapper to represent a Binding Item Configuration
 *
 * @author Benjamin Marty (Developed on behalf of Satelco.ch)
 * @since 1.8.0
 *
 */
public class SallegraBindingConfig implements BindingConfig {

    private String item;
    private String property;
    private String moduleName;
    private SallegraCommand cmdId;
    private String cmdValue;

    public SallegraBindingConfig(String moduleName, SallegraCommand cmdId, String cmdValue) {
        this.moduleName = moduleName;
        this.cmdId = cmdId;
        this.cmdValue = cmdValue;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String itemName) {
        this.item = itemName;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String Property) {
        this.property = Property;
    }

    public String getModuleName() {
        return moduleName;
    }

    public SallegraCommand getCmdId() {
        return cmdId;
    }

    public String getCmdValue() {
        return cmdValue;
    }
}
