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
package org.openhab.binding.plcbus.internal.protocol;

/**
 * Frame received from PLCBus
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public class ReceiveFrame extends Frame<ReceiveDataFrame> {

    private byte endByte;

    public ReceiveFrame() {
        super();
    }

    @Override
    public byte getEndByte() {
        return endByte;
    }

    public boolean isAcknowledgement() {
        return getData().isAcknowledgement();
    }

    public int getFirstParameter() {
        return getData().getFirstParameter();
    }

    public int getSecondParameter() {
        return getData().getSecondParameter();
    }

    public void parse(byte[] bytes) {
        data = new ReceiveDataFrame();
        data.parse(bytes);
        endByte = bytes[bytes.length - 1];
    }

}
