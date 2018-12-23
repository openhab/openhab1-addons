/**
 * Copyright (c) 2017-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * @author Guenther Schreiner
 * @since 1.13.0
 */
public interface VeluxBindingProvider extends AutoUpdateBindingProvider {

    /**
     * Returns the <code>config</code> to the given <code>itemName</code>.
     *
     * @param itemName
     *                     the item for which to find a id.
     *
     * @return the corresponding <code>config</code> to the given <code>itemName</code>.
     */
    public VeluxBindingConfig getConfigForItemName(String itemName);

    /**
     * Returns all items which are mapped to a Velux-In-Binding
     *
     * @return item which are mapped to a Velux-In-Binding
     */
    public List<String> getInBindingItemNames();

}

/*
 * end-of-VeluxBindingProvider.java
 */
