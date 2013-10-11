package org.openhab.binding.urtsi.internal;

import org.eclipse.xtend.lib.Data;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;
import org.openhab.core.binding.BindingConfig;

/**
 * This class contains all the configuration parameters you can define for the binding.
 * @author Oliver Libutzki
 * @since 1.3.0
 */
@Data
@SuppressWarnings("all")
public class UrtsiItemConfiguration implements BindingConfig {
  /**
   * Serial port of the urtsi device
   */
  private final String _deviceId;
  
  /**
   * Serial port of the urtsi device
   */
  public String getDeviceId() {
    return this._deviceId;
  }
  
  /**
   * Channel of the urtsi device
   */
  private final int _channel;
  
  /**
   * Channel of the urtsi device
   */
  public int getChannel() {
    return this._channel;
  }
  
  public UrtsiItemConfiguration(final String deviceId, final int channel) {
    super();
    this._deviceId = deviceId;
    this._channel = channel;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((_deviceId== null) ? 0 : _deviceId.hashCode());
    result = prime * result + _channel;
    return result;
  }
  
  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    UrtsiItemConfiguration other = (UrtsiItemConfiguration) obj;
    if (_deviceId == null) {
      if (other._deviceId != null)
        return false;
    } else if (!_deviceId.equals(other._deviceId))
      return false;
    if (other._channel != _channel)
      return false;
    return true;
  }
  
  @Override
  public String toString() {
    String result = new ToStringHelper().toString(this);
    return result;
  }
}
