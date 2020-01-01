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
package org.openhab.binding.openenergymonitor.internal;

/**
 * Exception for Open Energy Monitor errors.
 *
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorException extends Exception {

    private static final long serialVersionUID = 572419226403043307L;

    public OpenEnergyMonitorException() {
        super();
    }

    public OpenEnergyMonitorException(String message) {
        super(message);
    }

    public OpenEnergyMonitorException(String message, Throwable cause) {
        super(message, cause);
    }

    public OpenEnergyMonitorException(Throwable cause) {
        super(cause);
    }

}
