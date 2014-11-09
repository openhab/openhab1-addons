package org.hid4java;

import com.sun.jna.WString;
import org.hid4java.jna.HidDeviceInfoStructure;

/**
 * <p>High level wrapper to provide the following to API consumers:</p>
 * <ul>
 * <li>Simplified access to the underlying JNA HidDeviceInfoStructure</li>
 * <li>Representation of a single HID device info</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public class HidDeviceInfo {

  private String path;

  private short vendorId;
  private short productId;
  private WString serialNumber;

  private short releaseNumber;
  private WString manufacturerString;

  private WString productString;

  private short usagePage;
  private short usage;

  private int interfaceNumber;

  /**
   * @param hidDeviceInfoStructure The HidDeviceInfoStructure providing the data
   */
  public HidDeviceInfo(HidDeviceInfoStructure hidDeviceInfoStructure) {

    // Copy the contents across
    this.path = hidDeviceInfoStructure.path;
    this.vendorId = hidDeviceInfoStructure.vendor_id;
    this.productId = hidDeviceInfoStructure.product_id;
    this.serialNumber = hidDeviceInfoStructure.serial_number;
    this.releaseNumber = hidDeviceInfoStructure.release_number;
    this.manufacturerString = hidDeviceInfoStructure.manufacturer_string;
    this.productString = hidDeviceInfoStructure.product_string;
    this.usagePage = hidDeviceInfoStructure.usage_page;
    this.usage = hidDeviceInfoStructure.usage;
    this.interfaceNumber = hidDeviceInfoStructure.interface_number;

  }

  /**
   * @return The USB path
   */
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  /**
   * @return The Vendor ID
   */
  public short getVendorId() {
    return vendorId;
  }

  public void setVendorId(short vendorId) {
    this.vendorId = vendorId;
  }

  /**
   * @return The product ID
   */
  public short getProductId() {
    return productId;
  }

  public void setProductId(short productId) {
    this.productId = productId;
  }

  /**
   * @return The serial number (wide string)
   */
  public WString getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(WString serialNumber) {
    this.serialNumber = serialNumber;
  }

  /**
   * @return The release number
   */
  public short getReleaseNumber() {
    return releaseNumber;
  }

  public void setReleaseNumber(short releaseNumber) {
    this.releaseNumber = releaseNumber;
  }

  /**
   * @return The manufacturer string
   */
  public WString getManufacturerString() {
    return manufacturerString;
  }

  public void setManufacturerString(WString manufacturerString) {
    this.manufacturerString = manufacturerString;
  }

  /**
   * @return The product info for this Device/Interface (Windows/Mac only)
   */
  public WString getProductString() {
    return productString;
  }

  public void setProductString(WString productString) {
    this.productString = productString;
  }

  /**
   * @return The usage page for this Device/Interface (Windows/Mac only)
   */
  public short getUsagePage() {
    return usagePage;
  }

  public void setUsagePage(short usagePage) {
    this.usagePage = usagePage;
  }

  /**
   * @return The usage number
   */
  public short getUsage() {
    return usage;
  }

  public void setUsage(short usage) {
    this.usage = usage;
  }

  /**
   * @return The USB interface number
   */
  public int getInterfaceNumber() {
    return interfaceNumber;
  }

  public void setInterfaceNumber(int interfaceNumber) {
    this.interfaceNumber = interfaceNumber;
  }

  /**
   * @return A unique device ID made up from vendor ID, product ID and serial number
   */
  public String getId() {

    return "" + vendorId + "_" + productId + (serialNumber == null ? "" : "_" + serialNumber);

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    HidDeviceInfo that = (HidDeviceInfo) o;

    if (productId != that.productId) {
      return false;
    }
    if (vendorId != that.vendorId) {
      return false;
    }
    if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = (int) vendorId;
    result = 31 * result + (int) productId;
    result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "HidDeviceInfo{" +
      "path='" + path + '\'' +
      ", vendorId=" + Integer.toHexString(vendorId) +
      ", productId=" + Integer.toHexString(productId) +
      ", serialNumber=" + serialNumber +
      ", releaseNumber=" + releaseNumber +
      ", manufacturerString=" + manufacturerString +
      ", productString=" + productString +
      ", usagePage=" + usagePage +
      ", usage=" + usage +
      ", interfaceNumber=" + interfaceNumber +
      '}';
  }
}
