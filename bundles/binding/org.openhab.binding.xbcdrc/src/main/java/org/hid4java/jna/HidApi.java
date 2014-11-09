package org.hid4java.jna;

import com.sun.jna.Pointer;
import com.sun.jna.WString;

/**
 * <p>JNA utility class to provide the following to low level operations:</p>
 * <ul>
 * <li>Direct access to the hidapi library through JNA</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public class HidApi {

  /**
   * Default length for wide string buffer
   */
  private static int WSTR_LEN = 512;

  /**
   * Error message if device is not initialised
   */
  private static final String DEVICE_NULL = "Device not initialised";

  /**
   * Device error code
   */
  private static final int DEVICE_ERROR = -2;

  /**
   * The hidapi library
   */
  private static final HidApiLibrary hidApiLibrary = HidApiLibrary.INSTANCE;

  /**
   * <p>Open a HID device using a Vendor ID (VID), Product ID (PID) and optionally a serial number</p>
   */
  public static HidDeviceStructure open(int vendor, int product, String serial_number) {

    // Attempt to open the device
    Pointer p = hidApiLibrary.hid_open(
      (short) vendor,
      (short) product,
      serial_number == null ? null : new WString(serial_number)
    );

    if (p != null) {
      // Wrap the structure
      return new HidDeviceStructure(p);
    }

    return null;

  }

  /**
   * <p>Initialise the hidapi library</p>
   */
  public static void init() {
    hidApiLibrary.hid_init();
  }

  /**
   * <p>Finalise the hidapi library</p>
   */
  public static void exit() {
    hidApiLibrary.hid_exit();
  }

  /**
   * <p>Open a HID device by its path name</p>
   */
  public static HidDeviceStructure open(String path) {
    Pointer p = hidApiLibrary.hid_open_path(path);
    return (p == null ? null : new HidDeviceStructure(p));
  }

  /**
   * <p>Close a HID device</p>
   */
  public static void close(HidDeviceStructure device) {

    if (device != null) {
      hidApiLibrary.hid_close(device.ptr());
    }

  }

  /**
   * <p>Enumerate the attached HID devices</p>
   *
   * @param vendor  The vendor ID
   * @param product The product ID
   *
   * @return The device info of the matching device
   */
  public static HidDeviceInfoStructure enumerateDevices(int vendor, int product) {

    return hidApiLibrary.hid_enumerate((short) vendor, (short) product);

  }

  /**
   * <p>Free an enumeration linked list</p>
   *
   * @param list The list to free
   */
  public static void freeEnumeration(HidDeviceInfoStructure list) {

    hidApiLibrary.hid_free_enumeration(list.getPointer());

  }

  /**
   * <p>Get a string describing the last error which occurred</p>
   */
  public static String getLastErrorMessage(HidDeviceStructure device) {

    if (device == null) {
      return DEVICE_NULL;
    }

    Pointer p = hidApiLibrary.hid_error(device.ptr());

    return p == null ? null : new WideStringBuffer(p.getByteArray(0, WSTR_LEN)).toString();
  }

  /**
   * <p>Get The Manufacturer string from a HID device</p>
   *
   * @param device The HID device
   */
  public static String getManufacturer(HidDeviceStructure device) {

    if (device == null) {
      return DEVICE_NULL;
    }

    WideStringBuffer wStr = new WideStringBuffer(WSTR_LEN);
    int res = hidApiLibrary.hid_get_manufacturer_string(device.ptr(), wStr, WSTR_LEN);

    return wStr.toString();
  }

  /**
   * <p>Get The Product ID from a HID device</p>
   *
   * @param device The HID device
   */
  public static String getProductId(HidDeviceStructure device) {

    if (device == null) {
      return DEVICE_NULL;
    }

    WideStringBuffer wStr = new WideStringBuffer(WSTR_LEN);
    int res = hidApiLibrary.hid_get_product_string(device.ptr(), wStr, WSTR_LEN);

    return wStr.toString();
  }

  /**
   * <p>Get The Serial Number String from a HID device</p>
   *
   * @param device The HID device
   */
  public static String getSerialNumber(HidDeviceStructure device) {

    if (device == null) {
      return DEVICE_NULL;
    }

    WideStringBuffer wStr = new WideStringBuffer(WSTR_LEN);

    int res = hidApiLibrary.hid_get_serial_number_string(device.ptr(), wStr, WSTR_LEN);

    return wStr.toString();
  }

  /**
   * <p>Set the device handle to be non-blocking</p>
   *
   * <p>In non-blocking mode calls to hid_read() will return immediately with a value of 0 if there is no data to be read.
   * In blocking mode, hid_read() will wait (block) until there is data to read before returning</p>
   *
   * <p>Non-blocking can be turned on and off at any time</p>
   *
   * @param device      The HID device
   * @param nonBlocking True if non-blocking mode is required
   */
  public static boolean setNonBlocking(HidDeviceStructure device, boolean nonBlocking) {

    return device != null && 0 == hidApiLibrary.hid_set_nonblocking(device.ptr(), nonBlocking ? 1 : 0);

  }

  /**
   * <p>Read an Input report from a HID device</p>
   * <p>Input reports are returned to the host through the INTERRUPT IN endpoint. The first byte
   * will contain the Report number if the device uses numbered reports</p>
   *
   * @param device The HID device
   * @param bytes  The buffer to read into
   *
   * @return The actual number of bytes read and -1 on error. If no packet was available to be read
   * and the handle is in non-blocking mode, this function returns 0.
   */
  public static int read(HidDeviceStructure device, byte[] bytes) {

    if (device == null || bytes == null) {
      return DEVICE_ERROR;
    }

    WideStringBuffer m = new WideStringBuffer(bytes);

    return hidApiLibrary.hid_read(device.ptr(), m, m.buffer.length);

  }

  /**
   * <p>Read an Input report from a HID device with timeout</p>
   *
   * @param device        The HID device
   * @param bytes         The buffer to read into
   * @param timeoutMillis The number of milliseconds to wait before giving up
   *
   * @return The actual number of bytes read and -1 on error. If no packet was available to be read within
   * the timeout period returns 0.
   */
  public static int read(HidDeviceStructure device, byte[] bytes, int timeoutMillis) {

    if (device == null || bytes == null) {
      return DEVICE_ERROR;
    }

    WideStringBuffer m = new WideStringBuffer(bytes);

    return hidApiLibrary.hid_read_timeout(device.ptr(), m, bytes.length, timeoutMillis);

  }

  /**
   * <p>Get a feature report from a HID device</p>
   * <p>Under the covers the HID library will set the first byte of data[] to the Report ID of the report to be read.
   * Upon return, the first byte will still contain the Report ID, and the report data will start in data[1]</p>
   * <p>This method handles all the wide string and array manipulation for you</p>
   *
   * @param device   The HID device
   * @param data     The buffer to contain the report
   * @param reportId The report ID (or (byte) 0x00)
   *
   * @return The number of bytes read plus one for the report ID (which has been removed from the first byte), or -1 on error.
   */
  public static int getFeatureReport(HidDeviceStructure device, byte[] data, byte reportId) {

    if (device == null || data == null) {
      return DEVICE_ERROR;
    }

    // Create a large buffer
    WideStringBuffer m = new WideStringBuffer(WSTR_LEN);
    m.buffer[0] = reportId;
    int res = hidApiLibrary.hid_get_feature_report(device.ptr(), m, data.length + 1);

    if (res == -1) {
      return res;
    }

    System.arraycopy(m.buffer, 1, data, 0, res);
    return res;

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
   * @param device   The HID device
   * @param data     The feature report data (will be widened and have the report ID pre-pended)
   * @param reportId The report ID (or (byte) 0x00)
   *
   * @return This function returns the actual number of bytes written and -1 on error.
   */
  public static int sendFeatureReport(HidDeviceStructure device, byte[] data, byte reportId) {

    if (device == null || data == null) {
      return DEVICE_ERROR;
    }

    WideStringBuffer m = new WideStringBuffer(data.length + 1);
    m.buffer[0] = reportId;

    System.arraycopy(data, 0, m.buffer, 1, data.length);
    return hidApiLibrary.hid_send_feature_report(device.ptr(), m, m.buffer.length);

  }

  /**
   * <p>Get a string from a HID device, based on its string index</p>
   *
   * @param device The HID device
   * @param idx    The index
   *
   * @return The string
   */
  public static String getIndexedString(HidDeviceStructure device, int idx) {

    if (device == null) {
      return DEVICE_NULL;
    }
    WideStringBuffer wStr = new WideStringBuffer(WSTR_LEN);
    int res = hidApiLibrary.hid_get_indexed_string(device.ptr(), idx, wStr, WSTR_LEN);

    return res == -1 ? null : wStr.toString();
  }

  /**
   * <p>Write an Output report to a HID device</p>
   *
   * <p>The first byte of data[] must contain the Report ID. For devices which only support a single report, this must be set to 0x0.
   * The remaining bytes contain the report data. Since the Report ID is mandatory, calls to hid_write() will always contain one more
   * byte than the report contains. For example, if a hid report is 16 bytes long, 17 bytes must be passed to hid_write(), the Report ID
   * (or 0x0, for devices with a single report), followed by the report data (16 bytes). In this example, the length passed in would
   * be 17</p>
   *
   * <p>hid_write() will send the data on the first OUT endpoint, if one exists. If it does not, it will send the data through the
   * Control Endpoint (Endpoint 0)</p>
   *
   * @param device   The device
   * @param bytes    The bytes to write
   * @param len      The length
   * @param reportId The report ID (or (byte) 0x00)
   *
   * @return The number of bytes written, or -1 if an error occurs
   */
  public static int write(HidDeviceStructure device, byte[] bytes, int len, byte reportId) {

    if (device == null || bytes == null) {
      return DEVICE_ERROR;
    }

    WideStringBuffer m = new WideStringBuffer(len + 1);
    m.buffer[0] = reportId;

    if (bytes.length < len) {
      len = bytes.length;
    }

    if (len > 1) {
      System.arraycopy(bytes, 0, m.buffer, 1, len);
    }

    return hidApiLibrary.hid_write(device.ptr(), m, m.buffer.length);
  }
}
