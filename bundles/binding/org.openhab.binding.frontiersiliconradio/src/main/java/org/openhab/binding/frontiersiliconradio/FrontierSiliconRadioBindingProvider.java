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
package org.openhab.binding.frontiersiliconradio;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Binding provider for internet radios based on frontier silicon chipset. Binding configuration is:
 * "frontiersiliconradio=[deviceId]:[Property]", e.g. "frontiersiliconradio=kitchen:POWER"
 *
 * @author Rainer Ostendorf
 * @since 1.7.0
 */
public interface FrontierSiliconRadioBindingProvider extends BindingProvider {

    // return the property the item is bound to (e.g. "POWER")
    String getProperty(String itemName);

    // get the device id of the radio (e.g. "livingroom-radio")
    String getDeviceID(String itemName);

    /**
     * Returns the Type of the Item identified by {@code itemName}
     * 
     * @param itemName
     *            the name of the item to find the type for
     * @return the type of the Item identified by {@code itemName}
     */
    Class<? extends Item> getItemType(String itemName);
}
