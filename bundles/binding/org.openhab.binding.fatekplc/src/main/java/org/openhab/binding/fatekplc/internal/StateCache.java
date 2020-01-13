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
package org.openhab.binding.fatekplc.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.fatekplc.items.FatekPLCItem;
import org.openhab.core.types.State;

/**
 * Implementing cache state.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
class StateCache {

    private Map<String, State> cache = new HashMap<>();

    boolean isStateChange(FatekPLCItem item, State newState) {

        State oldState = cache.get(item.getItemName());

        if (newState != null && newState.equals(oldState)) {
            return false;
        } else {
            cache.put(item.getItemName(), newState);
            return true;
        }
    }
}
