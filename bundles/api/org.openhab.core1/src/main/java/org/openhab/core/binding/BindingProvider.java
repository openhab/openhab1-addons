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
package org.openhab.core.binding;

import java.util.Collection;

/**
 * @author Thomas.Eichstaedt-Engelen
 * @author Kai Kreuzer
 * @since 0.6.0
 */
public interface BindingProvider {

    /**
     * Adds a binding change listener, which gets notified whenever there
     * are changes in the binding configuration
     *
     * @param listener the binding change listener to add
     */
    public void addBindingChangeListener(BindingChangeListener listener);

    /**
     * Removes a binding change listener again.
     * Does nothing, if this listener has not been added before.
     *
     * @param listener the binding listener to remove
     */
    public void removeBindingChangeListener(BindingChangeListener listener);

    /**
     * Indicates whether this binding provider contains a binding for the given
     * <code>itemName</code>
     *
     * @param itemName the itemName to check
     *
     * @return <code>true</code> if this provider contains an adequate mapping
     *         for <code>itemName</code> and <code>false</code> otherwise.
     */
    boolean providesBindingFor(String itemName);

    /**
     * Indicates whether this binding provider contains any binding
     *
     * @return <code>true</code> if this provider contains any binding
     *         configuration and <code>false</code> otherwise
     */
    boolean providesBinding();

    /**
     * Returns all items which are mapped to this binding
     *
     * @return items which are mapped to this binding
     */
    Collection<String> getItemNames();

}
