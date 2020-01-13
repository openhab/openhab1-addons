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
package org.openhab.binding.maxcube;

import org.openhab.binding.maxcube.internal.BindingType;
import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and the MAX!Cube LAN gateway items.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author Andreas Heil (info@aheil.de)
 * @since 1.4.0
 */
public interface MaxCubeBindingProvider extends BindingProvider {

    /**
     * @return the corresponding serial number of the given <code>itemName</code>
     */
    public String getSerialNumber(String itemName);

    /**
     * @return the binding type if specified, <code>null</code> otherwise.
     */
    public BindingType getBindingType(String itemName);
}
