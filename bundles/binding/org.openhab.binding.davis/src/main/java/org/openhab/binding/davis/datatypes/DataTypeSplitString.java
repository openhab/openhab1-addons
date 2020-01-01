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

import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * Class to handle numeric values encoding rain based on rain clicks
 *
 * @author Trathnigg Thomas
 * @since 1.6.0
 */
public class DataTypeSplitString implements DavisDataType {

    /**
     * {@inheritDoc}
     */
    public State convertToState(byte[] data, DavisValueType valueType) {
        String dataString = new String(data);
        String[] splitString = dataString.split(" ");
        return new StringType(splitString[valueType.getDataOffset()]);
    }

}
