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
package org.openhab.binding.gc100ir.util;

import java.util.HashMap;

/**
 * Utility class for storing and retrieving items.
 * 
 * @author Parikshit Thakur & Team
 * @since 1.9.0
 * 
 */
public class GC100ItemUtility {

    static GC100ItemUtility gc100ItemUtility;
    static HashMap<String, GC100ItemBean> itemBeanMap = new HashMap<String, GC100ItemBean>();

    public static GC100ItemUtility getInstance() {
        if (gc100ItemUtility == null) {
            gc100ItemUtility = new GC100ItemUtility();
        }

        return gc100ItemUtility;
    }

    public void addItem(String itemName, GC100ItemBean itemBean) {
        itemBeanMap.put(itemName, itemBean);
    }

    public static void clearAllItems() {
        itemBeanMap.clear();
    }

    public HashMap<String, GC100ItemBean> getAllItems() {
        return itemBeanMap;
    }

    public String prepareCode(GC100ItemBean itemBean) {
        if (itemBean != null) {
            return getIRCode(itemBean);
        }
        return null;
    }

    private String getIRCode(GC100ItemBean itemBean) {
        return "sendir," + itemBean.getModule() + ":" + itemBean.getConnector() + ",1," + itemBean.getCode() + "\r";
    }
}
