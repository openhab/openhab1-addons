/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.config;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.openhab.model.config.ConfigFactory
 * @model kind="package"
 * @generated
 */
public interface ConfigPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "config";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.openhab.org/model/Config";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "config";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  ConfigPackage eINSTANCE = org.openhab.model.config.impl.ConfigPackageImpl.init();

  /**
   * The meta object id for the '{@link org.openhab.model.config.impl.ModelImpl <em>Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.config.impl.ModelImpl
   * @see org.openhab.model.config.impl.ConfigPackageImpl#getModel()
   * @generated
   */
  int MODEL = 0;

  /**
   * The feature id for the '<em><b>Items</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL__ITEMS = 0;

  /**
   * The number of structural features of the '<em>Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int MODEL_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link org.openhab.model.config.impl.ItemImpl <em>Item</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.config.impl.ItemImpl
   * @see org.openhab.model.config.impl.ConfigPackageImpl#getItem()
   * @generated
   */
  int ITEM = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ITEM__NAME = 0;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ITEM__LABEL = 1;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ITEM__ICON = 2;

  /**
   * The feature id for the '<em><b>Groups</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ITEM__GROUPS = 3;

  /**
   * The number of structural features of the '<em>Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int ITEM_FEATURE_COUNT = 4;

  /**
   * The meta object id for the '{@link org.openhab.model.config.impl.GroupItemImpl <em>Group Item</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.config.impl.GroupItemImpl
   * @see org.openhab.model.config.impl.ConfigPackageImpl#getGroupItem()
   * @generated
   */
  int GROUP_ITEM = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_ITEM__NAME = ITEM__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_ITEM__LABEL = ITEM__LABEL;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_ITEM__ICON = ITEM__ICON;

  /**
   * The feature id for the '<em><b>Groups</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_ITEM__GROUPS = ITEM__GROUPS;

  /**
   * The number of structural features of the '<em>Group Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int GROUP_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 0;

  /**
   * The meta object id for the '{@link org.openhab.model.config.impl.NormalItemImpl <em>Normal Item</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.config.impl.NormalItemImpl
   * @see org.openhab.model.config.impl.ConfigPackageImpl#getNormalItem()
   * @generated
   */
  int NORMAL_ITEM = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NORMAL_ITEM__NAME = ITEM__NAME;

  /**
   * The feature id for the '<em><b>Label</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NORMAL_ITEM__LABEL = ITEM__LABEL;

  /**
   * The feature id for the '<em><b>Icon</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NORMAL_ITEM__ICON = ITEM__ICON;

  /**
   * The feature id for the '<em><b>Groups</b></em>' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NORMAL_ITEM__GROUPS = ITEM__GROUPS;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NORMAL_ITEM__TYPE = ITEM_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Bindings</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NORMAL_ITEM__BINDINGS = ITEM_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>Normal Item</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int NORMAL_ITEM_FEATURE_COUNT = ITEM_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link org.openhab.model.config.impl.BindingImpl <em>Binding</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.openhab.model.config.impl.BindingImpl
   * @see org.openhab.model.config.impl.ConfigPackageImpl#getBinding()
   * @generated
   */
  int BINDING = 4;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__TYPE = 0;

  /**
   * The feature id for the '<em><b>Configuration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING__CONFIGURATION = 1;

  /**
   * The number of structural features of the '<em>Binding</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int BINDING_FEATURE_COUNT = 2;


  /**
   * Returns the meta object for class '{@link org.openhab.model.config.Model <em>Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Model</em>'.
   * @see org.openhab.model.config.Model
   * @generated
   */
  EClass getModel();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.model.config.Model#getItems <em>Items</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Items</em>'.
   * @see org.openhab.model.config.Model#getItems()
   * @see #getModel()
   * @generated
   */
  EReference getModel_Items();

  /**
   * Returns the meta object for class '{@link org.openhab.model.config.Item <em>Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Item</em>'.
   * @see org.openhab.model.config.Item
   * @generated
   */
  EClass getItem();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.config.Item#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see org.openhab.model.config.Item#getName()
   * @see #getItem()
   * @generated
   */
  EAttribute getItem_Name();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.config.Item#getLabel <em>Label</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Label</em>'.
   * @see org.openhab.model.config.Item#getLabel()
   * @see #getItem()
   * @generated
   */
  EAttribute getItem_Label();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.config.Item#getIcon <em>Icon</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Icon</em>'.
   * @see org.openhab.model.config.Item#getIcon()
   * @see #getItem()
   * @generated
   */
  EAttribute getItem_Icon();

  /**
   * Returns the meta object for the reference list '{@link org.openhab.model.config.Item#getGroups <em>Groups</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference list '<em>Groups</em>'.
   * @see org.openhab.model.config.Item#getGroups()
   * @see #getItem()
   * @generated
   */
  EReference getItem_Groups();

  /**
   * Returns the meta object for class '{@link org.openhab.model.config.GroupItem <em>Group Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Group Item</em>'.
   * @see org.openhab.model.config.GroupItem
   * @generated
   */
  EClass getGroupItem();

  /**
   * Returns the meta object for class '{@link org.openhab.model.config.NormalItem <em>Normal Item</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Normal Item</em>'.
   * @see org.openhab.model.config.NormalItem
   * @generated
   */
  EClass getNormalItem();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.config.NormalItem#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.openhab.model.config.NormalItem#getType()
   * @see #getNormalItem()
   * @generated
   */
  EAttribute getNormalItem_Type();

  /**
   * Returns the meta object for the containment reference list '{@link org.openhab.model.config.NormalItem#getBindings <em>Bindings</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Bindings</em>'.
   * @see org.openhab.model.config.NormalItem#getBindings()
   * @see #getNormalItem()
   * @generated
   */
  EReference getNormalItem_Bindings();

  /**
   * Returns the meta object for class '{@link org.openhab.model.config.Binding <em>Binding</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>Binding</em>'.
   * @see org.openhab.model.config.Binding
   * @generated
   */
  EClass getBinding();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.config.Binding#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see org.openhab.model.config.Binding#getType()
   * @see #getBinding()
   * @generated
   */
  EAttribute getBinding_Type();

  /**
   * Returns the meta object for the attribute '{@link org.openhab.model.config.Binding#getConfiguration <em>Configuration</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Configuration</em>'.
   * @see org.openhab.model.config.Binding#getConfiguration()
   * @see #getBinding()
   * @generated
   */
  EAttribute getBinding_Configuration();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  ConfigFactory getConfigFactory();

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
     * The meta object literal for the '{@link org.openhab.model.config.impl.ModelImpl <em>Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.config.impl.ModelImpl
     * @see org.openhab.model.config.impl.ConfigPackageImpl#getModel()
     * @generated
     */
    EClass MODEL = eINSTANCE.getModel();

    /**
     * The meta object literal for the '<em><b>Items</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference MODEL__ITEMS = eINSTANCE.getModel_Items();

    /**
     * The meta object literal for the '{@link org.openhab.model.config.impl.ItemImpl <em>Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.config.impl.ItemImpl
     * @see org.openhab.model.config.impl.ConfigPackageImpl#getItem()
     * @generated
     */
    EClass ITEM = eINSTANCE.getItem();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ITEM__NAME = eINSTANCE.getItem_Name();

    /**
     * The meta object literal for the '<em><b>Label</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ITEM__LABEL = eINSTANCE.getItem_Label();

    /**
     * The meta object literal for the '<em><b>Icon</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute ITEM__ICON = eINSTANCE.getItem_Icon();

    /**
     * The meta object literal for the '<em><b>Groups</b></em>' reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference ITEM__GROUPS = eINSTANCE.getItem_Groups();

    /**
     * The meta object literal for the '{@link org.openhab.model.config.impl.GroupItemImpl <em>Group Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.config.impl.GroupItemImpl
     * @see org.openhab.model.config.impl.ConfigPackageImpl#getGroupItem()
     * @generated
     */
    EClass GROUP_ITEM = eINSTANCE.getGroupItem();

    /**
     * The meta object literal for the '{@link org.openhab.model.config.impl.NormalItemImpl <em>Normal Item</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.config.impl.NormalItemImpl
     * @see org.openhab.model.config.impl.ConfigPackageImpl#getNormalItem()
     * @generated
     */
    EClass NORMAL_ITEM = eINSTANCE.getNormalItem();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute NORMAL_ITEM__TYPE = eINSTANCE.getNormalItem_Type();

    /**
     * The meta object literal for the '<em><b>Bindings</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference NORMAL_ITEM__BINDINGS = eINSTANCE.getNormalItem_Bindings();

    /**
     * The meta object literal for the '{@link org.openhab.model.config.impl.BindingImpl <em>Binding</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.openhab.model.config.impl.BindingImpl
     * @see org.openhab.model.config.impl.ConfigPackageImpl#getBinding()
     * @generated
     */
    EClass BINDING = eINSTANCE.getBinding();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINDING__TYPE = eINSTANCE.getBinding_Type();

    /**
     * The meta object literal for the '<em><b>Configuration</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute BINDING__CONFIGURATION = eINSTANCE.getBinding_Configuration();

  }

} //ConfigPackage
