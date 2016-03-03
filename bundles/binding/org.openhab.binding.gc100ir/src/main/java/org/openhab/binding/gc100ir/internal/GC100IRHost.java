/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gc100ir.internal;

/**
 * Connection properties for an GC100 instance
 * 
 * @author Parikshit Thakur & Team
 * @since 1.9.0
 */
public class GC100IRHost {

    private String hostname = "127.0.0.1";

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
