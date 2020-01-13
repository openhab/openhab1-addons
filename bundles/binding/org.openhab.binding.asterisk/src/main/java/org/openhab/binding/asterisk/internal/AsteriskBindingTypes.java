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
package org.openhab.binding.asterisk.internal;

/**
 * Enumerates the various BindingTypes which are allowed for the Asterisk binding
 *
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.9.0
 */
public enum AsteriskBindingTypes {

    /** binds active (i.e. connected) calls to an item */
    ACTIVE {
        {
            name = "active";
        }
    };

    String name;

    public static AsteriskBindingTypes fromString(String bindingType) {

        if ("".equals(bindingType)) {
            return null;
        }

        for (AsteriskBindingTypes type : AsteriskBindingTypes.values()) {
            if (type.name.equals(bindingType)) {
                return type;
            }
        }

        throw new IllegalArgumentException("invalid bindingType '" + bindingType + "'");
    }

}
