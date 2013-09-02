/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.zwave.internal.commandclass;

import java.math.BigDecimal;
import java.util.Arrays;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageClass;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessagePriority;
import org.openhab.binding.zwave.internal.protocol.SerialMessage.SerialMessageType;
import org.openhab.binding.zwave.internal.protocol.ZWaveController;
import org.openhab.binding.zwave.internal.protocol.ZWaveEndpoint;
import org.openhab.binding.zwave.internal.protocol.ZWaveEvent;
import org.openhab.binding.zwave.internal.protocol.ZWaveEvent.ZWaveEventType;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the Multi Level Sensor command class. Multi level sensors indicate their states as a 
 * value + unit. The commands include the possibility to get a given value and report a value.
 * 
 * @author Ben Jones
 * @since 1.3.0
 */
public class ZWaveMultiLevelSensorCommandClass extends ZWaveCommandClass implements ZWaveGetCommands {

	private static final Logger logger = 
		LoggerFactory.getLogger(ZWaveMultiLevelSensorCommandClass.class);

	private static final int SENSOR_MULTI_LEVEL_SUPPORTED_GET = 0x01;
	private static final int SENSOR_MULTI_LEVEL_SUPPORTED_REPORT = 0x02;
	private static final int SENSOR_MULTI_LEVEL_GET = 0x04;
	private static final int SENSOR_MULTI_LEVEL_REPORT = 0x05;

	/**
	 * Creates a new instance of the ZWaveMultiLevelSensorCommandClass class.
	 * 
	 * @param node
	 *            the node this command class belongs to
	 * @param controller
	 *            the controller to use
	 * @param endpoint
	 *            the endpoint this Command class belongs to
	 */
	public ZWaveMultiLevelSensorCommandClass(ZWaveNode node,
			ZWaveController controller, ZWaveEndpoint endpoint) {
		super(node, controller, endpoint);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CommandClass getCommandClass() {
		return CommandClass.SENSOR_MULTILEVEL;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleApplicationCommandRequest(SerialMessage serialMessage,
			int offset, int endpoint) {
		logger.trace("Handle Message Sensor Multi Level Request");
		logger.debug(String.format(
				"Received Sensor Multi Level Request for Node ID = %d", this
						.getNode().getNodeId()));
		int command = serialMessage.getMessagePayloadByte(offset);
		switch (command) {
		case SENSOR_MULTI_LEVEL_GET:
		case SENSOR_MULTI_LEVEL_SUPPORTED_GET:
		case SENSOR_MULTI_LEVEL_SUPPORTED_REPORT:
			logger.warn(String.format("Command 0x%02X not implemented.",
					command));
			return;
		case SENSOR_MULTI_LEVEL_REPORT:
			logger.trace("Process Multi Level Sensor Report");
			logger.debug(String.format(
					"Sensor Multi Level report from nodeId = %d", this
							.getNode().getNodeId()));

			// TODO: sensor type not used for anything currently
			// TODO: should extend to support filtering of sensor results based on type
			int sensorTypeCode = serialMessage.getMessagePayloadByte(offset + 1);
			logger.debug(String.format("Sensor Type = (0x%02x)", sensorTypeCode));

			byte[] valueData = Arrays.copyOfRange(
					serialMessage.getMessagePayload(), offset + 2,
					serialMessage.getMessagePayload().length);
			BigDecimal value = extractValue(valueData);
			ZWaveEvent zEvent = new ZWaveEvent(ZWaveEventType.SENSOR_EVENT,
					this.getNode().getNodeId(), endpoint, value);
			this.getController().notifyEventListeners(zEvent);
			break;
		default:
			logger.warn(String
					.format("Unsupported Command 0x%02X for command class %s (0x%02X).",
							command, this.getCommandClass().getLabel(), this
									.getCommandClass().getKey()));
		}
	}

	/**
	 * Gets a SerialMessage with the SENSOR_MULTI_LEVEL_GET command
	 * 
	 * @return the serial message
	 */
	public SerialMessage getValueMessage() {
		logger.debug(
				"Creating new message for application command SENSOR_MULTI_LEVEL_GET for node {}",
				this.getNode().getNodeId());
		SerialMessage result = new SerialMessage(this.getNode().getNodeId(),
				SerialMessageClass.SendData, SerialMessageType.Request,
				SerialMessageClass.ApplicationCommandHandler,
				SerialMessagePriority.Get);
		byte[] newPayload = { (byte) this.getNode().getNodeId(), 2,
				(byte) getCommandClass().getKey(),
				(byte) SENSOR_MULTI_LEVEL_GET };
		result.setMessagePayload(newPayload);
		return result;
	}

	private static final int SIZE_MASK = 0x07;
//	private static final SCALE_MASK = 0x18; // unused
//	private static final SCALE_SHIFT = 0x03; // unused
	private static final int PRECISION_MASK = 0xe0;
	private static final int PRECISION_SHIFT = 0x05;

	private BigDecimal extractValue(byte[] data) {
		int size = data[0] & SIZE_MASK;
		int precision = (data[0] & PRECISION_MASK) >> PRECISION_SHIFT;

		int value = 0;
		int i;
		for (i = 0; i < size; ++i) {
			value <<= 8;
			value |= data[i + 1] & 0xFF;
		}
		
		// Deal with sign extension. All values are signed
		BigDecimal result;
		if ((data[1] & 0x80) == 0x80) {

			// MSB is signed
			if (size == 1) {
				value |= 0xffffff00;
			} else if (size == 2) {
				value |= 0xffff0000;
			}
		}

		result = BigDecimal.valueOf(value);

		BigDecimal divisor = BigDecimal.valueOf(Math.pow(10, precision));
		return result.divide(divisor);
	}
	
}
