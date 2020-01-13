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
package org.openhab.core.types;

/**
 * There are situations when item states do not have any defined value.
 * This might be because they have not been initialized yet (never
 * received an state update so far) or because their state is ambiguous
 * (e.g. a dimmed light that is treated as a switch (ON/OFF) will have
 * an undefined state if it is dimmed to 50%).
 *
 * @author Kai Kreuzer
 * @since 0.1.0
 *
 */
public enum UnDefType implements PrimitiveType, State {
    UNDEF,
    NULL;

    @Override
    public String toString() {
        switch (this) {
            case UNDEF:
                return "Undefined";
            case NULL:
                return "Uninitialized";
        }
        return "";
    }

    @Override
    public String format(String pattern) {
        return String.format(pattern, this.toString());
    }

}
