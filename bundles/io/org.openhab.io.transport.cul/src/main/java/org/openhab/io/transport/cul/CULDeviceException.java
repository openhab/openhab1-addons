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
package org.openhab.io.transport.cul;

/**
 * An exception which represents error while opening/connecting to the culfw
 * based device.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULDeviceException extends Exception {

    private static final long serialVersionUID = 4834148919102194993L;

    public CULDeviceException() {
        super();
    }

    public CULDeviceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CULDeviceException(String message) {
        super(message);
    }

    public CULDeviceException(Throwable cause) {
        super(cause);
    }

}
