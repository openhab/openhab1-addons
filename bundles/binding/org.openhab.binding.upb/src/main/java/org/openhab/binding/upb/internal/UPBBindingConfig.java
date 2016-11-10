/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.upb.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.core.binding.BindingConfig;

/**
 * This is a helper class holding binding specific configuration details.
 *
 * @author cvanorman
 * @since 1.9.0
 */
public class UPBBindingConfig implements BindingConfig {
    private Byte id;
    private boolean dimmable;
    private boolean link;
    private Map<String, String> properties = new HashMap<>();

    /**
     * Instantiate a new UPBBindingConfig
     *
     * @param properties
     * @param dimmable
     */
    public UPBBindingConfig(String[] properties, boolean dimmable) {
        this.dimmable = dimmable;
        for (String s : properties) {
            String[] entry = s.split("=");

            if (entry.length == 2) {
                setProperty(entry[0], entry[1]);
            }
        }
    }

    /**
     * @return the id
     */
    public Byte getId() {
        return id;
    }

    /**
     * @return the requested property or null if it is not specified.
     */
    public String getProperty(String property) {
        return properties.get(property);
    }

    /**
     * @return the link
     */
    public boolean isLink() {
        return link;
    }

    /**
     * @return the dimmable
     */
    public boolean isDimmable() {
        return dimmable;
    }

    private void setProperty(String prop, String value) {

        if ("id".equals(prop)) {
            this.id = Integer.valueOf(value).byteValue();
        } else if ("link".equals(prop)) {
            this.link = Boolean.valueOf(value);
        } else {
            properties.put(prop, value);
        }
    }
}