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
package org.openhab.binding.bticino.internal;

/**
 * InitializationException
 *
 * @author Tom De Vlaminck
 * @serial 1.0
 * @since 1.7.0
 */
public class InitializationException extends Exception {

    private static final long serialVersionUID = -5106059856757667267L;

    public InitializationException(String msg) {
        super(msg);
    }

    public InitializationException(Throwable cause) {
        super(cause);
    }

    public InitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
