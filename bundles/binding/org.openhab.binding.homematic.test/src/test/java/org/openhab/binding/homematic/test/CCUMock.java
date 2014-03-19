/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openhab.binding.homematic.internal.ccu.CCU;
import org.openhab.binding.homematic.internal.ccu.CCUListener;
import org.openhab.binding.homematic.internal.config.HomematicParameterAddress;
import org.openhab.binding.homematic.internal.xmlrpc.XmlRpcConnection;
import org.openhab.binding.homematic.internal.xmlrpc.callback.CallbackReceiver;

public class CCUMock implements CCU<HMPhysicalDeviceMock>, CallbackReceiver {

    private HMPhysicalDeviceMock device = new HMPhysicalDeviceMock();
    private List<Event> events = new ArrayList<Event>();

    public List<Event> getEvents() {
        return events;
    }

    public XmlRpcConnection getConnection() {
        // TODO Auto-generated method stub
        return null;
    }

    public HMPhysicalDeviceMock getPhysicalDevice(String address) {
        return device;
    }

    public Set<HMPhysicalDeviceMock> getPhysicalDevices() {
        // TODO Auto-generated method stub
        return null;
    }

    public <S extends HMPhysicalDeviceMock> Set<S> getPhysicalDevices(Class<S> clazz) {
        // TODO Auto-generated method stub
        return null;
    }

    public void addCCUListener(CCUListener l) {
    }

    public void removeCCUListener(CCUListener l) {
    }

    public Integer event(String interfaceId, String address, String parameterKey, Object value) {
        Event event = new Event(interfaceId, HomematicParameterAddress.from(address, parameterKey), value);
        events.add(event);
        device.getChannel(0).updateProperty(parameterKey, value);
        return null;
    }

    public Object[] listDevices(String interfaceId) {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer newDevices(String interfaceId, Object[] deviceDescriptions) {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer deleteDevices(String interfaceId, Object[] addresses) {
        // TODO Auto-generated method stub
        return null;
    }

    public Integer updateDevice(String interfaceId, String address, Integer hint) {
        // TODO Auto-generated method stub
        return null;
    }

    static class Event {
        private String interfaceId;
        private Object value;
        private HomematicParameterAddress parameterAddress;

        public Event(String interfaceId, HomematicParameterAddress parameterAddress, Object value) {
            this.interfaceId = interfaceId;
            this.parameterAddress = parameterAddress;
            this.value = value;
        }

        public String getInterfaceId() {
            return interfaceId;
        }

        public HomematicParameterAddress getParameterAddress() {
            return parameterAddress;
        }

        public Object getValue() {
            return value;
        }
    }

}
