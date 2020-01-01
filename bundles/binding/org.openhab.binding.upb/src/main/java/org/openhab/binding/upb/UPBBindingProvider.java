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
package org.openhab.binding.upb;

import org.openhab.binding.upb.internal.UPBBindingConfig;
import org.openhab.core.binding.BindingProvider;

/**
 * Interface for the {@link BindingProvider} for the UPB binding.
 *
 * @author cvanorman
 * @since 1.9.0
 */
public interface UPBBindingProvider extends BindingProvider {

    /**
     * Gets the configuration of an item.
     *
     * @param itemName
     *            the name of the item.
     * @return the {@link UPBBindingConfig} for the given item or null if one
     *         does not exist.
     */
    UPBBindingConfig getConfig(String itemName);
}
