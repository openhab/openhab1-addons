package org.hid4java;

/**
 * <p>Caught exception to provide the following to API consumers:</p>
 * <ul>
 * <li>Notification of a serious problem with HID</li>
 * </ul>
 *
 * @since 0.0.1
 *  
 */
public class HidException extends Exception
{

  public HidException(String message) {
    super(message);
  }

  public HidException(String message, Throwable cause) {
    super(message, cause);
  }
}
