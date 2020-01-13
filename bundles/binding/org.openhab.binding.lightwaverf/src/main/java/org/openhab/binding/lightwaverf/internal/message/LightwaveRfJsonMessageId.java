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
package org.openhab.binding.lightwaverf.internal.message;

import java.util.Objects;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfJsonMessageId implements LightwaveRfMessageId {

    private final int messageId;

    public LightwaveRfJsonMessageId(int messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getMessageIdString() {
        return String.valueOf(messageId);
    }

    @Override
    public boolean equals(Object that) {
        if (that instanceof LightwaveRfJsonMessageId) {
            return Objects.equals(this.messageId, ((LightwaveRfJsonMessageId) that).messageId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }

    @Override
    public String toString() {
        return "LightwaveRfHeatingMessageId[" + messageId + "]";
    }

}
