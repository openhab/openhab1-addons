/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dimmable Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDimmableActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface DimmableActor extends EObject
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void increase();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void decrease();

} // DimmableActor
