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
package org.openhab.binding.plugwise.internal;

/**
 * Exception used during stick initialization
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PlugwiseInitializationException extends Exception {

    private static final long serialVersionUID = 2095258016390913221L;

    public PlugwiseInitializationException(String msg) {
        super(msg);
    }

    public PlugwiseInitializationException(Throwable cause) {
        super(cause);
    }

    public PlugwiseInitializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
