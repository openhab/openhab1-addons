package org.openhab.binding.hideki;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;

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
    switch(this.type) {
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
        if(!channel.equals("TEMPERATURE") && !channel.equals("CHILL") && !channel.equals("SPEED")) {
          this.channel = "";
        } else if(!channel.equals("GUST") && !channel.equals("DIRECTION")) {
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
