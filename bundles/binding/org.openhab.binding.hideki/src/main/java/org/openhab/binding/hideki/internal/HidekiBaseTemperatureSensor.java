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
 * Common description of hideki sensors having temperature values.
 *
 * @author Alexander Falkenstern
 * @since 1.5.0
 */
public class HidekiBaseTemperatureSensor extends HidekiBaseSensor {

  // private constructor
  protected HidekiBaseTemperatureSensor() {
    super();
  }

  /**
   * @{inheritDoc}
   */
  public HidekiBaseTemperatureSensor(int [] data) throws IllegalArgumentException {
    super(data);
  }
  
  /**
   * Return decoded temperature.
   */
  public double getTemperature() {
    int[] data = getSensorData();
    
    double value = (data[5] & 0x0F) * 10 + (data[4] >> 4) + (data[4] & 0x0F) * 0.1;
    if((data[5] >> 4) != 0x0C) {
      value = (data[5] >> 4) == 0x04 ? -value : Double.MAX_VALUE;
    }
    
    return value;
  }
}
