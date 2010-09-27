/**
 * <copyright>
 * </copyright>
 *
 */
package org.openhab.model.items.impl;

import org.eclipse.emf.ecore.EClass;
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
      case ItemsPackage.ITEM: return createItem();
      case ItemsPackage.GROUP_ITEM: return createGroupItem();
      case ItemsPackage.NORMAL_ITEM: return createNormalItem();
      case ItemsPackage.BINDING: return createBinding();
      case ItemsPackage.GROUP: return createGroup();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
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
  public Item createItem()
  {
    ItemImpl item = new ItemImpl();
    return item;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public GroupItem createGroupItem()
  {
    GroupItemImpl groupItem = new GroupItemImpl();
    return groupItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NormalItem createNormalItem()
  {
    NormalItemImpl normalItem = new NormalItemImpl();
    return normalItem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Binding createBinding()
  {
    BindingImpl binding = new BindingImpl();
    return binding;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Group createGroup()
  {
    GroupImpl group = new GroupImpl();
    return group;
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
