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
package org.openhab.binding.netatmo.internal;

/**
 * @author Andreas Brenk
 * @since 1.4.0
 */
public class NetatmoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NetatmoException(String message) {
        super(message);
    }

    public NetatmoException(final Throwable cause) {
        super(cause);
    }

    public NetatmoException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
