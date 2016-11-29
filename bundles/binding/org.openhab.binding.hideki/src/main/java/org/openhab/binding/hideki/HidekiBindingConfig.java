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
public interface HidekiBindingConfig extends BindingConfig {
  public int getSensorType();
  public String getSensorChannel();
}
