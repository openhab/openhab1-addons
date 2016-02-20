/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.types;

import java.util.BitSet;

/**
 * Available zone control types.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public enum PartitionControl implements ControlType {
    ARM_MODE_0(0x80),
    ARM_MODE_1(0x81),
    ARM_MODE_2(0x82),
    ARM_MODE_3(0x83),
    DISARM(0x84),
    CLEAR_ALARM(0x85),
    FORCE_ARM_MODE_0(0xa0),
    FORCE_ARM_MODE_1(0xa1),
    FORCE_ARM_MODE_2(0xa2),
    FORCE_ARM_MODE_3(0xa3);

    private static final BitSet stateBits;

    static {
        // for simplicity we just add all partition states here
        stateBits = new BitSet();
        for (PartitionState state : PartitionState.values()) {
            stateBits.set(state.getRefreshCommand());
        }
    }

    private byte controlCommand;

    PartitionControl(int controlCommand) {
        this.controlCommand = (byte) controlCommand;
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
        return ObjectType.PARTITION;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitSet getControlledStates() {
        return stateBits;
    }

}
