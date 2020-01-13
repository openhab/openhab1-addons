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

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.core.types.UnDefType;

/**
 * Represents an abstract device from the Particle REST API.
 *
 * @author John Cocula
 * @since 1.9.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractDevice {

    private String id; // Device ID

    private String name; // Device name

    private String last_app; // Name of the last application that was flashed to the device

    private String last_ip_address; // IP Address that was most recently used by the device

    private Date last_heard; // Date and Time that the cloud last heard from the device in ISO 8601 format

    private Integer product_id; // Indicates what product the device belongs to. Common values are 0 for Core, 6 for
                                // Photon.

    private Boolean connected; // Indicates whether the device is currently connected to the cloud

    @JsonIgnore
    protected Map<String, Object> vars = new HashMap<String, Object>(); // variables in the Particle REST API

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLast_app() {
        return last_app;
    }

    public String getLast_ip_address() {
        return last_ip_address;
    }

    public Date getLast_heard() {
        return last_heard;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public Boolean getConnected() {
        return connected;
    }

    /**
     * Any name that is not the name of a field of this class is considered a variable.
     *
     * @param varName
     *            the name of the variable we are looking for
     * @return
     *         true if we did not find a field of the given name
     */
    public static boolean isVar(String varName) {
        try {
            AbstractDevice.class.getDeclaredField(varName);
            return false;
        } catch (NoSuchFieldException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Return the value of the named variable, or <code>null</code> if no such variable exists.
     *
     * @param varName
     *            the variable name
     * @return
     *         the value of the named variable, or <code>null</code> if no such variable exists.
     */
    public Object getVar(String varName) {
        return vars.get(varName);
    }

    /**
     * Set the named variable to the given value.
     *
     * @param varName
     *            the variable name
     * @param value
     *            the value to associate with the variable
     */
    public void setVar(String varName, Object value) {
        vars.put(varName, value);
    }

    /**
     * Given the context kept in the {@link GaradgetSubscriber}, return the most
     * appropriate {@link State} for the given value.
     *
     * @param value
     *            the value to convert into a {@link State}
     * @param subscriber
     *            the context of the subscriber to use to return an accepted {@link State}
     * @return
     *         an accepted {@link State} for the given value
     */
    public State getObjState(Object value, GaradgetSubscriber subscriber) {

        if (value == null) {
            return UnDefType.NULL;
        } else if (value instanceof Boolean) {
            for (Class<? extends State> dataType : subscriber.getAcceptedDataTypes()) {
                if (dataType == OnOffType.class) {
                    return (Boolean) value ? OnOffType.ON : OnOffType.OFF;
                } else if (dataType == OpenClosedType.class) {
                    return (Boolean) value ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
                }
            }
        } else if (value instanceof Date) {
            for (Class<? extends State> dataType : subscriber.getAcceptedDataTypes()) {
                if (dataType == DateTimeType.class) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime((Date) value);
                    return new DateTimeType(cal);
                }
            }
        }
        return TypeParser.parseState(subscriber.getAcceptedDataTypes(), value.toString());
    }

    /**
     * Return an openHAB state that is of a type on the {@code acceptedDataTypes} list. Otherwise, return
     * <code>UnDefType.NULL</code>.
     *
     * @param key
     * @param acceptedDataTypes
     * @return an acceptable state
     */
    public State getVarState(GaradgetSubscriber subscriber) {
        return getObjState(getVar(subscriber.getVarName()), subscriber);
    }

    /**
     * Get the most appropriate {@link State} for the given subscriber's variable name and context.
     *
     * @param subscriber
     *            the subscriber containing the variable name to get, and further context for the conversion
     * @return
     *         the most appropriate {@link State} for the named variable
     */
    public State getState(GaradgetSubscriber subscriber) {
        switch (subscriber.getVarName()) {
            case "id":
                return getObjState(id, subscriber);
            case "name":
                return getObjState(name, subscriber);
            case "last_app":
                return getObjState(last_app, subscriber);
            case "last_ip_address":
                return getObjState(last_ip_address, subscriber);
            case "last_heard":
                return getObjState(last_heard, subscriber);
            case "product_id":
                return getObjState(product_id, subscriber);
            case "connected":
                return getObjState(connected, subscriber);
            default:
                return getVarState(subscriber);
        }
    }
}
