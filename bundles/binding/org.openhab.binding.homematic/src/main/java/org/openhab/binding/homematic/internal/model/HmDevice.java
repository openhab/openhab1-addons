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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.binding.homematic.internal.model.adapter.HmInterfaceAdapter;

/**
 * Object that represents a Homematic device.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */

@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.FIELD)
public class HmDevice {

	@XmlAttribute(name = "address", required = true)
	private String address;

	@XmlAttribute(name = "interface", required = true)
	@XmlJavaTypeAdapter(value = HmInterfaceAdapter.class)
	private HmInterface hmInterface;

	@XmlAttribute(name = "type", required = true)
	private String type;

	@XmlElement(name = "channel")
	private List<HmChannel> channels = new ArrayList<HmChannel>();

	/**
	 * Returns the address of the device.
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Returns the interface of the device.
	 */
	public HmInterface getHmInterface() {
		return hmInterface;
	}

	/**
	 * Returns the type of the device.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns all channels of the device.
	 */
	public List<HmChannel> getChannels() {
		return channels;
	}

	/**
	 * Adds a channel to this device.
	 */
	public void addChannel(HmChannel channel) {
		channel.setDevice(this);
		channels.add(channel);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("address", address)
				.append("hmInterface", hmInterface.toString()).append("type", type).toString();
	}

}
