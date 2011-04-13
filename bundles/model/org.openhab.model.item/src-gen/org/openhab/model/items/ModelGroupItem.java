/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.items;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Group Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.model.items.ModelGroupItem#getFunction <em>Function</em>}</li>
 *   <li>{@link org.openhab.model.items.ModelGroupItem#getActiveState <em>Active State</em>}</li>
 *   <li>{@link org.openhab.model.items.ModelGroupItem#getPassiveState <em>Passive State</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.model.items.ItemsPackage#getModelGroupItem()
 * @model
 * @generated
 */
public interface ModelGroupItem extends ModelItem
{
  /**
   * Returns the value of the '<em><b>Function</b></em>' attribute.
   * The literals are from the enumeration {@link org.openhab.model.items.ModelGroupFunction}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Function</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Function</em>' attribute.
   * @see org.openhab.model.items.ModelGroupFunction
   * @see #setFunction(ModelGroupFunction)
   * @see org.openhab.model.items.ItemsPackage#getModelGroupItem_Function()
   * @model
   * @generated
   */
  ModelGroupFunction getFunction();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelGroupItem#getFunction <em>Function</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Function</em>' attribute.
   * @see org.openhab.model.items.ModelGroupFunction
   * @see #getFunction()
   * @generated
   */
  void setFunction(ModelGroupFunction value);

  /**
   * Returns the value of the '<em><b>Active State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Active State</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Active State</em>' attribute.
   * @see #setActiveState(String)
   * @see org.openhab.model.items.ItemsPackage#getModelGroupItem_ActiveState()
   * @model
   * @generated
   */
  String getActiveState();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelGroupItem#getActiveState <em>Active State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Active State</em>' attribute.
   * @see #getActiveState()
   * @generated
   */
  void setActiveState(String value);

  /**
   * Returns the value of the '<em><b>Passive State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Passive State</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Passive State</em>' attribute.
   * @see #setPassiveState(String)
   * @see org.openhab.model.items.ItemsPackage#getModelGroupItem_PassiveState()
   * @model
   * @generated
   */
  String getPassiveState();

  /**
   * Sets the value of the '{@link org.openhab.model.items.ModelGroupItem#getPassiveState <em>Passive State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Passive State</em>' attribute.
   * @see #getPassiveState()
   * @generated
   */
  void setPassiveState(String value);

} // ModelGroupItem
