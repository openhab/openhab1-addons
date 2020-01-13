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
package org.openhab.binding.networkhealth;

import org.openhab.core.binding.BindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and NetworkHealth items.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public interface NetworkHealthBindingProvider extends BindingProvider {

    /**
     * @return the corresponding hostname of the given <code>itemName</code>
     */
    public String getHostname(String itemName);

    /**
     * @return the corresponding port of the given <code>itemName</code>
     */
    public int getPort(String itemName);

    /**
     * @return the corresponding timeout of the given <code>itemName</code>
     */
    public int getTimeout(String itemName);

}
