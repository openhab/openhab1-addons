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
package org.openhab.binding.rpircswitch;

import org.openhab.binding.rpircswitch.internal.RPiRcSwitchBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and RC switch devices.
 *
 * @author Matthias Röckl
 * @since 1.8.0
 */
public interface RPiRcSwitchBindingProvider extends BindingProvider {

    /**
     * Returns the configuration for the item with the given name
     * 
     * @param itemName
     *            the name of the item
     * @return the configuration of the item
     */
    RPiRcSwitchBindingConfig getItemConfig(String itemName);
}
