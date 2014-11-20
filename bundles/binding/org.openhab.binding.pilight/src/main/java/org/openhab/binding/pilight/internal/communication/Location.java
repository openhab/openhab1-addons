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
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * A 'location' in pilight. This class contains all devices present in this location.       
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class Location {

	private String name;
	
	private Integer order;

	private Map<String,Device> devices = new HashMap<String, Device>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	
    public Map<String, Device> getDevices() {
		return devices;
	}

	@JsonAnySetter
    public void set(String name, Object value) {
    	ObjectMapper mapper = new ObjectMapper();
    	Device device = mapper.convertValue(value, Device.class);
		getDevices().put(name,  device);
    }
	
}
