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
package org.openhab.action.xmpp.internal;

/**
 * This exception is used if the XMPP/XMPP connection has not
 * been initialized.
 *
 * @author Kai Kreuzer
 * @since 0.4.0
 *
 */
public class NotInitializedException extends Exception {

    private static final long serialVersionUID = 4706382830782417755L;

    public NotInitializedException() {
        super();
    }

    public NotInitializedException(String message) {
        super(message);
    }
}
