/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
