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
/**
 * 
 */
package org.openhab.binding.homematic.internal.xmlrpc.callback;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(CallbackServer.class);

    private Set<CallbackReceiver> receivers;

    public CallbackHandler() {
        receivers = new HashSet<CallbackReceiver>();
    }

    /*
     * methods from CallbackInterface
     */

    public void multicall(String interfaceId) {
        logger.debug("multicall received (NOOP)");
    }

    public Integer event(String interfaceId, String address, String parameterKey, Object value) {
        logger.debug("dispatching event to " + receivers.size() + " receivers");
        for (CallbackReceiver rcv : receivers) {
            rcv.event(interfaceId, address, parameterKey, value);
        }

        return 0;
    }

    public Object[] listDevices(String interfaceId) {
        logger.debug("dispatching event to " + receivers.size() + " receivers");
        for (CallbackReceiver rcv : receivers) {
            rcv.listDevices(interfaceId);
        }

        return null;
    }

    public Integer newDevices(String interfaceId, Object[] deviceDescriptions) {
        logger.debug("dispatching event to " + receivers.size() + " receivers");
        for (CallbackReceiver rcv : receivers) {
            rcv.newDevices(interfaceId, deviceDescriptions);
        }

        return 0;
    }

    public Integer deleteDevices(String interfaceId, Object[] addresses) {
        logger.debug("dispatching event to " + receivers.size() + " receivers");
        for (CallbackReceiver rcv : receivers) {
            rcv.deleteDevices(interfaceId, addresses);
        }

        return 0;
    }

    public Integer updateDevice(String interfaceId, String address, Integer hint) {
        logger.debug("called updateDevice: " + interfaceId + ", " + address + ", " + hint);

        logger.debug("dispatching event to " + receivers.size() + " receivers");
        for (CallbackReceiver rcv : receivers) {
            rcv.updateDevice(interfaceId, address, hint);
        }

        return 0;
    }

    /*
     * Allow CallbackInterface instances to register for updates
     */

    public void registerCallbackReceiver(CallbackReceiver receiver) {
        logger.debug("CallbackReceiver registered: " + receiver);
        receivers.add(receiver);
    }

    public void unregisterCallbackReceiver(CallbackReceiver receiver) {
        logger.debug("CallbackReceiver unregistered: " + receiver);
        receivers.remove(receiver);
    }

    /*
     * Apache XML-RPC does not allow Object type parameters so this is a
     * workaround. Actually, the methods below will be called but then
     * redirected to the main event(String, String, String, Object) method
     * above.
     */

    public Integer event(String name, String address, String valueKey, Integer value) {
        logger.debug("called event (Integer) => dispatching to event (Object)");
        return event(name, address, valueKey, (Object) value);
    }

    public Integer event(String name, String address, String valueKey, Double value) {
        logger.debug("called event (Double) => dispatching to event (Object)");
        return event(name, address, valueKey, (Object) value);
    }

    public Integer event(String name, String address, String valueKey, Float value) {
        logger.debug("called event (Float) => dispatching to event (Object)");
        return event(name, address, valueKey, (Object) value);
    }

    public Integer event(String name, String address, String valueKey, String value) {
        logger.debug("called event (String) => dispatching to event (Object)");
        return event(name, address, valueKey, (Object) value);
    }

    public Integer event(String name, String address, String valueKey, Boolean value) {
        logger.debug("called event (Boolean) => dispatching to event (Object)");
        return event(name, address, valueKey, (Object) value);
    }

}
