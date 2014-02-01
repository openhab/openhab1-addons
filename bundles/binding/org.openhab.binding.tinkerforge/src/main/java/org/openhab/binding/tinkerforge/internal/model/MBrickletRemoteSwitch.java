/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletRemoteSwitch;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Remote Switch</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletRemoteSwitch()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletRemoteSwitch> org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.RemoteSwitch> org.openhab.binding.tinkerforge.internal.model.SubDeviceAdmin"
 * @generated
 */
public interface MBrickletRemoteSwitch extends MDevice<BrickletRemoteSwitch>, MSubDeviceHolder<RemoteSwitch>, SubDeviceAdmin
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_remote_switch"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletRemoteSwitch_DeviceType()
   * @model default="bricklet_remote_switch" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

} // MBrickletRemoteSwitch
