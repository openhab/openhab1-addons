/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.event;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;

/**
 * ZWave Transaction Completed Event. Indicated that a transaction (a 
 * sequence of messages with an expected reply) has completed successfully.
 * @author Jan-Willem Spuij
 * @since 1.4.0
 */
public class ZWaveTransactionCompletedEvent extends ZWaveEvent {

	private final SerialMessage completedMessage;
	
	/**
	 * Constructor. Creates a new instance of the ZWaveTransactionCompletedEvent
	 * class.
	 * @param completedMessage the original {@link SerialMessage} that has been completed.
	 */
	public ZWaveTransactionCompletedEvent(SerialMessage completedMessage) {
		super(completedMessage.getMessageNode(), 1);

		this.completedMessage = completedMessage;
	}

	/**
	 * Gets the original {@link SerialMessage} that has been completed.
	 * @return the original message.
	 */
	public SerialMessage getCompletedMessage() {
		return completedMessage;
	}
}
