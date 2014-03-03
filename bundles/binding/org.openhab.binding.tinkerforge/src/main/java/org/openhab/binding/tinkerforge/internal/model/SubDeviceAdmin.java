/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sub Device Admin</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getSubDeviceAdmin()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface SubDeviceAdmin extends EObject
{

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model subIdUnique="false" subDeviceTypeUnique="false"
   * @generated
   */
  void addSubDevice(String subId, String subDeviceType);
} // SubDeviceAdmin
