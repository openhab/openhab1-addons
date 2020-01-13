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
 * DataFrame received from PLCBus
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public class ReceiveDataFrame extends DataFrame {

    private boolean acknowledgement;
    private boolean itself;
    private boolean risc;

    public ReceiveDataFrame() {
        super();
    }

    public boolean isAcknowledgement() {
        return acknowledgement;
    }

    public boolean isItself() {
        return itself;
    }

    public boolean isRisc() {
        return risc;
    }

    @Override
    public void parse(byte[] data) {
        super.parse(data);
        acknowledgement = ((data[5] & 0x20) == 0x20);
        itself = ((data[5] & 0x10) == 0x10);
        risc = ((data[5] & 0x08) == 0x08);
    }

}
