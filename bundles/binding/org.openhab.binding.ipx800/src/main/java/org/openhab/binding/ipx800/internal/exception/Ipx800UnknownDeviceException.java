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
package org.openhab.binding.ipx800.internal.exception;

/**
 * Device doesn't exist in configuration
 * 
 * @author Seebag
 * @since 1.8.0
 *
 */
public class Ipx800UnknownDeviceException extends Ipx800Exception {

    public Ipx800UnknownDeviceException(String message) {
        super(message);
    }

    /**
     * 
     */
    private static final long serialVersionUID = -4408071685549295251L;

}
