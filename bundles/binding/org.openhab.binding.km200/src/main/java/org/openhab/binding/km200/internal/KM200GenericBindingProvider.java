/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.km200.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.km200.KM200BindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse information from the generic binding format and
 * provides KM200 binding information from it. It registers as a
 * {@link KM200BindingProvider} service as well.
 *
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */
public class KM200GenericBindingProvider extends AbstractGenericBindingProvider implements KM200BindingProvider {

    static final Logger logger = LoggerFactory.getLogger(KM200GenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "km200";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof SwitchItem || item instanceof StringItem || item instanceof NumberItem
                || item instanceof DateTimeItem)) {
            throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName()
                    + "', only Switch- String- DateTime- and NumberItems are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {

        KM200BindingConfig config = parseBindingConfig(item, bindingConfig);
        addBindingConfig(item, config);

        super.processBindingConfiguration(context, item, bindingConfig);
    }

    /**
     * Checks if the bindingConfig contains a valid binding type and returns an appropriate instance.
     *
     * @param item
     * @param bindingConfig
     *
     * @throws BindingConfigParseException if bindingConfig is no valid binding type
     */
    protected KM200BindingConfig parseBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {
        String service = null, current = null;
        HashMap<String, String> parameterMap = new HashMap<String, String>();
        /* Check whether some defined services are used */
        logger.info("Bind Config: {}", bindingConfig);
        if (bindingConfig.trim().equals(KM200BindingProvider.DATE_TIME)) {
            return new KM200BindingConfig(item.getClass(), KM200BindingProvider.DATE_TIME, "/gateway/DateTime",
                    parameterMap);
        }
        if (bindingConfig.trim().equals(KM200BindingProvider.SYS_BRAND)) {
            return new KM200BindingConfig(item.getClass(), KM200BindingProvider.SYS_BRAND, "/system/brand",
                    parameterMap);
        }
        if (bindingConfig.trim().equals(KM200BindingProvider.SYS_TYPE)) {
            return new KM200BindingConfig(item.getClass(), KM200BindingProvider.SYS_TYPE, "/system/appliance/type",
                    parameterMap);
        }
        if (bindingConfig.trim().equals(KM200BindingProvider.SYS_STATE)) {
            return new KM200BindingConfig(item.getClass(), KM200BindingProvider.SYS_STATE, "/system/healthStatus",
                    parameterMap);
        }
        if (bindingConfig.trim().equals(KM200BindingProvider.VER_FIRMWARE)) {
            return new KM200BindingConfig(item.getClass(), KM200BindingProvider.VER_FIRMWARE,
                    "/gateway/versionFirmware", parameterMap);
        }
        if (bindingConfig.trim().equals(KM200BindingProvider.VER_HARDWARE)) {
            return new KM200BindingConfig(item.getClass(), KM200BindingProvider.VER_HARDWARE,
                    "/gateway/versionHardware", parameterMap);
        }

        /* Maybe it's a configuration */
        /* Configuration string should be in the form "service:/gateway/versionFirmware" */
        /* Available options for a SwitchItem are on:xxx and off:yyy with a allowed value xxx|yyy */

        String[] keyTypeStructure = bindingConfig.split("\\s+");
        logger.debug("Bind Config nbr: {}", keyTypeStructure.length);
        if (keyTypeStructure.length > 3) {
            logger.error("Incorrect number of structures in configuration string '{}'", bindingConfig);
            throw new BindingConfigParseException(
                    "Incorrect number of structures in configuration string '" + bindingConfig + "'");
        }
        for (String structure : keyTypeStructure) {

            String[] keyValueStructure = structure.split(":");

            if (keyValueStructure.length != 2) {
                logger.error("Incorrect key:value structure in configuration string '{}'", bindingConfig);
                throw new BindingConfigParseException(
                        "Incorrect key:value structure in configuration string '" + bindingConfig + "'");
            }

            String key = keyValueStructure[0];
            String value = keyValueStructure[1];

            if (key.equals("service")) {
                service = value;
                if (!service.contains("/")) {
                    logger.error("Wrong service string without / in configuration string '{}'", bindingConfig);
                    throw new BindingConfigParseException(
                            "Wrong service string without / in configuration string '" + bindingConfig + "'");
                }
            } else if (key.equals("current")) {
                current = value;
                if (!current.contains("/")) {
                    logger.error("Wrong current string without / in configuration string '{}'", bindingConfig);
                    throw new BindingConfigParseException(
                            "Wrong current string without / in configuration string '" + bindingConfig + "'");
                }
                parameterMap.put(key, current);
            } else if (key.equals("on") || key.equals("off")) {
                parameterMap.put(key, value);
            } else {
                logger.error("Unsupported key: ({}) in configuration string: '{}'", key, bindingConfig);
                throw new BindingConfigParseException(
                        "Unsupported key (" + key + ") in configuration string '" + bindingConfig + "'");
            }
        }
        if (parameterMap.containsKey("on") || parameterMap.containsKey("off")) {
            if (!(parameterMap.containsKey("on") && parameterMap.containsKey("off"))) {
                throw new BindingConfigParseException(
                        "Unsupported combination, 'on' and 'off' have to be configured together '" + bindingConfig
                                + "'");
            }
        }
        return new KM200BindingConfig(item.getClass(), KM200BindingProvider.DIRECT_SERVICE, service, parameterMap);

    }

    @Override
    public Class<? extends Item> getItemType(String itemName) {
        KM200BindingConfig config = (KM200BindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getItemType() : null;
    }

    @Override
    public String getType(String itemName) {
        KM200BindingConfig config = (KM200BindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getType() : null;
    }

    @Override
    public String getService(String itemName) {
        KM200BindingConfig config = (KM200BindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getService() : null;
    }

    @Override
    public HashMap<String, String> getParameter(String itemName) {
        KM200BindingConfig config = (KM200BindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getParameter() : null;
    }

    @Override
    public String[] getItemNamesForType(String eventType) {
        Set<String> itemNames = new HashSet<>();
        for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
            KM200BindingConfig fbConfig = (KM200BindingConfig) entry.getValue();
            if (fbConfig.getType().equals(eventType)) {
                itemNames.add(entry.getKey());
            }
        }
        return itemNames.toArray(new String[itemNames.size()]);
    }

    static class KM200BindingConfig implements BindingConfig {

        final private Class<? extends Item> itemType;
        final private String type;
        final private String service;
        private HashMap<String, String> parameterMap = null;

        public KM200BindingConfig(Class<? extends Item> itemType, String type, String service,
                HashMap<String, String> parameter) {
            this.itemType = itemType;
            this.type = type;
            this.service = service;
            this.parameterMap = parameter;
        }

        public Class<? extends Item> getItemType() {
            return itemType;
        }

        public String getType() {
            return type;
        }

        public String getService() {
            return service;
        }

        public HashMap<String, String> getParameter() {
            return parameterMap;
        }
    }

}
