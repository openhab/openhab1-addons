/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

import org.openhab.binding.hideki.HidekiBindingProvider;
import org.openhab.binding.hideki.HidekiBindingConfig;

import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Dictionary;

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Alexander Falkenstern
 * @since 1.5.0
 */
public class HidekiBinding extends AbstractActiveBinding<HidekiBindingProvider> implements ManagedService {

  private boolean decoderRuns = false;
  private static final Logger logger = LoggerFactory.getLogger(HidekiBinding.class);

  /** 
   * the refresh interval which is used to poll values from the hideki
   * weather station (optional, defaults to 500ms)
   */
  private long refreshInterval = 5000;

  /** 
   * Pin 433Mhz receiver connected to
   */
  private int pin = 27;

  public HidekiBinding() {
    logger.info("HidekiBinding constructor");
  }
  
  public void activate() {
  }
  
  public void deactivate() {
    for (HidekiBindingProvider provider : providers) {
      provider.removeBindingChangeListener(this);
    }
    providers.clear();

    if(decoderRuns) {
      HidekiDecoder.stopDecoder(pin);
      decoderRuns = false;
    }
  }

  /**
   * @{inheritDoc}
   */
  @Override
  protected long getRefreshInterval() {
    return refreshInterval;
  }

  /**
   * @{inheritDoc}
   */
  @Override
  protected String getName() {
    return "Hideki Polling Service";
  }
  
  /**
   * @{inheritDoc}
   */
  @Override
  protected void execute() {
    if (!bindingsExist()) {
      logger.debug("There is no existing hideki binding configuration => refresh cycle aborted!");
      return;
    }

    if(!decoderRuns) {
      decoderRuns = (HidekiDecoder.startDecoder(pin) == 0);
      if(!decoderRuns) {
        HidekiDecoder.stopDecoder(pin);
        decoderRuns = (HidekiDecoder.startDecoder(pin) == 0);
      }
      logger.debug("Hideki decoder on pin " + pin + " started: " + decoderRuns);
    }

    int[] data = HidekiDecoder.getDecodedData();
    if((data != null) && decoderRuns) {
      for (HidekiBindingProvider provider : providers) {
        for (String itemName : provider.getItemNames()) {
          HidekiBindingConfig config = (HidekiBindingConfig)provider.getBindingConfig(itemName);
          if((data[3] & 0x1F) == config.getSensorType()) {
            String channel = config.getSensorChannel();
            if(channel.equals("TEMPERATURE")) {
              double value = Double.MAX_VALUE;
              if(config.getSensorType() != 0x0D) {
                value = (data[5] & 0x0F) * 10 + (data[4] >> 4) + (data[4] & 0x0F) * 0.1;
                if((data[5] >> 4) != 0x0C) {
                  value = (data[5] >> 4) == 0x04 ? -value : Double.MAX_VALUE;
                }
              } else {
                value = (data[4] >> 4) + (data[4] & 0x0F) / 10.0 + (data[5] & 0x0F) * 10.0;
              }
              this.eventPublisher.postUpdate(itemName, new DecimalType(value));
            } else if(channel.equals("HUMIDITY")) {
              int value = (data[6] >> 4) * 10 + (data[6] & 0x0F);
              this.eventPublisher.postUpdate(itemName, new DecimalType(value));
            } else if(channel.equals("BATTERY")) {
              int value = (data[2] >> 6) > 0 ? 1 : 0;
              this.eventPublisher.postUpdate(itemName, new DecimalType(value));
            } else if(channel.equals("LEVEL")) {
              static int last = Integer.MAX_VALUE;
              int value = (data[5] << 8) + data[4];
              this.eventPublisher.postUpdate(itemName, new DecimalType(0.7 * (last - value)));
            } else if(channel.equals("CHILL")) {
              double value = (data[7] & 0x0F) * 10 + (data[6] >> 4) + (data[6] & 0x0F) * 0.1;
              if((data[7] >> 4) != 0x0C) {
                value = (data[7] >> 4) == 0x04 ? -value : Double.MAX_VALUE;
              }
              this.eventPublisher.postUpdate(itemName, new DecimalType(value));
            } else if(channel.equals("SPEED")) {
              double value = (data[8] >> 4) + (data[8] & 0x0F) / 10.0 + (data[9] & 0x0F) * 10.0;
              this.eventPublisher.postUpdate(itemName, new DecimalType(1.60934 * value));
            } else if(channel.equals("GUST")) {
              double value = (data[9] >> 4) / 10.0 + (data[10] & 0x0F) + (data[10] >> 4) * 10.0;
              this.eventPublisher.postUpdate(itemName, new DecimalType(1.60934 * value));
            } else if(channel.equals("DIRECTION")) {
              int segment = (data[11] >> 4);
              segment = segment ^ (segment & 8) >> 1;
              segment = segment ^ (segment & 4) >> 1;
              segment = segment ^ (segment & 2) >> 1;
              this.eventPublisher.postUpdate(itemName, new DecimalType(22.5 * (-segment & 0xF)));
            } else if(channel.equals("MED")) {
              // MED stay for "minimal erythema dose"
              // Some says: 1 MED/h = 2.778 UV-Index, another 1 MED/h = 2.33 UV-Index
              double value = (data[5] >> 4) / 10.0 + (data[6] & 0x0F) + (data[6] >> 4) * 10.0;
              this.eventPublisher.postUpdate(itemName, new DecimalType(value));
            } else if(channel.equals("UV")) {
              double value = (data[7] >> 4) + (data[7] & 0x0F) / 10.0 + (data[8] & 0x0F) * 10.0;
              this.eventPublisher.postUpdate(itemName, new DecimalType(value));
            }
          }
        }
      }
    }
  }

  /**
   * @{inheritDoc}
   */
  @Override
  protected void internalReceiveCommand(String itemName, Command command) {
    // the code being executed when a command was sent on the openHAB
    // event bus goes here. This method is only called if one of the 
    // BindingProviders provide a binding for the given 'itemName'.
    // Note itemname is the item name not the controller name/instance!
    //
    super.internalReceiveCommand(itemName, command);
    logger.debug("internalReceiveCommand() is called!");
  }
  
  /**
   * @{inheritDoc}
   */
  @Override
  protected void internalReceiveUpdate(String itemName, State newState) {
    // the code being executed when a state was sent on the openHAB
    // event bus goes here. This method is only called if one of the 
    // BindingProviders provide a binding for the given 'itemName'.
    super.internalReceiveUpdate(itemName, newState);
    logger.debug("internalReceiveUpdate() is called!");
  }
    
  /**
   * @{inheritDoc}
   */
  @Override
  public synchronized void updated(Dictionary<String, ?> config) throws ConfigurationException {
    if (config != null) {
      if(decoderRuns) {
        HidekiDecoder.stopDecoder(pin);
        decoderRuns = false;
      }

      String intervalString = (String) config.get("refresh");
      if (StringUtils.isNotBlank(intervalString)) {
        refreshInterval = Long.parseLong(intervalString);
        logger.debug("Set refresh interval to " + intervalString);
      }

      String timeoutString = (String) config.get("timeout");
      if (StringUtils.isNotBlank(timeoutString)) {
        HidekiDecoder.setTimeOut(Integer.parseInt(timeoutString));
        logger.debug("Set timeout to " + timeoutString);
      }

      String pinString = (String) config.get("pin");
      if (StringUtils.isNotBlank(pinString)) {
        pin = Integer.parseInt(pinString);
        logger.debug("Set pin to " + pinString);
      }
     
      setProperlyConfigured(true);
    } else {
      logger.warn("No configuration for hideki binding found" );
    }
  }
}
