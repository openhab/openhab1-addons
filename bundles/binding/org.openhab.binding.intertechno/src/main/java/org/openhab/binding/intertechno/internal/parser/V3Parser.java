/**
 * Copyright (c) 2010-2017 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.intertechno.internal.parser;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.intertechno.internal.CULIntertechnoBinding;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class parses the configuration for the new self-learning intertechno
 * protocol, which is referenced as V3.
 *
 * @author Michael Neuendorf
 * @since 1.11.0
 */
public class V3Parser implements IntertechnoAddressParser {

    private static final Logger logger = LoggerFactory.getLogger(CULIntertechnoBinding.class);

    private String commandON = "";
    private String commandOFF = "";

    @Override
    public void parseConfig(List<String> configParts) throws BindingConfigParseException {
        String id = "";
        Boolean group = false;
        Integer channelID = 0;

        // Extract parameter values from config parts
        for (int i = 0; i < configParts.size(); i++) {
            String paramName = configParts.get(i).split("=")[0].toLowerCase();
            String paramValue = configParts.get(i).split("=")[1];

            switch (paramName) {
                case "id":
                    id = paramValue;
                    break;
                case "group":
                    if (paramValue.equals("1") || paramValue.toLowerCase().equals("true")) {
                        group = true;
                    }
                    break;
                case "channel":
                    try {
                        channelID = Integer.parseInt(paramValue);
                    } catch (NumberFormatException e) {
                        throw new BindingConfigParseException("Channel ID (" + paramValue + ") is not a number.");
                    }
            }
        }

        // Check parameter values
        if (id.length() != 26) {
            throw new BindingConfigParseException("The ID must contain exactly 26 digits!");
        }

        for (int i = 0; i < id.length(); i++) {
            if (id.charAt(i) != '0' && id.charAt(i) != '1') {
                throw new BindingConfigParseException("The ID must contains only the digits 1 and 0!");
            }
        }

        if (channelID < 0 || channelID > 15) {
            throw new BindingConfigParseException("The channel ID must be in a range from 0 to 15!");
        }

        // Build command strings
        String channelIDCode = StringUtils.leftPad(Integer.toBinaryString(channelID), 4, "0");

        commandON = id + getGroupCode(group) + "1" + channelIDCode;
        commandOFF = id + getGroupCode(group) + "0" + channelIDCode;

        logger.trace("commandON = {}", commandON);
        logger.trace("commandOFF = {}", commandOFF);
    }

    private String getGroupCode(Boolean group) {
        if (group) {
            return "1";
        } else {
            return "0";
        }
    }

    @Override
    public String getCommandON() {
        return commandON;
    }

    @Override
    public String getCommandOFF() {
        return commandOFF;
    }

}
