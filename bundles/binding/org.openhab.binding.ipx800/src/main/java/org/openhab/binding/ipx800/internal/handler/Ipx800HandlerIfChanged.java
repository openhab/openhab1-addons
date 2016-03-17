/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ipx800.internal.handler;

import java.util.Map;

import org.openhab.binding.ipx800.internal.itemslot.Ipx800Item;

/**
 * This handler update items if the state changed
 * 
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800HandlerIfChanged implements Ipx800Handler {
    /** Previous state of this port as string */
    private String lastState = "";

    @Override
    public boolean updateState(Map<String, Ipx800Item> items, String state) {
        boolean changed = false;
        if (!state.equals(lastState)) {
            for (Ipx800Item itemSlot : items.values()) {
                changed |= itemSlot.updateState(state);
            }
            lastState = state;
        }
        return changed;
    }
}
