/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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

import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Class describing a device in pilight 
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Device {
	
	private String uuid;
	
	private String origin;
	
	private String timestamp;
	
	private List<String> protocol;
	
	private String state;
	
	private Integer dimlevel;
	
	private Integer dimlevelMaximum;
	
	private Integer dimlevelMinimum;
	
	private List<Map<String, String>> id;
	
	private Map<String,String> properties = new HashMap<String, String>();

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<String> getProtocol() {
		return protocol;
	}

	public void setProtocol(List<String> protocol) {
		this.protocol = protocol;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	public Integer getDimlevel() {
		return dimlevel;
	}

	public void setDimlevel(Integer dimlevel) {
		this.dimlevel = dimlevel;
	}

	public Integer getDimlevelMaximum() {
		return dimlevelMaximum;
	}

	@JsonProperty("dimlevel-maximum")
	public void setDimlevelMaximum(Integer dimlevelMaximum) {
		this.dimlevelMaximum = dimlevelMaximum;
	}

	public Integer getDimlevelMinimum() {
		return dimlevelMinimum;
	}

	@JsonProperty("dimlevel-minimum")
	public void setDimlevelMinimum(Integer dimlevelMinimum) {
		this.dimlevelMinimum = dimlevelMinimum;
	}

	public List<Map<String, String>> getId() {
		return id;
	}

	public void setId(List<Map<String, String>> id) {
		this.id = id;
	}
	
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	public Map<String, String> getProperties() {
		return properties;
	}
	
	@JsonAnySetter
    public void set(String name, Object value) {
    	properties.put(name, value.toString());
    }
}
