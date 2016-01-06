/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.ecosystem;

import org.openhab.binding.tinkerforge.internal.model.Ecosystem;

public class TinkerforgeContext {

  private static TinkerforgeContext instance;
  private Ecosystem ecosystem;
  
  private TinkerforgeContext() {}

  public static TinkerforgeContext getInstance() {
    if (instance == null) {
      instance = new TinkerforgeContext();
    }
    return instance;
  }

  public Ecosystem getEcosystem() {
    return ecosystem;
  }

  public void setEcosystem(Ecosystem ecosystem) {
    this.ecosystem = ecosystem;
  }

}
