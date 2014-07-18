/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 * 
 */
package org.openhab.binding.homematic.internal.xmlrpc.callback;

import java.util.HashSet;
import java.util.Set;

import org.openhab.binding.homematic.internal.communicator.HomematicCallbackReceiver;

/**
 * The CallbackHandler itself only dispatches calls to registered instances of
 * CallbackReceiver. This way you can register multiple CallbackReceivers to be
 * notified about certain events.
 * 
 * Workaround: Apache XMP-RPC does not deliver e.g. an Integer parameter to a
 * method with an Object signature (even though Integer is an Object).
 * Therefore, I had to dedebug an event method for each possible type of the
 * value parameter. Those methods redirect the call to event(String, String
 * String, Object) which then deliveres the call to CallbackReceivers.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class CallbackHandler {

    private Set<HomematicCallbackReceiver> receivers;

    public CallbackHandler() {
        receivers = new HashSet<HomematicCallbackReceiver>();
    }

    public String event(String interfaceId, String address, String parameterKey, Object value) {
        for (HomematicCallbackReceiver rcv : receivers) {
            rcv.event(interfaceId, address, parameterKey, value);
        }

        return "";
    }

    public Object[] listDevices(String interfaceId) {
//		for (CallbackReceiver rcv : receivers) {
//			Object[] devices = rcv.listDevices(interfaceId);
//			if (devices != null) {
//				return devices;
//			}
//		}

        return new Object[] {};
    }

    public Object[] newDevices(String interfaceId, Object[] deviceDescriptions) {
        for (HomematicCallbackReceiver rcv : receivers) {
            rcv.newDevices(interfaceId, deviceDescriptions);
        }

        return new Object[] {};
    }

    public Object[] deleteDevices(String interfaceId, Object[] addresses) {
//        for (CallbackReceiver rcv : receivers) {
//            rcv.deleteDevices(interfaceId, addresses);
//        }

        return new Object[] {};
    }

    public String updateDevice(String interfaceId, String address, Integer hint) {
//        for (CallbackReceiver rcv : receivers) {
//            rcv.updateDevice(interfaceId, address, hint);
//        }

        return "";
    }

    /*
     * Allow CallbackInterface instances to register for updates
     */

    public void registerCallbackReceiver(HomematicCallbackReceiver receiver) {
        receivers.add(receiver);
    }

    public void unregisterCallbackReceiver(HomematicCallbackReceiver receiver) {
        receivers.remove(receiver);
    }

    /*
     * Apache XML-RPC does not allow Object type parameters so this is a
     * workaround. Actually, the methods below will be called but then
     * redirected to the main event(String, String, String, Object) method
     * above.
     */

    public String event(String name, String address, String valueKey, Integer value) {
        return event(name, address, valueKey, (Object) value);
    }

    public String event(String name, String address, String valueKey, Double value) {
        return event(name, address, valueKey, (Object) value);
    }

    public String event(String name, String address, String valueKey, Float value) {
        return event(name, address, valueKey, (Object) value);
    }

    public String event(String name, String address, String valueKey, String value) {
        return event(name, address, valueKey, (Object) value);
    }

    public String event(String name, String address, String valueKey, Boolean value) {
        return event(name, address, valueKey, (Object) value);
    }

}
