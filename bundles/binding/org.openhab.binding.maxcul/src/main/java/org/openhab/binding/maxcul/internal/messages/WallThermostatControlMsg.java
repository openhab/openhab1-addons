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
 * Message class to handle Wall Thermostat Control messages
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class WallThermostatControlMsg extends BaseMsg {

	final static private int WALL_THERMOSTAT_CONTROL_SET_POINT_AND_MEASURED_PAYLOAD_LEN = 2; /*
																							 * in
																							 * bytes
																							 */
	final static private int WALL_THERMOSTAT_CONTROL_SET_POINT_ONLY_PAYLOAD_LEN = 1; /*
																					 * in
																					 * bytes
																					 */

	private static final Logger logger = LoggerFactory
			.getLogger(WallThermostatControlMsg.class);

	private Double desiredTemperature;
	private Double measuredTemperature;

	public WallThermostatControlMsg(String rawMsg) {
		super(rawMsg);
		logger.debug(this.msgType + " Payload Len -> " + this.payload.length);

		if (this.payload.length == WALL_THERMOSTAT_CONTROL_SET_POINT_AND_MEASURED_PAYLOAD_LEN) {
			desiredTemperature = (this.payload[0] & 0x7F) / 2.0;
			int mTemp = (this.payload[0] & 0x80);
			mTemp <<= 1;
			mTemp |= (((int) this.payload[1]) & 0xff);
			measuredTemperature = mTemp / 10.0; // temperature over 25.5 uses
												// extra bit in
												// desiredTemperature byte
		} else if (this.payload.length == WALL_THERMOSTAT_CONTROL_SET_POINT_ONLY_PAYLOAD_LEN) {
			desiredTemperature = (this.payload[0] & 0x7F) / 2.0;
			measuredTemperature = null;
		} else {
			logger.error("Got " + this.msgType
					+ " message with incorrect length!");
		}
	}

	public WallThermostatControlMsg(byte msgCount, byte msgFlag, byte groupId,
			String srcAddr, String dstAddr) {
		super(msgCount, msgFlag, MaxCulMsgType.WALL_THERMOSTAT_STATE, groupId,
				srcAddr, dstAddr);
	}

	@Override
	protected void printFormattedPayload() {
		logger.debug("\tDesired Temperature  => " + desiredTemperature);
		logger.debug("\tMeasured Temperature => " + measuredTemperature);
	}

	public Double getMeasuredTemperature() {
		return measuredTemperature;
	}

	public Double getDesiredTemperature() {
		return desiredTemperature;
	}
}
