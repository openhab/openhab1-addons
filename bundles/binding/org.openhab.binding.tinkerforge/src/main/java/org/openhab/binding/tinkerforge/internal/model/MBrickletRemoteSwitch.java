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
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch#getTypeADevices <em>Type ADevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch#getTypeBDevices <em>Type BDevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch#getTypeCDevices <em>Type CDevices</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletRemoteSwitch()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletRemoteSwitch> org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.RemoteSwitch> org.openhab.binding.tinkerforge.internal.model.SubDeviceAdmin org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration>"
 * @generated
 */
public interface MBrickletRemoteSwitch extends MDevice<BrickletRemoteSwitch>, MSubDeviceHolder<RemoteSwitch>, SubDeviceAdmin, MTFConfigConsumer<BrickletRemoteSwitchConfiguration>
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

  /**
   * Returns the value of the '<em><b>Type ADevices</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type ADevices</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type ADevices</em>' attribute.
   * @see #setTypeADevices(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletRemoteSwitch_TypeADevices()
   * @model unique="false"
   * @generated
   */
  String getTypeADevices();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch#getTypeADevices <em>Type ADevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type ADevices</em>' attribute.
   * @see #getTypeADevices()
   * @generated
   */
  void setTypeADevices(String value);

  /**
   * Returns the value of the '<em><b>Type BDevices</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type BDevices</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type BDevices</em>' attribute.
   * @see #setTypeBDevices(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletRemoteSwitch_TypeBDevices()
   * @model unique="false"
   * @generated
   */
  String getTypeBDevices();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch#getTypeBDevices <em>Type BDevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type BDevices</em>' attribute.
   * @see #getTypeBDevices()
   * @generated
   */
  void setTypeBDevices(String value);

  /**
   * Returns the value of the '<em><b>Type CDevices</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type CDevices</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type CDevices</em>' attribute.
   * @see #setTypeCDevices(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletRemoteSwitch_TypeCDevices()
   * @model unique="false"
   * @generated
   */
  String getTypeCDevices();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch#getTypeCDevices <em>Type CDevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type CDevices</em>' attribute.
   * @see #getTypeCDevices()
   * @generated
   */
  void setTypeCDevices(String value);

} // MBrickletRemoteSwitch
