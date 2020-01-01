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
package org.openhab.binding.pilight.internal.types;

import org.openhab.core.library.types.OpenClosedType;

/**
 * Enum to represent the state of a contact sensor in pilight
 *
 * @author Jeroen Idserda
 * @since 1.7
 */
public enum PilightContactType {
    OPENED,
    CLOSED;

    public OpenClosedType toOpenClosedType() {
        return this.equals(PilightContactType.OPENED) ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
    }
}
