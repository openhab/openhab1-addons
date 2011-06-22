/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.items;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.items.ModelItem#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.model.items.ModelItem#getLabel <em>Label</em>}</li>
 *   <li>{@link org.openhab.model.items.ModelItem#getIcon <em>Icon</em>}</li>
 *   <li>{@link org.openhab.model.items.ModelItem#getGroups <em>Groups</em>}</li>
 *   <li>{@link org.openhab.model.items.ModelItem#getBindings <em>Bindings</em>}</li>
 *   <li>{@link org.openhab.model.items.ModelItem#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.items.ItemsPackage#getModelItem()
 * @model
 * @generated
 */
public interface ModelItem extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.openhab.model.items.ItemsPackage#getModelItem_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelItem#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Label</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Label</em>' attribute.
   * @see #setLabel(String)
   * @see org.openhab.model.items.ItemsPackage#getModelItem_Label()
   * @model
   * @generated
   */
  String getLabel();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelItem#getLabel <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Label</em>' attribute.
   * @see #getLabel()
   * @generated
   */
  void setLabel(String value);

  /**
   * Returns the value of the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Icon</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Icon</em>' attribute.
   * @see #setIcon(String)
   * @see org.openhab.model.items.ItemsPackage#getModelItem_Icon()
   * @model
   * @generated
   */
  String getIcon();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelItem#getIcon <em>Icon</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Icon</em>' attribute.
   * @see #getIcon()
   * @generated
   */
  void setIcon(String value);

  /**
   * Returns the value of the '<em><b>Groups</b></em>' reference list.
   * The list contents are of type {@link org.openhab.model.items.ModelGroupItem}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Groups</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Groups</em>' reference list.
   * @see org.openhab.model.items.ItemsPackage#getModelItem_Groups()
   * @model
   * @generated
   */
  EList<ModelGroupItem> getGroups();

  /**
   * Returns the value of the '<em><b>Bindings</b></em>' containment reference list.
   * The list contents are of type {@link org.openhab.model.items.ModelBinding}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Bindings</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Bindings</em>' containment reference list.
   * @see org.openhab.model.items.ItemsPackage#getModelItem_Bindings()
   * @model containment="true"
   * @generated
   */
  EList<ModelBinding> getBindings();

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
   * @see org.openhab.model.items.ItemsPackage#getModelItem_Type()
   * @model
   * @generated
   */
  String getType();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelItem#getType <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Type</em>' attribute.
   * @see #getType()
   * @generated
   */
  void setType(String value);

} // ModelItem
