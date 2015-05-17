/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzaha.internal.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * This JAXB model class maps the XML response to an <b>getdevicelistinfos</b>
 * command on a Fritz!Box device. As of today, this class is able to to bind the
 * devicelist version 1 (currently used by AVM) response:
 * 
 * <pre>
 * <devicelist version="1">
 * 	 	<device identifier="##############" id="##" functionbitmask="896"
 * 			fwversion="03.36" manufacturer="AVM" productname="FRITZ!DECT 200">
 * 			<present>1</present>
 * 			<name>FRITZ!DECT 200 #1</name>
 * 			<switch>
 * 				<state>0</state>
 * 				<mode>manuell</mode>
 * 				<lock>0</lock>
 * 			</switch>
 * 			<powermeter>
 * 				<power>0</power>
 * 				<energy>166</energy>
 * 			</powermeter>
 * 			<temperature>
 * 				<celsius>255</celsius>
 * 				<offset>0</offset>
 * 			</temperature>
 * 		</device>
 * </devicelist>
 * 
 * <pre>
 * 
 * @author Robert Bausdorf
 * @since 1.6
 */
@SuppressWarnings("restriction")
@XmlRootElement(name = "devicelist")
public class DevicelistModel {

	@XmlAttribute(name = "version")
	private String apiVersion;

	@XmlElement(name = "device")
	private ArrayList<DeviceModel> devices;

	public ArrayList<DeviceModel> getDevicelist() {
		return devices;
	}

	public void setDevicelist(ArrayList<DeviceModel> devicelist) {
		this.devices = devicelist;
	}

	public String getXmlApiVersion() {
		return this.apiVersion;
	}
}
