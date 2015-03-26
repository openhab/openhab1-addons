/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.tools;

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

public class Tools {

  public static DecimalValue calculate(int value){
    BigDecimal bvalue = new BigDecimal(String.valueOf(value));
    return new DecimalValue(bvalue);
    
  }

  public static DecimalValue calculate(short value) {
    BigDecimal bvalue = new BigDecimal(String.valueOf(value));
    return new DecimalValue(bvalue);

  }
  public static DecimalValue calculate10(short value){
    return calculate(value, BigDecimal.TEN);
  }

  public static DecimalValue calculate10(int value){
    return calculate(value, BigDecimal.TEN);
  }

  public static DecimalValue calculate1000(int value){
    return calculate(value, new BigDecimal("1000"));
  }

  public static DecimalValue calculate100(short value){
    return calculate(value, new BigDecimal("100"));
  }

  public static DecimalValue calculate(short value, BigDecimal devider){
    BigDecimal bvalue = new BigDecimal(String.valueOf(value)).divide(devider);
    return new DecimalValue(bvalue);
  }

  public static DecimalValue calculate(int value, BigDecimal devider){
    BigDecimal bvalue = new BigDecimal(String.valueOf(value)).divide(devider);
    return new DecimalValue(bvalue);
  }
  
  public static BigDecimal getBigDecimalOpt(String key, DeviceOptions opts){
    return getBigDecimalOpt(key, opts, null);
  }
  
  public static BigDecimal getBigDecimalOpt(String key, DeviceOptions opts,
      BigDecimal bigdecimaldefault) {
    if (opts.containsKey(key.toLowerCase())) {
      BigDecimal value = new BigDecimal(opts.getOption(key.toLowerCase()));
      return value;
    }
    return bigdecimaldefault;
  }
  
  public static Short getShortOpt(String key, DeviceOptions opts) throws NumberFormatException {
    return getShortOpt(key, opts, null);
  }

  public static Short getShortOpt(String key, DeviceOptions opts, Short shortdefault)
      throws NumberFormatException {
    if (opts.containsKey(key.toLowerCase())) {
      short value = Short.parseShort(opts.getOption(key.toLowerCase()));
      return value;
    }
    return shortdefault;
  }

  public static Long getLongOpt(String key, DeviceOptions opts) throws NumberFormatException {
    return getLongOpt(key, opts, null);
  }

  public static Long getLongOpt(String key, DeviceOptions opts, Long shortdefault)
      throws NumberFormatException {
    if (opts.containsKey(key.toLowerCase())) {
      long value = Long.parseLong(opts.getOption(key.toLowerCase()));
      return value;
    }
    return shortdefault;
  }


  public static Integer getIntOpt(String key, DeviceOptions opts) throws NumberFormatException {
    return getIntOpt(key, opts, null);
  }

  public static Integer getIntOpt(String key, DeviceOptions opts, Integer intdefault)
      throws NumberFormatException {
    if (opts.containsKey(key.toLowerCase())) {
      int value = Integer.valueOf(opts.getOption(key.toLowerCase()));
      return value;
    }
    return intdefault;
  }

  public static String getStringOpt(String key, DeviceOptions opts) {
    return getStringOpt(key, opts, null);
  }

  public static String getStringOpt(String key, DeviceOptions opts, String stringdefault) {
    if (opts.containsKey(key.toLowerCase())) {
      return opts.getOption(key.toLowerCase());
    }
    return stringdefault;
  }
}
