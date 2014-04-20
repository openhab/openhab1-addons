/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.calc;

/**
 * Holds the calculated azimuth and elevation.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class SunPosition {
  private double azimuth;
  private double elevation;

  public SunPosition(double azimuth, double elevation) {
    this.azimuth = azimuth;
    this.elevation = elevation;
  }

  /**
   * Returns the azimuth.
   */
  public double getAzimuth() {
    return azimuth;
  }

  /**
   * Returns the elevation.
   */
  public double getElevation() {
    return elevation;
  }

}
