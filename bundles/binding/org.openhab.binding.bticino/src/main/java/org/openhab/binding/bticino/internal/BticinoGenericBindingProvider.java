/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.bticino.internal;

import java.util.HashMap;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BticinoBindingProvider provides binding for openHAB Items
 *
 * @author Tom De Vlaminck
 * @author Reinhard Freuis - various enhancements for heating, rollershutter
 * @serial 1.0
 * @since 1.7.0
 *
 */
public class BticinoGenericBindingProvider extends AbstractGenericBindingProvider implements BticinoBindingProvider {

    static final Logger logger = LoggerFactory.getLogger(BticinoGenericBindingProvider.class);
    static final String BINDING_TYPE = "bticino";

    /**
     * {@inheritDoc}
     */
    /*
     * (non-Javadoc)
     *
     * @see org.openhab.model.item.binding.BindingConfigReader#getBindingType()
     */
    public String getBindingType() {
        return BINDING_TYPE;
    }

    /**
     * @{inheritDoc
     */
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {

        if (!(item instanceof SwitchItem || item instanceof RollershutterItem || item instanceof NumberItem
                || item instanceof ContactItem || item instanceof StringItem)) {
            throw new BindingConfigParseException(
                    "item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
                            + "'. This Item is not allowed - please check your *.items configuration."
                            + "Only SwitchItem, RollershutterItem, NumberItem ContactItem and StringItem are allowed.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        if (bindingConfig != null) {
            BticinoBindingConfig config = parseBindingConfig(item, bindingConfig);
            addBindingConfig(item, config);
        } else {
            logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
        }
    }

    /**
     * Checks if the bindingConfig contains a valid binding type and returns an
     * appropriate instance.
     *
     * @param item
     * @param bindingConfig
     *
     * @throws BindingConfigParseException
     *             if bindingConfig is no valid binding type
     */
    protected BticinoBindingConfig parseBindingConfig(Item item, String bindingConfig)
            throws BindingConfigParseException {
        return new BticinoBindingConfig(item, bindingConfig);
    }

    public BticinoBindingConfig getConfig(String name) {
        return (BticinoBindingConfig) bindingConfigs.get(name);
    }

    /**
     * BticinoBindingConfig stores configuration of the item bound to openweb -
     * bticino bus
     *
     * @author Tom De Vlaminck
     * @serial 1.0
     * @since 1.7.0
     */
    public class BticinoBindingConfig implements BindingConfig {
        /**
         * Name of the openweb device (MH200) instance to read/write data to the
         * bus
         */
        String gatewayID;

        /**
         * WHO
         */
        String who;
        /**
         * WHAT
         */
        String what;
        /**
         * WHERE
         */
        String where;

        /**
         * OpenHAB Item to be configured
         */
        private Item item = null;

        public Item getItem() {
            return item;
        }

        State getItemState() {
            return item.getState();
        }

        /**
         * Constructor for config object
         *
         * @param item
         * @param config
         * @throws BindingConfigParseException
         */
        BticinoBindingConfig(Item item, String config) throws BindingConfigParseException {
            this.item = item;

            try {
                HashMap<String, String> l_decom_config = bticinoBindingConfigDecompose(config);

                // the gateway name is defined with the "if" property
                // when this is not defined, we revert to default (this
                // must be present in openhab*.cfg
                String gateway = "default";
                if (l_decom_config.containsKey("if")) {
                    gateway = l_decom_config.get("if");
                }
                gatewayID = gateway;

                // WHO
                if (l_decom_config.containsKey("who")) {
                    who = l_decom_config.get("who");
                } else {
                    throw new Exception("who is missing in the configuration : " + config);
                }

                // WHAT
                if (l_decom_config.containsKey("what")) {
                    what = l_decom_config.get("what");
                } else {
                    throw new Exception("what is missing in the configuration : " + config);
                }

                // WHERE
                if (l_decom_config.containsKey("where")) {
                    where = l_decom_config.get("where");
                } else {
                    throw new Exception("where is missing in the configuration : " + config);
                }
            } catch (Exception e) {
                throw new BindingConfigParseException(e.getMessage());
            }
        }

        /**
         * bticino="if=0;who=1;what=1;where=23" if => support for multiple MH200
         * devices (= several houses through VPN (wet dreams :,) )
         *
         * @param p_binding_config
         * @return
         */
        private HashMap<String, String> bticinoBindingConfigDecompose(String p_binding_config) {
            HashMap<String, String> l_configuration_hm = new HashMap<String, String>();
            // who=1;what=1;where=23
            String[] l_key_value_pairs = p_binding_config.split(";");
            for (int l_idx = 0; l_idx < l_key_value_pairs.length; l_idx++) {
                String[] l_key_value = l_key_value_pairs[l_idx].split("=");
                l_configuration_hm.put(l_key_value[0], l_key_value[1]);
            }
            return l_configuration_hm;
        }
    }

    @Override
    public void removeConfigurations(String context) {
        super.removeConfigurations(context);
    }

}
