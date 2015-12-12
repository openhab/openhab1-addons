/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.ArrayList;
import java.util.Collection;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import org.openhab.binding.zwave.internal.config.ZWaveDbCommandClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the Switch All command class. Sends all on or all off commands to
 * device
 * 
 * @author Pedro Paixao
 * @since 1.8.0
 */
@XStreamAlias(value = "switchAllCommandClass")
public class ZWaveSwitchAllCommandClass extends ZWaveCommandClass implements ZWaveCommandClassInitialization {

	@XStreamOmitField
	private static final Logger logger = LoggerFactory.getLogger(ZWaveSwitchAllCommandClass.class);

	private static final int SWITCH_ALL_SET = 0x01;
	private static final int SWITCH_ALL_GET = 0x02;
	private static final int SWITCH_ALL_REPORT = 0x03;
	private static final int SWITCH_ALL_ON = 0x04;
	private static final int SWITCH_ALL_OFF = 0x05;

	public enum SwitchAllMode {
		SWITCH_ALL_EXCLUDED(0x00, "not included in either All On or All Off groups"), SWITCH_ALL_INCLUDE_ON_ONLY(0x01,
				"device included in All On group"), SWITCH_ALL_INCLUDE_OFF_ONLY(0x02,
				"device included in All Off group"), SWITCH_ALL_INCLUDE_ON_OFF(0xFF,
				"device included in All On and All Off group");

		int mode;
		String description;

		SwitchAllMode(int mode) {
			this.mode = mode;
			this.description = "";
		}

		SwitchAllMode(int mode, String description) {
			this.mode = mode;
			this.description = description;
		}

		public int getMode() {
			return mode;
		}

		public String getDescription() {
			return description;
		}

		public static SwitchAllMode fromInteger(int m) {
			switch (m) {
			case 0x00:
				return SWITCH_ALL_EXCLUDED;
			case 0x01:
				return SWITCH_ALL_INCLUDE_ON_ONLY;
			case 0x02:
				return SWITCH_ALL_INCLUDE_OFF_ONLY;
			case 0xFF:
				return SWITCH_ALL_INCLUDE_ON_OFF;
			}

			return null;
		}
	}

	@XStreamOmitField
	private boolean initialiseDone = false;

	private boolean isGetSupported = true;
	private SwitchAllMode mode;

	/**
	 * Creates a new instance of the ZWaveSwitchAllCommandClass class.
	 * 
	 * @param node
	 *            the node this command class belongs to
	 * @param controller
	 *            the controller to use
	 * @param endpoint
	 *            the endpoint this Command class belongs to
	 */
	public ZWaveSwitchAllCommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ZWaveCommandClass.CommandClass getCommandClass() {
		return ZWaveCommandClass.CommandClass.SWITCH_ALL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage, int offset, int endpoint) {
		logger.trace("Handle Message Switch All Request");
		logger.debug(String.format("Received Switch All Request for Node ID = %d", this.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case SWITCH_ALL_SET: {
			logger.trace("Process Switch All Set");
			logger.debug("Switch All Set sent to the controller will be processed as Switch All Report");
			this.processSwitchAllReport(serialMessage, offset, endpoint);
			break;
		}
		case SWITCH_ALL_GET: {
			logger.warn("Command {} not implemented.", (Object) command);
			return;
		}
		case SWITCH_ALL_REPORT: {
			logger.trace("Process Switch All Report");
			this.processSwitchAllReport(serialMessage, offset, endpoint);
			initialiseDone = true;
			break;
		}
		default: {
			logger.warn(String.format("Unsupported Command 0x%02X for command class %s (0x%02X).", command, this
					.getCommandClass().getLabel(), this.getCommandClass().getKey()));
		}
		}
	}

	protected void processSwitchAllReport(SerialMessage serialMessage, int offset, int endpoint) {
		int m = serialMessage.getMessagePayloadByte(offset + 1);
		mode = SwitchAllMode.fromInteger(m);

		if (mode != null) {
			logger.debug("NODE {}: Switch All report, {}.", (Object) this.getNode().getNodeId(), mode.getDescription());
		} else {
			logger.debug("NODE {}: Switch All unsupported mode.", (Object) this.getNode().getNodeId());
			return;
		}

		ZWaveSwitchAllModeEvent zEvent = new ZWaveSwitchAllModeEvent(this.getNode().getNodeId(), endpoint, new Integer(
				m));
		this.getController().notifyEventListeners(zEvent);
	}

	public SerialMessage getValueMessage() {
		if (!this.isGetSupported) {
			logger.debug("NODE {}: Node doesn't support get requests", (Object) this.getNode().getNodeId());
			return null;
		}

		logger.debug("NODE {}: Creating new message for command SWITCH_ALL_GET", (Object) this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessageType.Request, SerialMessage.SerialMessageClass.ApplicationCommandHandler,
				SerialMessage.SerialMessagePriority.Get);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 2, (byte) this.getCommandClass().getKey(),
				(byte) SWITCH_ALL_GET };
		result.setMessagePayload(newPayload);
		return result;
	}

