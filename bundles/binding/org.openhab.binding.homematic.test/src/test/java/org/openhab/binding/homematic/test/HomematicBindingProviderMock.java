/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.homematic.test;

import java.util.ArrayList;
import java.util.Collection;

import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.config.AdminItem;
import org.openhab.binding.homematic.internal.config.ParameterAddress;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;

public class HomematicBindingProviderMock implements HomematicBindingProvider {

    public static final String DEFAULT_ITEM_NAME = "default";
    private Collection<String> itemNames = new ArrayList<String>();
    private ParameterAddress parameterAddress;
    private Item item = new SwitchItem(DEFAULT_ITEM_NAME);

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

    public ParameterAddress getParameterAddress(String itemName) {
        return parameterAddress;
    }

    public void setParameterAddress(ParameterAddress parameterAddress) {
        this.parameterAddress = parameterAddress;
        itemNames.add(DEFAULT_ITEM_NAME);
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

}
