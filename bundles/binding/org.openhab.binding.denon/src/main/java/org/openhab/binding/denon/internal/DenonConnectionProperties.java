/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal;

import java.math.BigDecimal;

/**
 * Properties for a connection to a Denon receiver
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class DenonConnectionProperties {
	
	// Default maximum volume
	public static final BigDecimal MAX_VOLUME = new BigDecimal("98");
	
	private String instance;
	
	private String host;
	
	private boolean telnet = true;
	
	private boolean http = false;
	
	private int telnetPort = 23;
	
	private int httpPort = 80;
	
	private DenonConnector connector;
	
	private Integer zoneCount = 2;

	private BigDecimal mainVolumeMax = MAX_VOLUME;
	
	public String getInstance() {
		return instance;
	}

	public void setInstance(String instance) {
		this.instance = instance;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isTelnet() {
		return telnet;
	}

	public void setTelnet(boolean telnet) {
		this.telnet = telnet;
	}

	public boolean isHttp() {
		return http;
	}

	public void setHttp(boolean http) {
		this.http = http;
	}

	public Integer getTelnetPort() {
		return telnetPort;
	}

	public void setTelnetPort(Integer telnetPort) {
		this.telnetPort = telnetPort;
	}

	public Integer getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(Integer httpPort) {
		this.httpPort = httpPort;
	}
	
	public DenonConnector getConnector() {
		return connector;
	}

	public void setConnector(DenonConnector connector) {
		this.connector = connector;
	}
	
	public BigDecimal getMainVolumeMax() {
		return mainVolumeMax;
	}
	
	public void setMainVolumeMax(BigDecimal mainVolumeMax) {
		this.mainVolumeMax = mainVolumeMax;
	}

	public Integer getZoneCount() {
		return zoneCount;
	}

	public void setZoneCount(Integer zoneCount) {
		this.zoneCount = zoneCount;
	}
}
