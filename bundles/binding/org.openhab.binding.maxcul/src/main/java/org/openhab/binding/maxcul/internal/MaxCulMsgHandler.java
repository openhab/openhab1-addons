/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.openhab.binding.maxcul.MaxCulBindingProvider;
import org.openhab.binding.maxcul.internal.messages.AckMsg;
import org.openhab.binding.maxcul.internal.messages.AddLinkPartnerMsg;
import org.openhab.binding.maxcul.internal.messages.BaseMsg;
import org.openhab.binding.maxcul.internal.messages.ConfigTemperaturesMsg;
import org.openhab.binding.maxcul.internal.messages.MaxCulBindingMessageProcessor;
import org.openhab.binding.maxcul.internal.messages.MaxCulMsgType;
import org.openhab.binding.maxcul.internal.messages.PairPingMsg;
import org.openhab.binding.maxcul.internal.messages.PairPongMsg;
import org.openhab.binding.maxcul.internal.messages.ResetMsg;
import org.openhab.binding.maxcul.internal.messages.SetGroupIdMsg;
import org.openhab.binding.maxcul.internal.messages.SetTemperatureMsg;
import org.openhab.binding.maxcul.internal.messages.ThermostatControlMode;
import org.openhab.binding.maxcul.internal.messages.ThermostatStateMsg;
import org.openhab.binding.maxcul.internal.messages.TimeInfoMsg;
import org.openhab.binding.maxcul.internal.messages.WakeupMsg;
import org.openhab.binding.maxcul.internal.messages.WallThermostatControlMsg;
import org.openhab.binding.maxcul.internal.messages.WallThermostatStateMsg;
import org.openhab.binding.maxcul.internal.message.sequencers.MessageSequencer;
import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULHandler;
import org.openhab.io.transport.cul.CULListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle messages going to and from the CUL device. Make sure to intercept
 * control command responses first before passing on valid MAX! messages to the
 * binding itself for processing.
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class MaxCulMsgHandler implements CULListener {

	private static final Logger logger = LoggerFactory
			.getLogger(MaxCulMsgHandler.class);

	class SenderQueueItem {
		BaseMsg msg;
		Date expiry;
		int retryCount = 0;
	}

	private Date lastTransmit = new Date();
	private Date endOfQueueTransmit;

	private int msgCount = 0;
	private CULHandler cul = null;
	private String srcAddr;
	private HashMap<Byte, MessageSequencer> sequenceRegister;
	private LinkedList<SenderQueueItem> sendQueue;
	private ConcurrentHashMap<Byte, SenderQueueItem> pendingAckQueue;
	private MaxCulBindingMessageProcessor mcbmp = null;
	private Map<SenderQueueItem, Timer> sendQueueTimers = new HashMap<SenderQueueItem, Timer>();
	private Collection<MaxCulBindingProvider> providers;

	private boolean listenMode = false;

	private final int MESSAGE_EXPIRY_PERIOD = 10000;

	public MaxCulMsgHandler(String srcAddr, CULHandler cul,
			Collection<MaxCulBindingProvider> providers) {
		this.cul = cul;
		cul.registerListener(this);
		this.srcAddr = srcAddr;
		this.sequenceRegister = new HashMap<Byte, MessageSequencer>();
		this.sendQueue = new LinkedList<SenderQueueItem>();
		this.pendingAckQueue = new ConcurrentHashMap<Byte, SenderQueueItem>();
		this.lastTransmit = new Date(); /* init as now */
		this.endOfQueueTransmit = this.lastTransmit;
		this.providers = providers;
	}

	private byte getMessageCount() {
		this.msgCount += 1;
		this.msgCount &= 0xFF;
		return (byte) this.msgCount;
	}

	private boolean enoughCredit(int requiredCredit, boolean fastSend) {
		int availableCredit = cul.getCredit10ms();
		int preambleCredit = fastSend ? 0 : 100;
		boolean result = (availableCredit >= (requiredCredit + preambleCredit));
		logger.debug("Fast Send? " + fastSend + ", preambleCredit = "
				+ preambleCredit + ", requiredCredit = " + requiredCredit
				+ ", availableCredit = " + availableCredit + ", enoughCredit? "
				+ result);
		return result;

	}

	private void transmitMessage(BaseMsg data, SenderQueueItem queueItem) {
		try {
			cul.send(data.rawMsg);
		} catch (CULCommunicationException e) {
			logger.error("Unable to send CUL message " + data + " because: "
					+ e.getMessage());
		}
		/* update surplus credit value */
		boolean fastSend = false;
		if (data.isPartOfSequence()) {
			fastSend = data.getMessageSequencer().useFastSend();
		}
		enoughCredit(data.requiredCredit(), fastSend);
		this.lastTransmit = new Date();
		if (this.endOfQueueTransmit.before(this.lastTransmit)) {
			/* hit a time after the queue finished tx'ing */
			this.endOfQueueTransmit = this.lastTransmit;
		}

		if (data.msgType != MaxCulMsgType.ACK) {
			/* awaiting ack now */
			SenderQueueItem qi = queueItem;
			if (qi == null) {
				qi = new SenderQueueItem();
				qi.msg = data;
			}

			qi.expiry = new Date(this.lastTransmit.getTime()
					+ MESSAGE_EXPIRY_PERIOD);
			this.pendingAckQueue.put(qi.msg.msgCount, qi);

			/* schedule a check of pending acks */
			TimerTask ackCheckTask = new TimerTask() {
				public void run() {
					checkPendingAcks();
				}
			};
			Timer timer = new Timer();
			timer.schedule(ackCheckTask, qi.expiry);
		}
	}

	public void sendMessage(BaseMsg msg) {
		sendMessage(msg, null);
	}

	/**
	 * Send a raw Base Message
	 * 
	 * @param msg
	 *            Base message to send
	 * @param queueItem
	 *            queue item (used for retransmission)
	 */
	private void sendMessage(BaseMsg msg, SenderQueueItem queueItem) {
		Timer timer = null;

		if (msg.readyToSend()) {
			if (enoughCredit(msg.requiredCredit(), msg.isFastSend())
					&& this.sendQueue.isEmpty()) {
				/*
				 * send message as we have enough credit and nothing is on the
				 * queue waiting
				 */
				logger.debug("Sending message immediately. Message is "
						+ msg.msgType + " => " + msg.rawMsg);
				transmitMessage(msg, queueItem);
				logger.debug("Credit required " + msg.requiredCredit());
			} else {
				/*
				 * message is going on the queue - this means that the device
				 * may well go to standby before it receives it so change into
				 * long slow send format with big preamble
				 */
				msg.setFastSend(false);
				/*
				 * don't have enough credit or there are messages ahead of us so
				 * queue up the item and schedule a task to process it
				 */
				SenderQueueItem qi = queueItem;
				if (qi == null) {
					qi = new SenderQueueItem();
					qi.msg = msg;
				}

				TimerTask task = new TimerTask() {
					public void run() {
						SenderQueueItem topItem = sendQueue.remove();
						logger.debug("Checking credit");
						if (enoughCredit(topItem.msg.requiredCredit(),
								topItem.msg.isFastSend())) {
							logger.debug("Sending item from queue. Message is "
									+ topItem.msg.msgType + " => "
									+ topItem.msg.rawMsg);
							if (topItem.msg.msgType == MaxCulMsgType.TIME_INFO) {
								((TimeInfoMsg) topItem.msg).updateTime();
							}
							transmitMessage(topItem.msg, topItem);
						} else {
							logger.error("Not enough credit after waiting. This is bad. Queued command is discarded");
						}
					}
				};

				timer = new Timer();
				sendQueueTimers.put(qi, timer);
				/*
				 * calculate when we want to TX this item in the queue, with a
				 * margin of 2 credits. x1000 as we accumulate 1 x 10ms credit
				 * every 1000ms
				 */
				int requiredCredit = msg.isFastSend() ? 0 : 100 + msg
						.requiredCredit() + 2;
				this.endOfQueueTransmit = new Date(
						this.endOfQueueTransmit.getTime()
								+ (requiredCredit * 1000));
				timer.schedule(task, this.endOfQueueTransmit);
				this.sendQueue.add(qi);

				logger.debug("Added message to queue to be TX'd at "
						+ this.endOfQueueTransmit.toString());
			}

			if (msg.isPartOfSequence()) {
				/* add to sequence register if part of a sequence */
				logger.debug("Message " + msg.msgCount
						+ " is part of sequence. Adding to register.");
				sequenceRegister.put(msg.msgCount, msg.getMessageSequencer());
			}
		} else {
			logger.error("Tried to send a message that wasn't ready!");
		}
	}

	/**
	 * Associate binding processor with this message handler
	 * 
	 * @param mcbmp
	 *            Binding processor to associate with this message handler
	 */
	public void registerMaxCulBindingMessageProcessor(
			MaxCulBindingMessageProcessor mcbmp) {
		if (this.mcbmp == null) {
			this.mcbmp = mcbmp;
			logger.debug("Associated MaxCulBindingMessageProcessor");
		} else {
			logger.error("Tried to associate a second MaxCulBindingMessageProcessor!");
		}
	}

	/**
	 * Check the ACK queue for any pending acks that have expired
	 */
	public void checkPendingAcks() {
		Date now = new Date();

		for (SenderQueueItem qi : pendingAckQueue.values()) {
			if (now.after(qi.expiry)) {
				logger.error("Packet " + qi.msg.msgCount + " ("
						+ qi.msg.msgType + ") lost - timeout");
				pendingAckQueue.remove(qi.msg.msgCount); // remove from ACK
															// queue
				if (sequenceRegister.containsKey(qi.msg.msgCount)) {
					/* message sequencer handles failed packet */
					MessageSequencer msgSeq = sequenceRegister
							.get(qi.msg.msgCount);
					sequenceRegister.remove(qi.msg.msgCount); // remove from
																// register
																// first as
																// packetLost
																// could add it
																// again
					msgSeq.packetLost(qi.msg);
				} else if (qi.retryCount < 3) {
					/* retransmit */
					qi.retryCount++;
					logger.debug("Retransmitting packet " + qi.msg.msgCount
							+ " attempt " + qi.retryCount);
					sendMessage(qi.msg, qi);
				} else {
					logger.error("Transmission of packet " + qi.msg.msgCount
							+ " failed 3 times");
				}
			}
		}
	}

	private void listenModeHandler(String data) {
		switch (BaseMsg.getMsgType(data)) {
		case WALL_THERMOSTAT_CONTROL:
			new WallThermostatControlMsg(data).printMessage();
			break;
		case TIME_INFO:
			new TimeInfoMsg(data).printMessage();
			break;
		case SET_TEMPERATURE:
			new SetTemperatureMsg(data).printMessage();
			break;
		case ACK:
			new AckMsg(data).printMessage();
			break;
		case PAIR_PING:
			new PairPingMsg(data).printMessage();
			break;
		case PAIR_PONG:
			new PairPongMsg(data).printMessage();
			break;
		case THERMOSTAT_STATE:
			new ThermostatStateMsg(data).printMessage();
			break;
		case SET_GROUP_ID:
			new SetGroupIdMsg(data).printMessage();
			break;
		case WAKEUP:
			new WakeupMsg(data).printMessage();
			break;
		case WALL_THERMOSTAT_STATE:
			new WallThermostatStateMsg(data).printMessage();
			break;
		case ADD_LINK_PARTNER:
		case CONFIG_TEMPERATURES:
		case CONFIG_VALVE:
		case CONFIG_WEEK_PROFILE:
		case PUSH_BUTTON_STATE:
		case REMOVE_GROUP_ID:
		case REMOVE_LINK_PARTNER:
		case RESET:
		case SET_COMFORT_TEMPERATURE:
		case SET_DISPLAY_ACTUAL_TEMP:
		case SET_ECO_TEMPERATURE:
		case SHUTTER_CONTACT_STATE:
		case UNKNOWN:
		default:
			BaseMsg baseMsg = new BaseMsg(data);
			baseMsg.printMessage();
			break;

		}
	}

	@Override
	public void dataReceived(String data) {
		logger.debug("MaxCulSender Received " + data);
		if (data.startsWith("Z")) {
			if (listenMode) {
				listenModeHandler(data);
				return;
			}

			/* Check message is destined for us */
			if (BaseMsg.isForUs(data, srcAddr)) {
				boolean passToBinding = true;
				/* Handle Internal Messages */
				MaxCulMsgType msgType = BaseMsg.getMsgType(data);
				if (msgType == MaxCulMsgType.ACK) {
					passToBinding = false;
					AckMsg msg = new AckMsg(data);
					if (pendingAckQueue.containsKey(msg.msgCount)
							&& msg.dstAddrStr.compareTo(srcAddr) == 0) {
						SenderQueueItem qi = pendingAckQueue
								.remove(msg.msgCount);
						/* verify ACK */
						if ((qi.msg.dstAddrStr.equalsIgnoreCase(msg.srcAddrStr))
								&& (qi.msg.srcAddrStr
										.equalsIgnoreCase(msg.dstAddrStr))) {
							if (msg.getIsNack()) {
								/* NAK'd! */
								// TODO resend?
								logger.error("Message was NAK'd, packet lost");
							} else {
								logger.debug("Message " + msg.msgCount
										+ " ACK'd ok!");
							}

						}
					} else {
						logger.info("Got ACK for message " + msg.msgCount
								+ " but it wasn't in the queue");
					}
				}

				if (sequenceRegister.containsKey(new BaseMsg(data).msgCount)) {
					passToBinding = false;
					/*
					 * message found in sequence register, so it will be handled
					 * by the sequence
					 */
					BaseMsg bMsg = new BaseMsg(data);
					logger.debug("Message "
							+ bMsg.msgCount
							+ " is part of sequence. Running next step in sequence.");
					sequenceRegister.get(bMsg.msgCount).runSequencer(bMsg);
					sequenceRegister.remove(bMsg.msgCount);
				}

				if (passToBinding) {
					/* pass data to binding for processing */
					this.mcbmp.maxCulMsgReceived(data, false);
				}
			} else if (BaseMsg.isForUs(data, "000000")) {
				switch (BaseMsg.getMsgType(data)) {
				case PAIR_PING:
				case WALL_THERMOSTAT_CONTROL:
				case THERMOSTAT_STATE:
				case WALL_THERMOSTAT_STATE:
					this.mcbmp.maxCulMsgReceived(data, true);
					break;
				default:
					/* TODO handle other broadcast */
					logger.debug("Unhandled broadcast message of type "
							+ BaseMsg.getMsgType(data).toString());
					break;

				}
			} else {
				// Associated devices send messages that tell of their status to
				// the associated
				// device. We need to spy on devices we know about to extract
				// useful data
				boolean passToBinding = false;
				BaseMsg bMsg = new BaseMsg(data);
				for (MaxCulBindingProvider provider : providers) {
					// look up source device configs
					List<MaxCulBindingConfig> configs = provider
							.getConfigsForRadioAddr(bMsg.srcAddrStr);
					if (!configs.isEmpty()) {
						// get asssociated devices with source device
						String serialNum = ((MaxCulBindingConfig) configs
								.get(0)).getSerialNumber();
						HashSet<MaxCulBindingConfig> assocDevs = provider
								.getAssociations(serialNum);
						if (!assocDevs.isEmpty()) {
							// check for matches with associated devices and the
							// message dest
							for (MaxCulBindingConfig device : assocDevs) {
								if (device.getDevAddr().equalsIgnoreCase(
										bMsg.dstAddrStr)) {
									passToBinding = true;
									break;
								}
							}
						}
					}
				}

				if (passToBinding
						&& (BaseMsg.getMsgType(data) != MaxCulMsgType.PAIR_PING && BaseMsg
								.getMsgType(data) != MaxCulMsgType.ACK)) {
					/*
					 * pass data to binding for processing - pretend it is
					 * broadcast so as not to ACK
					 */
					this.mcbmp.maxCulMsgReceived(data, true);
				}
			}
		}
	}

	@Override
	public void error(Exception e) {
		/*
		 * Ignore errors for now - not sure what I would need to handle here at
		 * the moment TODO lookup error cases
		 */
		logger.error("Received CUL Error", e);
	}

	/**
	 * Send response to PairPing as part of a message sequence
	 * 
	 * @param dstAddr
	 *            Address of device to respond to
	 * @param msgSeq
	 *            Message sequence to associate
	 */
	public void sendPairPong(String dstAddr, MessageSequencer msgSeq) {
		PairPongMsg pp = new PairPongMsg(getMessageCount(), (byte) 0, (byte) 0,
				this.srcAddr, dstAddr);
		pp.setMessageSequencer(msgSeq);
		sendMessage(pp);
	}

	public void sendPairPong(String dstAddr) {
		sendPairPong(dstAddr, null);
	}

	/**
	 * Send a wakeup message as part of a message sequence
	 * 
	 * @param dstAddr
	 *            Address of device to respond to
	 * @param msgSeq
	 *            Message sequence to associate
	 */
	public void sendWakeup(String devAddr, MessageSequencer msgSeq) {
		WakeupMsg msg = new WakeupMsg(getMessageCount(), (byte) 0x0, (byte) 0,
				this.srcAddr, devAddr);
		msg.setMessageSequencer(msgSeq);
		sendMessage(msg);
	}

	/**
	 * Send time information to device that has requested it as part of a
	 * message sequence
	 * 
	 * @param dstAddr
	 *            Address of device to respond to
	 * @param tzStr
	 *            Time Zone String
	 * @param msgSeq
	 *            Message sequence to associate
	 */
	public void sendTimeInfo(String dstAddr, String tzStr,
			MessageSequencer msgSeq) {
		TimeInfoMsg msg = new TimeInfoMsg(getMessageCount(), (byte) 0x0,
				(byte) 0, this.srcAddr, dstAddr, tzStr);
		msg.setMessageSequencer(msgSeq);
		sendMessage(msg);
	}

	/**
	 * Send time information to device in fast mode
	 * 
	 * @param dstAddr
	 *            Address of device to respond to
	 * @param tzStr
	 *            Time Zone String
	 * @param msgSeq
	 *            Message sequence to associate
	 */
	public void sendTimeInfoFast(String dstAddr, String tzStr) {
		TimeInfoMsg msg = new TimeInfoMsg(getMessageCount(), (byte) 0x0,
				(byte) 0, this.srcAddr, dstAddr, tzStr);
		msg.setFastSend(true);
		sendMessage(msg);
	}

	/**
	 * Send time information to device that has requested it
	 * 
	 * @param dstAddr
	 *            Address of device to respond to
	 * @param tzStr
	 *            Time Zone String
	 */
	public void sendTimeInfo(String dstAddr, String tzStr) {
		sendTimeInfo(dstAddr, tzStr, null);
	}

	/**
	 * Set the group ID on a device
	 * 
	 * @param devAddr
	 *            Address of device to set group ID on
	 * @param group_id
	 *            Group id to set
	 * @param msgSeq
	 *            Message sequence to associate
	 */
	public void sendSetGroupId(String devAddr, byte group_id,
			MessageSequencer msgSeq) {
		SetGroupIdMsg msg = new SetGroupIdMsg(getMessageCount(), (byte) 0x0,
				this.srcAddr, devAddr, group_id);
		msg.setMessageSequencer(msgSeq);
		sendMessage(msg);
	}

	/**
	 * Send an ACK response to a message
	 * 
	 * @param msg
	 *            Message we are acking
	 */
	public void sendAck(BaseMsg msg) {
		AckMsg ackMsg = new AckMsg(msg.msgCount, (byte) 0x0, msg.groupid,
				this.srcAddr, msg.srcAddrStr, false);
		ackMsg.setFastSend(true); // all ACKs are sent to waiting device.
		sendMessage(ackMsg);
	}

	/**
	 * Send an NACK response to a message
	 * 
	 * @param msg
	 *            Message we are nacking
	 */
	public void sendNack(BaseMsg msg) {
		AckMsg nackMsg = new AckMsg(msg.msgCount, (byte) 0x0, msg.groupid,
				this.srcAddr, msg.srcAddrStr, false);
		nackMsg.setFastSend(true); // all NACKs are sent to waiting device.
		sendMessage(nackMsg);
	}

	/**
	 * Send a set temperature message
	 * 
	 * @param devAddr
	 *            Radio addr of device
	 * @param temp
	 *            Temperature value to send
	 */
	public void sendSetTemperature(String devAddr, double temp) {
		SetTemperatureMsg msg = new SetTemperatureMsg(getMessageCount(),
				(byte) 0x0, (byte) 0x0, this.srcAddr, devAddr, temp,
				ThermostatControlMode.MANUAL);
		sendMessage(msg);
	}

	/**
	 * Send temperature configuration message
	 * 
	 * @param devAddr
	 *            Radio addr of device
	 * @param msgSeq
	 *            Message sequencer to associate with this message
	 * @param comfortTemp
	 *            comfort temperature value
	 * @param ecoTemp
	 *            Eco temperature value
	 * @param maxTemp
	 *            Maximum Temperature value
	 * @param minTemp
	 *            Minimum temperature value
	 * @param offset
	 *            Offset Temperature value
	 * @param windowOpenTemp
	 *            Window open temperature value
	 * @param windowOpenTime
	 *            Window open time value
	 */
	public void sendConfigTemperatures(String devAddr, MessageSequencer msgSeq,
			double comfortTemp, double ecoTemp, double maxTemp, double minTemp,
			double offset, double windowOpenTemp, double windowOpenTime) {
		ConfigTemperaturesMsg cfgTempMsg = new ConfigTemperaturesMsg(
				getMessageCount(), (byte) 0, (byte) 0, this.srcAddr, devAddr,
				comfortTemp, ecoTemp, maxTemp, minTemp, offset, windowOpenTemp,
				windowOpenTime);
		cfgTempMsg.setMessageSequencer(msgSeq);
		sendMessage(cfgTempMsg);
	}

	/**
	 * Link one device to another
	 * 
	 * @param devAddr
	 *            Destination device address
	 * @param msgSeq
	 *            Associated message sequencer
	 * @param partnerAddr
	 *            Radio address of partner
	 * @param devType
	 *            Type of device
	 */
	public void sendAddLinkPartner(String devAddr, MessageSequencer msgSeq,
			String partnerAddr, MaxCulDevice devType) {
		AddLinkPartnerMsg addLinkMsg = new AddLinkPartnerMsg(getMessageCount(),
				(byte) 0, (byte) 0, this.srcAddr, devAddr, partnerAddr, devType);
		addLinkMsg.setMessageSequencer(msgSeq);
		sendMessage(addLinkMsg);
	}

	/**
	 * Send a reset message to device
	 * 
	 * @param devAddr
	 *            Address of device to reset
	 */
	public void sendReset(String devAddr) {
		ResetMsg resetMsg = new ResetMsg(getMessageCount(), (byte) 0, (byte) 0,
				this.srcAddr, devAddr);
		sendMessage(resetMsg);
	}

	/**
	 * Set listen mode status. Doing this will stop proper message processing
	 * and will just turn this message handler into a snooper.
	 * 
	 * @param listenModeOn
	 *            TRUE sets listen mode to ON
	 */
	public void setListenMode(boolean listenModeOn) {
		listenMode = listenModeOn;
		logger.debug("Listen Mode is " + (listenMode ? "ON" : "OFF"));
	}

	public void startSequence(MessageSequencer ps, BaseMsg msg) {
		logger.debug("Starting sequence");
		ps.runSequencer(msg);
	}

	public int getCreditStatus() {
		return cul.getCredit10ms();
	}
}
