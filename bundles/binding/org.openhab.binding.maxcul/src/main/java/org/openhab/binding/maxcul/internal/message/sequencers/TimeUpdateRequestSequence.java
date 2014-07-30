/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.message.sequencers;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.openhab.binding.maxcul.internal.MaxCulMsgHandler;
import org.openhab.binding.maxcul.internal.messages.AckMsg;
import org.openhab.binding.maxcul.internal.messages.BaseMsg;
import org.openhab.binding.maxcul.internal.messages.MaxCulMsgType;
import org.openhab.binding.maxcul.internal.messages.TimeInfoMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle Time Update requests. Very simple sequence that simply responds to a
 * time request and then handles the response
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class TimeUpdateRequestSequence implements MessageSequencer {

	private enum TimeUpdateRequestState {
		RESPOND_TO_REQUEST, HANDLE_RESPONSE, FINISHED
	}

	private static final Logger logger = LoggerFactory
			.getLogger(TimeUpdateRequestSequence.class);

	private static final long TIME_TOLERANCE = 30000; // 30s margin
	TimeUpdateRequestState state = TimeUpdateRequestState.RESPOND_TO_REQUEST;
	private MaxCulMsgHandler messageHandler;
	private int pktLostCount = 0;
	private String tzStr;

	public TimeUpdateRequestSequence(String tz, MaxCulMsgHandler messageHandler) {
		this.tzStr = tz;
		this.messageHandler = messageHandler;
	}

	/**
	 * Compare two times and check they are within a certain tolerance
	 * 
	 * @param a
	 *            Time A
	 * @param b
	 *            Time B
	 * @param t
	 *            Tolerance in milliseconds
	 * @return true if within tolerance
	 */
	private boolean isValidDeviation(Calendar a, Calendar b, long t) {
		return (Math.abs(a.getTimeInMillis() - b.getTimeInMillis()) <= t);
	}

	@Override
	public void runSequencer(BaseMsg msg) {
		pktLostCount = 0;
		switch (state) {
		case RESPOND_TO_REQUEST:
			if (BaseMsg.getMsgType(msg.rawMsg) == MaxCulMsgType.TIME_INFO) {
				TimeInfoMsg timeMsg = new TimeInfoMsg(msg.rawMsg);
				if (isValidDeviation(timeMsg.getTimeInfo(),
						new GregorianCalendar(), TIME_TOLERANCE)) {
					messageHandler.sendAck(msg);
					state = TimeUpdateRequestState.FINISHED;
				} else {
					messageHandler.sendTimeInfo(msg.srcAddrStr, tzStr, this);
					state = TimeUpdateRequestState.HANDLE_RESPONSE;
				}
			} else {
				state = TimeUpdateRequestState.FINISHED;
				logger.debug("Got invalid message type for Time Update Sequence");
			}
			break;
		case HANDLE_RESPONSE:
			/* check for ACK */
			if (msg.msgType == MaxCulMsgType.ACK) {
				AckMsg ack = new AckMsg(msg.rawMsg);
				if (ack.getIsNack()) {
					logger.error("TIME_INFO was nacked. Ending sequence");
					state = TimeUpdateRequestState.FINISHED;
				} else
					state = TimeUpdateRequestState.FINISHED;
			}
			state = TimeUpdateRequestState.FINISHED;
			break;
		case FINISHED:
			/* do nothing */
			break;
		default:
			logger.error("Invalid state for PairingInitialisation Message Sequence!");
			break;

		}
	}

	@Override
	public void packetLost(BaseMsg msg) {
		pktLostCount++;
		logger.debug("Lost " + pktLostCount + " packets");
		if (pktLostCount < 3) {
			logger.debug("Attempt retransmission");
			messageHandler.sendMessage(msg);
		} else {
			logger.error("Lost " + pktLostCount
					+ " packets. Ending Sequence in state " + this.state);
			state = TimeUpdateRequestState.FINISHED;
		}
	}

	@Override
	public boolean isComplete() {
		return (state == TimeUpdateRequestState.FINISHED);
	}

	@Override
	public boolean useFastSend() {
		return true;
	}

}
