/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ipx800.internal.itemslot;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * Mirror switch item
 * 
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800Mirror extends Ipx800Item {
    public Ipx800Mirror() {
        lastState = OnOffType.OFF;
    }

    @Override
    public State getState() {
        return lastState;
    }

    @Override
    protected Type toState(String state) {
        return state.charAt(0) == '1' ? OnOffType.ON : OnOffType.OFF;
    }

    @Override
    protected boolean updateStateInternal(Type state) {
        boolean changed = false;
        if (state instanceof OnOffType) {
            OnOffType commandState = (OnOffType) state;
            changed = commandState.compareTo((OnOffType) lastState) != 0;
            lastState = commandState;
        }
        return changed;
    }

}
