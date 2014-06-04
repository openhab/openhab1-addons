/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

import org.openhab.core.library.types.HSBType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Color Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getColorActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface ColorActor extends EObject
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model colorDataType="org.openhab.binding.tinkerforge.internal.model.HSBType" colorUnique="false"
   * @generated
   */
  void setColor(HSBType color);

} // ColorActor
