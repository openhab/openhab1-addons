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
package org.openhab.binding.satel.internal.event;

/**
 * Event listener interface. All classes that want to receive Satel events must
 * implement this interface.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public interface SatelEventListener {

    /**
     * Event handler for Satel events.
     * 
     * @param event
     *            incoming event to handle
     */
    void incomingEvent(SatelEvent event);
}
