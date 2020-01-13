/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.fritzaha.internal.hardware.devices;

import org.openhab.binding.fritzaha.internal.hardware.FritzahaWebInterface;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaReauthCallback;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaWebserviceUpdateSwitchCallback;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaSwitchedOutlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Switch in outlet addressed via webservice
 *
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaWebserviceSwitch implements FritzahaSwitchedOutlet {
    /**
     * Host ID
     */
    String host;
    /**
     * Device ID
     */
    String id;

    /**
     * {@inheritDoc}
     */
    public String getHost() {
        return host;
    }

    /**
     * {@inheritDoc}
     */
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    static final Logger logger = LoggerFactory.getLogger(FritzahaWebserviceSwitch.class);

    /**
     * {@inheritDoc}
     */
    public void setSwitchState(boolean onOff, String itemName, FritzahaWebInterface webIface) {
        logger.debug("Setting Switch with AIN " + id + " to value " + (onOff ? "on" : "off"));
        String path = "webservices/homeautoswitch.lua";
        String args = "switchcmd=setswitch" + (onOff ? "on" : "off") + "&ain=" + id;
        webIface.asyncGet(path, args, new FritzahaWebserviceUpdateSwitchCallback(path, args, webIface,
                FritzahaReauthCallback.Method.GET, 1, itemName));
    }

    /**
     * {@inheritDoc}
     */
    public void updateSwitchState(String itemName, FritzahaWebInterface webIface) {
        logger.debug("Getting Switch value for AIN " + id);
        String path = "webservices/homeautoswitch.lua";
        String args = "switchcmd=getswitchstate&ain=" + id;
        webIface.asyncGet(path, args, new FritzahaWebserviceUpdateSwitchCallback(path, args, webIface,
                FritzahaReauthCallback.Method.GET, 1, itemName));
    }

    public FritzahaWebserviceSwitch(String host, String id) {
        this.host = host;
        this.id = id;
    }
}
