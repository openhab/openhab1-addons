/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletAccelerometer;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Accelerometer</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAccelerometer#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAccelerometer#getDataRate <em>Data Rate</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAccelerometer#getFullScale <em>Full Scale</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAccelerometer#getFilterBandwidth <em>Filter Bandwidth</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAccelerometer()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletAccelerometer> org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.AccelerometerDevice> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.BrickletAccelerometerConfiguration>"
 * @generated
 */
public interface MBrickletAccelerometer extends MDevice<BrickletAccelerometer>, MSubDeviceHolder<AccelerometerDevice>, MTFConfigConsumer<BrickletAccelerometerConfiguration>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_accelerometer"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAccelerometer_DeviceType()
   * @model default="bricklet_accelerometer" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Data Rate</b></em>' attribute.
   * The default value is <code>"6"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Data Rate</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Data Rate</em>' attribute.
   * @see #setDataRate(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAccelerometer_DataRate()
   * @model default="6" unique="false"
   * @generated
   */
  Short getDataRate();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAccelerometer#getDataRate <em>Data Rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Data Rate</em>' attribute.
   * @see #getDataRate()
   * @generated
   */
  void setDataRate(Short value);

  /**
   * Returns the value of the '<em><b>Full Scale</b></em>' attribute.
   * The default value is <code>"1"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Full Scale</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Full Scale</em>' attribute.
   * @see #setFullScale(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAccelerometer_FullScale()
   * @model default="1" unique="false"
   * @generated
   */
  Short getFullScale();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAccelerometer#getFullScale <em>Full Scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Full Scale</em>' attribute.
   * @see #getFullScale()
   * @generated
   */
  void setFullScale(Short value);

  /**
   * Returns the value of the '<em><b>Filter Bandwidth</b></em>' attribute.
   * The default value is <code>"2"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Filter Bandwidth</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Filter Bandwidth</em>' attribute.
   * @see #setFilterBandwidth(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAccelerometer_FilterBandwidth()
   * @model default="2" unique="false"
   * @generated
   */
  Short getFilterBandwidth();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAccelerometer#getFilterBandwidth <em>Filter Bandwidth</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Filter Bandwidth</em>' attribute.
   * @see #getFilterBandwidth()
   * @generated
   */
  void setFilterBandwidth(Short value);

} // MBrickletAccelerometer
