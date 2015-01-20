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
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMoveActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MoveActor extends EObject
{
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

} // MoveActor
