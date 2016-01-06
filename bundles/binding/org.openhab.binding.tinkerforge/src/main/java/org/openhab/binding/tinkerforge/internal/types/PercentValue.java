/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
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
