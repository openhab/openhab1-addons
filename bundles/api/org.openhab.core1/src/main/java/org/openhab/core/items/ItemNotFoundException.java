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

/**
 * This exception is thrown by the {@link ItemRegistry} if an item could
 * not be found.
 *
 * @author Kai Kreuzer
 *
 */
public class ItemNotFoundException extends ItemLookupException {

    public ItemNotFoundException(String name) {
        super("Item '" + name + "' could not be found in the item registry");
    }

    private static final long serialVersionUID = -3720784568250902711L;

}
