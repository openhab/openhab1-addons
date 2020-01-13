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
package org.openhab.binding.ipx800.internal.handler;

import java.util.Map;

import org.openhab.binding.ipx800.internal.itemslot.Ipx800Item;

/**
 * Generic class to handle items updates connected to an ipx800 port
 * 
 * @author Seebag
 * @since 1.8.0
 */
public interface Ipx800Handler {
    public boolean updateState(Map<String, Ipx800Item> items, String state);
}
