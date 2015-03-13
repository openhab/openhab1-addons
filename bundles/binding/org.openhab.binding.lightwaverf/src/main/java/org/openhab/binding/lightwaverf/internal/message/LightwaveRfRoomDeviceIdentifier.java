package org.openhab.binding.lightwaverf.internal.message;

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
