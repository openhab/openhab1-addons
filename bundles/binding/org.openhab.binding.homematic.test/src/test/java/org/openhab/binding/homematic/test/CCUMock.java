/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
