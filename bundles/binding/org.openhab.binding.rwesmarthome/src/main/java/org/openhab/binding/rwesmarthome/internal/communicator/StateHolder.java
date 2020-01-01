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
package org.openhab.binding.rwesmarthome.internal.communicator;

import org.openhab.binding.rwesmarthome.internal.RWESmarthomeContext;

/**
 * StateHolder to hold the RWE Smarthome context.
 *
 * @author ollie-dev
 *
 */
public class StateHolder {
    private RWESmarthomeContext context;

    /**
     * Constructor with context.
     * 
     * @param context
     */
    public StateHolder(RWESmarthomeContext context) {
        this.context = context;
    }

    /**
     * Destroys the cache.
     */
    public void destroy() {

    }
}
