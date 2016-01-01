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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tools {
  private static Pattern rangePattern = Pattern.compile("(.+)-(.+)");
  private static Logger logger = LoggerFactory.getLogger(Tools.class);

  public static DecimalValue calculate(int value){
    BigDecimal bvalue = new BigDecimal(String.valueOf(value));
    return new DecimalValue(bvalue);
    
  }

  public static DecimalValue calculate(short value) {
    BigDecimal bvalue = new BigDecimal(String.valueOf(value));
    return new DecimalValue(bvalue);
  }

  public static DecimalValue calculate(long value) {
    BigDecimal bvalue = new BigDecimal(String.valueOf(value));
    return new DecimalValue(bvalue);
  }

  public static DecimalValue calculate10(short value){
    return calculate(value, BigDecimal.TEN);
  }

  public static DecimalValue calculate10(int value){
    return calculate(value, BigDecimal.TEN);
  }

  public static DecimalValue calculate10(long value) {
    return calculate(value, BigDecimal.TEN);
  }

  public static DecimalValue calculate1000(int value){
    return calculate(value, new BigDecimal("1000"));
  }

  public static DecimalValue calculate1000000(int value) {
    return calculate(value, new BigDecimal("1000000"));
  }

  public static DecimalValue calculate100(int value) {
    return calculate(value, new BigDecimal("100"));
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
  
  public static DecimalValue calculate(long value, BigDecimal devider) {
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

  public static LedList parseLedString(String leds) {
    // parse leds variable to get the led numbers / range which should be switched.
    // the config looks like this:
    // 1. pipe separated list of led numbers, e.g. "1|2|4|5"
    // 2. a range of leds: e.g. "1-5"
    // 3. combination of 1. and 2., e.g. "0|1|4-6|8-9"
    LedList ledlist = new LedList();
    String[] tokens = leds.split("\\|");
    for (int i = 0; i < tokens.length; i++) {
      String token = tokens[i].trim();
      logger.trace("led token {}", token);
      if (token.length() == 0) {
        logger.trace("ignoring empty token");
        continue;
      }
      Matcher matcher = rangePattern.matcher(token);
      if (matcher.find()) {
        logger.trace("found range");
        int startLed = Integer.parseInt(matcher.group(1).trim());
        logger.debug("found startLed {}", startLed);
        short range = (short) (Short.parseShort(matcher.group(2).trim()) - startLed + 1);
        logger.debug("found range {}", range);
        // the tinkerforge api can only handle 16 leds at the same time
        short maxRange = 16;
        while (range > maxRange) {
          if (!ledlist.hasTrackingled()) {
            ledlist.setTrackingled(startLed);
          }
          ledlist.addLedRange(startLed, maxRange);
          startLed = startLed + maxRange;
          logger.trace("new startled {} range {}", startLed, range);
          range = (short) (range - maxRange);
          logger.trace("cutting down range: {}", range);
        }
        logger.trace("new startled {} range {}", startLed, range);
        ledlist.addLedRange(startLed, range);
      } else {
        int led = (int) Integer.parseInt(token.trim());
        logger.trace("found led {}", led);
        if (!ledlist.hasTrackingled()) {
          ledlist.setTrackingled(led);
        }
        ledlist.addLed(led);
      }
    }
    return ledlist;
  }
}
