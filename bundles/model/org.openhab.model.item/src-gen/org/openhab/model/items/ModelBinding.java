/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.items;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Binding</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.items.ModelBinding#getType <em>Type</em>}</li>
 *   <li>{@link org.openhab.model.items.ModelBinding#getConfiguration <em>Configuration</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.items.ItemsPackage#getModelBinding()
 * @model
 * @generated
 */
public interface ModelBinding extends EObject
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
   * @see org.openhab.model.items.ItemsPackage#getModelBinding_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelBinding#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

  /**
   * Returns the value of the '<em><b>Configuration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Configuration</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Configuration</em>' attribute.
   * @see #setConfiguration(String)
   * @see org.openhab.model.items.ItemsPackage#getModelBinding_Configuration()
   * @model
   * @generated
   */
  String getConfiguration();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelBinding#getConfiguration <em>Configuration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Configuration</em>' attribute.
   * @see #getConfiguration()
   * @generated
   */
  void setConfiguration(String value);

} // ModelBinding
