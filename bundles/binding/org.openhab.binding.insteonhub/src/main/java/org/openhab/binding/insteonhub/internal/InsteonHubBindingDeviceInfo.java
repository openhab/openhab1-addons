/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal;

/**
 * Info about a device stored in a single object. This is used to keep Sets of
 * hubid+device.
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubBindingDeviceInfo {

	private final String hubId;
	private final String deviceId;

	public InsteonHubBindingDeviceInfo(String hubId, String deviceId) {
		this.hubId = hubId;
		this.deviceId = deviceId;
	}

	public String getHubId() {
		return hubId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		result = prime * result + ((hubId == null) ? 0 : hubId.hashCode());
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
		InsteonHubBindingDeviceInfo other = (InsteonHubBindingDeviceInfo) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		if (hubId == null) {
			if (other.hubId != null)
				return false;
		} else if (!hubId.equals(other.hubId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "hub=" + hubId + " device=" + deviceId;
	}
}
