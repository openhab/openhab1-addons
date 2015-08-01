/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol.commandclass.proprietary;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.binding.zwave.internal.protocol.event.ZWaveCommandClassValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Have a look at: http://forum.micasaverde.com/index.php?topic=24050.0
 *
 * @author wenzel
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class FibaroFGRM222CommandClass extends ZWaveCommandClass {

	private static final Logger logger = LoggerFactory.getLogger(FibaroFGRM222CommandClass.class);
	private static final int blindOffset = 5;
	private static final int lamellaTiltOffset = 6;

	public FibaroFGRM222CommandClass(ZWaveNode node, ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	@Override
	public CommandClass getCommandClass() {
		return CommandClass.FIBARO_FGRM_222;
	}

	@Override
	public void handleApplicationCommandRequest(final SerialMessage serialMessage, final int offset, final int endpoint) {
		logger.debug("NODE {}: handleApplicationCommandRequest: {}", this.getNode().getNodeId(),
				serialMessage.toString());

		int blindValue = serialMessage.getMessagePayloadByte(offset + blindOffset);
		int lamellaTiltValue = serialMessage.getMessagePayloadByte(offset + lamellaTiltOffset);

		logger.debug("NODE {}: Blind Value: {}", this.getNode().getNodeId(), blindValue);
		logger.debug("NODE {}: Lamella Tilt Value: {}", this.getNode().getNodeId(), lamellaTiltValue);

		FibaroFGRM222ValueEvent shutterEvent = new FibaroFGRM222ValueEvent(this.getNode().getNodeId(), endpoint,
				FibaroFGRM222ValueType.Shutter, blindValue);
		this.getController().notifyEventListeners(shutterEvent);
		FibaroFGRM222ValueEvent lamellaEvent = new FibaroFGRM222ValueEvent(this.getNode().getNodeId(), endpoint,
				FibaroFGRM222ValueType.Lamella, lamellaTiltValue);
		this.getController().notifyEventListeners(lamellaEvent);
	}

	public SerialMessage setValueMessage(final int level, final String type) {
		logger.debug("NODE {}: Creating new message for application command FIBARO FGRM 222 set. type: {}. level {}.",
				this.getNode().getNodeId(), type, level);
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessageType.Request, SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessagePriority.Set);
		byte[] newPayload;
		if (type.equalsIgnoreCase(FibaroFGRM222ValueType.Shutter.name())) {
			newPayload = new byte[] { (byte) this.getNode().getNodeId(), // Node ID of Target Node
					(byte) 8, // Number of payload Bytes following
					(byte) 0x91, // 4 Magic Fibaro Bytes.
					(byte) 0x1, (byte) 0xF, (byte) 0x26, (byte) 1, // set blind % (1 --> set, 2 ? , 3 report
					(byte) 2, // set lamella
					(byte) level, // blind level
					(byte) 0 // lamella level
			};
		} else {
			newPayload = new byte[] { (byte) this.getNode().getNodeId(), (byte) 8, (byte) 0x91, (byte) 0x1, (byte) 0xF,
					(byte) 0x26, (byte) 1, // set blind % (1 --> set, 2 ? , 3 report
					(byte) 1, // set lamella
					(byte) 0, // blind level
					(byte) level // lamella level
			};
		}
		result.setMessagePayload(newPayload);
		return result;
	}

	/**
	 * Gets a SerialMessage with the SWITCH_MULTILEVEL_STOP_LEVEL_CHANGE command
	 *
	 * @return the serial message
	 */
	public SerialMessage stopLevelChangeMessage(final String type) {
		// logger.debug("Creating new stop message for application command FIBARO FGRM 222 set for node {}. type: {}. level {}.",
		// this.getNode().getNodeId(), type);
		// SerialMessage result = new SerialMessage(this.getNode().getNodeId(),
		// SerialMessage.SerialMessageClass.SendData, SerialMessage.SerialMessageType.Request,
		// SerialMessage.SerialMessageClass.SendData, SerialMessage.SerialMessagePriority.Set);
		// byte[] newPayload;
		// if (type.equalsIgnoreCase(FibaroFGRM222ValueType.Shutter.name())) {
		// newPayload = new byte[]{
		// (byte) this.getNode().getNodeId(),
		// (byte) 8,
		// (byte) -111, // 0x91 is -111 in java because of signed und unsigned byte bullshit...
		// (byte) 1,
		// (byte) 15,
		// (byte) 38,
		// (byte) 1, // set blind % (1 --> set, 2 ? , 3 report
		// (byte) 3, // set lamelle
		// (byte) 0, // blind level
		// (byte) 0 // lamella level
		// };
		// } else {
		// newPayload = new byte[]{
		// (byte) this.getNode().getNodeId(),
		// (byte) 8,
		// (byte) -111,
		// (byte) 1,
		// (byte) 15,
		// (byte) 38,
		// (byte) 3, // set blind % (1 --> set, 2 ? , 3 report
		// (byte) 1, // set lamelle
		// (byte) 0, // blind level
		// (byte) 0 // lamella level
		// };
		// }
		// result.setMessagePayload(newPayload);
		// return result;
		logger.debug("NODE {}: Creating new message for application command SWITCH_MULTILEVEL_STOP_LEVEL_CHANGE", this
				.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(), SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessageType.Request, SerialMessage.SerialMessageClass.SendData,
				SerialMessage.SerialMessagePriority.Set);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 2, (byte) CommandClass.SWITCH_MULTILEVEL.getKey(),
				(byte) 0x05 };
		result.setMessagePayload(newPayload);
		return result;
	}

	public enum FibaroFGRM222ValueType {

		Shutter, Lamella;
	}

	public class FibaroFGRM222ValueEvent extends ZWaveCommandClassValueEvent {

		private final FibaroFGRM222ValueType sensorType;

		/**
		 * Constructor. Creates a instance of the ZWaveBinarySensorValueEvent
		 * class.
		 *
		 * @param nodeId the nodeId of the event
		 * @param endpoint the endpoint of the event.
		 * @param sensorType the sensor type that triggered the event;
		 * @param value the value for the event.
		 */
		private FibaroFGRM222ValueEvent(int nodeId, int endpoint, FibaroFGRM222ValueType sensorType, Object value) {
			super(nodeId, endpoint, CommandClass.FIBARO_FGRM_222, value);
			this.sensorType = sensorType;
		}

		/**
		 * Gets the sensor type for this value event.
		 *
		 * @return
		 */
		public FibaroFGRM222ValueType getSensorType() {
			return sensorType;
		}
	}
}