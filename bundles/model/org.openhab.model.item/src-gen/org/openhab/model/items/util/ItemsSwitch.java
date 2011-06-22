/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.items.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.openhab.model.items.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.openhab.model.items.ItemsPackage
 * @generated
 */
public class ItemsSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static ItemsPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ItemsSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = ItemsPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case ItemsPackage.ITEM_MODEL:
      {
        ItemModel itemModel = (ItemModel)theEObject;
        T result = caseItemModel(itemModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ItemsPackage.MODEL_ITEM:
      {
        ModelItem modelItem = (ModelItem)theEObject;
        T result = caseModelItem(modelItem);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ItemsPackage.MODEL_GROUP_ITEM:
      {
        ModelGroupItem modelGroupItem = (ModelGroupItem)theEObject;
        T result = caseModelGroupItem(modelGroupItem);
        if (result == null) result = caseModelItem(modelGroupItem);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ItemsPackage.MODEL_NORMAL_ITEM:
      {
        ModelNormalItem modelNormalItem = (ModelNormalItem)theEObject;
        T result = caseModelNormalItem(modelNormalItem);
        if (result == null) result = caseModelItem(modelNormalItem);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case ItemsPackage.MODEL_BINDING:
      {
        ModelBinding modelBinding = (ModelBinding)theEObject;
        T result = caseModelBinding(modelBinding);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Item Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Item Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseItemModel(ItemModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model Item</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Item</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModelItem(ModelItem object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model Group Item</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Group Item</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModelGroupItem(ModelGroupItem object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model Normal Item</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Normal Item</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModelNormalItem(ModelNormalItem object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model Binding</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Binding</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModelBinding(ModelBinding object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //ItemsSwitch
