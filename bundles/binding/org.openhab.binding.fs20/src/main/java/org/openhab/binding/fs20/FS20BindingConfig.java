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
package org.openhab.binding.fs20;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Binding config for FS20 devices.
 *
 * @author Till Klocke
 *
 */
public class FS20BindingConfig implements BindingConfig {

    /**
     * The complete address including Housecode of the device
     */
    private String address;
    private Item item;
    /***
     * possible extra options when communicating with the device. currently
     * unused.
     */
    private String extraOpts;

    public FS20BindingConfig(String address, Item item) {
        this.address = address;
        this.item = item;
    }

    public String getExtraOpts() {
        return extraOpts;
    }

    public void setExtraOpts(String extraOpts) {
        this.extraOpts = extraOpts;
    }

    public String getAddress() {
        return address;
    }

    public Item getItem() {
        return item;
    }

}
