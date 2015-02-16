package org.openhab.binding.lightwaverf.internal.message;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomDeviceMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSerialMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfVersionMessage;

public interface LightwaveRFMessageListener {
	
	public void roomDeviceMessageReceived(LightwaveRfRoomDeviceMessage message);
	public void roomMessageReceived(LightwaveRfRoomMessage message);
	public void serialMessageReceived(LightwaveRfSerialMessage message);
	public void okMessageReceived(LightwaveRfCommandOk message);
	public void versionMessageReceived(LightwaveRfVersionMessage message);

}
