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
package org.openhab.binding.fritzaha.internal.hardware.interfaces;

import org.openhab.core.binding.BindingConfig;

/**
 * Classes implementing this interface are responsible for storing the binding
 * configuration and carrying out actions.
 *
 * @author Christian Brauers
 * @since 1.3.0
 */
public interface FritzahaDevice extends BindingConfig {
    /**
     * Getter for host ID
     * 
     * @return Host ID
     */
    public String getHost();

    /**
     * Getter for device ID
     * 
     * @return Device ID
     */
    public String getId();
}
