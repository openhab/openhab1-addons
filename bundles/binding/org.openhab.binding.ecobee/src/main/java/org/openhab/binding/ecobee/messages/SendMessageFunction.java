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
package org.openhab.binding.ecobee.messages;

/**
 * The send message function allows an alert message to be sent to the thermostat. The message properties are same
 * as those of the {@link Thermostat.Alert} object.
 *
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/SendMessage.shtml">SendMessage</a>
 * @author John Cocula
 * @since 1.7.0
 */
public final class SendMessageFunction extends AbstractFunction {

    /**
     * @param text
     *            the message text to send. Text will be truncated to 500 characters if longer.
     * @throws IllegalArgumentException
     *             is text is <code>null</code>.
     */
    public SendMessageFunction(String text) {
        super("sendMessage");
        if (text == null) {
            throw new IllegalArgumentException("text is required.");
        }

        makeParams().put("text", text);
    }
}
