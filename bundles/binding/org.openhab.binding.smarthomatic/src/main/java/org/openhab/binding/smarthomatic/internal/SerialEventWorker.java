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
package org.openhab.binding.smarthomatic.internal;

/**
 * Interface for serial events
 *
 * @author arohde
 * @since 1.9.0
 */
public interface SerialEventWorker {

    /**
     * fired is a event on the serial line has occurred
     * 
     * @param message
     */
    public void eventOccured(String message);

}
