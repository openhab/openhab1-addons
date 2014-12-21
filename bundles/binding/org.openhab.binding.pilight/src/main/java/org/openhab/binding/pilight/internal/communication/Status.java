/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.communication;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A Status message is received when a device in pilight changes state.     
 * 
 * {@link http://www.pilight.org/development/api/#receiver}
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class Status {
	
	public static String SERVER_UPDATE = "-1";
	
	public static String SWITCH_EVENT = "1";
	
	public static String DIMMER_EVENT = "2";
	
	private String origin;
	
	private String type;
	
	private String uuid;
	
	private Map<String, List<String>> devices = new HashMap<String, List<String>>();
	
	private Map<String, String> values = new HashMap<String, String>();
	
	public Status() {
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Map<String, List<String>> getDevices() {
		return devices;
	}

	public void setDevices(Map<String, List<String>> devices) {
		this.devices = devices;
	}

	public Map<String, String> getValues() {
		return values;
	}

	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	
}
