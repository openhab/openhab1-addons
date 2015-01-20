/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;

import org.openhab.core.library.types.UpDownType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Move Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MoveActor#getDirection <em>Direction</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMoveActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MoveActor extends EObject
{
  /**
   * Returns the value of the '<em><b>Direction</b></em>' attribute.
   * The literals are from the enumeration {@link org.openhab.binding.tinkerforge.internal.model.Direction}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Direction</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Direction</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.Direction
   * @see #setDirection(Direction)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMoveActor_Direction()
   * @model unique="false"
   * @generated
   */
  Direction getDirection();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MoveActor#getDirection <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Direction</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.Direction
   * @see #getDirection()
   * @generated
   */
  void setDirection(Direction value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model directionDataType="org.openhab.binding.tinkerforge.internal.model.UpDownType" directionUnique="false" optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void move(UpDownType direction, DeviceOptions opts);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void stop();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void moveon(DeviceOptions opts);

} // MoveActor
