/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

public class HidekiAnemometer extends HidekiBaseTemperatureSensor {

  // private constructor
  private HidekiAnemometer() {
    super(); // forbid object construction
  }

  /**
   * @{inheritDoc}
   */
  public HidekiAnemometer(int [] data) throws IllegalArgumentException {
    super(data);

    if(getSensorType() != 0x0C) {
      throw new IllegalArgumentException("Invalid anemometer data.");
    }
  }
  
  /**
   * Return decoded chill.
   */
  public double getChill() {
    int[] data = getSensorData();

    double value = (data[7] & 0x0F) * 10 + (data[6] >> 4) + (data[6] & 0x0F) * 0.1;
    if((data[7] >> 4) != 0x0C) {
      value = (data[7] >> 4) == 0x04 ? -value : Double.MAX_VALUE;
    }
    
    return value;
  }

  /**
   * Return decoded wind speed.
   */
  public double getSpeed() {
    int[] data = getSensorData();
    return 1.60934 * ((data[8] >> 4) + (data[8] & 0x0F) / 10.0 + (data[9] & 0x0F) * 10.0);
  }

  /**
   * Return decoded wind gust.
   */
  public double getGust() {
    int[] data = getSensorData();
    return 1.60934 * ((data[9] >> 4) / 10.0 + (data[10] & 0x0F) + (data[10] >> 4) * 10.0);
  }

  /**
   * Return decoded wind direction.
   */
  public double getDirection() {
    int[] data = getSensorData();

    int segment = (data[11] >> 4);
    segment = segment ^ (segment & 8) >> 1;
    segment = segment ^ (segment & 4) >> 1;
    segment = segment ^ (segment & 2) >> 1;
    return 22.5 * (-segment & 0xF);
  }
}
