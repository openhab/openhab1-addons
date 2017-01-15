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
    private Integer virtual;
    private Boolean updated = false;
    private String parent;
    private String fullServiceName = "";
    private String serviceType = "";
    private String jsonData = "";
    private Object value = null;
    private Object valueParameter = null;

    public KM200CommObject(String serviceName, String type, Integer read, Integer write, Integer record) {
        fullServiceName = serviceName;
        serviceType = type;
        readable = read;
        writeable = write;
        recordable = record;
        virtual = 0;
        parent = null;
    }

    public KM200CommObject(String serviceName, String type, Integer write, Integer record, Integer virtual,
            String parent) {
        fullServiceName = serviceName;
        serviceType = type;
        readable = 1;
        writeable = write;
        recordable = record;
        this.virtual = virtual;
        this.parent = parent;
    }

    public KM200CommObject(String serviceName, String type, Integer write, Integer record) {
        fullServiceName = serviceName;
        serviceType = type;
        readable = 1;
        writeable = write;
        recordable = record;
        virtual = 0;
        parent = null;
    }

    /* Sets */
    public void setValue(Object val) {
        value = val;
    }

    public void setUpdated(Boolean updt) {
        updated = updt;
    }

    public void setValueParameter(Object val) {
        valueParameter = val;
    }

    public void setJSONData(String data) {
        jsonData = data;
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

    public String getParent() {
        return parent;
    }

    public Integer getVirtual() {
        return virtual;
    }

    public Boolean getUpdated() {
        return updated;
    }

    public String getJSONData() {
        return jsonData;
    }
}
