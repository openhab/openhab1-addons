package org.openhab.binding.maxcul.internal.message.sequencers;

import org.openhab.binding.maxcul.internal.MaxCulMsgHandler;
import org.openhab.binding.maxcul.internal.messages.AckMsg;
import org.openhab.binding.maxcul.internal.messages.BaseMsg;
import org.openhab.binding.maxcul.internal.messages.MaxCulMsgType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle the pairing and initialisation sequence of a new device. This should be called
 * when the device has been verified etc as this will just send the pong without verifying
 * whether we should or not.
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class PairingInitialisationSequence implements MessageSequencer {

	enum PairingInitialisationState
	{
		INITIAL_PING,
		PONG_ACKED,
		WAKEUP_ACKED,
		TIME_ACKED,
		GROUP_ID_ACKED,
		CONFIG_TEMPS_ACKED,
		SENDING_WEEK_PROFILE,
		CONFIG_TEMPS_2_ACKED,
		FINISHED;
	}

	private static final Logger logger =
			LoggerFactory.getLogger(PairingInitialisationSequence.class);

	private PairingInitialisationState state = PairingInitialisationState.INITIAL_PING;

	private String devAddr;
	private byte group_id;
	private String tzStr;
	private MaxCulMsgHandler messageHandler;
	private int pktLostCount = 0;

	public PairingInitialisationSequence(byte group_id, String tzStr, MaxCulMsgHandler messageHandler)
	{
		this.group_id = group_id;
		this.tzStr = tzStr;
		this.messageHandler = messageHandler;
	}

	@Override
	public void runSequencer(BaseMsg msg)
	{
		/* This sequence is taken from observations of activity between the
		 * MAX! Cube and a wall thermostat
		 */
		pktLostCount = 0; // reset counter - ack received
		switch (state)
		{
		case INITIAL_PING:
			/* Send PONG */
			logger.debug("Sending PONG");
			this.devAddr = msg.srcAddrStr;
			messageHandler.sendPairPong(devAddr, this);
			state = PairingInitialisationState.PONG_ACKED;
			break;
		case PONG_ACKED:
			if (msg.msgType == MaxCulMsgType.ACK)
			{
				AckMsg ack = new AckMsg(msg.rawMsg);
				if (!ack.getIsNack())
				{
					/* send a wake up packet */
					logger.debug("Sending WAKEUP");
					messageHandler.sendWakeup(devAddr, this);
					state = PairingInitialisationState.WAKEUP_ACKED;
				} else {
					logger.error("PONG was nacked. Ending sequence");
					state = PairingInitialisationState.FINISHED;
				}
			}
			break;
		case WAKEUP_ACKED:
			if (msg.msgType == MaxCulMsgType.ACK)
			{
				AckMsg ack = new AckMsg(msg.rawMsg);
				if (!ack.getIsNack())
				{
					/* send time information */
					logger.debug("Sending TIME_INFO");
					messageHandler.sendTimeInfo(devAddr, tzStr, this);
					state = PairingInitialisationState.TIME_ACKED;
				} else {
					logger.error("WAKEUP was nacked. Ending sequence");
					state = PairingInitialisationState.FINISHED;
				}
			}
			break;
		case TIME_ACKED:
			if (msg.msgType == MaxCulMsgType.ACK)
			{
				AckMsg ack = new AckMsg(msg.rawMsg);
				if (!ack.getIsNack())
				{
					/* send time information */
					logger.debug("Sending GROUP_ID");
					messageHandler.sendSetGroupId(devAddr, group_id, this);
					state = PairingInitialisationState.GROUP_ID_ACKED;
				} else {
					logger.error("TIME_INFO was nacked. Ending sequence");
					state = PairingInitialisationState.FINISHED;
				}
			}
			break;
		case GROUP_ID_ACKED:
			if (msg.msgType == MaxCulMsgType.ACK)
			{
				AckMsg ack = new AckMsg(msg.rawMsg);
				if (!ack.getIsNack())
				{
					state = PairingInitialisationState.FINISHED;
				} else {
					logger.error("SET_GROUP_ID was nacked. Ending sequence");
					state = PairingInitialisationState.FINISHED;
				}
			}
			break;
		case CONFIG_TEMPS_ACKED:
			state = PairingInitialisationState.FINISHED;
			break;
		case SENDING_WEEK_PROFILE:
			state = PairingInitialisationState.FINISHED;
			break;
		case CONFIG_TEMPS_2_ACKED:
			state = PairingInitialisationState.FINISHED;
			break;
		case FINISHED:
			/* done, do nothing */
			break;
		default:
			logger.error("Invalid state for PairingInitialisation Message Sequence!");
			break;
		}
	}

	@Override
	public boolean isComplete() {
		return state==PairingInitialisationState.FINISHED;
	}

	@Override
	public void packetLost(BaseMsg msg) {
		pktLostCount++;
		logger.debug("Lost "+pktLostCount+" packets");
		if (pktLostCount < 3)
		{
			logger.debug("Attempt retransmission");
			messageHandler.sendMessage(msg);
		} else {
			logger.error("Lost "+pktLostCount+" packets. Ending Sequence in state "+this.state);
			state = PairingInitialisationState.FINISHED;
		}
	}

	@Override
	public boolean useFastSend() {
		// always use fast send - device just sent us a ping to start with!
		return true;
	}

}
