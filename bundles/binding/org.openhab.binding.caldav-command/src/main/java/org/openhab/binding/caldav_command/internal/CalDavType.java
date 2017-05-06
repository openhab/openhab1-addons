/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
