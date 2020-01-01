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
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaWebserviceUpdateNumberCallback;
import org.openhab.binding.fritzaha.internal.hardware.callbacks.FritzahaWebserviceUpdateXmlCallback;
import org.openhab.binding.fritzaha.internal.hardware.interfaces.FritzahaOutletMeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Meter in outlet addressed via webservice
 *
 * @author Christian Brauers
 * @since 1.3.0
 */
public class FritzahaWebserviceMeter implements FritzahaOutletMeter {
    /**
     * Host ID
     */
    String host;
    /**
     * Device ID
     */
    String id;
    /**
     * Meter type
     */
    MeterType type;

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
    public MeterType getMeterType() {
        return type;
    }

    static final Logger logger = LoggerFactory.getLogger(FritzahaWebserviceMeter.class);

    /**
     * {@inheritDoc}
     */
    public void updateMeterValue(String itemName, FritzahaWebInterface webIface) {
        String valueType;
        if (type == MeterType.POWER) {
            valueType = "getswitchpower";
        } else if (type == MeterType.ENERGY) {
            valueType = "getswitchenergy";
        } else if (type == MeterType.TEMPERATURE) {
            valueType = "getdevicelistinfos";
        } else {
            return;
        }
        logger.debug("Getting " + valueType + " value for AIN " + id);
        String path = "webservices/homeautoswitch.lua";
        String args = "switchcmd=" + valueType + "&ain=" + id;
        if (type.equals(MeterType.TEMPERATURE)) {
            webIface.asyncGet(path, args, new FritzahaWebserviceUpdateXmlCallback(path, args, type, webIface,
                    FritzahaReauthCallback.Method.GET, 1, itemName, id));
        } else {
            webIface.asyncGet(path, args, new FritzahaWebserviceUpdateNumberCallback(path, args, type, webIface,
                    FritzahaReauthCallback.Method.GET, 1, itemName));
        }
    }

    /**
     * {@inheritDoc}
     */
    public FritzahaWebserviceMeter(String host, String id, MeterType type) {
        this.host = host;
        this.id = id;
        this.type = type;
    }
}
