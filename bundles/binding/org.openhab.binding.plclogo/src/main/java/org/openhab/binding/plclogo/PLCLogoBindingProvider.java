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
package org.openhab.binding.plclogo;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Lehane Kellett
 * @since 1.9.0
 */
public interface PLCLogoBindingProvider extends BindingProvider {
    public PLCLogoBindingConfig getBindingConfig(String itemName);

    /**
     * Returns the item identified by {@code itemName}
     *
     * @param itemName the name of the item to find
     * @return item identified by {@code itemName}
     */
    public Item getItem(String itemName);

}
