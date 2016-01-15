/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
