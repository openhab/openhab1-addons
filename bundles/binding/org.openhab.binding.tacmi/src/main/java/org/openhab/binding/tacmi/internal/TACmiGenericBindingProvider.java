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
package org.openhab.binding.tacmi.internal;

import org.openhab.binding.tacmi.TACmiBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * *
 * <p>
 * Valid bindings for digital ports are:
 * <ul>
 * <li><code>{ tacmi="&lt;can_node&gt;#d#&lt;port&gt;" }</code></li>
 * <ul>
 * <li><code>{ tacmi="50#d#1" }</code></li>
 * </ul>
 * </li>
 * </ul>
 * <p>
 * Valid bindings for analog ports are:
 * <ul>
 * <li>
 * <code>{ tacmi="&lt;can_node&gt;#d#&lt;port&gt;#Measuretype" }</code></li>
 * <ul>
 * <li>
 * <code>{ tacmi="50#a#1#Temperature" }</code></li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author Timo Wendt
 * @author Wolfgang Klimt
 * @since 1.8.0
 */
public class TACmiGenericBindingProvider extends AbstractGenericBindingProvider implements TACmiBindingProvider {

    private static Logger logger = LoggerFactory.getLogger(TACmiGenericBindingProvider.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getBindingType() {
        return "tacmi";
    }

    /**
     * @{inheritDoc
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);
        TACmiBindingConfig config;
        logger.debug("Processing binding configuration: '{}'", bindingConfig);

        final String[] configParts = bindingConfig.split("#");
        switch (configParts.length) {
            case 3:
                config = new TACmiBindingConfig(Integer.parseInt(configParts[0]), configParts[1],
                        Integer.parseInt(configParts[2]), null, bindingConfig);
                break;
            case 4:
                config = new TACmiBindingConfig(Integer.parseInt(configParts[0]), configParts[1],
                        Integer.parseInt(configParts[2]), TACmiMeasureType.fromString(configParts[3]), bindingConfig);
                break;
            default:
                throw new BindingConfigParseException(
                        "A TACmi binding configuration must consist of three or four parts - please verify your *.items file");
        }

        logger.debug("Adding Binding configuration: {}", config);

        addBindingConfig(item, config);
    }

    /**
     * This is a helper class holding binding specific configuration details
     *
     * @author Timo Wendt
     * @since 1.7.0
     */
    class TACmiBindingConfig implements BindingConfig {
        int canNode;
        String portType;
        int portNumber;
        TACmiMeasureType measureType;
        String configurationString;

        public TACmiBindingConfig(int canNode, String portType, int portNumber, TACmiMeasureType measureType,
                String configurationString) {
            this.canNode = canNode;
            this.portType = portType;
            this.portNumber = portNumber;
            if (measureType == null) {
                this.measureType = TACmiMeasureType.NONE;
            } else {
                this.measureType = measureType;
            }

            this.configurationString = configurationString;
        }

        @Override
        public String toString() {
            return "TACmiBindingConfig [canNode=" + this.canNode + ", portType=" + this.portType + ", portNumber="
                    + this.portNumber + ", measure=" + this.measureType.getTypeValue() + "]";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfigurationString(String itemName) {
        final TACmiBindingConfig config = (TACmiBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.configurationString : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCanNode(String itemName) {
        final TACmiBindingConfig config = (TACmiBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.canNode : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPortType(String itemName) {
        final TACmiBindingConfig config = (TACmiBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.portType : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPortNumber(String itemName) {
        final TACmiBindingConfig config = (TACmiBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.portNumber : 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TACmiMeasureType getMeasureType(String itemName) {
        final TACmiBindingConfig config = (TACmiBindingConfig) this.bindingConfigs.get(itemName);
        return config != null ? config.measureType : null;
    }

}
