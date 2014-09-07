/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.enocean.internal.bus;

import java.util.ArrayList;
import java.util.Collection;

import org.opencean.core.address.EnoceanParameterAddress;
import org.opencean.core.common.EEPId;
import org.openhab.binding.enocean.EnoceanBindingProvider;
import org.openhab.binding.enocean.internal.profiles.Profile;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;

public class EnoceanBindingProviderMock implements EnoceanBindingProvider {

    public static final String DEVICE_ID = "00:00:00:00";
    public static final String DEFAULT_ITEM_NAME = "default";
    private Collection<String> itemNames = new ArrayList<String>();
    private EnoceanParameterAddress parameterAddress;
    private Item item = new SwitchItem(DEFAULT_ITEM_NAME);
    private EEPId eepId;

    public EnoceanBindingProviderMock() {
        itemNames.add(DEFAULT_ITEM_NAME);
    }

    public void addBindingChangeListener(BindingChangeListener listener) {
        // TODO Auto-generated method stub

    }

    public void removeBindingChangeListener(BindingChangeListener listener) {
        // TODO Auto-generated method stub

    }

    public boolean providesBindingFor(String itemName) {
        return true;
    }

    public boolean providesBinding() {
        return true;
    }

    public Collection<String> getItemNames() {
        return itemNames;
    }

    public EnoceanParameterAddress getParameterAddress(String itemName) {
        return parameterAddress;
    }

    public void setParameterAddress(EnoceanParameterAddress parameterAddress) {
        this.parameterAddress = parameterAddress;
    }

    public EEPId getEEP(String itemName) {
        return eepId;
    }

    public void setEep(EEPId eepId) {
        this.eepId = eepId;
    }

    public Item getItem(String itemName) {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Class<Profile> getCustomProfile(String itemName) {
        // TODO Auto-generated method stub
        return null;
    }
}
