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
 * This parser is able to parse the configs for "FLS" Intertechno devices, like
 * the ones sold at Conrad.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class FLSParser extends AbstractGroupAddressParser {

    private static final Logger logger = LoggerFactory.getLogger(CULIntertechnoBinding.class);

    @Override
    public void parseConfig(List<String> configParts) throws BindingConfigParseException {
        super.parseConfig(configParts);

        commandON = getGroupAddress(group) + getSubAddress(address) + "00" + "FF";
        commandOFF = getGroupAddress(group) + getSubAddress(address) + "00" + "F0";

        logger.trace("commandON = {}", commandON);
        logger.trace("commandOFF = {}", commandOFF);
    }

    private String getGroupAddress(String group) throws BindingConfigParseException {
        StringBuffer addressBuffer = new StringBuffer(4);
        addressBuffer.append("FFFF");
        if ("I".equalsIgnoreCase(group)) {
            addressBuffer.setCharAt(0, '0');

        } else if ("II".equalsIgnoreCase(group)) {
            addressBuffer.setCharAt(1, '0');
        } else if ("III".equalsIgnoreCase(group)) {
            addressBuffer.setCharAt(2, '0');
        } else if ("IV".equalsIgnoreCase(group)) {
            addressBuffer.setCharAt(3, '0');
        } else {
            throw new BindingConfigParseException("Unknown roman number given: " + group);
        }
        return addressBuffer.toString();
    }

    private String getSubAddress(int remoteId) throws BindingConfigParseException {
        if (remoteId < 1 || remoteId > 4) {
            throw new BindingConfigParseException("Only remote addresses in the range 1 - 4 are supported");
        }
        StringBuffer buffer = new StringBuffer(4);
        buffer.append("FFFF");
        buffer.setCharAt(remoteId - 1, '0');
        return buffer.toString();
    }

}
