/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

/**
 * Common description of all hideki sensors.
 *
 * @author Alexander Falkenstern
 * @since 1.5.0
 */
public class HidekiBaseSensor {

  private int[] data = null;

  // protected constructor
  protected HidekiBaseSensor() {
  }

  /**
   * Create sensor with received data.
   */
  public HidekiBaseSensor(int [] data) throws IllegalArgumentException {
    String message = "Data array is too short.";

    if(data.length < 4) {
      message = message + " Must be at least 4.";
      throw new IllegalArgumentException(message);
    }
    
    this.data = data;
    if(data.length < getLength()) {
      message = message + " Got " + data.length + " elements instead of " + getLength();
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Return received sensor data.
   */
  protected int[] getSensorData() {
    return this.data;
  }

  /**
   * Return received sensor id.
   */
  public int getSensorId() {
    return this.data[1] & 0x1F;
  }

  /**
   * Return decoded sensor channel is used for transmittion.
   * Is not zero for thermo/hygrometer only.
   */
  public int getChannel() {
    int channel = this.data[1] >> 5;
    if((channel == 5) || (channel == 6)) {
      channel = channel - 1;
    } else if (channel > 3) {
      channel = 0;
    }

    return channel;
  }
  
  /**
   * Return decoded length of sensor data.
   */
  public int getLength() {
    return (this.data[2] >> 1) & 0x1F;
  }

  /**
   * Return decoded sensor type.
   */
  public int getSensorType() {
    return this.data[3] & 0x1F;
  }
  
  /**
   * Return decoded number of message.
   */
  public int getMessageNumber() {
    return this.data[3] >> 6;
  }
}
