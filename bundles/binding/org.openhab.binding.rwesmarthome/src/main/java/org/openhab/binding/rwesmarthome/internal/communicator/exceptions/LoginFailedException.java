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
 * Exception if login has failed.
 * 
 * @author ollie-dev
 *
 */
public class LoginFailedException extends SHFunctionalException {

    /**
     * 
     */
    private static final long serialVersionUID = 3148382279756806820L;

    /**
     * Constructor
     */
    public LoginFailedException() {
        super();
    }

    /**
     * Constructor with message and throwable.
     * 
     * @param detailMessage
     * @param throwable
     */
    public LoginFailedException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructor with message.
     * 
     * @param detailMessage
     */
    public LoginFailedException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructor with throwable.
     * 
     * @param throwable
     */
    public LoginFailedException(Throwable throwable) {
        super(throwable);
    }

}
