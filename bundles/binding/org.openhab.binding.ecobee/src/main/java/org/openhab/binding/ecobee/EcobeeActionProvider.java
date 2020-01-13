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
package org.openhab.binding.ecobee;

import org.openhab.binding.ecobee.messages.AbstractFunction;

/**
 * openHAB Action Provider interface for Ecobee thermostats.
 *
 * @author John Cocula
 * @since 1.8.0
 */
public interface EcobeeActionProvider {
    /**
     * Call the specified Ecobee function.
     *
     * Requests the named Ecobee function, associated with the Item be invoked. Callers can pass an AbstractFunction to
     * be
     * called.
     *
     * @param selection
     *            the selection of thermostat(s) against which the Action invocation should be performed.
     * @param function
     *            the function to call using the specified itemName.
     * @return true if the function call was performed.
     */
    public boolean callEcobee(String selection, AbstractFunction function);
}
