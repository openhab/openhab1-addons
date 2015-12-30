/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletIndustrialDualAnalogIn;

import java.math.BigDecimal;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Industrial Dual Analog In</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDualAnalogIn#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDualAnalogIn#getSampleRate <em>Sample Rate</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletIndustrialDualAnalogIn()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletIndustrialDualAnalogIn> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.BrickletIndustrialDualAnalogInConfiguration> org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.IndustrialDualAnalogInChannel>"
 * @generated
 */
public interface MBrickletIndustrialDualAnalogIn extends MDevice<BrickletIndustrialDualAnalogIn>, MTFConfigConsumer<BrickletIndustrialDualAnalogInConfiguration>, MSubDeviceHolder<IndustrialDualAnalogInChannel>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_industrial_dual_analogin"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletIndustrialDualAnalogIn_DeviceType()
   * @model default="bricklet_industrial_dual_analogin" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Sample Rate</b></em>' attribute.
   * The default value is <code>"6"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sample Rate</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sample Rate</em>' attribute.
   * @see #setSampleRate(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletIndustrialDualAnalogIn_SampleRate()
   * @model default="6" unique="false"
   * @generated
   */
  short getSampleRate();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDualAnalogIn#getSampleRate <em>Sample Rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sample Rate</em>' attribute.
   * @see #getSampleRate()
   * @generated
   */
  void setSampleRate(short value);

} // MBrickletIndustrialDualAnalogIn
