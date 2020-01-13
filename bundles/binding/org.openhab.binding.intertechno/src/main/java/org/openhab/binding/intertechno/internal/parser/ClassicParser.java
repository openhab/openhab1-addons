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
package org.openhab.binding.intertechno.internal.parser;

import java.util.List;

import org.openhab.binding.intertechno.internal.CULIntertechnoBinding;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This parser is able to parse classic Intertechno configs.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class ClassicParser extends AbstractGroupAddressParser {

    private static final Logger logger = LoggerFactory.getLogger(CULIntertechnoBinding.class);

    @Override
    public void parseConfig(List<String> configParts) throws BindingConfigParseException {
        super.parseConfig(configParts);

        if (group.length() != 1) {
            throw new BindingConfigParseException("group parameter must contain exactly one character!");
        }

        commandON = getGroupAddress(group.charAt(0)) + getSubAddress(address) + "0F" + "FF";
        commandOFF = getGroupAddress(group.charAt(0)) + getSubAddress(address) + "0F" + "F0";

        logger.trace("commandON = {}", commandON);
        logger.trace("commandOFF = {}", commandOFF);
    }

    private String getGroupAddress(char address) {
        char aChar = 'A';
        int intValue = address - aChar;
        return getEncodedString(4, intValue, 'F', '0');
    }

    private String getSubAddress(int address) {
        return getEncodedString(4, address - 1, 'F', '0');
    }

}
