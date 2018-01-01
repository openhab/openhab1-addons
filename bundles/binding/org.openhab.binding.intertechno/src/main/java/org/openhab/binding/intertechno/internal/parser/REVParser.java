/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.intertechno.internal.parser;

import java.util.List;

import org.openhab.binding.intertechno.internal.CULIntertechnoBinding;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses configurations for REV type Intertechno devices. This is
 * untested since I don't own such devices.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class REVParser extends AbstractGroupAddressParser {

    private static final Logger logger = LoggerFactory.getLogger(CULIntertechnoBinding.class);

    @Override
    public void parseConfig(List<String> configParts) throws BindingConfigParseException {
        super.parseConfig(configParts);

        commandON = getGroupAddress(group) + getSubAddress(address) + "0FF" + "FF";
        commandOFF = getGroupAddress(group) + getSubAddress(address) + "0FF" + "00";

        logger.trace("commandON = {}", commandON);
        logger.trace("commandOFF = {}", commandOFF);
    }

    private String getGroupAddress(String group) throws BindingConfigParseException {
        char groupChar = group.charAt(0);
        switch (groupChar) {
            case 'A':
                return "1FFF";
            case 'B':
                return "F1FF";
            case 'C':
                return "FF1F";
            case 'D':
                return "FFF1";
            default:
                throw new BindingConfigParseException("Unknown group: " + group);
        }
    }

    private String getSubAddress(int sub) throws BindingConfigParseException {
        switch (sub) {
            case 1:
                return "1FF";
            case 2:
                return "F1F";
            case 3:
                return "FF1";
            default:
                throw new BindingConfigParseException("Unknown sub address: " + sub);
        }
    }

}
