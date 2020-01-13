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
package org.openhab.binding.davis.internal;

/**
 * Default serial port initialization exception. Is used to catch and handle
 * different serial ios exceptions
 *
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class InitializationException extends Exception {

    private static final long serialVersionUID = -4304325635378784203L;

    /**
     * Initialize the exceptions with an error message
     * 
     * @param msg
     */
    public InitializationException(String msg) {
        super(msg);
    }

    public InitializationException(Throwable e) {
        super(e);
    }

}
