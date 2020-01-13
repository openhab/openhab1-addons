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
package org.openhab.binding.smarthomatic;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * these is smarthomatic specific interface of the binding provider
 *
 * @author arohde
 * @author mcjobo
 * @since 1.9.0
 */
public interface SmarthomaticBindingProvider extends BindingProvider {

    /**
     * returns the message group id
     * 
     * @param itemName
     * @return
     */
    public int getMessageGroupId(String itemName);

    /**
     * returns the message id
     * 
     * @param itemName
     * @return
     */
    public int getMessageId(String itemName);

    /**
     * returns the device id
     * 
     * @param itemName
     * @return
     */
    public int getDeviceId(String itemName);

    /**
     * returns the message part id
     * 
     * @param itemName
     * @return
     */
    public int getMessagePartId(String itemName);

    /**
     * returns the message item id
     * 
     * @param itemName
     * @return
     */
    public int getMessageItemId(String itemName);

    /**
     * returns the item
     * 
     * @param itemName
     * @return
     */
    public Item getItem(String itemName);

    /**
     * returns the config parameters
     * 
     * @param itemName
     * @param paramName
     * @return
     */
    public String getConfigParam(String itemName, String paramName);

}
