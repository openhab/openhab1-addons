/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.items;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.openhab.model.items.ItemsFactory
 * @model kind="package"
 * @generated
 */
public interface ItemsPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "items";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.openhab.org/model/Items";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "items";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ItemsPackage eINSTANCE = org.openhab.model.items.impl.ItemsPackageImpl.init();

  /**
   * The meta object id for the '{@link org.openhab.model.items.impl.ItemModelImpl <em>Item Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.items.impl.ItemModelImpl
   * @see org.openhab.model.items.impl.ItemsPackageImpl#getItemModel()
   * @generated
   */
  int ITEM_MODEL = 0;

  /**
   * The feature id for the '<em><b>Items</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ITEM_MODEL__ITEMS = 0;

  /**
   * The number of structural features of the '<em>Item Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ITEM_MODEL_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.openhab.model.items.impl.ModelItemImpl <em>Model Item</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.items.impl.ModelItemImpl
   * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelItem()
   * @generated
   */
  int MODEL_ITEM = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ITEM__NAME = 0;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ITEM__LABEL = 1;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ITEM__ICON = 2;

  /**
   * The feature id for the '<em><b>Groups</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ITEM__GROUPS = 3;

  /**
   * The feature id for the '<em><b>Bindings</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ITEM__BINDINGS = 4;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ITEM__TYPE = 5;

  /**
   * The number of structural features of the '<em>Model Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_ITEM_FEATURE_COUNT = 6;

  /**
   * The meta object id for the '{@link org.openhab.model.items.impl.ModelGroupItemImpl <em>Model Group Item</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.items.impl.ModelGroupItemImpl
   * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelGroupItem()
   * @generated
   */
  int MODEL_GROUP_ITEM = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM__NAME = MODEL_ITEM__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM__LABEL = MODEL_ITEM__LABEL;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM__ICON = MODEL_ITEM__ICON;

  /**
   * The feature id for the '<em><b>Groups</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM__GROUPS = MODEL_ITEM__GROUPS;

  /**
   * The feature id for the '<em><b>Bindings</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM__BINDINGS = MODEL_ITEM__BINDINGS;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM__TYPE = MODEL_ITEM__TYPE;

  /**
   * The feature id for the '<em><b>Function</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM__FUNCTION = MODEL_ITEM_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Active State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM__ACTIVE_STATE = MODEL_ITEM_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Passive State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM__PASSIVE_STATE = MODEL_ITEM_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>Model Group Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_GROUP_ITEM_FEATURE_COUNT = MODEL_ITEM_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link org.openhab.model.items.impl.ModelNormalItemImpl <em>Model Normal Item</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.items.impl.ModelNormalItemImpl
   * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelNormalItem()
   * @generated
   */
  int MODEL_NORMAL_ITEM = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_NORMAL_ITEM__NAME = MODEL_ITEM__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_NORMAL_ITEM__LABEL = MODEL_ITEM__LABEL;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_NORMAL_ITEM__ICON = MODEL_ITEM__ICON;

  /**
   * The feature id for the '<em><b>Groups</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_NORMAL_ITEM__GROUPS = MODEL_ITEM__GROUPS;

  /**
   * The feature id for the '<em><b>Bindings</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_NORMAL_ITEM__BINDINGS = MODEL_ITEM__BINDINGS;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_NORMAL_ITEM__TYPE = MODEL_ITEM__TYPE;

  /**
   * The number of structural features of the '<em>Model Normal Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_NORMAL_ITEM_FEATURE_COUNT = MODEL_ITEM_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.openhab.model.items.impl.ModelBindingImpl <em>Model Binding</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.items.impl.ModelBindingImpl
   * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelBinding()
   * @generated
   */
  int MODEL_BINDING = 4;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_BINDING__TYPE = 0;

  /**
   * The feature id for the '<em><b>Configuration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_BINDING__CONFIGURATION = 1;

  /**
   * The number of structural features of the '<em>Model Binding</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_BINDING_FEATURE_COUNT = 2;

  /**
   * The meta object id for the '{@link org.openhab.model.items.ModelGroupFunction <em>Model Group Function</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.items.ModelGroupFunction
   * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelGroupFunction()
   * @generated
   */
  int MODEL_GROUP_FUNCTION = 5;


  /**
   * Returns the meta object for class '{@link org.openhab.model.items.ItemModel <em>Item Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Item Model</em>'.
   * @see org.openhab.model.items.ItemModel
   * @generated
   */
  EClass getItemModel();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.model.items.ItemModel#getItems <em>Items</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Items</em>'.
   * @see org.openhab.model.items.ItemModel#getItems()
   * @see #getItemModel()
   * @generated
   */
  EReference getItemModel_Items();

  /**
   * Returns the meta object for class '{@link org.openhab.model.items.ModelItem <em>Model Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model Item</em>'.
   * @see org.openhab.model.items.ModelItem
   * @generated
   */
  EClass getModelItem();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.items.ModelItem#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.openhab.model.items.ModelItem#getName()
   * @see #getModelItem()
   * @generated
   */
  EAttribute getModelItem_Name();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.items.ModelItem#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Label</em>'.
   * @see org.openhab.model.items.ModelItem#getLabel()
   * @see #getModelItem()
   * @generated
   */
  EAttribute getModelItem_Label();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.items.ModelItem#getIcon <em>Icon</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Icon</em>'.
   * @see org.openhab.model.items.ModelItem#getIcon()
   * @see #getModelItem()
   * @generated
   */
  EAttribute getModelItem_Icon();

  /**
   * Returns the meta object for the reference list '{@link org.openhab.model.items.ModelItem#getGroups <em>Groups</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Groups</em>'.
   * @see org.openhab.model.items.ModelItem#getGroups()
   * @see #getModelItem()
   * @generated
   */
  EReference getModelItem_Groups();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.model.items.ModelItem#getBindings <em>Bindings</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Bindings</em>'.
   * @see org.openhab.model.items.ModelItem#getBindings()
   * @see #getModelItem()
   * @generated
   */
  EReference getModelItem_Bindings();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.items.ModelItem#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.openhab.model.items.ModelItem#getType()
   * @see #getModelItem()
   * @generated
   */
  EAttribute getModelItem_Type();

  /**
   * Returns the meta object for class '{@link org.openhab.model.items.ModelGroupItem <em>Model Group Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model Group Item</em>'.
   * @see org.openhab.model.items.ModelGroupItem
   * @generated
   */
  EClass getModelGroupItem();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.items.ModelGroupItem#getFunction <em>Function</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Function</em>'.
   * @see org.openhab.model.items.ModelGroupItem#getFunction()
   * @see #getModelGroupItem()
   * @generated
   */
  EAttribute getModelGroupItem_Function();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.items.ModelGroupItem#getActiveState <em>Active State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Active State</em>'.
   * @see org.openhab.model.items.ModelGroupItem#getActiveState()
   * @see #getModelGroupItem()
   * @generated
   */
  EAttribute getModelGroupItem_ActiveState();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.items.ModelGroupItem#getPassiveState <em>Passive State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Passive State</em>'.
   * @see org.openhab.model.items.ModelGroupItem#getPassiveState()
   * @see #getModelGroupItem()
   * @generated
   */
  EAttribute getModelGroupItem_PassiveState();

  /**
   * Returns the meta object for class '{@link org.openhab.model.items.ModelNormalItem <em>Model Normal Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model Normal Item</em>'.
   * @see org.openhab.model.items.ModelNormalItem
   * @generated
   */
  EClass getModelNormalItem();

  /**
   * Returns the meta object for class '{@link org.openhab.model.items.ModelBinding <em>Model Binding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model Binding</em>'.
   * @see org.openhab.model.items.ModelBinding
   * @generated
   */
  EClass getModelBinding();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.items.ModelBinding#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.openhab.model.items.ModelBinding#getType()
   * @see #getModelBinding()
   * @generated
   */
  EAttribute getModelBinding_Type();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.items.ModelBinding#getConfiguration <em>Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Configuration</em>'.
   * @see org.openhab.model.items.ModelBinding#getConfiguration()
   * @see #getModelBinding()
   * @generated
   */
  EAttribute getModelBinding_Configuration();

  /**
   * Returns the meta object for enum '{@link org.openhab.model.items.ModelGroupFunction <em>Model Group Function</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>Model Group Function</em>'.
   * @see org.openhab.model.items.ModelGroupFunction
   * @generated
   */
  EEnum getModelGroupFunction();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ItemsFactory getItemsFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link org.openhab.model.items.impl.ItemModelImpl <em>Item Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.items.impl.ItemModelImpl
     * @see org.openhab.model.items.impl.ItemsPackageImpl#getItemModel()
     * @generated
     */
    EClass ITEM_MODEL = eINSTANCE.getItemModel();

    /**
     * The meta object literal for the '<em><b>Items</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ITEM_MODEL__ITEMS = eINSTANCE.getItemModel_Items();

    /**
     * The meta object literal for the '{@link org.openhab.model.items.impl.ModelItemImpl <em>Model Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.items.impl.ModelItemImpl
     * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelItem()
     * @generated
     */
    EClass MODEL_ITEM = eINSTANCE.getModelItem();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL_ITEM__NAME = eINSTANCE.getModelItem_Name();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL_ITEM__LABEL = eINSTANCE.getModelItem_Label();

    /**
     * The meta object literal for the '<em><b>Icon</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL_ITEM__ICON = eINSTANCE.getModelItem_Icon();

    /**
     * The meta object literal for the '<em><b>Groups</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL_ITEM__GROUPS = eINSTANCE.getModelItem_Groups();

    /**
     * The meta object literal for the '<em><b>Bindings</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL_ITEM__BINDINGS = eINSTANCE.getModelItem_Bindings();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL_ITEM__TYPE = eINSTANCE.getModelItem_Type();

    /**
     * The meta object literal for the '{@link org.openhab.model.items.impl.ModelGroupItemImpl <em>Model Group Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.items.impl.ModelGroupItemImpl
     * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelGroupItem()
     * @generated
     */
    EClass MODEL_GROUP_ITEM = eINSTANCE.getModelGroupItem();

    /**
     * The meta object literal for the '<em><b>Function</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL_GROUP_ITEM__FUNCTION = eINSTANCE.getModelGroupItem_Function();

    /**
     * The meta object literal for the '<em><b>Active State</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL_GROUP_ITEM__ACTIVE_STATE = eINSTANCE.getModelGroupItem_ActiveState();

    /**
     * The meta object literal for the '<em><b>Passive State</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL_GROUP_ITEM__PASSIVE_STATE = eINSTANCE.getModelGroupItem_PassiveState();

    /**
     * The meta object literal for the '{@link org.openhab.model.items.impl.ModelNormalItemImpl <em>Model Normal Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.items.impl.ModelNormalItemImpl
     * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelNormalItem()
     * @generated
     */
    EClass MODEL_NORMAL_ITEM = eINSTANCE.getModelNormalItem();

    /**
     * The meta object literal for the '{@link org.openhab.model.items.impl.ModelBindingImpl <em>Model Binding</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.items.impl.ModelBindingImpl
     * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelBinding()
     * @generated
     */
    EClass MODEL_BINDING = eINSTANCE.getModelBinding();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL_BINDING__TYPE = eINSTANCE.getModelBinding_Type();

    /**
     * The meta object literal for the '<em><b>Configuration</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute MODEL_BINDING__CONFIGURATION = eINSTANCE.getModelBinding_Configuration();

    /**
     * The meta object literal for the '{@link org.openhab.model.items.ModelGroupFunction <em>Model Group Function</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.items.ModelGroupFunction
     * @see org.openhab.model.items.impl.ItemsPackageImpl#getModelGroupFunction()
     * @generated
     */
    EEnum MODEL_GROUP_FUNCTION = eINSTANCE.getModelGroupFunction();

  }

} //ItemsPackage
