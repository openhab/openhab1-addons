/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump.messages;

import static org.openhab.binding.ulux.internal.ump.messages.LedMessage.Led.LED_1;
import static org.openhab.binding.ulux.internal.ump.messages.LedMessage.Led.LED_2;
import static org.openhab.binding.ulux.internal.ump.messages.LedMessage.Led.LED_3;
import static org.openhab.binding.ulux.internal.ump.messages.LedMessage.Led.LED_4;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.EnumMap;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * Message to query or set the status of the LEDs.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class LedMessage extends AbstractUluxMessage {

	private static final byte MESSAGE_LENGTH = (byte) 0x08;

	private final EnumMap<Led, BigInteger> ledState = new EnumMap<Led, BigInteger>(Led.class);

	/**
	 * Creates a message to set the state of the actor's LEDs.
	 */
	public LedMessage(short actorId) {
		super(MESSAGE_LENGTH, UluxMessageId.LED, actorId);

		this.ledState.put(LED_1, BigInteger.ZERO);
		this.ledState.put(LED_2, BigInteger.ZERO);
		this.ledState.put(LED_3, BigInteger.ZERO);
		this.ledState.put(LED_4, BigInteger.ZERO);
	}

	public void setOverride(Led led, boolean override) {
		BigInteger state = this.ledState.get(led);

		if (override) {
			state = state.setBit(7);
		} else {
			state = state.clearBit(7);
		}

		this.ledState.put(led, state);
	}

	public void setColor(Led led, Color color) {
		BigInteger state = this.ledState.get(led);

		switch (color) {
		case COLOR_OFF: // 0x00
			state = state.clearBit(3);
			state = state.clearBit(2);
			state = state.clearBit(1);
			state = state.clearBit(0);
			break;
		case COLOR_RED: // 0x01
			state = state.clearBit(3);
			state = state.clearBit(2);
			state = state.clearBit(1);
			state = state.setBit(0);
			break;
		case COLOR_GREEN: // 0x02
			// TODO
			break;
		case COLOR_YELLOW: // 0x03
			// TODO
			break;
		case COLOR_BLUE: // 0x04
			// TODO
			break;
		case COLOR_MAGENTA: // 0x05
			// TODO
			break;
		case COLOR_CYAN: // 0x06
			// TODO
			break;
		case COLOR_WHITE: // 0x07
			// TODO
			break;
		}

		this.ledState.put(led, state);
	}

	public void setBlinkMode(Led led, BlinkMode blinkMode) {
		BigInteger state = this.ledState.get(led);

		switch (blinkMode) {
		case BLINK_NONE:
			state = state.clearBit(6);
			state = state.clearBit(5);
			state = state.clearBit(4);
			break;
		case BLINK_SLOW: // 0x1
			// TODO
			break;
		case BLINK_MEDIUM: // 0x2
			// TODO
			break;
		case BLINK_FAST: // 0x3
			state = state.clearBit(6);
			state = state.setBit(5);
			state = state.setBit(4);
			break;
		case BLINK_INTERVAL_1:
			// TODO
			break;
		case BLINK_INTERVAL_2:
			// TODO
			break;
		case BLINK_INTERVAL_3:
			// TODO
			break;
		case BLINK_INTERVAL_4:
			// TODO
			break;
		}

		this.ledState.put(led, state);
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		buffer.put((byte) this.ledState.get(LED_1).shortValue());
		buffer.put((byte) this.ledState.get(LED_2).shortValue());
		buffer.put((byte) this.ledState.get(LED_3).shortValue());
		buffer.put((byte) this.ledState.get(LED_4).shortValue());
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());

		return builder.toString();
	}

	public static enum Led {
		LED_1, LED_2, LED_3, LED_4;
	}

	public static enum Color {
		COLOR_OFF, COLOR_RED, COLOR_GREEN, COLOR_YELLOW, COLOR_BLUE, COLOR_MAGENTA, COLOR_CYAN, COLOR_WHITE;
	}

	public static enum BlinkMode {
		BLINK_NONE, BLINK_SLOW, BLINK_MEDIUM, BLINK_FAST, BLINK_INTERVAL_1, BLINK_INTERVAL_2, BLINK_INTERVAL_3, BLINK_INTERVAL_4
	}
}
