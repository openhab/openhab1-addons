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
 * Available partition control types.
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
