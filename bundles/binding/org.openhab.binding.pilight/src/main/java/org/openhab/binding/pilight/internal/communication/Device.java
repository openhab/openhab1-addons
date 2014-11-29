/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal.communication;

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
	
	private String name;
	
	private String state;
	
	private Integer dimlevel;
	
	private Integer dimlevelMaximum;
	
	private Integer dimlevelMinimum;
	
	public Device() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
