/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.HighLowValue;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Touch Device</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MultiTouchDevice#getPin <em>Pin</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMultiTouchDevice()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSubDevice<org.openhab.binding.tinkerforge.internal.model.MBrickletMultiTouch> org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.DigitalValue>"
 * @generated
 */
public interface MultiTouchDevice extends MSubDevice<MBrickletMultiTouch>, MSensor<HighLowValue>
{

  /**
   * Returns the value of the '<em><b>Pin</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pin</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pin</em>' attribute.
   * @see #setPin(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMultiTouchDevice_Pin()
   * @model unique="false"
   * @generated
   */
  int getPin();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MultiTouchDevice#getPin <em>Pin</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pin</em>' attribute.
   * @see #getPin()
   * @generated
   */
  void setPin(int value);
} // MultiTouchDevice
