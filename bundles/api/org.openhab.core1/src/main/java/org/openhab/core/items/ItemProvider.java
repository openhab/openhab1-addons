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
package org.openhab.core.items;

import java.util.Collection;

/**
 * An item provider provides instances of {@link GenericItem}. These
 * items can be constructed from some static configuration files or
 * they can be derived from some dynamic logic.
 *
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public interface ItemProvider {

    /**
     * Provides an array of items.
     *
     * @return a collection of items
     */
    Collection<Item> getItems();

    public void addItemChangeListener(ItemsChangeListener listener);

    public void removeItemChangeListener(ItemsChangeListener listener);
}
