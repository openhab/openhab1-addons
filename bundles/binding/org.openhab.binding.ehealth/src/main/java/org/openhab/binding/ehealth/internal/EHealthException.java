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
package org.openhab.binding.ehealth.internal;

/**
 * A specialised Exception in the context of the Binding.
 *
 * @author Thomas Eichstaedt-Engelen
 * @since 1.6.0
 */
public class EHealthException extends Exception {

    /** generated serial version uid */
    private static final long serialVersionUID = -1242847455651393036L;

    public EHealthException(Exception e) {
        super(e);
    }

    public EHealthException(String message) {
        super(message);
    }

}
