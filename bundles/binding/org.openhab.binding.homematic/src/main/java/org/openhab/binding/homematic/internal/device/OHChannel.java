package org.openhab.binding.homematic.internal.device;

import java.util.HashMap;
import java.util.Map;

public class OHChannel {

    private Map<String, OHParameter> parameters = new HashMap<String, OHParameter>();

    public OHChannel() {
    }

    public void addParameter(String name, OHParameter parameter) {
        parameters.put(name, parameter);
    }

    public Map<String, OHParameter> getParameters() {
        return parameters;
    }
}
