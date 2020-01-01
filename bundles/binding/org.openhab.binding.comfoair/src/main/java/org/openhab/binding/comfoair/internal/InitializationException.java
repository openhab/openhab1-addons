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
package org.openhab.binding.comfoair.internal;

/**
 * Default serial port initialization exception. Is used to catch and handle
 * different serial ios exceptions
 *
 * @author Holger Hees
 * @since 1.3.0
 */
public class InitializationException extends Exception {

    private static final long serialVersionUID = -5106059856757667266L;

    /**
     * Initialize the exceptions with an error message
     *
     * @param msg
     */
    public InitializationException(String msg) {
        super(msg);
    }

    /**
     * Initialize the exceptions with a encapsulated exceptions
     *
     * @param msg
     */
    public InitializationException(Throwable cause) {
        super(cause);
    }

    /**
     * Initialize the exceptions with a error message and a encapsulated exceptions
     *
     * @param msg
     */
    public InitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}