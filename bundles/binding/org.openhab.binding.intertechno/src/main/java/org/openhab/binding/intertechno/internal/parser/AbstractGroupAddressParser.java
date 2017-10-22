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

import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class implements the basic parsing for a config with a group and an
 * address parameter.
 *
 * @author Michael Neuendorf
 * @since 1.11.0
 */
public abstract class AbstractGroupAddressParser extends AbstractIntertechnoParser {

    protected String group;
    protected int address;

    @Override
    public void parseConfig(List<String> configParts) throws BindingConfigParseException {
        group = "";
        address = 0;

        for (int i = 0; i < configParts.size(); i++) {
            String paramName = configParts.get(i).split("=")[0];
            String paramValue = configParts.get(i).split("=")[1];

            switch (paramName) {
                case "group":
                    group = paramValue;
                    break;
                case "address":
                    try {
                        address = Integer.parseInt(paramValue);
                    } catch (NumberFormatException e) {
                        throw new BindingConfigParseException(
                                "Address is not a number. Configured address: " + paramValue);
                    }
                    break;
            }
        }
    }

}
