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
package org.openhab.binding.omnilink;

import org.openhab.binding.omnilink.internal.OmniLinkBindingConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

/**
 * Binds a item to a Omnilink System
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public interface OmniLinkBindingProvider extends BindingProvider {

    /**
     * Returns the name of the item
     * 
     * @param itemName
     * @return
     */
    public Item getItem(String itemName);

    /**
     * Returns the binding configuration for the item
     * 
     * @param itemName
     * @return
     */
    public OmniLinkBindingConfig getOmniLinkBindingConfig(String itemName);

}
