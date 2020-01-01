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
package org.openhab.binding.satel.internal.types;

import java.util.BitSet;

/**
 * Available zone control types.
 *
 * @author Krzysztof Goworek
 * @since 1.9.0
 */
public enum ZoneControl implements ControlType {
    BYPASS(0x86, ZoneState.BYPASS),
    UNBYPASS(0x87, ZoneState.BYPASS),
    ISOLATE(0x90, ZoneState.ISOLATE);

    private byte controlCommand;
    private BitSet stateBits;

    ZoneControl(int controlCommand, ZoneState... controlledStates) {
        this.controlCommand = (byte) controlCommand;
        this.stateBits = getStatesBitSet(controlledStates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getControlCommand() {
        return controlCommand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectType getObjectType() {
        return ObjectType.ZONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitSet getControlledStates() {
        return stateBits;
    }

    private static BitSet getStatesBitSet(ZoneState... states) {
        BitSet stateBits = new BitSet();
        for (ZoneState state : states) {
            stateBits.set(state.getRefreshCommand());
        }
        return stateBits;
    }

}
