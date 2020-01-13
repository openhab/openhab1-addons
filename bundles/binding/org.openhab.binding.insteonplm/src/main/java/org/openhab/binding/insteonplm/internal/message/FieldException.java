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
package org.openhab.binding.insteonplm.internal.message;

/**
 * Exception to be thrown if there is an error processing a field, for
 * example type mismatch, out of bounds etc.
 *
 * @author Daniel Pfrommer
 * @since 1.5.0
 */
@SuppressWarnings("serial")
public class FieldException extends Exception {
    public FieldException() {
        super();
    }

    public FieldException(String m) {
        super(m);
    }

    public FieldException(String m, Throwable cause) {
        super(m, cause);
    }

    public FieldException(Throwable cause) {
        super(cause);
    }
}
