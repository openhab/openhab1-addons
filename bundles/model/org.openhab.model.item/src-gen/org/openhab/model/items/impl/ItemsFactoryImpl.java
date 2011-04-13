/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.items.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.openhab.model.items.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ItemsFactoryImpl extends EFactoryImpl implements ItemsFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ItemsFactory init()
  {
    try
    {
      ItemsFactory theItemsFactory = (ItemsFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.openhab.org/model/Items"); 
      if (theItemsFactory != null)
      {
        return theItemsFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new ItemsFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ItemsFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case ItemsPackage.ITEM_MODEL: return createItemModel();
      case ItemsPackage.MODEL_ITEM: return createModelItem();
      case ItemsPackage.MODEL_GROUP_ITEM: return createModelGroupItem();
      case ItemsPackage.MODEL_NORMAL_ITEM: return createModelNormalItem();
      case ItemsPackage.MODEL_BINDING: return createModelBinding();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case ItemsPackage.MODEL_GROUP_FUNCTION:
        return createModelGroupFunctionFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case ItemsPackage.MODEL_GROUP_FUNCTION:
        return convertModelGroupFunctionToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ItemModel createItemModel()
  {
    ItemModelImpl itemModel = new ItemModelImpl();
    return itemModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelItem createModelItem()
  {
    ModelItemImpl modelItem = new ModelItemImpl();
    return modelItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelGroupItem createModelGroupItem()
  {
    ModelGroupItemImpl modelGroupItem = new ModelGroupItemImpl();
    return modelGroupItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelNormalItem createModelNormalItem()
  {
    ModelNormalItemImpl modelNormalItem = new ModelNormalItemImpl();
    return modelNormalItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelBinding createModelBinding()
  {
    ModelBindingImpl modelBinding = new ModelBindingImpl();
    return modelBinding;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelGroupFunction createModelGroupFunctionFromString(EDataType eDataType, String initialValue)
  {
    ModelGroupFunction result = ModelGroupFunction.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertModelGroupFunctionToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ItemsPackage getItemsPackage()
  {
    return (ItemsPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static ItemsPackage getPackage()
  {
    return ItemsPackage.eINSTANCE;
  }

} //ItemsFactoryImpl
