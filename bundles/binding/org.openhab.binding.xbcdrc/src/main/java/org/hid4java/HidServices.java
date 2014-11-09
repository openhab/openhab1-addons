package org.hid4java;

import org.hid4java.event.HidServicesListenerList;

import java.util.List;

/**
 * <p>JNA bridge class to provide the following to USB HID:</p>
 * <ul>
 * <li>Access to the <code>signal11/hidapi</code> via JNA</li>
 * </ul>
 * <p>Requires the hidapi to be present on the classpath or the system library search path.</p>
 *
 *
 * @since 0.0.1
 *  
 */
public class HidServices {

  /**
   * The HID services listeners
   */
  private final HidServicesListenerList listeners = new HidServicesListenerList();

  /**
   * The HID device scanner
   */
  private final HidDeviceManager deviceManager;

  /**
   *
   * @throws HidException If something goes wrong
   */
  public HidServices() throws HidException {

    deviceManager = new HidDeviceManager(listeners, 500);
    deviceManager.start();

  }

  /**
   * @param listener The listener to add
   */
  public void addHidServicesListener(final HidServicesListener listener) {
    this.listeners.add(listener);
  }

  /**
   * @param listener The listener to remove
   */
  public void removeUsbServicesListener(final HidServicesListener listener) {
    this.listeners.remove(listener);
  }

  /**
   * Manually scans for HID device connection changes and triggers listener events as required
   */
  public void scan() {
    this.deviceManager.scan();
  }

  /**
   * @return A list of all attached HID devices
   */
  public List<HidDeviceInfo> getAttachedHidDevices() {

    return deviceManager.getAttachedHidDevices();
  }

  /**
   * @param vendorId     The vendor ID
   * @param productId    The product ID
   * @param serialNumber The serial number (use null for wildcard)
   *
   * @return The device if attached, null if detached
   */
  public HidDevice getHidDevice(int vendorId, int productId, String serialNumber) {
    return deviceManager.open(vendorId, productId, serialNumber);
  }
}
