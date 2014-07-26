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
 * Send temperature configuration to the device, setting comfort, eco, max, min,
 * measurement offset and window parameters.
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class ConfigTemperaturesMsg extends BaseMsg {

	final static private int CONFIG_TEMPERATURES_PAYLOAD_LEN = 7; /* in bytes */

	private static final Logger logger = LoggerFactory
			.getLogger(ConfigTemperaturesMsg.class);

	public static final double DEFAULT_COMFORT_TEMP = 21.0;
	public static final double DEFAULT_ECO_TEMP = 17.0;
	public static final double DEFAULT_MAX_TEMP = 30.5;
	public static final double DEFAULT_MIN_TEMP = 4.5;
	public static final double DEFAULT_OFFSET = 0.0;
	public static final double DEFAULT_WINDOW_OPEN_TEMP = 4.5; // OFF
	public static final double DEFAULT_WINDOW_OPEN_TIME = 0; // OFF

	private double comfortTemp = 21.0;
	private double ecoTemp = 17.0;
	private double maxTemp = 30.5;
	private double minTemp = 4.5;
	private double offset = 0.0;
	private double windowOpenTemp = 4.5; // off
	private double windowOpenTime = 0;

	public ConfigTemperaturesMsg(String rawMsg) {
		super(rawMsg);
		logger.debug(this.msgType + " Payload Len -> " + this.payload.length);

		if (this.payload.length == CONFIG_TEMPERATURES_PAYLOAD_LEN) {
			this.comfortTemp = payload[0] / 2.0;
			this.ecoTemp = payload[1] / 2.0;
			this.maxTemp = payload[2] / 2.0;
			this.minTemp = payload[3] / 2.0;
			this.offset = (payload[4] / 2.0) - 3.5;
			this.windowOpenTemp = payload[5] / 2.0;
			this.windowOpenTime = payload[6] * 5.0;
		} else {
			logger.error("Got " + this.msgType
					+ " message with incorrect length!");
		}
	}

	private byte[] buildPayload() {
		byte[] payload = new byte[CONFIG_TEMPERATURES_PAYLOAD_LEN];
		payload[0] = (byte) (comfortTemp * 2.0);
		payload[1] = (byte) (ecoTemp * 2.0);
		payload[2] = (byte) (maxTemp * 2.0);
		payload[3] = (byte) (minTemp * 2.0);

		if (offset < -3.5)
			offset = -3.5; // cap offset
		payload[4] = (byte) ((offset + 3.5) * 2.0);
		payload[5] = (byte) (windowOpenTemp * 2.0);
		payload[6] = (byte) (windowOpenTime / 5.0);

		return payload;
	}

	/**
	 * Construct with default values
	 */
	public ConfigTemperaturesMsg(byte msgCount, byte msgFlag, byte groupId,
			String srcAddr, String dstAddr) {
		super(msgCount, msgFlag, MaxCulMsgType.CONFIG_TEMPERATURES, groupId,
				srcAddr, dstAddr);
		super.appendPayload(buildPayload());
	}

	public ConfigTemperaturesMsg(byte msgCount, byte msgFlag, byte groupId,
			String srcAddr, String dstAddr, double comfortTemp, double ecoTemp,
			double maxTemp, double minTemp, double offset,
			double windowOpenTemp, double windowOpenTime) {
		super(msgCount, msgFlag, MaxCulMsgType.CONFIG_TEMPERATURES, groupId,
				srcAddr, dstAddr);

		this.comfortTemp = comfortTemp;
		this.ecoTemp = ecoTemp;
		this.maxTemp = maxTemp;
		this.minTemp = minTemp;
		this.offset = offset;
		this.windowOpenTemp = windowOpenTemp;
		this.windowOpenTime = windowOpenTime;

		super.appendPayload(buildPayload());
	}

	public double getComfortTemp() {
		return comfortTemp;
	}

	public double getEcoTemp() {
		return ecoTemp;
	}

	public double getMaxTemp() {
		return maxTemp;
	}

	public double getMinTemp() {
		return minTemp;
	}

	public double getOffset() {
		return offset;
	}

	public double getWindowOpenTemp() {
		return windowOpenTemp;
	}

	public double getWindowOpenTime() {
		return windowOpenTime;
	}
}
