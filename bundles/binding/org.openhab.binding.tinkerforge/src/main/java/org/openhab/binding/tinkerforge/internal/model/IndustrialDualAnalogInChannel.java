/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.math.BigDecimal;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Industrial Dual Analog In Channel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.IndustrialDualAnalogInChannel#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.IndustrialDualAnalogInChannel#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.IndustrialDualAnalogInChannel#getChannelNum <em>Channel Num</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getIndustrialDualAnalogInChannel()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.MDecimalValue> org.openhab.binding.tinkerforge.internal.model.MSubDevice<org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDualAnalogIn> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration> org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface IndustrialDualAnalogInChannel extends MSensor<DecimalValue>, MSubDevice<MBrickletIndustrialDualAnalogIn>, MTFConfigConsumer<TFBaseConfiguration>, CallbackListener
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"industrial_dual_analogin_channel"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getIndustrialDualAnalogInChannel_DeviceType()
   * @model default="industrial_dual_analogin_channel" unique="false" changeable="false"
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
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getIndustrialDualAnalogInChannel_Threshold()
   * @model default="0" unique="false"
   * @generated
   */
  BigDecimal getThreshold();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.IndustrialDualAnalogInChannel#getThreshold <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Threshold</em>' attribute.
   * @see #getThreshold()
   * @generated
   */
  void setThreshold(BigDecimal value);

  /**
   * Returns the value of the '<em><b>Channel Num</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Channel Num</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Channel Num</em>' attribute.
   * @see #setChannelNum(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getIndustrialDualAnalogInChannel_ChannelNum()
   * @model unique="false"
   * @generated
   */
  Short getChannelNum();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.IndustrialDualAnalogInChannel#getChannelNum <em>Channel Num</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Channel Num</em>' attribute.
   * @see #getChannelNum()
   * @generated
   */
  void setChannelNum(Short value);

} // IndustrialDualAnalogInChannel
