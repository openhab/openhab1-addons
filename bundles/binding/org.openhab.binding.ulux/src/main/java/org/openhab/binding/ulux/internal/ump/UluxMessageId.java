/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the logical message ids of the u::Lux Message Protocol.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public enum UluxMessageId {

	/**
	 */
	State("ID-State", (byte) 0x01),

	/**
	 */
	Init("ID-Init", (byte) 0x02),

	/**
	 */
	Lux("ID-Lux", (byte) 0x03),

	/**
	 */
	Signature("ID-Signature", (byte) 0x04),

	/**
	 */
	PageCount("ID-PageCount", (byte) 0x0E),

	/**
	 */
	IdList("ID-IDList", (byte) 0x0F),

	/**
	 */
	Control("ID-Control", (byte) 0x21),

	/**
	 */
	Activate("ID-Activate", (byte) 0x2D),

	/**
	 */
	PageIndex("ID-PageIndex", (byte) 0x2E),

	/**
	 */
	DateTime("ID-DateTime", (byte) 0x2F),

	/**
	 */
	Value("ID-Value", (byte) 0x41),

	/**
	 */
	EditValue("ID-EditValue", (byte) 0x42),

	/**
	 */
	RealValue("ID-RealValue", (byte) 0x43),

	/**
	 */
	LED("ID-LED", (byte) 0x44),

	/**
	 */
	Text("ID-Text", (byte) 0x45),

	/**
	 */
	Event("ID-Event", (byte) 0x51),

	/**
	 */
	I2C_Temperature("ID-I2C-Temperature", (byte) 0x71),

	/**
	 */
	I2C_Humidity("ID-I2C-Humidity", (byte) 0x72),

	/**
	 */
	I2C_CO2("ID-I2C-CO2", (byte) 0x73),

	/**
	 */
	I2C_IN2("ID-I2C-IN2", (byte) 0x74),

	/**
	 */
	AudioVolume("ID-AudioVolume", (byte) 0x91),

	/**
	 */
	AudioStop("ID-AudioStop", (byte) 0x92),

	/**
	 */
	AudioPlayLocal("ID-AudioPlayLocal", (byte) 0x98),

	/**
	 */
	AudioPlayRemote("ID-AudioPlayRemote", (byte) 0x99),

	/**
	 */
	AudioRecord("ID-AudioRecord", (byte) 0x9A),

	/**
	 */
	VideoState("ID-VideoState", (byte) 0xA1),

	/**
	 */
	VideoStart("ID-VideoStart", (byte) 0xA2),

	/**
	 */
	VideoStop("ID-VideoStop", (byte) 0xA3);

	/**
	 * Map from raw byte value to logical message id.
	 */
	private static final Map<Byte, UluxMessageId> ID_MAP = new HashMap<Byte, UluxMessageId>();

	static {
		for (final UluxMessageId id : UluxMessageId.values()) {
			ID_MAP.put(id.toByte(), id);
		}
	}

	private final String name;
	private final byte id;

	private UluxMessageId(final String name, final byte id) {
		this.name = name;
		this.id = id;
	}

	/**
	 * Returns the raw byte value of this message id.
	 */
	public byte toByte() {
		return id;
	}

	/**
	 * Returns a string representation of this message id, e.g. "ID-EditValue (0x42)".
	 */
	@Override
	public String toString() {
		return String.format("%s (0x%02x)", this.name, this.id);
	}

	/**
	 * Converts a raw byte value into a logical message id.
	 * 
	 * @return the logical message id specified by the given raw byte value, or <code>null</code> if unknown
	 */
	public static UluxMessageId valueOf(final byte id) {
		return ID_MAP.get(id);
	}

}
