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
 * Message to query or set the control flags.
 * 
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class ControlMessage extends AbstractUluxMessage {

	public static enum BackgroundLight {
		AUTO_DAY, AUTO_NIGHT, ON, OFF
	}

	public static enum LockMode {
		NONE, NAVIGATION, ALL, ALL_LOGO
	}

	private boolean i2cPlugAndPlay;
	private boolean i2cHumidityChangeRequest;
	private boolean i2cTemperatureChangeRequest;
	private BackgroundLight backgroundLight;
	private LockMode lockMode;
	private boolean motionSensorChangeRequest;
	private boolean keepAlive;
	private boolean changeFilter;
	private boolean frameAcknowledgement;
	private boolean volumeChangeRequest;
	private boolean pageChangeRequest;
	private boolean audioActiveChangeRequest;
	private boolean displayActiveChangeRequest;
	private boolean proximitySensorChangeRequest;
	private boolean lightSensorChangeRequest;

	public ControlMessage() {
		super((byte) 0x08, UluxMessageId.Control);
		this.volumeChangeRequest = true;
		this.pageChangeRequest = true;
		this.audioActiveChangeRequest = true;
		this.displayActiveChangeRequest = true;
		this.proximitySensorChangeRequest = true;
		this.lightSensorChangeRequest = true;
	}

	public ControlMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x08, UluxMessageId.Control, actorId, data);

		parse(data);
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		BigInteger controlFlags = BigInteger.valueOf(0);

		// TODO this.lockMode
		// TODO this.backgroundLight

		if (this.i2cPlugAndPlay) {
			controlFlags = controlFlags.setBit(31);
		}
		if (this.i2cHumidityChangeRequest) {
			controlFlags = controlFlags.setBit(25);
		}
		if (this.i2cTemperatureChangeRequest) {
			controlFlags = controlFlags.setBit(24);
		}
		if (this.motionSensorChangeRequest) {
			controlFlags = controlFlags.setBit(11);
		}
		if (this.keepAlive) {
			controlFlags = controlFlags.setBit(10);
		}
		if (this.changeFilter) {
			controlFlags = controlFlags.setBit(9);
		}
		if (this.frameAcknowledgement) {
			controlFlags = controlFlags.setBit(8);
		}
		if (this.volumeChangeRequest) {
			controlFlags = controlFlags.setBit(5);
		}
		if (this.pageChangeRequest) {
			controlFlags = controlFlags.setBit(4);
		}
		if (this.audioActiveChangeRequest) {
			controlFlags = controlFlags.setBit(3);
		}
		if (this.displayActiveChangeRequest) {
			controlFlags = controlFlags.setBit(2);
		}
		if (this.proximitySensorChangeRequest) {
			controlFlags = controlFlags.setBit(1);
		}
		if (this.lightSensorChangeRequest) {
			controlFlags = controlFlags.setBit(0);
		}

		buffer.putInt(controlFlags.intValue());
	}

	private void parse(final ByteBuffer data) {
		final BigInteger controlFlags = BigInteger.valueOf(data.getInt());

		this.i2cPlugAndPlay = controlFlags.testBit(31);
		this.i2cHumidityChangeRequest = controlFlags.testBit(25);
		this.i2cTemperatureChangeRequest = controlFlags.testBit(24);
		this.motionSensorChangeRequest = controlFlags.testBit(11);
		this.keepAlive = controlFlags.testBit(10);
		this.changeFilter = controlFlags.testBit(9);
		this.frameAcknowledgement = controlFlags.testBit(8);
		this.volumeChangeRequest = controlFlags.testBit(5);
		this.pageChangeRequest = controlFlags.testBit(4);
		this.audioActiveChangeRequest = controlFlags.testBit(3);
		this.displayActiveChangeRequest = controlFlags.testBit(2);
		this.proximitySensorChangeRequest = controlFlags.testBit(1);
		this.lightSensorChangeRequest = controlFlags.testBit(0);

		boolean lockMode0 = controlFlags.testBit(12);
		boolean lockMode1 = controlFlags.testBit(13);
		if (lockMode0) {
			if (lockMode1) {
				this.lockMode = LockMode.ALL_LOGO;
			} else {
				this.lockMode = LockMode.ALL; // TODO verify
			}
		} else {
			if (lockMode1) {
				this.lockMode = LockMode.NAVIGATION; // TODO verify
			} else {
				this.lockMode = LockMode.NONE;
			}
		}

		boolean backgroundLight0 = controlFlags.testBit(14);
		boolean backgroundLight1 = controlFlags.testBit(15);
		if (backgroundLight0) {
			if (backgroundLight1) {
				this.backgroundLight = BackgroundLight.ON;
			} else {
				this.backgroundLight = BackgroundLight.OFF; // TODO verify
			}
		} else {
			if (backgroundLight1) {
				this.backgroundLight = BackgroundLight.AUTO_NIGHT; // TODO
																	// verify
			} else {
				this.backgroundLight = BackgroundLight.AUTO_DAY;
			}
		}
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("backgroundLight", this.backgroundLight);
		builder.append("lockMode", this.lockMode);
		builder.append("i2cPlugAndPlay", this.i2cPlugAndPlay);
		builder.append("i2cHumidityChangeRequest", this.i2cHumidityChangeRequest);
		builder.append("i2cTemperatureChangeRequest", this.i2cTemperatureChangeRequest);
		builder.append("motionSensorChangeRequest", this.motionSensorChangeRequest);
		builder.append("keepAlive", this.keepAlive);
		builder.append("changeFilter", this.changeFilter);
		builder.append("frameAcknowledgement", this.frameAcknowledgement);
		builder.append("volumeChangeRequest", this.volumeChangeRequest);
		builder.append("pageChangeRequest", this.pageChangeRequest);
		builder.append("audioActiveChangeRequest", this.audioActiveChangeRequest);
		builder.append("displayActiveChangeRequest", this.displayActiveChangeRequest);
		builder.append("proximitySensorChangeRequest", this.proximitySensorChangeRequest);
		builder.append("lightSensorChangeRequest", this.lightSensorChangeRequest);

		return builder.toString();
	}
}
