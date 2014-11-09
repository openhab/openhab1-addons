package org.hid4java.jna;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Low level JNA value object to provide a HID device pointer</p>
 */
public class HidDeviceStructure extends Structure implements Structure.ByReference {

  public Pointer ptr;

  public HidDeviceStructure(Pointer p) {
    ptr = p;
  }

  public Pointer ptr() {
    return ptr;
  }

  @Override
  protected List getFieldOrder() {
    return Arrays.asList("ptr");
  }
}
