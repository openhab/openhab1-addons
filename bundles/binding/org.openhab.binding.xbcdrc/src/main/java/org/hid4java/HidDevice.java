package org.hid4java;

import org.hid4java.jna.HidApi;
import org.hid4java.jna.HidDeviceStructure;

/**
 * <p>High level wrapper to provide the following to API consumers:</p>
 * <ul>
 * <li>Simplified access to the underlying JNA HidDeviceStructure</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public class HidDevice {

  private final HidDeviceManager hidDeviceManager;
  private final HidDeviceStructure hidDeviceStructure;
  private final int vendorId;
  private final int productId;
  private final String serialNumber;

  public HidDevice(HidDeviceManager hidDeviceManager, HidDeviceStructure hidDeviceStructure, int vendorId, int productId, String serialNumber) {

    this.hidDeviceManager = hidDeviceManager;
    this.hidDeviceStructure = hidDeviceStructure;

    this.vendorId = vendorId;
    this.productId = productId;
    this.serialNumber = serialNumber;
  }

  /**
   * @return A unique device ID made up from vendor ID, product ID and serial number
   */
  public String getId() {

    return "" + vendorId + "_" + productId + (serialNumber == null ? "" : "_" + serialNumber);

  }

  /**
   * @return The vendor ID
   */
  public int getVendorId() {
    return vendorId;
  }

  /**
   * @return The product ID
   */
  public int getProductId() {
    return productId;
  }

  /**
   * @return The serial number or null
   */
  public String getSerialNumber() {
    return serialNumber;
  }

  public int write(byte[] message, int packetLength, byte reportId) {
    return HidApi.write(hidDeviceStructure, message, packetLength, reportId);
  }

  public String getLastErrorMessage() {
    return HidApi.getLastErrorMessage(hidDeviceStructure);
  }

  /**
   * <p>Set the device handle to be non-blocking</p>
   *
   * <p>In non-blocking mode calls to hid_read() will return immediately with a value of 0 if there is no data to be read.
   * In blocking mode, hid_read() will wait (block) until there is data to read before returning</p>
   *
   * <p>Non-blocking can be turned on and off at any time</p>
   *
   * @param nonBlocking True if non-blocking mode is required
   */
  public void setNonBlocking(boolean nonBlocking) {
    HidApi.setNonBlocking(hidDeviceStructure, nonBlocking);
  }

  /**
   * <p>Read an Input report from a HID device</p>
   * <p>Input reports are returned to the host through the INTERRUPT IN endpoint. The first byte
   * will contain the Report number if the device uses numbered reports</p>
   *
   * @param data The buffer to read into
   *
   * @return The actual number of bytes read and -1 on error. If no packet was available to be read
   * and the handle is in non-blocking mode, this function returns 0.
   */
  public int read(byte[] data) {
    return HidApi.read(hidDeviceStructure, data);
  }

  /**
   * <p>Read an Input report from a HID device with timeout</p>
   *
   * @param bytes         The buffer to read into
   * @param timeoutMillis The number of milliseconds to wait before giving up
   *
   * @return The actual number of bytes read and -1 on error. If no packet was available to be read within
   * the timeout period returns 0.
   */
  public int read(byte[] bytes, int timeoutMillis) {

    return HidApi.read(hidDeviceStructure, bytes, timeoutMillis);

  }

  /**
   * <p>Get a feature report from a HID device</p>
   * <p>Under the covers the HID library will set the first byte of data[] to the Report ID of the report to be read.
   * Upon return, the first byte will still contain the Report ID, and the report data will start in data[1]</p>
   * <p>This method handles all the wide string and array manipulation for you</p>
   *
   * @param data     The buffer to contain the report
   * @param reportId The report ID (or (byte) 0x00)
   *
   * @return The number of bytes read plus one for the report ID (which has been removed from the first byte), or -1 on error.
   */
  public int getFeatureReport(byte[] data, byte reportId) {
    return HidApi.getFeatureReport(hidDeviceStructure, data, reportId);
  }

  /**
   * <p>Send a Feature report to the device</p>
   *
   * <p>Under the covers, feature reports are sent over the Control endpoint as a Set_Report transfer.
   * The first byte of data[] must contain the Report ID. For devices which only support a single report,
   * this must be set to 0x0. The remaining bytes contain the report data</p>
   * <p>Since the Report ID is mandatory, calls to hid_send_feature_report() will always contain one more byte than
   * the report contains. For example, if a hid report is 16 bytes long, 17 bytes must be passed to
   * hid_send_feature_report(): the Report ID (or 0x0, for devices which do not use numbered reports), followed by
   * the report data (16 bytes). In this example, the length passed in would be 17</p>
   *
   * <p>This method handles all the array manipulation for you</p>
   *
   * @param data     The feature report data (will be widened and have the report ID pre-pended)
   * @param reportId The report ID (or (byte) 0x00)
   *
   * @return This function returns the actual number of bytes written and -1 on error.
   */
  public int sendFeatureReport(byte[] data, byte reportId) {
    return HidApi.sendFeatureReport(hidDeviceStructure, data, reportId);
  }

  /**
   * <p>Get a string from a HID device, based on its string index</p>
   *
   * @param index The index
   *
   * @return The string
   */
  public String getIndexedString(int index) {
    return HidApi.getIndexedString(hidDeviceStructure, index);
  }

  /**
   * <p>Close this device</p>
   */
  public void close() {
    HidApi.close(hidDeviceStructure);
  }


  @Override
  public String toString() {
    return "HidDevice{" +
      "hidDeviceManager=" + hidDeviceManager +
      ", vendorId=" + Integer.toHexString(vendorId) +
      ", productId=" + Integer.toHexString(productId) +
      ", serialNumber='" + serialNumber + '\'' +
      '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    HidDevice hidDevice = (HidDevice) o;

    if (productId != hidDevice.productId) {
      return false;
    }
    if (vendorId != hidDevice.vendorId) {
      return false;
    }
    if (serialNumber != null ? !serialNumber.equals(hidDevice.serialNumber) : hidDevice.serialNumber != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = vendorId;
    result = 31 * result + productId;
    result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
    return result;
  }
}
