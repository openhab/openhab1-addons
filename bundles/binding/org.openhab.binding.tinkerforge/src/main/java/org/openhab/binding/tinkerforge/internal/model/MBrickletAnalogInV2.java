/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletAnalogInV2;

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Analog In V2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogInV2#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogInV2#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogInV2#getMovingAverage <em>Moving Average</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAnalogInV2()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletAnalogInV2> org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.MDecimalValue> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.TFAnalogInV2Configuration> org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface MBrickletAnalogInV2 extends MDevice<BrickletAnalogInV2>, MSensor<DecimalValue>, MTFConfigConsumer<TFAnalogInV2Configuration>, CallbackListener
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_analoginv2"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAnalogInV2_DeviceType()
   * @model default="bricklet_analoginv2" unique="false" changeable="false"
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
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAnalogInV2_Threshold()
   * @model default="0" unique="false"
   * @generated
   */
  BigDecimal getThreshold();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogInV2#getThreshold <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Threshold</em>' attribute.
   * @see #getThreshold()
   * @generated
   */
  void setThreshold(BigDecimal value);

  /**
   * Returns the value of the '<em><b>Moving Average</b></em>' attribute.
   * The default value is <code>"100"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Moving Average</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Moving Average</em>' attribute.
   * @see #setMovingAverage(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAnalogInV2_MovingAverage()
   * @model default="100" unique="false"
   * @generated
   */
  Short getMovingAverage();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogInV2#getMovingAverage <em>Moving Average</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Moving Average</em>' attribute.
   * @see #getMovingAverage()
   * @generated
   */
  void setMovingAverage(Short value);

} // MBrickletAnalogInV2
