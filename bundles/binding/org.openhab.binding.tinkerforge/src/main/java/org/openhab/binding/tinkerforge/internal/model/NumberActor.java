/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.math.BigDecimal;
import org.eclipse.emf.ecore.EObject;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Number Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getNumberActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface NumberActor extends EObject
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model valueUnique="false"
   * @generated
   */
  void setNumber(BigDecimal value);

} // NumberActor
