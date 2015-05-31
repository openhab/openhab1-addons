package org.openhab.binding.hue.internal.data;

import org.json.simple.JSONObject;

/**
 * Encapsulates HUE bridge attributes.
 * 
 * @author andreypereverzin
 */
public class BridgeData {
    private static final String INTERNAL_IP_ADDRESS = "internalipaddress";
    private static final String ID = "id";
    
    private final String id;
    private final String internalIpAddress;
    
    public BridgeData(JSONObject jsonObject) {
        this.id = (String) jsonObject.get(ID);
        this.internalIpAddress = (String) jsonObject.get(INTERNAL_IP_ADDRESS);
    }

    public String getId() {
        return id;
    }

    public String getInternalIpAddress() {
        return internalIpAddress;
    }
}
