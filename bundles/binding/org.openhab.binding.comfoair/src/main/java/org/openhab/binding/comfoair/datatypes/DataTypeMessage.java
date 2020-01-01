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
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to handle error messages
 *
 * @author Holger Hees
 * @since 1.3.0
 */
public class DataTypeMessage implements ComfoAirDataType {

    private Logger logger = LoggerFactory.getLogger(DataTypeMessage.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public State convertToState(int[] data, ComfoAirCommandType commandType) {

        if (data == null || commandType == null) {
            logger.trace("\"DataTypeMessage\" class \"convertToState\" method parameter: null");
            return null;
        } else {

            int[] get_reply_data_pos = commandType.getGetReplyDataPos();

            int errorAlo = data[get_reply_data_pos[0]];
            int errorE = data[get_reply_data_pos[1]];
            int errorEA = data[get_reply_data_pos[2]];
            int errorAhi = data[get_reply_data_pos[3]];

            String errorCode = "";

            if (errorAlo > 0) {
                errorCode = "A:" + convertToCode(errorAlo);
            }

            else if (errorAhi > 0) {
                if (errorAhi == 0x80) {
                    errorCode = "A0";
                }
                errorCode = "A:" + (convertToCode(errorAhi) + 8);
            }

            if (errorE > 0) {
                if (errorCode.length() > 0) {
                    errorCode += " ";
                }
                errorCode += "E:" + convertToCode(errorE);
            } else if (errorEA > 0) {
                if (errorCode.length() > 0) {
                    errorCode += " ";
                }
                errorCode += "EA:" + convertToCode(errorEA);
            }

            return new StringType(errorCode.length() > 0 ? errorCode : "Ok");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] convertFromState(State value, ComfoAirCommandType commandType) {
        return null;
    }

    private int convertToCode(int code) {
        if (code == 0x1) {
            return 1;
        }
        if (code == 0x2) {
            return 2;
        }
        if (code == 0x4) {
            return 3;
        }
        if (code == 0x8) {
            return 4;
        }
        if (code == 0xF) {
            return 5;
        }
        if (code == 0x20) {
            return 6;
        }
        if (code == 0x40) {
            return 7;
        }
        if (code == 0x80) {
            return 8;
        }
        return -1;
    }

}