package org.openhab.binding.tinkerforge.internal.types;

import java.math.BigDecimal;

import org.openhab.core.library.types.PercentType;

public class PercentValue extends PercentType implements TinkerforgeValue {

  public PercentValue(BigDecimal bigDecimal) {
    super(bigDecimal);
  }

  /**
   * 
   */
  private static final long serialVersionUID = 8087283524157935305L;

}
