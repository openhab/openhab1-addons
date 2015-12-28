/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;

import org.openhab.binding.tinkerforge.internal.types.PercentValue;

import org.openhab.core.library.types.PercentType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Percent Type Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.PercentTypeActor#getPercentValue <em>Percent Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getPercentTypeActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface PercentTypeActor extends EObject
{
  /**
   * Returns the value of the '<em><b>Percent Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Percent Value</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Percent Value</em>' attribute.
   * @see #setPercentValue(PercentValue)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getPercentTypeActor_PercentValue()
   * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.PercentValue"
   * @generated
   */
  PercentValue getPercentValue();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.PercentTypeActor#getPercentValue <em>Percent Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Percent Value</em>' attribute.
   * @see #getPercentValue()
   * @generated
   */
  void setPercentValue(PercentValue value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model newValueDataType="org.openhab.binding.tinkerforge.internal.model.PercentType" newValueUnique="false" optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void setValue(PercentType newValue, DeviceOptions opts);

} // PercentTypeActor
