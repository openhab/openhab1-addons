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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle boolean values which are handled as decimal 0/1 states
 *
 * @author Holger Hees
 * @since 1.3.0
 */
public class DataTypeBoolean implements ComfoAirDataType {

    private Logger logger = LoggerFactory.getLogger(DataTypeBoolean.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public State convertToState(int[] data, ComfoAirCommandType commandType) {

        if (data == null || commandType == null) {
            logger.trace("\"DataTypeBoolean\" class \"convertToState\" method parameter: null");
            return null;
        } else {

            int[] get_reply_data_pos = commandType.getGetReplyDataPos();
            int get_reply_data_bits = commandType.getGetReplyDataBits();

            if (get_reply_data_pos[0] < data.length) {
                boolean result = (data[get_reply_data_pos[0]] & get_reply_data_bits) == get_reply_data_bits;
                return (result) ? new DecimalType(1) : new DecimalType(0);
            } else {
                return null;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] convertFromState(State value, ComfoAirCommandType commandType) {

        if (value == null || commandType == null) {
            logger.trace("\"DataTypeBoolean\" class \"convertFromState\" method parameter: null");
            return null;
        } else {

            int[] template = commandType.getChangeDataTemplate();

            template[commandType.getChangeDataPos()] = ((DecimalType) value).intValue() == 1
                    ? commandType.getPossibleValues()[0]
                    : 0x00;

            return template;
        }
    }

}