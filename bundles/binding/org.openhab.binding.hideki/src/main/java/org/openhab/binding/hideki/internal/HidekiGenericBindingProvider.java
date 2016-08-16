/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

import org.openhab.binding.hideki.HidekiBindingConfig;
import org.openhab.binding.hideki.HidekiBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 *
 * @author Alexander Falkenstern
 * @since 1.5.0
 */
public class HidekiGenericBindingProvider extends AbstractGenericBindingProvider implements HidekiBindingProvider {
  private static final Logger logger = LoggerFactory.getLogger(HidekiGenericBindingProvider.class);

  /**
   * {@inheritDoc}
   */
  public String getBindingType() {
    return "hideki";
  }

  /**
   * @{inheritDoc}
   */
  @Override
  public Item getItem(String itemName) {
    HidekiBindingConfig config = getBindingConfig(itemName);
    return config != null ? config.getItem() : null;
  }

  /**
   * @{inheritDoc}
   */
  @Override
  public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
    // @TODO may add additional checking based on the memloc
    if (!(item instanceof NumberItem)) {
      throw new BindingConfigParseException("item '" + item.getName() +
                "' is of type '" + item.getClass().getSimpleName() + "', only " +
                "Number items are allowed - please check your *.items configuration");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
    super.processBindingConfiguration(context, item, bindingConfig);

    // the config string has the format "sensor:value"
    String[] configString = bindingConfig.split(":");
    if (configString.length != 2) {
      logger.error("Invalid item format " + bindingConfig + " for item " + item.getName() + " found");
      throw new BindingConfigParseException("Invalid item format " + bindingConfig + ", " + "should be sensor:value");
    }

    HidekiBindingConfig config = new HidekiBindingConfig(item, configString[0], configString[1]);
    if((config.getSensorType() != HidekiBindingConfig.INVALID_TYPE) && (config.getSensorChannel() != "")) {
      addBindingConfig(item, config);
    } else {
      logger.warn("Invalid binding " + bindingConfig + " for item " + item.getName() + " found");
      if(config.getSensorType() == HidekiBindingConfig.INVALID_TYPE) {
        logger.warn("Valid sensors are: HYGROMETER, THERMOMETER, PLUVIOMETER, ANEMOMETER and UVMETER");
      } else if(config.getSensorType() == 0x1E) {
        logger.warn("Valid channels for hygro/thermometer are TEMPERATURE, HUMIDITY and BATTERY");
      } else if(config.getSensorType() == 0x0E) {
        logger.warn("Valid channel for pluviometer is LEVEL");
      } else if(config.getSensorType() == 0x0C) {
        logger.warn("Valid channels for anemometer are TEMPERATURE, CHILL, SPEED, GUST and DIRECTION");
      } else if(config.getSensorType() == 0x0D) {
        logger.warn("Valid channels for uv-meter are TEMPERATURE, MED and UV");
      } else {
        logger.warn("Please report unknown sensor and channels");
      }
    }
  }

  @Override
  public HidekiBindingConfig getBindingConfig(String itemName) {
    return (HidekiBindingConfig) this.bindingConfigs.get(itemName);
  }
}
