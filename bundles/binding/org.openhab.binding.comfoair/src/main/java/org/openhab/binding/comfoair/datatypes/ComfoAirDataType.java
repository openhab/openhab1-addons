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
package org.openhab.binding.comfoair.datatypes;

import org.openhab.binding.comfoair.handling.ComfoAirCommandType;
import org.openhab.core.types.State;

/**
 * Abstract class to convert binary hex values into openHAB states and vice
 * versa
 *
 * @author Holger Hees
 * @since 1.3.0
 */
public interface ComfoAirDataType {

    /**
     * Generate a openhab State object based on response data.
     *
     * @param data
     * @param commandType
     * @return converted State object
     */
    State convertToState(int[] data, ComfoAirCommandType commandType);

    /**
     * Generate byte array based on a openhab State.
     *
     * @param value
     * @param commandType
     * @return converted byte array
     */
    int[] convertFromState(State value, ComfoAirCommandType commandType);

}