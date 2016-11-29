/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hideki.internal;

public class HidekiDecoder {

  // private constructor
  private HidekiDecoder()  {
    // forbid object construction
  }

  static {
    // Load the platform library
    NativeLibraryLoader.load("libhideki.so");
  }
    
  public static native void setTimeOut(int timeout);

  public static native int startDecoder(int pin);
  public static native int stopDecoder(int pin);

  public static native int[] getDecodedData();
}
