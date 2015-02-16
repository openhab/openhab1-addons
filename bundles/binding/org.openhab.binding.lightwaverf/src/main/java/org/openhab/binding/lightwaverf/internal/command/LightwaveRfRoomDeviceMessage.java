package org.openhab.binding.lightwaverf.internal.command;

public interface LightwaveRfRoomDeviceMessage extends LightwaveRFCommand {
	
	public String getRoomId();
	public String getDeviceId();

}
