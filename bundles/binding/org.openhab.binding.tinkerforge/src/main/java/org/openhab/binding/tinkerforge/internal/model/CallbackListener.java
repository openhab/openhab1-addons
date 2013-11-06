/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Callback Listener</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.CallbackListener#getCallbackPeriod <em>Callback Period</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getCallbackListener()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface CallbackListener extends EObject
{
  /**
   * Returns the value of the '<em><b>Callback Period</b></em>' attribute.
   * The default value is <code>"1000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Callback Period</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Callback Period</em>' attribute.
   * @see #setCallbackPeriod(long)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getCallbackListener_CallbackPeriod()
   * @model default="1000" unique="false"
   * @generated
   */
  long getCallbackPeriod();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.CallbackListener#getCallbackPeriod <em>Callback Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Callback Period</em>' attribute.
   * @see #getCallbackPeriod()
   * @generated
   */
  void setCallbackPeriod(long value);

} // CallbackListener
