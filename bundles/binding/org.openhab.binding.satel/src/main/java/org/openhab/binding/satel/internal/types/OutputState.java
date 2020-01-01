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

/**
 * Available output states.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public enum OutputState implements StateType {
    OUTPUT(0x17);

    private byte refreshCommand;

    OutputState(int refreshCommand) {
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
        return extendedCmd ? 32 : 16;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjectType getObjectType() {
        return ObjectType.OUTPUT;
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
