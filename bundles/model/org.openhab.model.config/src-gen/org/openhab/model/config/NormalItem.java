/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.config;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Normal Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.config.NormalItem#getType <em>Type</em>}</li>
 *   <li>{@link org.openhab.model.config.NormalItem#getBindings <em>Bindings</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.config.ConfigPackage#getNormalItem()
 * @model
 * @generated
 */
public interface NormalItem extends Item
{
  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see #setType(String)
   * @see org.openhab.model.config.ConfigPackage#getNormalItem_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link org.openhab.model.config.NormalItem#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

  /**
   * Returns the value of the '<em><b>Bindings</b></em>' containment reference list.
   * The list contents are of type {@link org.openhab.model.config.Binding}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Bindings</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Bindings</em>' containment reference list.
   * @see org.openhab.model.config.ConfigPackage#getNormalItem_Bindings()
   * @model containment="true"
   * @generated
   */
  EList<Binding> getBindings();

} // NormalItem
