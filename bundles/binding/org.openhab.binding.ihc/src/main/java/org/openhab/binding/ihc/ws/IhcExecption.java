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
package org.openhab.binding.ihc.ws;

/**
 * Exception for handling communication errors to controller.
 *
 * @author Pauli Anttila
 * @since 1.5.0
 */
public class IhcExecption extends Exception {

    private static final long serialVersionUID = -8048415193494625295L;

    public IhcExecption() {
        super();
    }

    public IhcExecption(String message) {
        super(message);
    }

    public IhcExecption(String message, Throwable cause) {
        super(message, cause);
    }

    public IhcExecption(Throwable cause) {
        super(cause);
    }

}
