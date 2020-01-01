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
package org.openhab.binding.urtsi.internal;

/**
 * The InitializationException indicates that an error occurred while initializing the device or while parsing the
 * binding configuration.
 * 
 * @author Oliver Libutzki
 * @since 1.3.0
 *
 */
public class InitializationException extends Exception {

    private static final long serialVersionUID = -5106059856757667266L;

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
