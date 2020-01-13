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
package org.openhab.binding.insteonplm.internal.device;

import java.util.HashMap;

/**
 * Ugly little helper class to facilitate late instantiation of handlers
 *
 * @author Bernd Pfrommer
 * @since 1.7.0
 */
public class HandlerEntry {
    HandlerEntry(String name, HashMap<String, String> params) {
        m_hname = name;
        m_params = params;
    }

    HashMap<String, String> m_params = null;
    String m_hname = null;

    HashMap<String, String> getParams() {
        return m_params;
    }

    String getName() {
        return m_hname;
    }
}
