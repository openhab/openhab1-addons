/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TFIO Actor Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration#isDefaultState <em>Default State</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFIOActorConfiguration()
 * @model
 * @generated
 */
public interface TFIOActorConfiguration extends TFConfig
{
  /**
   * Returns the value of the '<em><b>Default State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default State</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default State</em>' attribute.
   * @see #setDefaultState(boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFIOActorConfiguration_DefaultState()
   * @model unique="false"
   * @generated
   */
  boolean isDefaultState();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration#isDefaultState <em>Default State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default State</em>' attribute.
   * @see #isDefaultState()
   * @generated
   */
  void setDefaultState(boolean value);

} // TFIOActorConfiguration
