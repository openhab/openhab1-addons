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
package org.openhab.binding.stiebelheatpump.internal;

/**
 * Exception for Stiebel heat pump errors.
 *
 * @author Peter Kreutzer
 */
public class StiebelHeatPumpException extends Exception {

    private static final long serialVersionUID = -6984210580603245188L;

    public StiebelHeatPumpException() {
        super();
    }

    public StiebelHeatPumpException(String message) {
        super(message);
    }

    public StiebelHeatPumpException(String message, Throwable cause) {
        super(message, cause);
    }

    public StiebelHeatPumpException(Throwable cause) {
        super(cause);
    }

}
