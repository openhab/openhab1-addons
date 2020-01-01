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
package org.openhab.binding.wemo;

import org.openhab.binding.wemo.internal.WemoGenericBindingProvider.WemoChannelType;
import org.openhab.core.binding.BindingProvider;

/**
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public interface WemoBindingProvider extends BindingProvider {

    /**
     * Returns the friendlyName for the item with the given name.
     * 
     * @param itemName
     * @return The friendlyName if there is an item with the given name, null
     *         otherwise.
     */
    public String getUDN(String itemName);

    /**
     * Returns the channelType for the item with the given name.
     * 
     * @param itemName
     * @return The channelType if there is an item with the given name, null
     *         otherwise.
     */
    public WemoChannelType getChannelType(String itemName);

}
