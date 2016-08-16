/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

public class HidekiUVMeter extends HidekiBaseTemperatureSensor {

  // private constructor
  private HidekiUVMeter() {
    super(); // forbid object construction
  }

  /**
   * @{inheritDoc}
   */
  public HidekiUVMeter(int [] data) throws IllegalArgumentException {
    super(data);

    if(getSensorType() != 0x0D) {
      throw new IllegalArgumentException("Invalid UV-Meter data.");
    }
  }
  
  /**
   * @{inheritDoc}
   */
  @Override
  public double getTemperature() {
    int[] data = getSensorData();
    return (data[4] >> 4) + (data[4] & 0x0F) / 10.0 + (data[5] & 0x0F) * 10.0;
  }

  /**
   * Return decoded MED value.
   */
  public double getMED() {
    // MED stay for "minimal erythema dose"
    // Some says: 1 MED/h = 2.778 UV-Index, another 1 MED/h = 2.33 UV-Index
    int[] data = getSensorData();
    return (data[5] >> 4) / 10.0 + (data[6] & 0x0F) + (data[6] >> 4) * 10.0;
  }  

  /**
   * Return decoded UV-index.
   */
  public double getUVIndex() {
    int[] data = getSensorData();
    return (data[7] >> 4) + (data[7] & 0x0F) / 10.0 + (data[8] & 0x0F) * 10.0;
  }  
}
