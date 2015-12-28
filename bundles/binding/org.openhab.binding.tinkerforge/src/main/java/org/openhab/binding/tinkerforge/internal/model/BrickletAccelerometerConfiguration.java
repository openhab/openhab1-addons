/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bricklet Accelerometer Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletAccelerometerConfiguration#getDataRate <em>Data Rate</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletAccelerometerConfiguration#getFullScale <em>Full Scale</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.BrickletAccelerometerConfiguration#getFilterBandwidth <em>Filter Bandwidth</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletAccelerometerConfiguration()
 * @model
 * @generated
 */
public interface BrickletAccelerometerConfiguration extends TFConfig
{
  /**
   * Returns the value of the '<em><b>Data Rate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Data Rate</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Data Rate</em>' attribute.
   * @see #setDataRate(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletAccelerometerConfiguration_DataRate()
   * @model unique="false"
   * @generated
   */
  Short getDataRate();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletAccelerometerConfiguration#getDataRate <em>Data Rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Data Rate</em>' attribute.
   * @see #getDataRate()
   * @generated
   */
  void setDataRate(Short value);

  /**
   * Returns the value of the '<em><b>Full Scale</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Full Scale</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Full Scale</em>' attribute.
   * @see #setFullScale(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletAccelerometerConfiguration_FullScale()
   * @model unique="false"
   * @generated
   */
  Short getFullScale();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletAccelerometerConfiguration#getFullScale <em>Full Scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Full Scale</em>' attribute.
   * @see #getFullScale()
   * @generated
   */
  void setFullScale(Short value);

  /**
   * Returns the value of the '<em><b>Filter Bandwidth</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Filter Bandwidth</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Filter Bandwidth</em>' attribute.
   * @see #setFilterBandwidth(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickletAccelerometerConfiguration_FilterBandwidth()
   * @model unique="false"
   * @generated
   */
  Short getFilterBandwidth();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.BrickletAccelerometerConfiguration#getFilterBandwidth <em>Filter Bandwidth</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Filter Bandwidth</em>' attribute.
   * @see #getFilterBandwidth()
   * @generated
   */
  void setFilterBandwidth(Short value);

} // BrickletAccelerometerConfiguration
