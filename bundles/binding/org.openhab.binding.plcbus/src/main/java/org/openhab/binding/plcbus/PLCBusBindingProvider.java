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
package org.openhab.binding.plcbus;

import org.openhab.binding.plcbus.internal.PLCBusBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * Interface for PLCBusBindingProvider
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public interface PLCBusBindingProvider extends BindingProvider {

    /**
     * Provides BindingConfig for ItemName
     * 
     * @param itemName Name of items
     * @return BindingConfig
     */
    PLCBusBindingConfig getConfigFor(String itemName);

}
