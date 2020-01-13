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
package org.openhab.binding.denon.internal;

import org.openhab.core.types.State;

/**
 * Callback interface to signal that a property was updated.
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public interface DenonPropertyUpdatedCallback {

    /**
     * Property was updated.
     * 
     * @param instance Name of the Denon receiver instance
     * @param property The property that was updated
     * @param state Its current state
     */
    public void updated(String instance, String property, State state);

}
