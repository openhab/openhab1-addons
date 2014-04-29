/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Touch Device Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MultiTouchDeviceConfiguration#getDisableElectrode <em>Disable Electrode</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMultiTouchDeviceConfiguration()
 * @model
 * @generated
 */
public interface MultiTouchDeviceConfiguration extends TFConfig
{
  /**
   * Returns the value of the '<em><b>Disable Electrode</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Disable Electrode</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Disable Electrode</em>' attribute.
   * @see #setDisableElectrode(Boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMultiTouchDeviceConfiguration_DisableElectrode()
   * @model unique="false"
   * @generated
   */
  Boolean getDisableElectrode();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MultiTouchDeviceConfiguration#getDisableElectrode <em>Disable Electrode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Disable Electrode</em>' attribute.
   * @see #getDisableElectrode()
   * @generated
   */
  void setDisableElectrode(Boolean value);

} // MultiTouchDeviceConfiguration
