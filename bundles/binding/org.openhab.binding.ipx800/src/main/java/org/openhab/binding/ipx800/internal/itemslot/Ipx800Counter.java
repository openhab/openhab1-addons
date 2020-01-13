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
package org.openhab.binding.ipx800.internal.itemslot;

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * Simple counter item
 * 
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800Counter extends Ipx800Item {
    private int lastState = 0;

    public Ipx800Counter() {
    }

    @Override
    public State getState() {
        return new DecimalType(lastState);
    }

    @Override
    protected Type toState(String state) {
        return new DecimalType(Integer.parseInt(state));
    }

    @Override
    protected boolean updateStateInternal(Type state) {
        if (state instanceof DecimalType) {
            lastState = ((DecimalType) state).intValue();
        }
        return true;
    }

}
