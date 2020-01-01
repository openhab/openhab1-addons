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
package org.openhab.binding.oceanic;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Karel Goderis
 * @since 1.5.0
 *
 */
public interface OceanicBindingProvider extends BindingProvider {

    /**
     * Returns the id to the given <code>itemName</code>.
     * 
     * @param itemName
     *            the item for which to find a id.
     * 
     * @return the corresponding id to the given <code>itemName</code>.
     */
    public String getSerialPort(String itemName);

    /**
     * Returns the value selector to the given <code>itemName</code>.
     * 
     * @param itemName
     *            the item for which to find a unit code.
     * 
     * @return the corresponding value selector to the given
     *         <code>itemName</code>.
     */
    public String getValueSelector(String itemName);

    public int getPollingInterval(String itemName);

}
