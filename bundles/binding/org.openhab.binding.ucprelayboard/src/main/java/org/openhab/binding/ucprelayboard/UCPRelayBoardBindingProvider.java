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
package org.openhab.binding.ucprelayboard;

/**
 * @author Robert Michalak
 * @since 1.8.0
 */
import org.openhab.binding.ucprelayboard.internal.UCPRelayConfig;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;

public interface UCPRelayBoardBindingProvider extends BindingProvider {

    /**
     * Returns the binding configuration for an item
     * 
     * @param itemName
     * @return
     */
    public UCPRelayConfig getRelayConfigForItem(String itemName);

    public Item getItemForRelayConfig(UCPRelayConfig config);

}