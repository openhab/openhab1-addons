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
package org.openhab.binding.rwesmarthome;

import org.openhab.binding.rwesmarthome.internal.RWESmarthomeGenericBindingProvider.RWESmarthomeBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Interface for the RWE Smarthome binding provider.
 *
 * @author ollie-dev
 * @since 1.8.0
 */
public interface RWESmarthomeBindingProvider extends BindingProvider {

    /**
     * Returns the item object by itemName.
     */
    Item getItem(String itemName);

    /**
     * Returns the itemname of the logicaldeviceid.
     */
    String getItemNameById(String id);

    /**
     * Returns the itemname of the logicaldeviceid with the given param.
     */
    String getItemNameByIdAndParam(String id, String param);

    /**
     * Returns the bindingConfig by itemName.
     */
    RWESmarthomeBindingConfig getBindingConfigFor(String itemName);
}
