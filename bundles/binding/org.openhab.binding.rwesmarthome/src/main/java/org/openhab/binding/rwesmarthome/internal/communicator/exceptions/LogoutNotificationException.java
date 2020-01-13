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
package org.openhab.binding.rwesmarthome.internal.communicator.exceptions;

/**
 * Exception if user was logged out.
 *
 * @author ollie-dev
 *
 */
public class LogoutNotificationException extends SHFunctionalException {
    /**
     * 
     */
    private static final long serialVersionUID = -3885036245387829505L;

    /**
     * Constructor with message.
     * 
     * @param message
     */
    public LogoutNotificationException(String message) {
        super(message);
    }
}
