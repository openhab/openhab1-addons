/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.test;

import java.util.ArrayList;
import java.util.Collection;

import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.config.AdminItem;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.converter.state.StateConverter;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;

public class HomematicBindingProviderMock implements HomematicBindingProvider {

    public static final String DEFAULT_ITEM_NAME = "default";
    private Collection<String> itemNames = new ArrayList<String>();
    private HomematicParameterAddress parameterAddress;
    private Item item = new SwitchItem(DEFAULT_ITEM_NAME);
    private Class<? extends StateConverter<?, ?>> customConverter;

    public HomematicBindingProviderMock() {
    }

    public void addBindingChangeListener(BindingChangeListener listener) {
    }

    public void removeBindingChangeListener(BindingChangeListener listener) {
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

    public HomematicParameterAddress getParameterAddress(String itemName) {
        return parameterAddress;
    }

    public void setParameterAddress(HomematicParameterAddress parameterAddress) {
        this.parameterAddress = parameterAddress;
    }

    public AdminItem getAdminItem(String itemName) {
        return null;
    }

    public boolean isAdminItem(String itemName) {
        return false;
    }

    public Item getItem(String itemName) {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @SuppressWarnings("unchecked")
    public Class<? extends StateConverter<?, ?>> getConverter(String itemName) {
        return customConverter;
    }

    public void setCustomConverter(Class<? extends StateConverter<?, ?>> customConverter) {
        this.customConverter = customConverter;
    }

}
