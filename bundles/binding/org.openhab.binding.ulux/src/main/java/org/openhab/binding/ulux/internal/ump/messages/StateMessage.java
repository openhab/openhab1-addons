/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump.messages;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * Message to query the state flags.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class StateMessage extends AbstractUluxMessage {

	private boolean timeRequest;
	private boolean introActive;
	private boolean audioActive;
	private boolean displayActive;
	private boolean proximitySensor;
	private boolean lightSensor;
	private boolean initRequest;
	private boolean internalError;
	private boolean motionSensor;
	private boolean i2cTemperatureValid;
	private boolean i2cHumidityValid;

	public StateMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x04, UluxMessageId.State, actorId, data);

		final BigInteger stateFlags = BigInteger.valueOf(data.getInt());

		this.i2cHumidityValid = stateFlags.testBit(25);
		this.i2cTemperatureValid = stateFlags.testBit(24);
		this.motionSensor = stateFlags.testBit(11);
		this.internalError = stateFlags.testBit(7);
		this.initRequest = stateFlags.testBit(6);
		this.timeRequest = stateFlags.testBit(5);
		this.introActive = stateFlags.testBit(4);
		this.audioActive = stateFlags.testBit(3);
		this.displayActive = stateFlags.testBit(2);
		this.proximitySensor = stateFlags.testBit(1);
		this.lightSensor = stateFlags.testBit(0);
	}

	public boolean isInitRequest() {
		return initRequest;
	}

	public boolean isTimeRequest() {
		return timeRequest;
	}

	public boolean isDisplayActive() {
		return this.displayActive;
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		// empty data
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("i2cHumidityValid", this.i2cHumidityValid);
		builder.append("i2cTemperatureValid", this.i2cTemperatureValid);
		builder.append("motionSensor", this.motionSensor);
		builder.append("internalError", this.internalError);
		builder.append("initRequest", this.initRequest);
		builder.append("timeRequest", this.timeRequest);
		builder.append("introActive", this.introActive);
		builder.append("audioActive", this.audioActive);
		builder.append("displayActive", this.displayActive);
		builder.append("proximitySensor", this.proximitySensor);
		builder.append("lightSensor", this.lightSensor);

		return builder.toString();
	}
}
