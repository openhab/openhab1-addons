/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

import org.openhab.binding.plclogo.PLCLogoBindingConfig;
import org.openhab.binding.plclogo.PLCLogoBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author g8kmh
 * @since 1.5.0
 */
public class PLCLogoGenericBindingProvider extends AbstractGenericBindingProvider implements PLCLogoBindingProvider {
    private static final Logger logger = LoggerFactory.getLogger(PLCLogoBinding.class);

    /**
     * {@inheritDoc}
     */
    public String getBindingType() {
        return "plclogo";
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Item getItem(String itemName) {
        PLCLogoBindingConfig config = (PLCLogoBindingConfig) bindingConfigs.get(itemName);
        return config != null ? config.getItem() : null;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
        // @TODO may add additional checking based on the memloc
        if (!(item instanceof SwitchItem || item instanceof ContactItem || item instanceof NumberItem)) {
            throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
                    + item.getClass().getSimpleName()
                    + "', only Switch - Contact Items & Number are allowed - please check your *.items configuration");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processBindingConfiguration(String context, Item item, String bindingConfig)
            throws BindingConfigParseException {
        super.processBindingConfiguration(context, item, bindingConfig);

        // the config string has the format
        //
        // instancename:blocktype [activelow=yes|no]
        //
        String shouldBe = "should be controllername:blocktype [activelow=yes|no]";

        int delta = 0;
        boolean invert = false;

        String[] segments = bindingConfig.split(" ");
        if (segments.length > 2) {
            throw new BindingConfigParseException("invalid item format: " + bindingConfig + ", " + shouldBe);
        }

        String[] block = segments[0].split(":");
        if (block.length != 2) {
            throw new BindingConfigParseException(
                    "invalid item name/memory format: " + bindingConfig + ", " + shouldBe);
        }

        // check for invert or analogdelta
        if (segments.length == 2) {
            logger.debug("Addtional binding config " + segments[1]);
            String[] inversion = segments[1].split("=");
            if (inversion.length != 2) {
                throw new BindingConfigParseException("invalid second parameter: " + bindingConfig + ", " + shouldBe);
            }
            if ((inversion[0].compareToIgnoreCase("activelow") == 0)) {
                invert = (inversion[1].compareToIgnoreCase("yes") == 0);
            }
            if (inversion[0].compareToIgnoreCase("analogdelta") == 0) {
                delta = Integer.parseInt(inversion[1]);
                logger.debug("Setting analogDelta " + delta);
            }
        }
        addBindingConfig(item, new PLCLogoBindingConfig(item, block[0], block[1], invert, delta));
    }

    @Override
    public PLCLogoBindingConfig getBindingConfig(String itemName) {
        return (PLCLogoBindingConfig) this.bindingConfigs.get(itemName);

    }

}
