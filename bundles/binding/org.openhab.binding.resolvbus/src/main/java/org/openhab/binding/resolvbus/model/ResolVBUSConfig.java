/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.resolvbus.model;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * @author Michael Heckmann
 * @since 1.7.0
 */

public class ResolVBUSConfig {

	@XStreamImplicit
	protected List<ResolVBUSDevice> device;
	@XStreamImplicit
	protected List<ResolVBUSPacket> packet;
	
	public List<ResolVBUSDevice> getDevice() {
		return device;
	}

	public void setDevice(List<ResolVBUSDevice> device) {
		this.device = device;
	}

	public List<ResolVBUSPacket> getPacket() {
		return packet;
	}

	public void setPacket(List<ResolVBUSPacket> packet) {
		this.packet = packet;
	}

	public ResolVBUSPacket getPacketWithDevice(String deviceAddress) {
		
		List<ResolVBUSPacket> packetList = new ArrayList<ResolVBUSPacket>(getPacket());
		
		for (ResolVBUSPacket packet : packetList) {
			if (packet.getSource().equals("0x"+deviceAddress))
				return packet;
		}
		return null;
		
	}

	public ResolVBUSPacket getPacketWithDevice(String deviceAddressSource,String deviceAddressDestination) {
		
		List<ResolVBUSPacket> packetList = new ArrayList<ResolVBUSPacket>(getPacket());
		
		
		for (ResolVBUSPacket packet : packetList) {
			if (packet.getSource().equals("0x"+deviceAddressSource) && packet.getDestination().equals("0x"+deviceAddressDestination))
				return packet;
		}
		return null;
		
	}
}
