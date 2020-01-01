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
package org.openhab.core.transform;

/**
 * A TransformationException is thrown when any step of a transformation went
 * wrong. The originating exception should be attached to increase traceability.
 *
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.7.0
 */
public class TransformationException extends Exception {

    /** generated serial Version UID */
    private static final long serialVersionUID = -535237375844795145L;

    public TransformationException(String message) {
        super(message);
    }

    public TransformationException(String message, Throwable cause) {
        super(message, cause);
    }

}
