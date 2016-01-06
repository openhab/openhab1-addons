/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal.communication.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains information about a Denon receiver. 
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name="device_Info")
@XmlAccessorType(XmlAccessType.FIELD)
public class Deviceinfo {
	
	private Integer deviceZones;
	
	private String modelName;

	public Integer getDeviceZones() {
		return deviceZones;
	}

	public void setDeviceZones(Integer deviceZones) {
		this.deviceZones = deviceZones;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
