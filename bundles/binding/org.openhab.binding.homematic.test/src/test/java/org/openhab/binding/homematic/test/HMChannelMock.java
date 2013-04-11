/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
