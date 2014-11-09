package org.hid4java.event;

import org.hid4java.HidDeviceInfo;


/**
 * <p>Event to provide the following to API consumers:</p>
 * <ul>
 * <li>Provision of HID device information</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public class HidServicesEvent {

  private final HidDeviceInfo hidDeviceInfo;

  /**
   * @param device The HidDeviceInfo involved in the event
   */
  public HidServicesEvent(HidDeviceInfo device) {
    hidDeviceInfo = device;
  }

  /**
   * @return The associated HidDeviceInfo
   */
  public HidDeviceInfo getHidDeviceInfo() {
    return hidDeviceInfo;
  }

  @Override
  public String toString() {
    return "HidServicesEvent{" +
      "hidDeviceInfo=" + hidDeviceInfo +
      '}';
  }
}
