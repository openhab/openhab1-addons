/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ucprelayboard.internal;

/**
 * This class represents configuration for a serial device
 * 
 * @author Robert Michalak
 * @since 1.8.0
 */
public class SerialDeviceConfig {

	private String name;
	private String port;
	private int baud = 57600; // default value

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getBaud() {
		return baud;
	}

	public void setBaud(int baud) {
		this.baud = baud;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isValid() {
		return (name != null && port != null);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + baud;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((port == null) ? 0 : port.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SerialDeviceConfig other = (SerialDeviceConfig) obj;
		if (baud != other.baud)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (port == null) {
			if (other.port != null)
				return false;
		} else if (!port.equals(other.port))
			return false;
		return true;
	}
	
	
}
