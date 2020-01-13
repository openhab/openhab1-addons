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
package org.openhab.binding.dmx;

import org.openhab.binding.dmx.internal.config.DmxItem;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.types.State;

/**
 * DMX Binding Provider Interface.
 *
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public interface DmxBindingProvider extends BindingProvider {

    /**
     * Get the BindingConfig for a given itemName.
     * 
     * @param itemName
     *            the item for which to get the binding configuration.
     * @return BindingConfig for the item or null.
     */
    public DmxItem getBindingConfig(String itemName);

    /**
     * Send a status update to the openhab bus.
     * 
     * @param itemName
     *            item for which to send update
     * @param state
     *            status
     */
    public void postUpdate(String itemName, State state);

}
