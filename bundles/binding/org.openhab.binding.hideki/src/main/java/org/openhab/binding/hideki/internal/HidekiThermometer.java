/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

public class HidekiThermometer extends HidekiBaseTemperatureSensor {

  public static final int TYPE = 0x1E;

  // private constructor
  private HidekiThermometer() {
    super(); // forbid object construction
  }

  /**
   * @{inheritDoc}
   */
  public HidekiThermometer(int [] data) throws IllegalArgumentException {
    super(data);

    if(getSensorType() != HidekiThermometer.TYPE) {
      throw new IllegalArgumentException("Invalid thermo/hygrometer data.");
    }
  }
  
  /**
   * Return decoded humidity.
   */
  public double getHumidity() {
    int[] data = getSensorData();
    return (data[6] >> 4) * 10 + (data[6] & 0x0F);
  }

  /**
   * Return decoded battery state.
   */
  public boolean getBatteryState() {
    int[] data = getSensorData();
    return (data[2] >> 6) > 0;
  }
}
