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
 * This is an abstract parent exception to be extended by any exceptions
 * related to item lookups in the item registry.
 *
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public abstract class ItemLookupException extends Exception {

    public ItemLookupException(String string) {
        super(string);
    }

    private static final long serialVersionUID = -4617708589675048859L;

}
