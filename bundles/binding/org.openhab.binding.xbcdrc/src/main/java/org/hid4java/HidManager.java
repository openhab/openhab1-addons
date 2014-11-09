package org.hid4java;

/**
 * <p>Factory to provide the following to API consumers:</p>
 * <ul>
 * <li>Access to configured HID services</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public class HidManager {

  private static final Object servicesLock = new Object();

  private static HidServices hidServices = null;

  /**
   * @return A single instance of the HID services
   */
  public static HidServices getHidServices() throws HidException {

    synchronized (servicesLock) {
      if (null == hidServices) {
        hidServices = new HidServices();
      }
    }

    return hidServices;

  }

}
