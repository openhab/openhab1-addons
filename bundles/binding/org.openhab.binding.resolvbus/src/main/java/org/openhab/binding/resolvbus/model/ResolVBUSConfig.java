package org.openhab.binding.resolvbus.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "device", "packet"})
@XmlRootElement(name = "vbusSpecification")

public class ResolVBUSConfig {

	@XmlElement(required = true)
	protected List<ResolVBUSDevice> device;
	@XmlElement(required = true)
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

}
