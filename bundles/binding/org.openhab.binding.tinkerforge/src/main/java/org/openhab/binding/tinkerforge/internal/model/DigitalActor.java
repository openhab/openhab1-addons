/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

import org.openhab.binding.tinkerforge.internal.types.HighLowValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Digital Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getDigitalState <em>Digital State</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface DigitalActor extends EObject
{
  /**
   * Returns the value of the '<em><b>Digital State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Digital State</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Digital State</em>' attribute.
   * @see #setDigitalState(HighLowValue)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActor_DigitalState()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.DigitalValue"
   * @generated
   */
  HighLowValue getDigitalState();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActor#getDigitalState <em>Digital State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Digital State</em>' attribute.
   * @see #getDigitalState()
   * @generated
   */
  void setDigitalState(HighLowValue value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model digitalStateDataType="org.openhab.binding.tinkerforge.internal.model.DigitalValue" digitalStateUnique="false"
   * @generated
   */
  void turnDigital(HighLowValue digitalState);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dataType="org.openhab.binding.tinkerforge.internal.model.DigitalValue" unique="false"
   * @generated
   */
  HighLowValue fetchDigitalValue();

} // DigitalActor
