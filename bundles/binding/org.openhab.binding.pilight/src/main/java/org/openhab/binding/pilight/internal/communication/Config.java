/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.communication;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * pilight configuration object 
 * 
 * {@link http://www.pilight.org/development/api/#controller}
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Config {
	
	private Map<String, Device> devices;

	public Map<String, Device> getDevices() {
		return devices;
	}

	public void setDevices(Map<String, Device> devices) {
		this.devices = devices;
	}

}
