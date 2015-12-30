/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Programmable Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getProgrammableActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProgrammableActor extends EObject
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void action(DeviceOptions opts);

} // ProgrammableActor
