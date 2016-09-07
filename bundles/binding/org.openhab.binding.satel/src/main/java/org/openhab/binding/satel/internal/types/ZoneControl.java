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
 * @since 1.9.0
 */
public enum ZoneControl implements ControlType {
    BYPASS(0x86),
    UNBYPASS(0x87),
    ISOLATE(0x90);

    private static final BitSet stateBits;

    static {
        stateBits = new BitSet();
        stateBits.set(ZoneState.BYPASS.getRefreshCommand());
        stateBits.set(ZoneState.ISOLATE.getRefreshCommand());
    }

    private byte controlCommand;

    ZoneControl(int controlCommand) {
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
        return ObjectType.ZONE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitSet getControlledStates() {
        return stateBits;
    }

}
