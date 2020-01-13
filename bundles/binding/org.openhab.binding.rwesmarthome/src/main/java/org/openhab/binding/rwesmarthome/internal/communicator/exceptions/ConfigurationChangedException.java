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
package org.openhab.binding.rwesmarthome.internal.communicator.exceptions;

/**
 * Exception if the configuration has changed on the SHC in between.
 *
 * @author ollie-dev
 *
 */
public class ConfigurationChangedException extends SHFunctionalException {

    /**
     * 
     */
    private static final long serialVersionUID = -4999720741275874292L;

    /**
     * Constructor
     * 
     * @param message
     */
    public ConfigurationChangedException(String message) {
        super(message);
    }
}
