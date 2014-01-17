/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Transmission state enumeration. Indicates the
 * transmission state of the message to the node.
 * @author Jan-Willem Spuij
 * @ since 1.3.0
 */
public enum TransmissionState {
	COMPLETE_OK(0x00, "Transmission complete and ACK received."),
	COMPLETE_NO_ACK(0x01, "Transmission complete, no ACK received."),
	COMPLETE_FAIL(0x02, "Transmission failed."),
	COMPLETE_NOT_IDLE(0x03, "Transmission failed, network busy."),
	COMPLETE_NOROUTE(0x04, "Tranmission complete, no return route.");
	
	/**
	 * A mapping between the integer code and its corresponding transmission state
	 * class to facilitate lookup by code.
	 */
	private static Map<Integer, TransmissionState> codeToTransmissionStateMapping;

	private int key;
	private String label;

	private TransmissionState(int key, String label) {
		this.key = key;
		this.label = label;
	}

	private static void initMapping() {
		codeToTransmissionStateMapping = new HashMap<Integer, TransmissionState>();
		for (TransmissionState s : values()) {
			codeToTransmissionStateMapping.put(s.key, s);
		}
	}

	/**
	 * Lookup function based on the transmission state code.
	 * Returns null when there is no transmission state with code i.
	 * @param i the code to lookup
	 * @return enumeration value of the transmission state.
	 */
	public static TransmissionState getTransmissionState(int i) {
		if (codeToTransmissionStateMapping == null) {
			initMapping();
		}
		
		return codeToTransmissionStateMapping.get(i);
	}

	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
}