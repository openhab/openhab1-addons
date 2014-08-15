/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message class to handle desired temperature updates
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class SetTemperatureMsg extends BaseMsg {

	final static private int SET_TEMPERATURE_PAYLOAD_LEN = 1; /* in bytes */

	private static final Logger logger = LoggerFactory
			.getLogger(SetTemperatureMsg.class);

	private double desiredTemperature;
	private ThermostatControlMode ctrlMode;

	private static final double TEMPERATURE_MAX = 30.5;
	private static final double TEMPERATURE_MIN = 4.5;

	public static double TEMPERATURE_ON = 30.5;
	public static double TEMPERATURE_OFF = 4.5;

	public SetTemperatureMsg(String rawMsg) {
		super(rawMsg);
		logger.debug(this.msgType + " Payload Len -> " + this.payload.length);

		if (this.payload.length == SET_TEMPERATURE_PAYLOAD_LEN) {
			/* extract temperature information */
			desiredTemperature = (this.payload[0] & 0x3f) / 2.0;
			/* extract control mode */
			ctrlMode = ThermostatControlMode.values()[(this.payload[0] >> 6)];
		} else {
			logger.error("Got " + this.msgType
					+ " message with incorrect length!");
		}
	}

	public SetTemperatureMsg(byte msgCount, byte msgFlag, byte groupId,
			String srcAddr, String dstAddr, double temperature,
			ThermostatControlMode mode) {
		super(msgCount, msgFlag, MaxCulMsgType.SET_TEMPERATURE, groupId,
				srcAddr, dstAddr);

		desiredTemperature = temperature;
		ctrlMode = mode;

		if (temperature > TEMPERATURE_MAX)
			temperature = TEMPERATURE_MAX;
		else if (temperature < TEMPERATURE_MIN)
			temperature = TEMPERATURE_MIN;

		byte[] payload = new byte[SET_TEMPERATURE_PAYLOAD_LEN];
		payload[0] = (byte) (temperature * 2.0);
		payload[0] |= ((mode.toByte() & 0x3) << 6);
		super.appendPayload(payload);
	}

	public double getDesiredTemperature() {
		return desiredTemperature;
	}

	public ThermostatControlMode getControlMode() {
		return ctrlMode;
	}

	/**
	 * Print output as decoded fields
	 */
	@Override
	protected void printFormattedPayload() {
		logger.debug("\tDesired Temperature => " + desiredTemperature);
		logger.debug("\tControl Mode => " + ctrlMode);
	}
}
