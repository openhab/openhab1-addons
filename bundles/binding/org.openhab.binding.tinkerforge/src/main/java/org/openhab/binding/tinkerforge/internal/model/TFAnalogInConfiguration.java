/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TF Analog In Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFAnalogInConfiguration#getMovingAverage <em>Moving Average</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFAnalogInConfiguration#getRange <em>Range</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFAnalogInConfiguration()
 * @model
 * @generated
 */
public interface TFAnalogInConfiguration extends TFBaseConfiguration
{
  /**
   * Returns the value of the '<em><b>Moving Average</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Moving Average</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Moving Average</em>' attribute.
   * @see #setMovingAverage(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFAnalogInConfiguration_MovingAverage()
   * @model unique="false"
   * @generated
   */
  Short getMovingAverage();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFAnalogInConfiguration#getMovingAverage <em>Moving Average</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Moving Average</em>' attribute.
   * @see #getMovingAverage()
   * @generated
   */
  void setMovingAverage(Short value);

  /**
   * Returns the value of the '<em><b>Range</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Range</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Range</em>' attribute.
   * @see #setRange(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFAnalogInConfiguration_Range()
   * @model unique="false"
   * @generated
   */
  Short getRange();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFAnalogInConfiguration#getRange <em>Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Range</em>' attribute.
   * @see #getRange()
   * @generated
   */
  void setRange(Short value);

} // TFAnalogInConfiguration
