package org.openhab.binding.maxcul.internal.message.sequencers;

import java.util.HashSet;
import java.util.Iterator;

import org.openhab.binding.maxcul.internal.MaxCulBindingConfig;
import org.openhab.binding.maxcul.internal.MaxCulDevice;
import org.openhab.binding.maxcul.internal.MaxCulMsgHandler;
import org.openhab.binding.maxcul.internal.messages.AckMsg;
import org.openhab.binding.maxcul.internal.messages.BaseMsg;
import org.openhab.binding.maxcul.internal.messages.MaxCulMsgType;
import org.openhab.binding.maxcul.internal.messages.PairPingMsg;
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

	private enum PairingInitialisationState
	{
		INITIAL_PING,
		PONG_ACKED,
		WAKEUP_ACKED,
		TIME_ACKED,
		GROUP_ID_ACKED,
		CONFIG_TEMPS_ACKED,
		SENDING_ASSOCIATIONS,
		SENDING_ASSOCIATIONS_ACKED,
		SENDING_WEEK_PROFILE,
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
	private MaxCulDevice deviceType = MaxCulDevice.UNKNOWN;
	private MaxCulBindingConfig config;
	private HashSet<MaxCulBindingConfig> associations;
	private Iterator<MaxCulBindingConfig> assocIter;

	public PairingInitialisationSequence(byte group_id, String tzStr, MaxCulMsgHandler messageHandler, MaxCulBindingConfig cfg, HashSet<MaxCulBindingConfig> associations)
	{
		this.group_id = group_id;
		this.tzStr = tzStr;
		this.messageHandler = messageHandler;
		this.config = cfg;
		this.associations = associations;
	}

	@Override
	public void runSequencer(BaseMsg msg)
	{
		/* This sequence is taken from observations of activity between the
		 * MAX! Cube and a wall thermostat
		 */
		pktLostCount = 0; // reset counter - ack received
		logger.debug("Sequence State: "+state);
		switch (state)
		{
		case INITIAL_PING:
			/* get device type */
			PairPingMsg ppMsg = new PairPingMsg(msg.rawMsg);
			this.deviceType = MaxCulDevice.getDeviceTypeFromInt(ppMsg.type);
			/* Send PONG - assumes PING is checked*/
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
					if (this.deviceType == MaxCulDevice.PUSH_BUTTON)
					{
						/* for a push button we're done now */
						state = PairingInitialisationState.FINISHED;
					}
					else
					{
						/* send a wake up packet */
						logger.debug("Sending WAKEUP");
						messageHandler.sendWakeup(devAddr, this);
						state = PairingInitialisationState.WAKEUP_ACKED;
					}
				} else {
					logger.error("PONG was nacked. Ending sequence");
					state = PairingInitialisationState.FINISHED;
				}
			} else logger.error("Received "+msg.msgType+" when expecting ACK");
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
			} else logger.error("Received "+msg.msgType+" when expecting ACK");
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
			} else logger.error("Received "+msg.msgType+" when expecting ACK");
			break;
		case GROUP_ID_ACKED:
			if (msg.msgType == MaxCulMsgType.ACK)
			{
				AckMsg ack = new AckMsg(msg.rawMsg);
				if (!ack.getIsNack() &&
						(this.deviceType == MaxCulDevice.RADIATOR_THERMOSTAT ||
						 this.deviceType == MaxCulDevice.WALL_THERMOSTAT ||
						 this.deviceType == MaxCulDevice.RADIATOR_THERMOSTAT_PLUS))
				{
					// send temps for comfort/eco etc
					messageHandler.sendConfigTemperatures(devAddr, this, config.getComfortTemp(), config.getEcoTemp(),
							config.getMaxTemp(), config.getMinTemp(),
							config.getMeasurementOffset(), config.getWindowOpenTemperature(), config.getWindowOpenDuration());
					state = PairingInitialisationState.CONFIG_TEMPS_ACKED;
				} else {
					logger.error("SET_GROUP_ID was nacked. Ending sequence");
					state = PairingInitialisationState.FINISHED;
				}
			} else logger.error("Received "+msg.msgType+" when expecting ACK");
			break;
		case CONFIG_TEMPS_ACKED:
			if (msg.msgType == MaxCulMsgType.ACK)
			{
				AckMsg ack = new AckMsg(msg.rawMsg);
				if (!ack.getIsNack())
				{
					if (associations.isEmpty() == false)
					{
						/* send first association message */
						assocIter = associations.iterator();
						MaxCulBindingConfig partnerCfg = assocIter.next();
						messageHandler.sendAddLinkPartner(devAddr, this, partnerCfg.getDevAddr(), partnerCfg.getDeviceType());
						/* if it's the last association message then wait for ACK, otherwise keep going */
						if (assocIter.hasNext())
							state = PairingInitialisationState.SENDING_ASSOCIATIONS;
						else
							state = PairingInitialisationState.SENDING_ASSOCIATIONS_ACKED;
					}
					else
					{
						logger.debug("No associations");
						state = PairingInitialisationState.FINISHED;
					}
				} else {
					logger.error("CONFIG_TEMPERATURES was nacked. Ending sequence");
					state = PairingInitialisationState.FINISHED;
				}
			} else logger.error("Received "+msg.msgType+" when expecting ACK");
			break;
		case SENDING_ASSOCIATIONS:
			if (msg.msgType == MaxCulMsgType.ACK)
			{
				AckMsg ack = new AckMsg(msg.rawMsg);
				if (!ack.getIsNack())
				{
					if (assocIter.hasNext()) /* this should always be true, but good to check */
					{
						MaxCulBindingConfig partnerCfg = assocIter.next();
						messageHandler.sendAddLinkPartner(this.devAddr, this, partnerCfg.getDevAddr(), partnerCfg.getDeviceType());
						/* if it's the last association message then wait for last ACK */
						if (assocIter.hasNext())
							state = PairingInitialisationState.SENDING_ASSOCIATIONS;
						else
							state = PairingInitialisationState.SENDING_ASSOCIATIONS_ACKED;
					}
					else
					{
						// TODO NOTE: if further states are added then ensure you go to the right state. I.e. when all associations are done
						state = PairingInitialisationState.FINISHED;
					}
				} else {
					logger.error("SENDING_ASSOCIATIONS was nacked. Ending sequence");
					state = PairingInitialisationState.FINISHED;
				}
			} else logger.error("Received "+msg.msgType+" when expecting ACK");
			break;
		case SENDING_ASSOCIATIONS_ACKED:
			state = PairingInitialisationState.FINISHED;
			break;
		case SENDING_WEEK_PROFILE:
			// TODO implement this - but where to get a week profile from. Meaningless at the moment!
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
