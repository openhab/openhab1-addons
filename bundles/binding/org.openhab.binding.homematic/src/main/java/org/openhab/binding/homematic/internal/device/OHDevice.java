package org.openhab.binding.homematic.internal.device;

import java.util.HashMap;
import java.util.Map;

public class OHDevice {

    private String deviceId;
    private Map<String, OHChannel> channel = new HashMap<String, OHChannel>();

    public OHDevice() {
    }

    public OHDevice(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Map<String, OHChannel> getChannel() {
        return channel;
    }

    public void setChannel(Map<String, OHChannel> channel) {
        this.channel = channel;
    }

}
