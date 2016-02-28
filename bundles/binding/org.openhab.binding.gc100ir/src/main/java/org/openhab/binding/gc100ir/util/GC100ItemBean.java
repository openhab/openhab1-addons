/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
