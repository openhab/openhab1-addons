/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.config;

import java.util.HashMap;

/**
 * A Class for storing device options from the items configuration. The <key>=<value> options from
 * the items configuration will be are stored in a HashMap with <key> as key and <value> as value.
 * This class is meant to pass through the items configuration to the tinkerforge device when doing
 * method calls on it.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 */
public class DeviceOptions {
  HashMap<String, String> deviceOptions = new HashMap<String, String>();

  /**
   * Add an option to the DeviceOptions.
   * 
   * @param key option name as String
   * @param value option value as String
   */
  public void put(String key, String value) {
    deviceOptions.put(key, value);
  }

  /**
   * Returns the value for the specified option key.
   * 
   * @param option key as {@link String}
   * @return the value as String
   */
  public String getOption(String option) {
    return deviceOptions.get(option);
  }

  /**
   * Returns true if this DeviceOptions contains a option for the specified key.
   * 
   * @param key option name as String
   * @return true if there is an option with this key otherwise false
   */
  public boolean containsKey(String key) {
    return deviceOptions.containsKey(key);
  }

  /**
   * Returns all options as HashMap of option keys and option values as Strings.
   * 
   * @return
   */
  public HashMap<String, String> getDeviceOptions() {
    return deviceOptions;
  }

}
