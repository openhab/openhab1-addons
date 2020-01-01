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
package org.openhab.binding.pilight.internal.communication;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * pilight configuration object
 *
 * {@link http://www.pilight.org/development/api/#controller}
 *
 * @author Jeroen Idserda
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Config {

    private Map<String, Device> devices;

    public Map<String, Device> getDevices() {
        return devices;
    }

    public void setDevices(Map<String, Device> devices) {
        this.devices = devices;
    }

}
