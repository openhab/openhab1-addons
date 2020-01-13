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
package org.openhab.binding.davis.datatypes;

import org.openhab.core.types.State;

/**
 * Interface to convert binary data into openHAB states
 *
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public interface DavisDataType {

    /**
     * Generate a openhab State object based on response data.
     * 
     * @param data
     * @param valueType
     * @return converted State object
     */
    State convertToState(byte[] data, DavisValueType valueType);

}
