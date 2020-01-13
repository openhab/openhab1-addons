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
package org.openhab.binding.freeswitch.internal;

/*
 * Message headers used in a ESL connection
 */
public enum FreeswitchMessageHeader {
    UUID("Unique-ID"),
    CHANNEl_CREATE("CHANNEL_CREATE"),
    CHANNEL_DESTROY("CHANNEL_DESTROY"),
    CALL_DIRECTION("Call-Direction"),
    CID_NAME("Caller-Caller-ID-Name"),
    CID_NUMBER("Caller-Caller-ID-Number"),
    DEST_NUMBER("Caller-Destination-Number"),
    ORIG_NUMBER("Caller-ANI"),
    MESSAGE_WAITING("MESSAGE_WAITING"),
    MWI_WAITING("MWI-Messages-Waiting"),
    MWI_ACCOUNT("MWI-Message-Account"),
    MWI_MESSAGE("MWI-Voice-Message");

    private final String text;

    private FreeswitchMessageHeader(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public boolean matches(String string) {
        return this.toString().equals(string);
    }
}
