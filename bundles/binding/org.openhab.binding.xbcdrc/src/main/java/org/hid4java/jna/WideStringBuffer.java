package org.hid4java.jna;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Wrapper for a wide character (WCHAR) structure</p>
 */
public class WideStringBuffer extends Structure implements Structure.ByReference {

  public byte[] buffer = null;

  public WideStringBuffer(int len) {
    buffer = new byte[len];
  }

  public WideStringBuffer(byte[] bytes) {
    buffer = bytes;
  }

  @Override
  protected List getFieldOrder() {
    return Arrays.asList("buffer");
  }

  /**
   * <p>hidapi uses wchar_t which is written l i k e   t h i s (with '\0' in between)</p>
   */
  public String toString() {
    String str = "";
    for (int i = 0; i < buffer.length && buffer[i] != 0; i += 2)
      str += (char) (buffer[i] | buffer[i + 1] << 8);
    return str;
  }

}
