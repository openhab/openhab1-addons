/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.swegonventilation.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Simple value cache.
 *
 * @author Pauli Anttila
 */
public class ValueCache<T, V> {

    private Map<T, V> stateMap = Collections.synchronizedMap(new HashMap<T, V>());

    public void clear() {
        stateMap.clear();
    }

    public boolean isValueChanged(T type, V value) {
        V oldValue = stateMap.get(type);
        if ((value == null && oldValue == null) || (value != null && value.equals(oldValue))) {
            return false;
        }
        return true;
    }

    public void update(T type, V value) {
        stateMap.put(type, value);
    }
}
