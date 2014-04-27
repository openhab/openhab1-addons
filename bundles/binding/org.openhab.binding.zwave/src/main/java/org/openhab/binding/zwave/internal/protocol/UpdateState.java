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
 * Update state enumeration. Indicates the type of application update state that was sent.
 * @author Jan-Willem Spuij
 * @ since 1.3.0
 */
public enum UpdateState {
	NODE_INFO_RECEIVED(0x84, "Node info received"),
	NODE_INFO_REQ_DONE(0x82, "Node info request done"),
	NODE_INFO_REQ_FAILED(0x81, "Node info request failed"),
	ROUTING_PENDING(0x80, "Routing pending"),
	NEW_ID_ASSIGNED(0x40, "New ID Assigned"),
	DELETE_DONE(0x20, "Delete done"),
	SUC_ID(0x10, "SUC ID");
	
	/**
	 * A mapping between the integer code and its corresponding update state
	 * class to facilitate lookup by code.
	 */
	private static Map<Integer, UpdateState> codeToUpdateStateMapping;

	private int key;
	private String label;

	private UpdateState(int key, String label) {
		this.key = key;
		this.label = label;
	}

	private static void initMapping() {
		codeToUpdateStateMapping = new HashMap<Integer, UpdateState>();
		for (UpdateState s : values()) {
			codeToUpdateStateMapping.put(s.key, s);
		}
	}

	/**
	 * Lookup function based on the update state code.
	 * Returns null when there is no update state with code i.
	 * @param i the code to lookup
	 * @return enumeration value of the update state.
	 */
	public static UpdateState getUpdateState(int i) {
		if (codeToUpdateStateMapping == null) {
			initMapping();
		}
		
		return codeToUpdateStateMapping.get(i);
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