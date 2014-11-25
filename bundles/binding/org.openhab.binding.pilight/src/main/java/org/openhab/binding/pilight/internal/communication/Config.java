/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.communication;

import java.util.List;
import java.util.Map;

/**
 * pilight configuration object 
 * 
 * {@link http://www.pilight.org/development/api/#controller}
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class Config {
	
	private Map<String, Location> config;
	
	private List<String> version;
	
	private Firmware firmware;
	
	public Config() {
		
	}

	public Map<String, Location> getConfig() {
		return config;
	}

	public void setConfig(Map<String, Location> config) {
		this.config = config;
	}

	public List<String> getVersion() {
		return version;
	}

	public void setVersion(List<String> version) {
		this.version = version;
	}

	public Firmware getFirmware() {
		return firmware;
	}

	public void setFirmware(Firmware firmware) {
		this.firmware = firmware;
	}
}
