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
package org.openhab.binding.gc100ir.util;

/**
 * Stores GC100 item's properties.
 * 
 * @author Parikshit Thakur & Team
 * @since 1.9.0
 * 
 */
public class GC100ItemBean {

    String gc100Instance;
    int module;
    int connector;
    String code;

    public String getGC100Instance() {
        return gc100Instance;
    }

    public void setGC100Instance(String gc100Instance) {
        this.gc100Instance = gc100Instance;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public int getConnector() {
        return connector;
    }

    public void setConnector(int connector) {
        this.connector = connector;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
