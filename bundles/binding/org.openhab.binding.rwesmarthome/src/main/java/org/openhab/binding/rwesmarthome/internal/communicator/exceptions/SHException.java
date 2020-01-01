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
 * Base exception for RWE Smarthome.
 *
 * @author ollie-dev
 *
 */
public class SHException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 4861308355876842323L;

    /**
     * Constructor.
     */
    public SHException() {
        super();
    }

    /**
     * Constructor with message and throwable.
     * 
     * @param detailMessage
     * @param throwable
     */
    public SHException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructor with message.
     * 
     * @param detailMessage
     */
    public SHException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructor throwable.
     * 
     * @param throwable
     */
    public SHException(Throwable throwable) {
        super(throwable);
    }

}
