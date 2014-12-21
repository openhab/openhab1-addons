/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Object that represents a Homematic channel.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
@XmlRootElement(name = "channel")
@XmlAccessorType(XmlAccessType.FIELD)
public class HmChannel {

	@XmlAttribute(name = "number", required = true)
	private String number;

	@XmlTransient
	private HmDevice device;

	@XmlElement(name = "datapoint")
	private List<HmDatapoint> datapoints = new ArrayList<HmDatapoint>();

	/**
	 * Returns the channel number.
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * Returns the device of the channel.
	 */
	public HmDevice getDevice() {
		return device;
	}

	/**
	 * Sets the device of the channel.
	 */
	protected void setDevice(HmDevice device) {
		this.device = device;
	}

	/**
	 * Returns all datapoints of the channel.
	 */
	public List<HmDatapoint> getDatapoints() {
		return datapoints;
	}

	/**
	 * Adds a datapoint to the channel.
	 */
	public void addDatapoint(HmDatapoint datapoint) {
		datapoint.setChannel(this);
		datapoints.add(datapoint);
	}

	/**
	 * Returns channel address.
	 */
	public String getAddress() {
		return device.getAddress() + ":" + number;
	}

}
