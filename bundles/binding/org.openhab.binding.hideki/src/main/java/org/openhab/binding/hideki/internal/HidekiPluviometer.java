/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

public class HidekiPluviometer extends HidekiBaseSensor {

  // private constructor
  private HidekiPluviometer() {
    super(); // forbid object construction
  }

  /**
   * @{inheritDoc}
   */
  public HidekiPluviometer(int [] data) throws IllegalArgumentException {
    super(data);

    if(getSensorType() != 0x0E) {
      throw new IllegalArgumentException("Invalid pluviometer data.");
    }
  }
  
  /**
   * Return decoded accumulated rain level.
   */
  public double getRainLevel() {
    int[] data = getSensorData();
    return 0.7 * ((data[5] << 8) + data[4]);
  }
}
