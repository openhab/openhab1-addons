/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.config;

import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to parse the key - value base config for an Homematic item. Generic
 * enough to be used for other bindings as well.
 * 
 * The values are set into the attributes which match the keys in the config
 * lines. Leading / trailing brackets ({}) or quotes are removed.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.3.0
 * 
 * @param <TYPE>
 *            The BindingConfig to parse into.
 */
public class BindingConfigParser {

    private static final Logger logger = LoggerFactory.getLogger(BindingConfigParser.class);
    
    private boolean warningIssued = false;

    /**
     * Parse the configLine into the given config.
     * 
     * @param configLine
     * @param config
     * @throws BindingConfigParseException
     */
    public void parse(String configLine, HomematicGenericBindingProvider.HomematicBindingConfig config) throws BindingConfigParseException {
        if (isOldFormat(configLine)) {
            if(!warningIssued) {
            	logger.warn("You are using the old Homematic item format. Please update your item definitions.");
            	warningIssued = true;
            }
            parseOldFormat(configLine, config);
        } else {
            parseNewFormat(configLine, config);
        }
    }

    private boolean isOldFormat(String configLine) {
        return configLine.contains(":") && (configLine.contains("#") || configLine.contains(AdminItem.ID));
    }

    private void parseNewFormat(String configLine, HomematicGenericBindingProvider.HomematicBindingConfig config)
            throws BindingConfigParseException {
        configLine = removeFirstBrakets(configLine);
        configLine = removeLastBrakets(configLine);
        String[] entries = configLine.trim().split("[,]");
        for (String entry : entries) {
            String[] entryParts = entry.trim().split("[=]");
            if (entryParts.length != 2) {
                throw new BindingConfigParseException("Each entry must have a key and a value");
            }
            String key = entryParts[0];
            String value = entryParts[1];
            value = removeFirstQuotes(value);
            value = removeLastQuotes(value);
            try {
                config.getClass().getDeclaredField(key).set(config, value);
            } catch (Exception e) {
                logger.error("Could set value " + value + " to attribute " + key + " in class " + config.getClass());
                throw new BindingConfigParseException("Could set value " + value + " to attribute " + key + " in class "
                        + config.getClass());
            }
        }
    }

    private void parseOldFormat(String configLine, HomematicGenericBindingProvider.HomematicBindingConfig config)
            throws BindingConfigParseException {
        String[] configParts = configLine.trim().split("[:#]");
        if (configParts.length == 2 && configParts[0].equals(AdminItem.ID)) {
            config.admin = configParts[1];
        } else if (configParts.length == 3) {
            config.id = configParts[0];
            config.channel = configParts[1];
            config.parameter = configParts[2];
        } else {
            throw new BindingConfigParseException("Homematic device configurations must contain three parts "
                    + "<physicalDeviceAddress>:<channel>#<parameterKey>");
        }
    }

    private String removeLastBrakets(String configLine) {
        if (configLine.substring(configLine.length() - 1).equals("}")) {
            return configLine.substring(0, configLine.length() - 1);
        }
        return configLine;
    }

    private String removeFirstBrakets(String configLine) {
        if (configLine.substring(0, 1).equals("{")) {
            return configLine.substring(1);
        }
        return configLine;
    }

    private String removeLastQuotes(String value) {
        if (value.substring(value.length() - 1).equals("\"")) {
            return value.substring(0, value.length() - 1);
        }
        return value;
    }

    private String removeFirstQuotes(String value) {
        if (value.substring(0, 1).equals("\"")) {
            return value.substring(1);
        }
        return value;
    }

}
