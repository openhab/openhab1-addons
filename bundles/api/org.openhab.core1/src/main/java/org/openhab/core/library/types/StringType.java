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
package org.openhab.core.library.types;

import org.openhab.core.types.Command;
import org.openhab.core.types.PrimitiveType;
import org.openhab.core.types.State;

/**
 *
 * @author Kai Kreuzer - Initial contribution
 */
public class StringType implements PrimitiveType, State, Command {

    public final static StringType EMPTY = new StringType("");

    private final String value;

    public StringType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static StringType valueOf(String value) {
        return new StringType(value);
    }

    @Override
    public String format(String pattern) {
        return String.format(pattern, value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            return obj.equals(value);
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        StringType other = (StringType) obj;
        if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

}
