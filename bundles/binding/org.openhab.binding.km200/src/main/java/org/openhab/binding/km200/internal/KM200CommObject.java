/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.km200.internal;

/**
 * The KM200CommObject representing a service on a device with its all capabilities
 *
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */

public class KM200CommObject {
    private Integer readable;
    private Integer writeable;
    private Integer recordable;
    private String fullServiceName = "";
    private String serviceType = "";
    private Object value = null;
    private Object valueParameter = null;

    public KM200CommObject(String serviceName, String type, Integer read, Integer write, Integer record) {
        fullServiceName = serviceName;
        serviceType = type;
        readable = read;
        writeable = write;
        recordable = record;
    }

    public KM200CommObject(String serviceName, String type, Integer write, Integer record) {
        fullServiceName = serviceName;
        serviceType = type;
        readable = 1;
        writeable = write;
        recordable = record;
    }

    /* Sets */
    public void setValue(Object val) {
        value = val;
    }

    public void setValueParameter(Object val) {
        valueParameter = val;
    }

    /* gets */
    public Integer getReadable() {
        return readable;
    }

    public Integer getWriteable() {
        return writeable;
    }

    public Integer getRecordable() {
        return recordable;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getFullServiceName() {
        return fullServiceName;
    }

    public Object getValue() {
        return value;
    }

    public Object getValueParameter() {
        return valueParameter;
    }
}