	/**
	 * Create a new SwitchAll set message
	 * 
	 * @param mode
	 *            as (0x00 - Exclude, 0x01 Only All On, 0x02 Only All Off, 0xFF
	 *            Both All on and All off)
	 * @return SerialMessage
	 */
	public SerialMessage setValueMessage(int m) {
		mode = SwitchAllMode.fromInteger(m);

		if (mode != null) {
			logger.debug("NODE {}: Switch All report, {}.", (Object) this.getNode().getNodeId(), mode.getDescription());
		} else {
			logger.debug("NODE {}: Switch All unsupported mode.", (Object) this.getNode().getNodeId());
			return null;
		}

		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessageType.Request, SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessagePriority.Set);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 3, (byte) this.getCommandClass().getKey(),
				(byte) SWITCH_ALL_SET, (byte) m };

		result.setMessagePayload(newPayload);
		return result;
	}

	/**
	 * Create the All On message
	 * 
	 * @return
	 */
	public SerialMessage allOnMessage() {
		logger.debug("NODE {}: Switch All - Creating All On message.", (Object) this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessageType.Request, SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessagePriority.Set);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 2, (byte) this.getCommandClass().getKey(),
				(byte) SWITCH_ALL_ON };
		result.setMessagePayload(newPayload);
		return result;
	}

	/**
	 * Create the All Off message
	 * 
	 * @return
	 */
	public SerialMessage allOffMessage() {
		logger.debug("NODE {}: Switch All - Creating All Off message.", (Object) this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessageType.Request, SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessagePriority.Set);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 2, (byte) this.getCommandClass().getKey(),
				(byte) SWITCH_ALL_OFF };
		result.setMessagePayload(newPayload);
		return result;
	}

	@Override
	public boolean setOptions(ZWaveDbCommandClass options) {
		if (options.isGetSupported != null) {
			this.isGetSupported = options.isGetSupported;
		}
		return true;
	}

	/**
	 * get the Switch All mode
	 * 
	 * @return the mode
	 */
	public SwitchAllMode getMode() {
		return mode;
	}

	/**
	 * ZWave Switch All mode received event. Sent from the Switch All Command
	 * Class to the binding when the switch all mode is received.
	 * 
	 * @author Pedro Paixao
	 * @since 1.8.0
	 */
	public class ZWaveSwitchAllModeEvent extends ZWaveCommandClassValueEvent {

		/**
		 * Constructor. Creates a new instance of the ZWaveSwitchAllModeEvent
		 * class.
		 * 
		 * @param nodeId
		 *            the nodeId of the event
		 */
		public ZWaveSwitchAllModeEvent(int nodeId, int endpoint, Integer mode) {
			super(nodeId, endpoint, CommandClass.SWITCH_ALL, mode);
		}

		/**
		 * Returns the switch all mode that was received as event.
		 * 
		 * @return the mode.
		 */
		public Integer getParameter() {
			return (Integer) this.getValue();
		}
	}

	@Override
	public Collection<SerialMessage> initialize(boolean refresh) {
		ArrayList<SerialMessage> result = new ArrayList<SerialMessage>();
		// If we're already initialized, then don't do it again unless we're
		// refreshing
		if (refresh == true || initialiseDone == false) {
			result.add(this.getValueMessage());
		}

		return result;
	}
}
