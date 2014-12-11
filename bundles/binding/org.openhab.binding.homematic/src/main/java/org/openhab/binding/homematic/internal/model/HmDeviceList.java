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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple class with the JAXB mapping for a list of Homematic devices.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */

@XmlRootElement(name = "devices")
@XmlAccessorType(XmlAccessType.FIELD)
public class HmDeviceList {

	@XmlElement(name = "device")
	private List<HmDevice> devices = new ArrayList<HmDevice>();

	/**
	 * Returns all devices.
	 */
	public List<HmDevice> getDevices() {
		return devices;
	}

}
