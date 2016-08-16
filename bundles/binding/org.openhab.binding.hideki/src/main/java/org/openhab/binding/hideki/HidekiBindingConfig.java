/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

/**
 * Configuration of hideki binding. Item configuration is done by hideki="Sensor:Channel"
 * For example: Number HidekiAnemometerTemperature {hideki="Anemometer:Temperature"}
 * Valid sensors are: HYGROMETER, THERMOMETER, PLUVIOMETER, ANEMOMETER and UVMETER
 * Hygro/Thermometer supports TEMPERATURE, HUMIDITY and BATTERY channels.
 * Pluviometer support LEVEL channel only.
 * Anemometer supports TEMPERATURE, CHILL, SPEED, GUST and DIRECTION channels.
 * UV-meter supports TEMPERATURE, MED and UV channels.
 * 
 * @author Alexander Falkenstern
 * @since 1.8.0
 */
public class HidekiBindingConfig implements BindingConfig {
  private Item item;
  private int type;
  private String channel;
  
  public static final int INVALID_TYPE = 0x00;
  
  public HidekiBindingConfig(Item item, String sensor, String channel) {
    this.item = item;
    this.channel = channel.toUpperCase();

    this.type = INVALID_TYPE;
    switch(sensor.toUpperCase()) {
      case "HYGROMETER":
      case "THERMOMETER": {
        type = 0x1E;
        break;
      }
      case "PLUVIOMETER": {
        type = 0x0E;
        break;
      }
      case "ANEMOMETER": {
        type = 0x0C;
        break;
      }
      case "UVMETER": {
        type = 0x0D;
        break;
      }
      default: {
        this.channel = "";
        break;
      }
    }
  }
  
  public int getSensorType() {
    return this.type;
  }

  public String getSensorChannel() {
    switch(getSensorType()) {
      case 0x1E: { // Sensor returns temperature, humidity and battery state
        if(!channel.equals("TEMPERATURE") && !channel.equals("HUMIDITY") && !channel.equals("BATTERY")) {
          this.channel = "";
        }
        break;
      }
      case 0x0E: { // Sensor returns rain level
        if(!channel.equals("LEVEL")) {
          this.channel = "";
        }
        break;
      }
      case 0x0C: { // Sensor returns temperature, windchill, speed, gust and wind direction
        if(!channel.equals("TEMPERATURE") && !channel.equals("CHILL") && !channel.equals("SPEED") &&
           !channel.equals("GUST") && !channel.equals("DIRECTION")) {
          this.channel = "";
        }
        break;
      }
      case 0x0D: { // Sensor returns temperature, MED and UV index
        if(!channel.equals("TEMPERATURE") && !channel.equals("MED") && !channel.equals("UV")) {
          this.channel = "";
        }
        break;
      }
      default: {
        this.channel = "";
        break;
      }
    }
    
    return this.channel;
  }

  public Item getItem() {
    return this.item;
  }
}
