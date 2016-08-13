package org.openhab.binding.hideki.internal;

public class HidekiDecoder {

  // private constructor
  private HidekiDecoder()  {
    // forbid object construction
  }

  static {
    // Load the platform library
    System.loadLibrary("hideki");
  }
    
  public static native void setTimeOut(int timeout);

  public static native int startDecoder(int pin);
  public static native int stopDecoder(int pin);

  public static native int[] getDecodedData();
}
