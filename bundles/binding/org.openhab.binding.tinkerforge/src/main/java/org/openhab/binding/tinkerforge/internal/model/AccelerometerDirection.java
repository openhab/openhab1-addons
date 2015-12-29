/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Accelerometer Direction</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.AccelerometerDirection#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.AccelerometerDirection#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.AccelerometerDirection#getDirection <em>Direction</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getAccelerometerDirection()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.AccelerometerDevice org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.MDecimalValue> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration> org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface AccelerometerDirection extends AccelerometerDevice, MSensor<DecimalValue>, MTFConfigConsumer<TFBaseConfiguration>, CallbackListener
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"accelerometer_direction"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getAccelerometerDirection_DeviceType()
   * @model default="accelerometer_direction" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Threshold</b></em>' attribute.
   * The default value is <code>"0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Threshold</em>' attribute.
   * @see #setThreshold(BigDecimal)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getAccelerometerDirection_Threshold()
   * @model default="0" unique="false"
   * @generated
   */
  BigDecimal getThreshold();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.AccelerometerDirection#getThreshold <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Threshold</em>' attribute.
   * @see #getThreshold()
   * @generated
   */
  void setThreshold(BigDecimal value);

  /**
   * Returns the value of the '<em><b>Direction</b></em>' attribute.
   * The literals are from the enumeration {@link org.openhab.binding.tinkerforge.internal.model.AccelerometerCoordinate}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Direction</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Direction</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.AccelerometerCoordinate
   * @see #setDirection(AccelerometerCoordinate)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getAccelerometerDirection_Direction()
   * @model unique="false"
   * @generated
   */
  AccelerometerCoordinate getDirection();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.AccelerometerDirection#getDirection <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Direction</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.AccelerometerCoordinate
   * @see #getDirection()
   * @generated
   */
  void setDirection(AccelerometerCoordinate value);

} // AccelerometerDirection
