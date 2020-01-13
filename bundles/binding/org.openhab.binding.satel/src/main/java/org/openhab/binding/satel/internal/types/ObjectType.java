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
package org.openhab.binding.satel.internal.types;

/**
 * Available object types:
 * <ul>
 * <li>ZONE - various kinds of devices connected to inputs, like reed switches, PIRs, etc</li>
 * <li>PARTITION - group of zones and outputs</li>
 * <li>OUTPUT - outputs to various devices like sirens, relays, etc.</li>
 * <li>DOORS - inputs connected to reed switches mounted on doors</li>
 * <li>TROUBLE - indication of a particular trouble</li>
 * <li>TROUBLE_MEMORY - particular trouble state latched</li>
 * </ul>
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public enum ObjectType {
    ZONE,
    PARTITION,
    OUTPUT,
    DOORS,
    TROUBLE,
    TROUBLE_MEMORY;
}
