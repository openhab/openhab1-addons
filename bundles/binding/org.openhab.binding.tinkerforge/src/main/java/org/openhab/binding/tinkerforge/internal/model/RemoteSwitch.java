/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Remote Switch</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitch#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitch()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface RemoteSwitch extends MInSwitchActor, MSubDevice<MBrickletRemoteSwitch>
{

  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"remote_switch"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitch_DeviceType()
   * @model default="remote_switch" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();
} // RemoteSwitch
