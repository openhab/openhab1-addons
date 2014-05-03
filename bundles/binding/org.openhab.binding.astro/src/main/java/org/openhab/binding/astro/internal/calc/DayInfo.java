/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.calc;

import java.util.Calendar;

/**
 * Holds the calculated sunrise, noon and sunset.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class DayInfo {
  private Calendar sunrise;
  private Calendar noon;
  private Calendar sunset;

  public DayInfo(Calendar sunrise, Calendar noon, Calendar sunset) {
    this.sunrise = sunrise;
    this.noon = noon;
    this.sunset = sunset;
  }

  /**
   * Returns the sunrise.
   */
  public Calendar getSunrise() {
    return sunrise;
  }

  /**
   * Returns the noon.
   */
  public Calendar getNoon() {
    return noon;
  }

  /**
   * Returns the sunset.
   */
  public Calendar getSunset() {
    return sunset;
  }

}
