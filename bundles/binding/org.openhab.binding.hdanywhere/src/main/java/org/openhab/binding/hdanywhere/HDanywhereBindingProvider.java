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
package org.openhab.binding.hdanywhere;

import java.util.HashMap;
import java.util.List;

import org.openhab.core.binding.BindingProvider;

/**
 * Interface of the HDanywhere Binding Provider
 *
 * @author Karel Goderis
 * @since 1.4.0
 */
public interface HDanywhereBindingProvider extends BindingProvider {

    /**
     * Returns a <code>List</code> of host names/IP addresses associated to <code>itemName</code>.
     *
     * @param itemName the item for which to find a host names for
     * @return a List of matching host names or <code>null</code> if no host name
     *         could be found.
     */
    List<String> getHosts(String itemName);

    /**
     * Returns a <code>List</code> of HDanywhere matrix output port numbers associated to <code>host</code> and
     * <code>itemName</code>.
     *
     * @param host     the HDanywhere matrix to find matrix output ports for
     * @param itemName the item for which to find matrix output ports for
     * @return a List of matching output port numbers or <code>null</code> if no port numbers
     *         could be found.
     */
    List<Integer> getPorts(String host, String itemName);

    /**
     * Returns a <code>HashMap</code> of {<code>host<code>,<code>interval<code>} tuples with interval being the smallest
     * polling
     * interval for the given <code>host<code>
     *
     * @return a HashMap
     */
    HashMap<String, Integer> getIntervalList();

}
