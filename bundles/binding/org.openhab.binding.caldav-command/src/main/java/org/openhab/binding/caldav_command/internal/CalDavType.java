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
package org.openhab.binding.caldav_command.internal;

/**
 * Item type.
 * Actions which can be performed on items.
 *
 * @author Robert Delbrück
 * @since 1.8.0
 */
public enum CalDavType {
    /**
     * Get the value from the event (triggered at BEGIN or END).
     */
    VALUE,
    /**
     * Get the date when the item will next be switched.
     */
    DATE,
    /**
     * Disables the event triggered execution.
     */
    DISABLE
}
