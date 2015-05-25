/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.message;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfRoomDeviceIdentifier implements LightwaveRfDeviceId {

	private final String roomId;
	private final String deviceId;

	public LightwaveRfRoomDeviceIdentifier(String roomId, String deviceId) {
		this.roomId = roomId;
		this.deviceId = deviceId;
	}

	@Override
	public String getDeviceIdentifier() {
		return "R" + roomId + "D" + deviceId;
	}

}
