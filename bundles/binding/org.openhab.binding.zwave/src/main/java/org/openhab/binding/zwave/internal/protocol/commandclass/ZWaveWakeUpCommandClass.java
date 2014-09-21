/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;

import org.openhab.binding.zwave.internal.protocol.NodeStage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveEventListener;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveTransactionCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * Wake Up Command Class. Enables a node to notify another device
 * that it woke up and is ready to receive commands.
 * @author Jan-Willem Spuij
 * @author Chris Jackson
 * @since 1.3.0
 */
@XStreamAlias("WakeUpCommandClass")
public class ZWaveWakeUpCommandClass extends ZWaveCommandClass implements ZWaveCommandClassInitialization, ZWaveEventListener {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveWakeUpCommandClass.class);
	private static final int MAX_SUPPORTED_VERSION = 2;

	public static final int WAKE_UP_INTERVAL_SET = 0x04;
	public static final int WAKE_UP_INTERVAL_GET = 0x05;
	public static final int WAKE_UP_INTERVAL_REPORT = 0x06;
	public static final int WAKE_UP_NOTIFICATION = 0x07;
	public static final int WAKE_UP_NO_MORE_INFORMATION = 0x08;
	public static final int WAKE_UP_INTERVAL_CAPABILITIES_GET = 0x09;
	public static final int WAKE_UP_INTERVAL_CAPABILITIES_REPORT = 0x0A;

	private static final int MAX_BUFFFER_SIZE = 128;

	@XStreamOmitField
	private ArrayBlockingQueue<SerialMessage> wakeUpQueue;
	
	private int targetNodeId = 0;
	private int interval = 0;
	
	private int minInterval = 0;
	private int maxInterval = 0;
	private int defaultInterval = 0;
	private int intervalStep = 0;
	
	@XStreamOmitField
	private volatile boolean isAwake = false;
	
	@XStreamOmitField
	private boolean initializationComplete = false;
	
	@XStreamOmitField
	private Timer timer = null;
	@XStreamOmitField
	private TimerTask timerTask = null;

	
	/**
	 * Creates a new instance of the ZWaveWakeUpCommandClass class.
	 * @param node the node this command class belongs to
	 * @param controller the controller to use
	 * @param endpoint the endpoint this Command class belongs to
	 */
	public ZWaveWakeUpCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
		wakeUpQueue = new ArrayBlockingQueue<SerialMessage>(MAX_BUFFFER_SIZE, true);
		
		timer = new Timer();
	}
	
	/**
	 * Resolves uninitialized fields after XML Deserialization.
	 *
	 * @return The current {@link ZWaveWakeUpCommandClass} instance.
	 */
	private Object readResolve() {
		wakeUpQueue = new ArrayBlockingQueue<SerialMessage>(MAX_BUFFFER_SIZE, true);
		timer = new Timer();
		return this;
	}	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.WAKE_UP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getMaxVersion() {
		return MAX_SUPPORTED_VERSION;
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Wake Up Request");
		logger.debug("NODE {}: Received Wake Up Request", this.getNode().getNodeId());
		int command = serialMessage.getMessagePayloadByte(offset);

		switch (command) {
			case WAKE_UP_INTERVAL_SET:
			case WAKE_UP_INTERVAL_GET:
			case WAKE_UP_INTERVAL_CAPABILITIES_GET:
			case WAKE_UP_NO_MORE_INFORMATION:
				logger.warn(String.format("Command 0x%02X not implemented.", command));
				return;
			case WAKE_UP_INTERVAL_REPORT:
				logger.trace("Process Wake Up Interval");
				
				// according to open-zwave: it seems that some devices send incorrect interval report messages. Don't know if they are spurious.
				// if not we should advance the node stage.
                if(serialMessage.getMessagePayload().length < offset + 4) {
                		logger.error("NODE {}: Unusual response: WAKE_UP_INTERVAL_REPORT with length = {}. Ignored.", this.getNode().getNodeId(), serialMessage.getMessagePayload().length);
                		return;
                }
                
                targetNodeId = serialMessage.getMessagePayloadByte(offset +4);
                int receivedInterval = ((serialMessage.getMessagePayloadByte(offset + 1)) << 16) | ((serialMessage.getMessagePayloadByte(offset + 2)) << 8) | (serialMessage.getMessagePayloadByte(offset + 3));
				logger.debug(String.format("NODE %d: Wake up interval report, value = %d seconds, targetNodeId = %d", this.getNode().getNodeId(), receivedInterval, targetNodeId));
                
				this.interval = receivedInterval;
				logger.debug("NODE {}: Wake up interval set", this.getNode().getNodeId());
				
				this.initializationComplete = true;
				ZWaveWakeUpEvent event = new ZWaveWakeUpEvent(getNode().getNodeId(), WAKE_UP_INTERVAL_REPORT);
				this.getController().notifyEventListeners(event);

				this.getNode().advanceNodeStage(NodeStage.DYNAMIC);
				break;
			case WAKE_UP_INTERVAL_CAPABILITIES_REPORT:
				logger.trace("Process Wake Up Interval Capabilities");
				
                this.minInterval = ((serialMessage.getMessagePayloadByte(offset + 1)) << 16) | ((serialMessage.getMessagePayloadByte(offset + 2)) << 8) | (serialMessage.getMessagePayloadByte(offset + 3));
                this.maxInterval = ((serialMessage.getMessagePayloadByte(offset + 4)) << 16) | ((serialMessage.getMessagePayloadByte(offset + 5)) << 8) | (serialMessage.getMessagePayloadByte(offset + 6));
                this.defaultInterval = ((serialMessage.getMessagePayloadByte(offset + 7)) << 16) | ((serialMessage.getMessagePayloadByte(offset + 8)) << 8) | (serialMessage.getMessagePayloadByte(offset + 9));
                this.intervalStep = ((serialMessage.getMessagePayloadByte(offset + 10)) << 16) | ((serialMessage.getMessagePayloadByte(offset + 11)) << 8) | (serialMessage.getMessagePayloadByte(offset + 12));
				
				logger.debug("NODE {}: Wake up interval capabilities report", this.getNode().getNodeId());
				logger.debug("NODE {}: Minimum interval = {}", this.getNode().getNodeId(), this.minInterval);
				logger.debug("NODE {}: Maximum interval = {}", this.getNode().getNodeId(), this.maxInterval);
				logger.debug("NODE {}: Default interval = {}", this.getNode().getNodeId(), this.defaultInterval);
				logger.debug("NODE {}: Interval step = {}", this.getNode().getNodeId(), this.intervalStep);
                
				this.initializationComplete = true;
				this.getNode().advanceNodeStage(NodeStage.DYNAMIC);
				break;
			case WAKE_UP_NOTIFICATION:
				logger.trace("Process Wake Up Notification");
				
				logger.debug("NODE {}: is awake", this.getNode().getNodeId());
				serialMessage.setTransActionCanceled(true);

				// if this node has not gone through it's query stages yet, and there
				// are no initialization packets on the wake-up queue, restart initialization.
				if (!this.initializationComplete && (this.wakeUpQueue.isEmpty() || this.getNode().getNodeStage() == NodeStage.DEAD)) {
					logger.info("NODE {}: Got Wake Up Notification from node, continuing initialization.", this.getNode().getNodeId());
					
					this.getNode().setNodeStage(NodeStage.WAKEUP);
					this.getNode().advanceNodeStage(NodeStage.DETAILS);
				}

				// Set the awake flag. This will also empty the queue
				this.setAwake(true);
				break;
			default:
				logger.warn(String.format("NODE %d: Unsupported Command 0x%02X for command class %s (0x%02X).", 
					this.getNode().getNodeId(),
					command, 
					this.getCommandClass().getLabel(),
					this.getCommandClass().getKey()));
		}
	}
	
	/**
	 * Gets a SerialMessage with the WAKE_UP_NO_MORE_INFORMATION command.
	 * @return the serial message
	 */
	public SerialMessage getNoMoreInformationMessage() {
		logger.debug("NODE {}: Creating new message for application command WAKE_UP_NO_MORE_INFORMATION", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.SendData, SerialMessagePriority.Low);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) WAKE_UP_NO_MORE_INFORMATION };
    	result.setMessagePayload(newPayload);

    	return result;
	}
	
	/**
	 * If the device is awake, it returns true to indicate that this message can be sent
	 * immediately. If the device is not awake, it puts the message in the wake-up queue
	 * to send the message on next wake-up.
	 * The message is only added if it's not the WAKE_UP_NO_MORE_INFORMATION message
	 * since we don't want to send this at the next wakeup.
	 * This combines the previous 'putInWakeUpQueue' with 'isAlive'.
	 * @param serialMessage the message to put in the wake-up queue.
	 * @return true if the message can be sent immediately
	 */
	public boolean processOutgoingWakeupMessage(SerialMessage serialMessage) {
		// The message is Ok, if we're awake, send it now...
		if(isAwake) {
			return true;
		}

		// Make sure we never add the WAKE_UP_NO_MORE_INFORMATION message to the queue
		if (serialMessage.getMessagePayload().length >= 2 && serialMessage.getMessagePayload()[2] == (byte) WAKE_UP_NO_MORE_INFORMATION) {
			logger.debug("NODE {}: Last MSG not queuing.", this.getNode().getNodeId());
			return false;
		}
		if (this.wakeUpQueue.contains(serialMessage)) {
			logger.debug("NODE {}: Message already on the wake-up queue. Removing original.", this.getNode().getNodeId());
			this.wakeUpQueue.remove(serialMessage);
		}

		logger.debug("NODE {}: Putting message in wakeup queue.", this.getNode().getNodeId());
		this.wakeUpQueue.add(serialMessage);
		
		// This message has been queued - don't send it now...
		return false;
	}
	
	/**
	 * Gets a SerialMessage with the WAKE UP INTERVAL GET command 
	 * @return the serial message
	 */
	public SerialMessage getIntervalMessage() {
		logger.debug("NODE {}: Creating new message for application command WAKE_UP_INTERVAL_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) WAKE_UP_INTERVAL_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * Gets a SerialMessage with the WAKE UP INTERVAL CAPABILITIES GET command 
	 * @return the serial message
	 */
	public SerialMessage getIntervalCapabilitiesMessage() {
		logger.debug("NODE {}: Creating new message for application command WAKE_UP_INTERVAL_CAPABILITIES_GET", this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							2, 
								(byte) getCommandClass().getKey(), 
								(byte) WAKE_UP_INTERVAL_CAPABILITIES_GET };
    	result.setMessagePayload(newPayload);
    	return result;		
	}
	
	/**
	 * Returns the interval at which the device wakes up every time.
	 * @return the interval in seconds.
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * Returns the minimum wake up interval that can be set to the device.
	 * @return the minInterval in seconds.
	 */
	public int getMinInterval() {
		return minInterval;
	}

	/**
	 * Returns the maximum wake up interval that can be set to the device.
	 * @return the maxInterval in seconds.
	 */
	public int getMaxInterval() {
		return maxInterval;
	}

	/**
	 * Returns the factory default wake up interval for the device.
	 * @return the defaultInterval in seconds.
	 */
	public int getDefaultInterval() {
		return defaultInterval;
	}

	/**
	 * Returns the minimum step that must be taken into account when setting the interval for the device.
	 * @return the intervalStep in seconds.
	 */
	public int getIntervalStep() {
		return intervalStep;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SerialMessage> initialize() {
		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>(2);
		result.add(this.getIntervalMessage()); // get wake up interval.
		if (this.getVersion() > 1)
			result.add(this.getIntervalCapabilitiesMessage()); // get default values for wake up interval.
		return result;
	}

	/**
	 * Event handler for incoming Z-Wave events. We monitor Z-Wave events for completed
	 * transactions. Once a transaction is completed for the WAKE_UP_NO_MORE_INFORMATION
	 * event, we set the node state to asleep.
	 * {@inheritDoc}
	 */
	@Override
	public void ZWaveIncomingEvent(ZWaveEvent event) {
		if (!(event instanceof ZWaveTransactionCompletedEvent))
			return;
		
		SerialMessage serialMessage = ((ZWaveTransactionCompletedEvent)event).getCompletedMessage();
		
		if (serialMessage.getMessageClass() != SerialMessageClass.SendData && serialMessage.getMessageType() != SerialMessageType.Request)
			return;
				
		byte[] payload = serialMessage.getMessagePayload();
		
		// Check if it's addressed to this node
		if (payload.length == 0 || (payload[0] & 0xFF) != this.getNode().getNodeId())
			return;

		// We now know that this is a message to this node.
		// If it's not the WAKE_UP_NO_MORE_INFORMATION, then we need to set the wakeup timer
		if (payload.length >= 4 && 
				(payload[2] & 0xFF) == this.getCommandClass().getKey() &&
				(payload[3] & 0xFF) == WAKE_UP_NO_MORE_INFORMATION) {
			// This is confirmation of our 'go to sleep' message
			logger.debug("NODE {}: Went to sleep", this.getNode().getNodeId());
			this.setAwake(false);
			return;
		}
		
		// Send the next message in the wake-up queue
		if (!this.wakeUpQueue.isEmpty()) {
			serialMessage = this.wakeUpQueue.poll();
			this.getController().sendData(serialMessage);
		}
		else if(isAwake() == true){
			// No more messages in the queue.
			// Start a timer to send the "Go To Sleep" message
			// This gives other tasks some time to do something if they want
			setSleepTimer();
		}
	}

	/**
	 * Returns whether the node is awake.
	 * @return the isAwake
	 */
	public boolean isAwake() {
		return isAwake;
	}

	/**
	 * Sets whether the node is awake.
	 * If the node is awake we send the first message in the wake-up queue.
	 * The remaining messages are triggered within the notification handler  
	 * @param isAwake the isAwake to set
	 */
	public void setAwake(boolean isAwake) {
		this.isAwake = isAwake;
		
		if(isAwake) {
			ZWaveWakeUpEvent event = new ZWaveWakeUpEvent(getNode().getNodeId(), WAKE_UP_NOTIFICATION);
			this.getController().notifyEventListeners(event);
			
			logger.debug("NODE {}: Is awake with {} messages in the wake-up queue.", this.getNode().getNodeId(), this.wakeUpQueue.size());

			// Handle the wake-up queue for this node.
			// We send the first message, and when that's ACKed, we sent the next
			if (!this.wakeUpQueue.isEmpty()) {
				SerialMessage serialMessage = this.wakeUpQueue.poll();
				this.getController().sendData(serialMessage);
			}
			else {
				// No messages in the queue.
				// Start a timer to send the "Go To Sleep" message
				// This gives other tasks some time to do something if they want
				setSleepTimer();
			}
		}
	}

	/**
	 * Sends a command to the device to set the wakeup interval.
	 * The wakeup node is set to the controller.
	 * @param interval the wakeup interval in seconds
	 * @return the serial message
	 * @author Chris Jackson
	 */
	public SerialMessage setInterval(int interval) {
		logger.debug("NODE {}: Creating new message for application command WAKE_UP_INTERVAL_SET to {}", this.getNode().getNodeId(), interval);
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessageClass.SendData, SerialMessageType.Request, SerialMessageClass.ApplicationCommandHandler, SerialMessagePriority.Get);
    	byte[] newPayload = { 	(byte) this.getNode().getNodeId(), 
    							6, 
								(byte) getCommandClass().getKey(), 
								(byte) WAKE_UP_INTERVAL_SET,
								(byte)(( interval >> 16 ) & 0xff),
				                (byte)(( interval >> 8 ) & 0xff),
				                (byte)( interval & 0xff ),
				                (byte) getController().getOwnNodeId()};
    	result.setMessagePayload(newPayload);
    	return result;		
	}

	/**
	 * Gets the size of the wake up queue
	 * @return number of messages currently queued
	 */
	public int getWakeupQueueLength() {
		return wakeUpQueue.size();
	}
	
	/**
	 * Gets the target node for the Wakeup command class
	 * @return wakeup target node id
	 */
	public int getTargetNodeId() {
		return targetNodeId;
	}

	// The following timer implements a re-triggerable timer. The timer is triggered
	// when there are no more messages to be sent in the wake-up queue. When the timer
	// times out it will send the 'Go To Sleep' message to the node.
	// The timer just provides some time for anything further to be sent as
	// a result of any processing.
	private class WakeupTimerTask extends TimerTask {
		ZWaveWakeUpCommandClass wakeup;

		WakeupTimerTask(ZWaveWakeUpCommandClass wakeup) {
			this.wakeup = wakeup;
		}

		@Override
		public void run() {
			if(!wakeup.isAwake()) {
				logger.debug("NODE {}: Already asleep", wakeup.getNode().getNodeId());
				return;
			}
			// Tell the device to back to sleep.
			logger.debug("NODE {}: No more messages, go back to sleep", wakeup.getNode().getNodeId());
			wakeup.getController().sendData(wakeup.getNoMoreInformationMessage());
		}
	}
	
	public synchronized void setSleepTimer() {
		// Stop any existing timer
		if(timerTask != null) {
			timerTask.cancel();
		}

		// Create the timer task
		timerTask = new WakeupTimerTask(this);

		// Start the timer
		timer.schedule(timerTask, 2000);
	}
	
	/**
	 * ZWave wake-up event.
	 * Notifies users that a device has woken up or changed its wakeup parameters
	 * 
	 * @author Chris Jackson
	 * @since 1.5.0
	 */
	public class ZWaveWakeUpEvent extends ZWaveEvent {
		private final int event;

		/**
		 * Constructor. Creates a new instance of the ZWaveWakeUpEvent
		 * class.
		 * @param nodeId the nodeId of the event.
		 */
		public ZWaveWakeUpEvent(int nodeId, int event) {
			super(nodeId, 1);
			this.event = event;
		}
		
		public int getEvent() {
			return this.event;
		}
	}
}
