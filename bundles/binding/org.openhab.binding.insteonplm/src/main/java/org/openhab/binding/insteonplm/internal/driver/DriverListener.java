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
package org.openhab.binding.insteonplm.internal.driver;

/**
 * Interface for classes that want to listen to notifications from
 * the driver.
 *
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public interface DriverListener {
    /**
     * Notification that querying of the modems on all ports has successfully completed.
     */
    public abstract void driverCompletelyInitialized();
}
