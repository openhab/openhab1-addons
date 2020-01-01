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
package org.openhab.binding.garadget.internal;

import java.util.HashSet;
import java.util.Set;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A specific kind of Particle device with compound variables. This class breaks them out into separate variables.
 *
 * @author John Cocula
 * @since 1.9.0
 */
public class GaradgetDevice extends AbstractDevice {
    private final Logger logger = LoggerFactory.getLogger(GaradgetDevice.class);

    public static final String DOOR_STATUS = "doorStatus";
    public static final String DOOR_CONFIG = "doorConfig";
    public static final String NET_CONFIG = "netConfig";

    // Compound variables needing to be broken out
    private static final Set<String> compoundVars = new HashSet<String>();

    static {
        compoundVars.add(DOOR_STATUS);
        compoundVars.add(DOOR_CONFIG);
        compoundVars.add(NET_CONFIG);
    }

    /**
     * {@inheritDoc}
     *
     * This override splits the compound variables for this kind of device in
     * the form <code>name1=value1|name2=value2|name3=value3</code>.
     *
     * @param map the map to parse into
     * @param value the formatted string
     */
    @Override
    public void setVar(String key, Object value) {
        super.setVar(key, value);
        if (compoundVars.contains(key)) {
            logger.trace("setVar parsing key={}, value={}", key, value);
            String[] results = value.toString().split("\\|");
            for (int i = 0; i < results.length; i++) {
                String[] nameValue = (results[i]).split("\\=");
                if (nameValue.length == 2) {
                    super.setVar(key + "_" + nameValue[0], nameValue[1]);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * This override has specific knowledge of Garadget variables and produces the proper
     * types for those variables.
     */
    @Override
    public State getVarState(GaradgetSubscriber subscriber) {
        final String varName = subscriber.getVarName();
        if (varName.equals(DOOR_STATUS + "_status") && getVar(varName) != null) {
            final String status = getVar(varName).toString();
            for (Class<? extends State> type : subscriber.getAcceptedDataTypes()) {
                if (OpenClosedType.class == type) {
                    if (status.equals("open")) {
                        return OpenClosedType.OPEN;
                    } else if (status.equals("closed")) {
                        return OpenClosedType.CLOSED;
                    }
                } else if (UpDownType.class == type) {
                    if (status.equals("open")) {
                        return UpDownType.UP;
                    } else if (status.equals("closed")) {
                        return UpDownType.DOWN;
                    }
                } else if (OnOffType.class == type) {
                    if (status.equals("open")) {
                        return OnOffType.ON;
                    } else if (status.equals("closed")) {
                        return OnOffType.OFF;
                    }
                } else if (PercentType.class == type) {
                    if (status.equals("open")) {
                        return PercentType.ZERO;
                    } else if (status.equals("closed")) {
                        return PercentType.HUNDRED;
                    } else {
                        return PercentType.valueOf("50");
                    }
                } else if (StringType.class == type) {
                    return StringType.valueOf(status);
                }
            }
        }
        return super.getVarState(subscriber);
    }
}
