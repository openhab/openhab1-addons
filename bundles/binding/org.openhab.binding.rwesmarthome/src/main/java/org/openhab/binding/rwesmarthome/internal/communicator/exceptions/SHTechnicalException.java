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
 * Exception if technical errors occur.
 *
 * @author ollie-dev
 *
 */
public class SHTechnicalException extends SHException {

    /**
     * Constructor.
     */
    public SHTechnicalException() {
        super();
    }

    /**
     * Constructor with message and throwable.
     * 
     * @param detailMessage
     * @param throwable
     */
    public SHTechnicalException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * Constructor with message.
     * 
     * @param detailMessage
     */
    public SHTechnicalException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Constructor with throwable.
     * 
     * @param throwable
     */
    public SHTechnicalException(Throwable throwable) {
        super(throwable);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 7670415727186606365L;

}
