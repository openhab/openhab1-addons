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
package org.openhab.binding.panstamp.internal;

/**
 * Thrown if the internals of the PanStampBinding has a problem with data conversion.
 *
 * @author Gideon le Grange
 * @since 1.8.0
 */
public class ValueException extends Exception {

    private static final long serialVersionUID = 1L;

    public ValueException(String message) {
        super(message);
    }

    public ValueException(String message, Throwable cause) {
        super(message, cause);
    }

}
