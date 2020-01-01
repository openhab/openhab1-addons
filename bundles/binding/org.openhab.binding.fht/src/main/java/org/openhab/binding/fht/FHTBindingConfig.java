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
package org.openhab.binding.fht;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * This class represents FHT based devices. These can be FHT-80b, FHT-8v or the
 * window sensor.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class FHTBindingConfig implements BindingConfig {

    /**
     * This enum represents the available data types of the FHT devices. Not all
     * datapoints are available in all devices. Refer to your FHT manual to know
     * which to use.
     * 
     * @author Till Klocke
     * 
     */
    public static enum Datapoint {
        MEASURED_TEMP,
        DESIRED_TEMP,
        BATTERY,
        WINDOW,
        VALVE;
    }

    /**
     * The housecode in hexadecimal notation
     */
    private String housecode;
    /**
     * The address in hexadecimal notation. Not needed in FHT-80b.
     */
    private String address;

    private Datapoint datapoint;

    private Item item;

    public FHTBindingConfig(Item item, String housecode, String address, Datapoint datapoint) {
        this.housecode = housecode;
        this.address = address;
        this.datapoint = datapoint;
        this.item = item;
    }

    public String getHousecode() {
        return housecode;
    }

    public String getAddress() {
        return address;
    }

    public Datapoint getDatapoint() {
        return datapoint;
    }

    /**
     * Return the full address needed to address the device via rf.
     * 
     * @return
     */
    public String getFullAddress() {
        if (address != null) {
            return housecode + address;
        }
        return housecode;
    }

    public Item getItem() {
        return item;
    }

}
