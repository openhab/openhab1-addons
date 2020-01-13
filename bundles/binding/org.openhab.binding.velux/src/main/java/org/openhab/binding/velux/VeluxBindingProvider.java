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
package org.openhab.binding.velux;

import java.util.List;

import org.openhab.binding.velux.internal.VeluxBindingConfig;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;

/**
 * This interface is implemented by classes that can provide mapping information
 * between openHAB items and Velux items.
 *
 * Implementing classes should register themselves as a service in order to be
 * taken into account.
 *
 * @author Guenther Schreiner - Initial contribution
 * @since 1.13.0
 */
public interface VeluxBindingProvider extends AutoUpdateBindingProvider {

    /**
     * Returns the <code>config</code> to the given <code>itemName</code>.
     *
     * @param itemName the item for which to find an id.
     * @return the corresponding <code>config</code> to the given <code>itemName</code>
     */
    public VeluxBindingConfig getConfigForItemName(String itemName);

    /**
     * Returns all items which are mapped to a Velux-In-Binding
     *
     * @return <B>items</B> as list of type String which are mapped to a Velux-In-Binding.
     */
    public List<String> getInBindingItemNames();

}
