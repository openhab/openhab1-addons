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

  private static final Logger logger = LoggerFactory.getLogger(HidekiBinding.class);

  private boolean decoderRuns = false;

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
  
  /**
   * @{inheritDoc}
   */
  public void activate() {
  }
  
  /**
   * @{inheritDoc}
   */
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
          String channel = provider.getSensorChannel(itemName);
          switch(provider.getSensorType(itemName)) {
            case HidekiThermometer.TYPE: {
              HidekiThermometer sensor = new HidekiThermometer(data);
              if(channel.equals("TEMPERATURE")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getTemperature()));
              } else if(channel.equals("HUMIDITY")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getHumidity()));
              } else if(channel.equals("BATTERY")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getBatteryState() ? 1 : 0));
              }
            }
            case HidekiPluviometer.TYPE: {
              HidekiPluviometer sensor = new HidekiPluviometer(data);
              if(channel.equals("LEVEL")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getRainLevel()));
              }
              break;
            }
            case HidekiAnemometer.TYPE: {
              HidekiAnemometer sensor = new HidekiAnemometer(data);
              if(channel.equals("TEMPERATURE")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getTemperature()));
              } else if(channel.equals("CHILL")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getChill()));
              } else if(channel.equals("SPEED")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getSpeed()));
              } else if(channel.equals("GUST")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getGust()));
              } else if(channel.equals("DIRECTION")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getDirection()));
              }
              break;
            }
            case HidekiUVMeter.TYPE: {
              HidekiUVMeter sensor = new HidekiUVMeter(data);
              if(channel.equals("TEMPERATURE")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getTemperature()));
              } else if(channel.equals("MED")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getMED()));
              } else if(channel.equals("UV")) {
                this.eventPublisher.postUpdate(itemName, new DecimalType(sensor.getUVIndex()));
              }
              break;
            }
            default: {
              break;
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
