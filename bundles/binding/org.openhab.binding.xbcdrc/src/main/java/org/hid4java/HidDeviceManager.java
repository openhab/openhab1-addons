package org.hid4java;

import org.hid4java.event.HidServicesListenerList;
import org.hid4java.jna.HidApi;
import org.hid4java.jna.HidDeviceInfoStructure;
import org.hid4java.jna.HidDeviceStructure;

import java.util.*;

/**
 * <p>Manager to provide the following to HID services:</p>
 * <ul>
 * <li>Access to the underlying JNA and hidapi library</li>
 * <li>Device attach/detach detection</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
class HidDeviceManager {

  /**
   * The scan interval in milliseconds
   */
  private int scanInterval = 500;

  /**
   * The currently attached devices keyed on ID
   */
  private final Map<String, HidDeviceInfo> attachedDevices = Collections.synchronizedMap(new HashMap<String, HidDeviceInfo>());

  /**
   * HID services listener list
   */
  private final HidServicesListenerList listenerList;

  /**
   * Constructs a new device manager
   *
   * @param listenerList The HID services providing access to the event model
   * @param scanInterval The scan interval in milliseconds (default is 500ms)
   *
   * @throws HidException If USB HID initialization fails
   */
  HidDeviceManager(HidServicesListenerList listenerList, final int scanInterval) throws HidException {

    this.listenerList = listenerList;
    this.scanInterval = scanInterval;

  }

  /**
   * Updates the device list by adding newly connected devices to it and by
   * removing no longer connected devices
   */
  public synchronized void scan() {

    List<String> removeList = new ArrayList<String>();

    List<HidDeviceInfo> currentList = getAttachedHidDevices();

    for (HidDeviceInfo deviceInfo : currentList) {

      if (!this.attachedDevices.containsKey(deviceInfo.getId())) {

        // Device has become attached so add it but do not open
        attachedDevices.put(deviceInfo.getId(), deviceInfo);

        // Fire the event on a separate thread
        listenerList.fireHidDeviceAttached(deviceInfo);

      }

    }

    for (Map.Entry<String, HidDeviceInfo> entry : attachedDevices.entrySet()) {

      String deviceId = entry.getKey();
      HidDeviceInfo hidDeviceInfo = entry.getValue();

      if (!currentList.contains(hidDeviceInfo)) {

        // Keep track of removals
        removeList.add(deviceId);

        // Fire the event on a separate thread
        listenerList.fireHidDeviceDetached(this.attachedDevices.get(deviceId));

      }

    }

    if (!removeList.isEmpty()) {
      // Update the attached devices map
      this.attachedDevices.keySet().removeAll(removeList);
    }

  }

  /**
   * Performs an immediate scan of attached devices then continues scanning in the background
   */
  public void start() {

    // Perform a one-off scan to populate attached devices
    scan();

    // Do not start the scan thread when interval is set to 0
    final int scanInterval = this.scanInterval;
    if (scanInterval == 0) {
      return;
    }

    // Create a daemon thread to ensure lifecycle
    final Thread thread = new Thread(
      new Runnable() {
        @Override
        public void run() {
          while (true) {
            try {
              Thread.sleep(scanInterval);
            } catch (final InterruptedException e) {
              Thread.currentThread().interrupt();
            }
            scan();
          }
        }
      });
    thread.setDaemon(true);
    thread.setName("hid4java Device Scanner");
    thread.start();
  }

  /**
   * @return A list of all attached HID devices
   */
  public List<HidDeviceInfo> getAttachedHidDevices() {

    List<HidDeviceInfo> hidDeviceInfoList = new ArrayList<HidDeviceInfo>();

    // Use 0,0 to list all attached devices
    // This comes back as a linked list from hidapi
    HidDeviceInfoStructure root = HidApi.enumerateDevices(0, 0);
    if (root != null) {

      HidDeviceInfoStructure hidDeviceInfoStructure = root;
      do {
        // Wrap in HidDeviceInfo
        hidDeviceInfoList.add(new HidDeviceInfo(hidDeviceInfoStructure));
        // Move to the next in the linked list
        hidDeviceInfoStructure = hidDeviceInfoStructure.next();
      } while (hidDeviceInfoStructure != null);

      // Dispose of the device list to free memory
      HidApi.freeEnumeration(root);
    }

    return hidDeviceInfoList;
  }

  public HidDevice open(int vendorId, int productId, String serialNumber) {

    HidDeviceStructure deviceStructure = HidApi.open(vendorId, productId, serialNumber);

    if (deviceStructure != null) {
      return new HidDevice(this, deviceStructure, vendorId, productId, serialNumber);
    }
    return null;
  }

}