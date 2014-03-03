/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>OHTF Sub Device Admin Device</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getOHTFSubDeviceAdminDevice()
 * @model IDSBounds="org.openhab.binding.tinkerforge.internal.model.Enum"
 * @generated
 */
public interface OHTFSubDeviceAdminDevice<TFC extends TFConfig, IDS extends Enum> extends OHTFDevice<TFC, IDS>
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model unique="false" subIdUnique="false"
   *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return true;'"
   * @generated
   */
  boolean isValidSubId(String subId);

} // OHTFSubDeviceAdminDevice
