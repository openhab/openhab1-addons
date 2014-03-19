/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.test;

import java.beans.PropertyChangeListener;
import java.util.HashMap;

import org.openhab.binding.homematic.internal.device.channel.HMChannel;
import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;
import org.openhab.binding.homematic.internal.xmlrpc.impl.Paramset;
import org.openhab.binding.homematic.internal.xmlrpc.impl.ParamsetDescription;

public class HMChannelMock implements HMChannel {

    private ParamsetDescription valuesDescription;
    private Paramset values;
    private ParamsetDescription master;

    public HMChannelMock() {
        valuesDescription = new ParamsetDescription(new HashMap<String, Object>());
    }

    public String getAddress() {
        // TODO Auto-generated method stub
        return null;
    }

    public DeviceDescription getDeviceDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    public ParamsetDescription getMaster() {
        return master;
    }

    public void setMaster(ParamsetDescription master) {
        this.master = master;
    }

    public void sync() {
        // TODO Auto-generated method stub

    }

    public HMPhysicalDevice getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateProperty(String parameterKey, Object value) {
        if(values != null) {
            this.values.getValues().put(parameterKey, value);
        }
    }

    public ParamsetDescription getValuesDescription() {
        return valuesDescription;
    }

    public void setValuesDescription(ParamsetDescription values) {
        this.valuesDescription = values;
    }

    public void setValue(String key, Object value) {
        values.getValues().put(key, value);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        // TODO Auto-generated method stub

    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        // TODO Auto-generated method stub

    }

    public Paramset getValues() {
        return values;
    }

    public void setValues(Paramset paramset) {
        values = paramset;
    }

}
