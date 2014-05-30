/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletTemperatureIR;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Temperature IR</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletTemperatureIR#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletTemperatureIR()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletTemperatureIR> org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.MTemperatureIRDevice>"
 * @generated
 */
public interface MBrickletTemperatureIR extends MDevice<BrickletTemperatureIR>, MSubDeviceHolder<MTemperatureIRDevice>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_temperatureIR"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletTemperatureIR_DeviceType()
   * @model default="bricklet_temperatureIR" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

} // MBrickletTemperatureIR
