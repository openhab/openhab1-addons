/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletMotionDetector;

import org.openhab.binding.tinkerforge.internal.types.HighLowValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Motion Detector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletMotionDetector#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletMotionDetector()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletMotionDetector> org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.DigitalValue>"
 * @generated
 */
public interface MBrickletMotionDetector extends MDevice<BrickletMotionDetector>, MSensor<HighLowValue>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"motion_detector"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletMotionDetector_DeviceType()
   * @model default="motion_detector" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
   * @generated
   */
  void init();

} // MBrickletMotionDetector
