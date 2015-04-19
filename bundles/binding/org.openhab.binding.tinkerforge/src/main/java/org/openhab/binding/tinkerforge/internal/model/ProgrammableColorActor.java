/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;

import org.openhab.core.library.types.HSBType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Programmable Color Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getProgrammableColorActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ProgrammableColorActor extends ColorActor
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model colorDataType="org.openhab.binding.tinkerforge.internal.model.HSBType" colorUnique="false" optsDataType="org.openhab.binding.tinkerforge.internal.model.DeviceOptions" optsUnique="false"
   * @generated
   */
  void setSelectedColor(HSBType color, DeviceOptions opts);

} // ProgrammableColorActor
