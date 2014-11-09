package org.hid4java.jna;

import com.sun.jna.Structure;
import com.sun.jna.WString;

import java.util.Arrays;
import java.util.List;

/**
 * <p>Value object to provide HID device information</p>
 */
public class HidDeviceInfoStructure extends Structure implements Structure.ByReference {

  /**
   * USB path
   */
  public String path;

  /**
   * Vendor ID
   */
  public short vendor_id;
  /**
   * Produce ID
   */
  public short product_id;
  /**
   * Serial number
   */
  public WString serial_number;

  /**
   * Release number
   */
  public short release_number;
  /**
   * Manufacturer string
   */
  public WString manufacturer_string;

  /**
   * Usage Page for this Device/Interface (Windows/Mac only)
   */
  public WString product_string;
  /**
   * Usage for this Device/Interface (Windows/Mac only)
   */
  public short usage_page;

  /**
   * Usage number
   */
  public short usage;
  /**
   * Interface number
   */
  public int interface_number;

  /**
   * Reference to next device
   */
  // Consider public HidDeviceInfo.ByReference next;
  public HidDeviceInfoStructure next;

  public HidDeviceInfoStructure next() {
    return next;
  }

  public boolean hasNext() {
    return next != null;
  }

  @Override
  protected List getFieldOrder() {

    // If this precise order is not specified you get "SIGSEGV (0xb)"
    return Arrays.asList(
      "path",
      "vendor_id",
      "product_id",
      "serial_number",
      "release_number",
      "manufacturer_string",
      "product_string",
      "usage_page",
      "usage",
      "interface_number",
      "next"
    );

  }

  /**
   * @return A string representation of the attached device
   */
  public String show() {
    HidDeviceInfoStructure u = this;
    String str = "HidDeviceInfo\n";
    str += "\tpath:" + u.path + ">\n";
    str += "\tvendor_id: " + Integer.toHexString(u.vendor_id) + "\n";
    str += "\tproduct_id: " + Integer.toHexString(u.product_id) + "\n";
    str += "\tserial_number: " + u.serial_number + ">\n";
    str += "\trelease_number: " + u.release_number + "\n";
    str += "\tmanufacturer_string: " + u.manufacturer_string + ">\n";
    str += "\tproduct_string: " + u.product_string + ">\n";
    str += "\tusage_page: " + u.usage_page + "\n";
    str += "\tusage: " + u.usage + "\n";
    str += "\tinterface_number: " + u.interface_number + "\n";
    return str;
  }
}

