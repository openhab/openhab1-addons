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
package org.openhab.binding.denon.internal.communication.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains information about a Denon receiver.
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name = "device_Info")
@XmlAccessorType(XmlAccessType.FIELD)
public class Deviceinfo {

    private Integer deviceZones;

    private String modelName;

    public Integer getDeviceZones() {
        return deviceZones;
    }

    public void setDeviceZones(Integer deviceZones) {
        this.deviceZones = deviceZones;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
