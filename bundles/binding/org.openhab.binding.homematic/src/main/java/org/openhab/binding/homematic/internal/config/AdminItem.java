/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * AdminItems are used to access admin information from the homematic binding.
 * This is kind of a workaround for a clean interface.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * 
 */
public class AdminItem {

    public static String ID = "ADMIN";

    private String command;

    public static AdminItem fromBindingConfig(String bindingConfig) throws BindingConfigParseException {
        String[] configParts = bindingConfig.trim().split(":");
        if (configParts.length != 2 || !configParts[0].equals(ID)) {
            throw new BindingConfigParseException("Homematic admin items must contain two parts ADMIN:<command>");
        }
        AdminItem adminItem = new AdminItem(configParts[1]);
        return adminItem;
    }

    public AdminItem(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

}
