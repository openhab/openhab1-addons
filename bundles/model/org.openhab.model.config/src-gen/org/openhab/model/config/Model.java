/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.config;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.config.Model#getItems <em>Items</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.config.ConfigPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject
{
  /**
   * Returns the value of the '<em><b>Items</b></em>' containment reference list.
   * The list contents are of type {@link org.openhab.model.config.Item}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Items</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Items</em>' containment reference list.
   * @see org.openhab.model.config.ConfigPackage#getModel_Items()
   * @model containment="true"
   * @generated
   */
  EList<Item> getItems();

} // Model
