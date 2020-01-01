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
package org.openhab.binding.panstamp;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Gideon le Grange
 * @since 1.8.0
 */
public interface PanStampBindingProvider extends BindingProvider {

    /**
     * Return the configuration for the named item
     * 
     * @param itemName
     *            The item name for which to lookup the configuration.
     * @return The configuration
     */
    public PanStampBindingConfig<?> getConfig(String itemName);

}
