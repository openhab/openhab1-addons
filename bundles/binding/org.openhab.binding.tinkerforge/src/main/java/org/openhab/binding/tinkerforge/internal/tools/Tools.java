package org.openhab.binding.tinkerforge.internal.tools;

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

public class Tools {

  public static DecimalValue calculate(int value){
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
}
