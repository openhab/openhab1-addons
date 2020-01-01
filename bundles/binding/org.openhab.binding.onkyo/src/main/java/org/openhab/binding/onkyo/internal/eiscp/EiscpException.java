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
package org.openhab.binding.onkyo.internal.eiscp;

/**
 * Exception for eISCP errors.
 *
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class EiscpException extends Exception {

    private static final long serialVersionUID = -7970958467980752003L;

    public EiscpException() {
        super();
    }

    public EiscpException(String message) {
        super(message);
    }

    public EiscpException(String message, Throwable cause) {
        super(message, cause);
    }

    public EiscpException(Throwable cause) {
        super(cause);
    }

}
