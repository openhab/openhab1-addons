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
package org.openhab.binding.akm868;

import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * @author Michael Heckmann
 * @since 1.8.0
 */
public interface AKM868BindingProvider extends BindingProvider {

    /**
     * Returns the item object by itemName.
     */
    public Item getItem(String itemName);

    /**
     * Returns the id of the object by itemName.
     */
    public String getId(String itemName);

    /**
     * Returns the channel of the object by itemName.
     */
    public String getChannel(String itemName);

}
