/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletMultiTouch;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Multi Touch</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletMultiTouch#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletMultiTouch()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletMultiTouch> org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.MultiTouchDevice>"
 * @generated
 */
public interface MBrickletMultiTouch extends MDevice<BrickletMultiTouch>, MSubDeviceHolder<MultiTouchDevice>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_multitouch"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletMultiTouch_DeviceType()
   * @model default="bricklet_multitouch" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

} // MBrickletMultiTouch
