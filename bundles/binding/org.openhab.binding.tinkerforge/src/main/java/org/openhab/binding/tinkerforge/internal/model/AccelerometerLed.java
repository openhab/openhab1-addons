/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Accelerometer Led</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.AccelerometerLed#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getAccelerometerLed()
 * @model
 * @generated
 */
public interface AccelerometerLed extends AccelerometerDevice, DigitalActor
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"accelerometer_led"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getAccelerometerLed_DeviceType()
   * @model default="accelerometer_led" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

} // AccelerometerLed
