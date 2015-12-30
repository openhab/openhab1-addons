/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bricklet Color Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletColorConfiguration#getGain <em>Gain</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletColorConfiguration#getIntegrationTime <em>Integration Time</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletColorConfiguration()
 * @model
 * @generated
 */
public interface BrickletColorConfiguration extends TFConfig
{
  /**
   * Returns the value of the '<em><b>Gain</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Gain</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Gain</em>' attribute.
   * @see #setGain(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletColorConfiguration_Gain()
   * @model unique="false"
   * @generated
   */
  Short getGain();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletColorConfiguration#getGain <em>Gain</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Gain</em>' attribute.
   * @see #getGain()
   * @generated
   */
  void setGain(Short value);

  /**
   * Returns the value of the '<em><b>Integration Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Integration Time</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Integration Time</em>' attribute.
   * @see #setIntegrationTime(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletColorConfiguration_IntegrationTime()
   * @model unique="false"
   * @generated
   */
  Short getIntegrationTime();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletColorConfiguration#getIntegrationTime <em>Integration Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Integration Time</em>' attribute.
   * @see #getIntegrationTime()
   * @generated
   */
  void setIntegrationTime(Short value);

} // BrickletColorConfiguration
