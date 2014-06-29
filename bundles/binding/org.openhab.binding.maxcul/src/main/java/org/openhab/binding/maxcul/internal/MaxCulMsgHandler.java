package org.openhab.binding.maxcul.internal;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.maxcul.internal.messages.AckMsg;
import org.openhab.binding.maxcul.internal.messages.BaseMsg;
import org.openhab.binding.maxcul.internal.messages.MaxCulBindingMessageProcessor;
import org.openhab.binding.maxcul.internal.messages.MaxCulMsgType;
import org.openhab.binding.maxcul.internal.messages.PairPingMsg;
import org.openhab.binding.maxcul.internal.messages.PairPongMsg;
import org.openhab.binding.maxcul.internal.messages.SetGroupIdMsg;
import org.openhab.binding.maxcul.internal.messages.SetTemperatureMsg;
import org.openhab.binding.maxcul.internal.messages.ThermostatControlMode;
import org.openhab.binding.maxcul.internal.messages.TimeInfoMsg;
import org.openhab.binding.maxcul.internal.messages.WakeupMsg;
import org.openhab.binding.maxcul.internal.messages.WallThermostatControlMsg;
import org.openhab.binding.maxcul.internal.message.sequencers.MessageSequencer;
import org.openhab.binding.maxcul.internal.message.sequencers.PairingInitialisationSequence;
import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULHandler;
import org.openhab.io.transport.cul.CULListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle messages going to and from the CUL device. Make sure to intercept control command
 * responses first before passing on valid MAX! messages to the binding itself for processing.
 *
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class MaxCulMsgHandler implements CULListener {

	private static final Logger logger =
			LoggerFactory.getLogger(MaxCulMsgHandler.class);

	class SenderQueueItem {
		BaseMsg msg;
		Date expiry;
		int retryCount = 0;
	}

	private int surplusCredit = 0;
	private Date lastTransmit;
	private Date endOfQueueTransmit;

	private int msgCount = 0;
	private CULHandler cul = null;
	private String srcAddr;
	private HashMap<Byte, MessageSequencer> sequenceRegister;
	private LinkedList<SenderQueueItem> sendQueue;
	private HashMap<Byte, SenderQueueItem> pendingAckQueue;
	private MaxCulBindingMessageProcessor mcbmp = null;
	private Map<SenderQueueItem, Timer> timers = new HashMap<SenderQueueItem,Timer>();

	private boolean listenMode = false;

	private final int MESSAGE_EXPIRY_PERIOD = 30000;

	private String tzStr;

	public MaxCulMsgHandler(String srcAddr, CULHandler cul)
	{
		this.cul = cul;
		cul.registerListener(this);
		this.srcAddr = srcAddr;
		this.sequenceRegister = new HashMap<Byte, MessageSequencer>();
		this.sendQueue = new LinkedList<SenderQueueItem>();
		this.pendingAckQueue = new HashMap<Byte, SenderQueueItem>();
		this.lastTransmit = new Date(); /* init as now */
		this.endOfQueueTransmit = this.lastTransmit;
	}

	private byte getMessageCount()
	{
		this.msgCount += 1;
		this.msgCount &= 0xFF;
		return (byte)this.msgCount;
	}

	private boolean enoughCredit(int requiredCredit, boolean fastSend)
	{
		return enoughCredit(requiredCredit, fastSend, false);
	}

	private boolean enoughCredit(int requiredCredit, boolean fastSend, boolean updateSurplus)
	{
		Date now = new Date();
		/* units are accumulated as 1% of time elapsed with no TX */
		long credit = ((now.getTime() - this.lastTransmit.getTime())/100)+this.surplusCredit;

		/* if device isn't awake we need long preamble for wakeup, otherwise we don't */
		int preambleCredit = fastSend?0:100;
		boolean result = (credit > (requiredCredit+preambleCredit));
		if (result && updateSurplus)
		{
			this.surplusCredit = (int)credit - (requiredCredit+100);
			/* accumulate a max of 1hr credit */
			if (this.surplusCredit > 360) this.surplusCredit = 360;
		}

		return result;
	}

	private void transmitMessage( BaseMsg data )
	{
		try {
			cul.send(data.rawMsg);
		} catch (CULCommunicationException e) {
			logger.error("Unable to send CUL message "+data+" because: "+e.getMessage());
		}
		/* update surplus credit value */
		boolean fastSend = false;
		if (data.isPartOfSequence())
		{
			fastSend = data.getMessageSequencer().useFastSend();
		}
		enoughCredit(data.requiredCredit(), fastSend, true);
		this.lastTransmit = new Date();
		if (this.endOfQueueTransmit.before(this.lastTransmit))
		{
			/* hit a time after the queue finished tx'ing */
			this.endOfQueueTransmit = this.lastTransmit;
		}

		if (data.msgType != MaxCulMsgType.ACK)
		{
			/* awaiting ack now */
			SenderQueueItem qi = new SenderQueueItem();
			qi.msg = data;
			qi.expiry = new Date(this.lastTransmit.getTime()+MESSAGE_EXPIRY_PERIOD);
			this.pendingAckQueue.put(qi.msg.msgCount, qi);
		}
	}

	/**
	 * Send a raw Base Message
	 * @param msg Base message to send
	 */
	public void sendMessage( BaseMsg msg )
	{
		Timer timer = null;

		if (msg.readyToSend())
		{
			if (enoughCredit(msg.requiredCredit(), msg.isFastSend()) && this.sendQueue.isEmpty())
			{
				/* send message as we have enough credit and nothing is on the queue waiting */
				logger.debug("Sending message immediately. Message is "+msg.msgType+" => "+msg.rawMsg);
        		transmitMessage(msg);
			} else {
				/* messages ahead of us so queue up the item and schedule a task to process it */
				SenderQueueItem qi = new SenderQueueItem();
				qi.msg = msg;
				TimerTask task = new TimerTask() {
	                public void run() {
	                	SenderQueueItem topItem = sendQueue.remove();
	                	logger.debug("Checking credit");
	                	if (enoughCredit(topItem.msg.requiredCredit(), topItem.msg.isFastSend()))
	                	{
	                		logger.debug("Sending item from queue. Message is "+topItem.msg.msgType+" => "+topItem.msg.rawMsg);
	                		if (topItem.msg.msgType == MaxCulMsgType.TIME_INFO)
	                		{
	                			((TimeInfoMsg) topItem.msg).updateTime();
	                		}
	                		transmitMessage(topItem.msg);
	                	} else {
	                		logger.error("Not enough credit after waiting. This is bad. Queued command is discarded");
	                	}
	                }
				};

				timer = new Timer();
				timers.put(qi, timer);
				/* calculate when we want to TX this item in the queue, with a margin of 2 credits. x1000 as we accumulate 1 x 10ms credit every 1000ms */
				this.endOfQueueTransmit = new Date(this.endOfQueueTransmit.getTime() + ((msg.requiredCredit()+2)*1000));
				timer.schedule(task, this.endOfQueueTransmit);
				this.sendQueue.add(qi);

				logger.debug("Added message to queue to be TX'd at "+this.endOfQueueTransmit.toString());
			}

			if (msg.isPartOfSequence())
			{
				/* add to sequence register if part of a sequence */
				logger.debug("Message "+msg.msgCount+" is part of sequence. Adding to register.");
				sequenceRegister.put(msg.msgCount, msg.getMessageSequencer());
			}
		} else logger.debug("Tried to send a message that wasn't ready!");
	}

	/**
	 * Associate binding processor with this message handler
	 * @param mcbmp Binding processor to associate with this message handler
	 */
	public void registerMaxCulBindingMessageProcessor( MaxCulBindingMessageProcessor mcbmp )
	{
		if (this.mcbmp == null)
		{
			this.mcbmp = mcbmp;
			logger.debug("Associated MaxCulBindingMessageProcessor");
		}
		else
			logger.error("Tried to associate a second MaxCulBindingMessageProcessor!");
	}

	public void checkPendingAcks()
	{
		Date now = new Date();

		for (SenderQueueItem qi : pendingAckQueue.values())
		{
			if (now.after(qi.expiry))
			{
				logger.error("Packet "+qi.msg.msgCount+" ("+qi.msg.msgType+") lost - timeout");
				pendingAckQueue.remove(qi.msg.msgCount); // remove from ACK queue
				if (sequenceRegister.containsKey(qi.msg.msgCount))
				{
					sequenceRegister.get(qi.msg.msgCount).packetLost(qi.msg);
					sequenceRegister.remove(qi.msg.msgCount);
				}
			}
		}
	}

	private  void listenModeHandler(String data)
	{
		switch (BaseMsg.getMsgType(data))
		{
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
		case WALL_THERMOSTAT_STATE:
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
		case SET_GROUP_ID:
		case SHUTTER_CONTACT_STATE:
		case WAKEUP:
		case THERMOSTAT_STATE:
		case UNKNOWN:
		default:
			BaseMsg baseMsg = new BaseMsg(data);
			baseMsg.printMessage();
			break;

		}
	}

	@Override
	public void dataReceived(String data) {
		logger.debug("MaxCulSender Received "+data);
		if (data.startsWith("Z"))
		{
			if (listenMode)
			{
				listenModeHandler(data);
				return;
			}
			/* Check message is destined for us */
			if (BaseMsg.isForUs(data, srcAddr))
			{
				/* Handle Internal Messages */
				MaxCulMsgType msgType = BaseMsg.getMsgType(data);
				if (msgType == MaxCulMsgType.ACK)
				{
					AckMsg msg = new AckMsg(data);
					if (pendingAckQueue.containsKey(msg.msgCount) && msg.dstAddrStr.compareTo(srcAddr) == 0)
					{
						SenderQueueItem qi = pendingAckQueue.remove(msg.msgCount);
						/* verify ACK */
						if ((qi.msg.dstAddrStr.equalsIgnoreCase(msg.srcAddrStr)) &&
								(qi.msg.srcAddrStr.equalsIgnoreCase(msg.dstAddrStr)))
								{
									if (msg.getIsNack())
									{
										/* NAK'd! */
										// TODO resend?
										logger.error("Message was NAK'd, packet lost");
									} else logger.debug("Message "+msg.msgCount+" ACK'd ok!");

								}
					} else logger.info("Got ACK for message "+msg.msgCount+" but it wasn't in the queue");

					if (sequenceRegister.containsKey(new BaseMsg(data).msgCount))
					{
						/* message found in sequence register, so it will be handled by the sequence */
						BaseMsg bMsg = new BaseMsg(data);
						logger.debug("Message "+bMsg.msgCount+" is part of sequence. Running next step in sequence.");
						sequenceRegister.get(bMsg.msgCount).runSequencer(bMsg);
					}
				}
				else if (msgType == MaxCulMsgType.TIME_INFO)
				{
					/* send latest time information */
					TimeInfoMsg msg = new TimeInfoMsg(data);
					sendTimeInfo(msg.srcAddrStr, this.tzStr);
				}
				else if (sequenceRegister.containsKey(new BaseMsg(data).msgCount))
				{
					/* message found in sequence register, so it will be handled by the sequence */
					BaseMsg bMsg = new BaseMsg(data);
					logger.debug("Message "+bMsg.msgCount+" is part of sequence. Running next step in sequence.");
					sequenceRegister.get(bMsg.msgCount).runSequencer(bMsg);
				}
				else
				{
					/* pass data to binding for processing */
					this.mcbmp.MaxCulMsgReceived(data, false);
				}
			}
			else if (BaseMsg.isForUs(data, "000000"))
			{
				switch (BaseMsg.getMsgType(data))
				{
				case PAIR_PING:
				case WALL_THERMOSTAT_CONTROL:
					this.mcbmp.MaxCulMsgReceived(data, true);
					break;
				default:
					/* TODO handle other broadcast */
					logger.debug("Unhandled broadcast message of type "+BaseMsg.getMsgType(data).toString());
					break;

				}
			}
		}
	}

	@Override
	public void error(Exception e) {
		/* Ignore errors for now - not sure what I would need to handle here at the moment
		 * TODO lookup error cases
		 */
	}

	/**
	 * Send response to PairPing as part of a message sequence
	 * @param dstAddr Address of device to respond to
	 * @param msgSeq Message sequence to associate
	 */
	public void sendPairPong(String dstAddr, MessageSequencer msgSeq)
	{
		PairPongMsg pp = new PairPongMsg(getMessageCount(), (byte)0, (byte) 0, this.srcAddr, dstAddr);
		pp.setMessageSequencer(msgSeq);
		sendMessage(pp);
	}

	public void sendPairPong(String dstAddr)
	{
		sendPairPong(dstAddr,null);
	}

	/**
	 * Send a wakeup message as part of a message sequence
	 * @param dstAddr Address of device to respond to
	 * @param msgSeq Message sequence to associate
	 */
	public void sendWakeup(String devAddr, MessageSequencer msgSeq) {
		WakeupMsg msg = new WakeupMsg(getMessageCount(), (byte)0x0, (byte) 0, this.srcAddr, devAddr);
		msg.setMessageSequencer(msgSeq);
		sendMessage(msg);
	}

	/**
	 * Send time information to device that has requested it as part of a message sequence
	 * @param dstAddr Address of device to respond to
	 * @param tzStr Time Zone String
	 * @param msgSeq Message sequence to associate
	 */
	public void sendTimeInfo(String dstAddr, String tzStr, MessageSequencer msgSeq)
	{
		TimeInfoMsg msg = new TimeInfoMsg(getMessageCount(), (byte)0x0, (byte) 0, this.srcAddr, dstAddr, tzStr);
		msg.setMessageSequencer(msgSeq);
		sendMessage(msg);
	}

	/**
	 * Send time information to device that has requested it
	 * @param dstAddr Address of device to respond to
	 * @param tzStr Time Zone String
	 */
	public void sendTimeInfo(String dstAddr, String tzStr)
	{
		sendTimeInfo(dstAddr, tzStr, null);
	}

	/**
	 * Set the group ID on a device
	 * @param devAddr Address of device to set group ID on
	 * @param group_id Group id to set
	 * @param msgSeq Message sequence to associate
	 */
	public void sendSetGroupId(String devAddr, byte group_id, MessageSequencer msgSeq) {
		SetGroupIdMsg msg = new SetGroupIdMsg(getMessageCount(), (byte)0x0, this.srcAddr, devAddr, group_id);
		msg.setMessageSequencer(msgSeq);
		sendMessage(msg);
	}

	/**
	 * Send an ACK response to a message
	 * @param msg Message we are acking
	 */
	public void sendAck(BaseMsg msg)
	{
		AckMsg ackMsg = new AckMsg(msg.msgCount, (byte) 0x0, msg.groupid, this.srcAddr, msg.srcAddrStr, false);
		ackMsg.setFastSend(true); // all ACKs are sent to waiting device.
		sendMessage(ackMsg);
	}

	/**
	 * Send an NACK response to a message
	 * @param msg Message we are nacking
	 */
	public void sendNack(BaseMsg msg)
	{
		AckMsg nackMsg = new AckMsg(msg.msgCount, (byte) 0x0, msg.groupid, this.srcAddr, msg.srcAddrStr, false);
		nackMsg.setFastSend(true); // all NACKs are sent to waiting device.
		sendMessage(nackMsg);
	}

	/**
	 * Send a set temperature message
	 * @param devAddr Radio addr of device
	 * @param temp Temperature value to send
	 */
	public void sendSetTemperature(String devAddr, double temp)
	{
		SetTemperatureMsg msg = new SetTemperatureMsg(getMessageCount(), (byte) 0x0, (byte) 0x0, this.srcAddr, devAddr, temp, ThermostatControlMode.MANUAL);
		sendMessage(msg);
	}

	/**
	 * Set listen mode status. Doing this will stop proper message processing and will
	 * just turn this message handler into a snooper.
	 * @param listenModeOn TRUE sets listen mode to ON
	 */
	public void setListenMode(boolean listenModeOn)
	{
		listenMode = listenModeOn;
		logger.debug("Listen Mode is "+(listenMode?"ON":"OFF"));
	}

	public void setTz( String tzStr )
	{
		this.tzStr = tzStr;
	}

	public void startSequence(PairingInitialisationSequence ps, BaseMsg msg)
	{
		ps.runSequencer(msg);
	}





}
