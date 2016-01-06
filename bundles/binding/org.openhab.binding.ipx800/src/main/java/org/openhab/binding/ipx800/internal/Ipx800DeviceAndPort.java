package org.openhab.binding.ipx800.internal;

import org.openhab.binding.ipx800.internal.command.Ipx800Port;

/**
 * Device and port bean
 * 
 * @author Seebag
 * @since 1.8.0
 */
public class Ipx800DeviceAndPort { 
	private final Ipx800DeviceConnector device;
	private final Ipx800Port port;
	public Ipx800DeviceAndPort(Ipx800DeviceConnector device, Ipx800Port port) {
		this.device = device;
		this.port = port;
	}
	
	public Ipx800DeviceConnector getDevice() {
		return device;
	}
	 
	public Ipx800Port getPort() {
		return port;
	}
}
