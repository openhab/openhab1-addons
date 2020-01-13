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
package org.openhab.binding.gc100ir.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.gc100ir.GC100IRBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Parikshit Thakur & Team
 * @since 1.9.0
 */
public class GC100IRGenericBindingProvider extends AbstractGenericBindingProvider implements GC100IRBindingProvider {

    private static final Pattern CONFIG_PATTERN = Pattern.compile("\\[(.*)\\|(.*)\\|(.*)\\|(.*)\\]");

    public String getBindingType() {
        return "gc100ir";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        if (!(item instanceof StringItem) && !(item instanceof SwitchItem)) {
            throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName() + "', but only String or Switch items are allowed.");
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);
        GC100BindingConfig config = parseBindingConfig(item, bindingConfig);
        addBindingConfig(item, config);
    }

    /**
     * Parses the binding configuration and returns GC100BindingConfig instance.
     * 
     * @param item
     * @param bindingConfig
     * @return GC100BindingConfig instance
     * @throws BindingConfigParseException
     */
    private GC100BindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
        Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);

        if (!matcher.matches())
            throw new BindingConfigParseException("Config for item '" + item.getName() + "' could not be parsed.");

        String gc100Instance = matcher.group(1);
        String module = matcher.group(2);
        String connector = matcher.group(3);
        String code = matcher.group(4);

        return new GC100BindingConfig(gc100Instance, Integer.parseInt(module), Integer.parseInt(connector), code);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGC100InstanceName(String itemname) {
        GC100BindingConfig bindingConfig = (GC100BindingConfig) bindingConfigs.get(itemname);
        return bindingConfig.getGC100Instance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGC100Module(String itemname) {
        GC100BindingConfig bindingConfig = (GC100BindingConfig) bindingConfigs.get(itemname);
        return bindingConfig.getModule();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getGC100Connector(String itemname) {
        GC100BindingConfig bindingConfig = (GC100BindingConfig) bindingConfigs.get(itemname);
        return bindingConfig.getConnector();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCode(String itemname) {
        GC100BindingConfig bindingConfig = (GC100BindingConfig) bindingConfigs.get(itemname);
        return bindingConfig.getCode();
    }

    /**
     * Stores binding configuration.
     * 
     * @author Prashant Goswami
     * 
     */
    class GC100BindingConfig implements BindingConfig {

        private String gc100Instance;
        private int module;
        private int connector;
        private String code;

        public GC100BindingConfig(String gc100Instance, int module, int connector, String code) {
            this.gc100Instance = gc100Instance;
            this.module = module;
            this.connector = connector;
            this.code = code;
        }

        public String getGC100Instance() {
            return gc100Instance;
        }

        public int getModule() {
            return module;
        }

        public int getConnector() {
            return connector;
        }

        public String getCode() {
            return code;
        }
    }
}
