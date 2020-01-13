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
package org.openhab.binding.plugwise.protocol;

/**
 * Message that is used to switch On or Off a Circle relais
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PowerChangeRequestMessage extends Message {

    public PowerChangeRequestMessage(String MAC, boolean powerState) {
        super(MAC, powerState ? "01" : "00");
        type = MessageType.POWER_CHANGE_REQUEST;
    }

    @Override
    protected String payLoadToHexString() {
        return payLoad;
    }

    @Override
    protected void parsePayLoad() {
    }

    @Override
    protected String sequenceNumberToHexString() {
        return "";
    }

}
