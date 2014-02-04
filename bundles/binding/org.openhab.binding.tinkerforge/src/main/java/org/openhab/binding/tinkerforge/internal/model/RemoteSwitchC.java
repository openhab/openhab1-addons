/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Remote Switch C</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC#getSystemCode <em>System Code</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC#getDeviceCode <em>Device Code</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC#getRepeats <em>Repeats</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchC()
 * @model
 * @generated
 */
public interface RemoteSwitchC extends RemoteSwitch, MTFConfigConsumer<RemoteSwitchCConfiguration>
{

  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"remote_switch_c"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchC_DeviceType()
   * @model default="remote_switch_c" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>System Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>System Code</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>System Code</em>' attribute.
   * @see #setSystemCode(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchC_SystemCode()
   * @model unique="false"
   * @generated
   */
  String getSystemCode();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC#getSystemCode <em>System Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>System Code</em>' attribute.
   * @see #getSystemCode()
   * @generated
   */
  void setSystemCode(String value);

  /**
   * Returns the value of the '<em><b>Device Code</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Code</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Code</em>' attribute.
   * @see #setDeviceCode(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchC_DeviceCode()
   * @model unique="false"
   * @generated
   */
  Short getDeviceCode();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC#getDeviceCode <em>Device Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Device Code</em>' attribute.
   * @see #getDeviceCode()
   * @generated
   */
  void setDeviceCode(Short value);

  /**
   * Returns the value of the '<em><b>Repeats</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Repeats</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Repeats</em>' attribute.
   * @see #setRepeats(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getRemoteSwitchC_Repeats()
   * @model unique="false"
   * @generated
   */
  Short getRepeats();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.RemoteSwitchC#getRepeats <em>Repeats</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Repeats</em>' attribute.
   * @see #getRepeats()
   * @generated
   */
  void setRepeats(Short value);
} // RemoteSwitchC
