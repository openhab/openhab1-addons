/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bricklet Remote Switch Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration#getTypeADevices <em>Type ADevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration#getTypeBDevices <em>Type BDevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration#getTypeCDevices <em>Type CDevices</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletRemoteSwitchConfiguration()
 * @model
 * @generated
 */
public interface BrickletRemoteSwitchConfiguration extends TFConfig
{
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
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletRemoteSwitchConfiguration_TypeADevices()
   * @model unique="false"
   * @generated
   */
  String getTypeADevices();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration#getTypeADevices <em>Type ADevices</em>}' attribute.
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
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletRemoteSwitchConfiguration_TypeBDevices()
   * @model unique="false"
   * @generated
   */
  String getTypeBDevices();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration#getTypeBDevices <em>Type BDevices</em>}' attribute.
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
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletRemoteSwitchConfiguration_TypeCDevices()
   * @model unique="false"
   * @generated
   */
  String getTypeCDevices();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletRemoteSwitchConfiguration#getTypeCDevices <em>Type CDevices</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type CDevices</em>' attribute.
   * @see #getTypeCDevices()
   * @generated
   */
  void setTypeCDevices(String value);

} // BrickletRemoteSwitchConfiguration
