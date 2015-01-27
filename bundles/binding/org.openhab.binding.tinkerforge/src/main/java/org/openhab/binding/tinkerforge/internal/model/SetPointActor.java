/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.math.BigDecimal;

import org.openhab.core.library.types.PercentType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Set Point Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getSetPointActor()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface SetPointActor<C extends DimmableConfiguration> extends DimmableActor<C>
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model newValueUnique="false"
   * @generated
   */
  void setValue(BigDecimal newValue);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model newValueDataType="org.openhab.binding.tinkerforge.internal.model.PercentType" newValueUnique="false"
   * @generated
   */
  void setValue(PercentType newValue);

} // SetPointActor
