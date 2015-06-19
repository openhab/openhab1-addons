/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plex.internal.communication;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Part of {@link MediaContainer}. Strangely, this object contains information about a Plex client.  
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
@XmlRootElement(name = "Server")
@XmlAccessorType(XmlAccessType.FIELD)
public class Server {
	
	@XmlAttribute
	private String name;
	
	@XmlAttribute
	private String host;
	
	@XmlAttribute
	private String address;
	
	@XmlAttribute
	private String port = "80";
	
	@XmlAttribute
	private String machineIdentifier;
	
	@XmlAttribute
	private String version;
	
	@XmlAttribute
	private String protocol;

	@XmlAttribute
	private String product;
	
	@XmlAttribute
	private String deviceClass;
	
	@XmlAttribute
	private String protocolVersion;
	
	@XmlAttribute
	private String protocolCapabilities;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getMachineIdentifier() {
		return machineIdentifier;
	}

	public void setMachineIdentifier(String machineIdentifier) {
		this.machineIdentifier = machineIdentifier;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(String deviceClass) {
		this.deviceClass = deviceClass;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getProtocolCapabilities() {
		return protocolCapabilities;
	}

	public void setProtocolCapabilities(String protocolCapabilities) {
		this.protocolCapabilities = protocolCapabilities;
	}

}
