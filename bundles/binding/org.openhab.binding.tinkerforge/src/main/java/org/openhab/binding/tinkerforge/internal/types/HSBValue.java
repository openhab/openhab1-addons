/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.types;

import org.openhab.core.library.types.HSBType;

/*
 * Just a simple wrapper for {@link HSBType}, to get the {@link TinkerforgeValue}
 * marker interface.
 * 
 * @author Theo Weiss
 * @since 1.7.0
 */
public class HSBValue implements TinkerforgeValue {

  private HSBType hsbType;

  public HSBValue(HSBType hsbValue) {
    this.hsbType = hsbValue;
  }

  public HSBType getHsbValue() {
    return hsbType;
  }

  public void setHsbValue(HSBType hsbType) {
    this.hsbType = hsbType;
  }


}
