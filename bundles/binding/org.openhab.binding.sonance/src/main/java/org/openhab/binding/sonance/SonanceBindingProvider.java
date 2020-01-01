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
package org.openhab.binding.sonance;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Laurens Van Acker
 * @since 1.8.0
 */
public interface SonanceBindingProvider extends BindingProvider {
    /**
     * @param itemName
     *            item name where we want the information from
     * @return IP of the amplifier we want to modify
     */
    public String getIP(String itemName);

    /**
     * @param itemName
     *            item name where we want the information from
     * @return Port on the amplifier where we can connect to
     */
    public int getPort(String itemName);

    /**
     * @param itemName
     *            item name where we want the information from
     * @return Group (music zone) we cant to modify
     */
    public String getGroup(String itemName);

    /**
     * @param itemName
     *            item name where we want the information from
     * @return returns true if this item is a mute item
     */
    public Boolean isMute(String itemName);

    /**
     * @param itemName
     *            item name where we want the information from
     * @return returns true if this item is a volume item
     */
    public Boolean isVolume(String itemName);

    /**
     * @param itemName
     *            item name where we want the information from
     * @return returns true if this item is a power item
     */
    public Boolean isPower(String itemName);
}
