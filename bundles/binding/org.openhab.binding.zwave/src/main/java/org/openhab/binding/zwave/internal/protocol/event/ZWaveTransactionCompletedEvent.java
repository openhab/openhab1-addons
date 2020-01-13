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
package org.openhab.binding.zwave.internal.protocol.event;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;

/**
 * ZWave Transaction Completed Event. Indicated that a transaction (a
 * sequence of messages with an expected reply) has completed.
 *
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveTransactionCompletedEvent extends ZWaveEvent {

    private final SerialMessage completedMessage;
    private final boolean state;

    /**
     * Constructor. Creates a new instance of the ZWaveTransactionCompletedEvent
     * class.
     * The 'state' flag is provided by the message handler when the message is processed
     * and its value is defined by the message class.
     *
     * @param completedMessage the original {@link SerialMessage} that has been completed.
     * @param state a flag indicating success / failure of the transaction processing
     */
    public ZWaveTransactionCompletedEvent(SerialMessage completedMessage, boolean state) {
        super(completedMessage.getMessageNode());

        this.completedMessage = completedMessage;
        this.state = state;
    }

    /**
     * Gets the original {@link SerialMessage} that has been completed.
     *
     * @return the original message.
     */
    public SerialMessage getCompletedMessage() {
        return completedMessage;
    }

    /**
     * Returns the processing state of this transaction
     *
     * @return
     */
    public boolean getState() {
        return state;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ZWaveTransactionCompletedEvent [completedMessage=").append(completedMessage).append(", state=")
                .append(state).append("]");
        return builder.toString();
    }
}
