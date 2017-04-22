/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.types;

/**
 * Available partition states.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public enum PartitionState implements StateType {
    ARMED(0x09),
    REALLY_ARMED(0x0a),
    ARMED_MODE_2(0x0b),
    ARMED_MODE_3(0x0c),
    FIRST_CODE_ENTERED(0x0d),
    ENTRY_TIME(0x0e),
    EXIT_TIME_GT_10(0x0f),
    EXIT_TIME_LT_10(0x10),
    TEMPORARY_BLOCKED(0x11),
    BLOCKED_FOR_GUARD(0x12),
    ALARM(0x13),
    FIRE_ALARM(0x14),
    ALARM_MEMORY(0x15),
    FIRE_ALARM_MEMORY(0x16),
    VIOLATED_ZONES(0x25),
    VERIFIED_ALARMS(0x27),
    ARMED_MODE_1(0x2a),
    WARNING_ALARMS(0x2b);

    private byte refreshCommand;

    PartitionState(int refreshCommand) {
        this.refreshCommand = (byte) refreshCommand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte getRefreshCommand() {
        return refreshCommand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPayloadLength(boolean extendedCmd) {
        return 4;
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
    public int getStartByte() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getBytesCount(boolean extendedCmd) {
        return getPayloadLength(extendedCmd);
    }

}