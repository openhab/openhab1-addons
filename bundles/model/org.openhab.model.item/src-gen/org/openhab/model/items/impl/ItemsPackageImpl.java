/**
 * <copyright>
 * </copyright>
 *
 */
package org.openhab.model.items.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.openhab.model.items.ItemModel;
import org.openhab.model.items.ItemsFactory;
import org.openhab.model.items.ItemsPackage;
import org.openhab.model.items.ModelBinding;
import org.openhab.model.items.ModelGroupFunction;
import org.openhab.model.items.ModelGroupItem;
import org.openhab.model.items.ModelItem;
import org.openhab.model.items.ModelNormalItem;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ItemsPackageImpl extends EPackageImpl implements ItemsPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass itemModelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass modelItemEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass modelGroupItemEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass modelNormalItemEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass modelBindingEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum modelGroupFunctionEEnum = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.openhab.model.items.ItemsPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private ItemsPackageImpl()
  {
    super(eNS_URI, ItemsFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link ItemsPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static ItemsPackage init()
  {
    if (isInited) return (ItemsPackage)EPackage.Registry.INSTANCE.getEPackage(ItemsPackage.eNS_URI);

    // Obtain or create and register package
    ItemsPackageImpl theItemsPackage = (ItemsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof ItemsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new ItemsPackageImpl());

    isInited = true;

    // Create package meta-data objects
    theItemsPackage.createPackageContents();

    // Initialize created meta-data
    theItemsPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theItemsPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(ItemsPackage.eNS_URI, theItemsPackage);
    return theItemsPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getItemModel()
  {
    return itemModelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getItemModel_Items()
  {
    return (EReference)itemModelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getModelItem()
  {
    return modelItemEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getModelItem_Name()
  {
    return (EAttribute)modelItemEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getModelItem_Label()
  {
    return (EAttribute)modelItemEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getModelItem_Icon()
  {
    return (EAttribute)modelItemEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModelItem_Groups()
  {
    return (EReference)modelItemEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getModelItem_Bindings()
  {
    return (EReference)modelItemEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getModelItem_Type()
  {
    return (EAttribute)modelItemEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getModelGroupItem()
  {
    return modelGroupItemEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getModelGroupItem_Function()
  {
    return (EAttribute)modelGroupItemEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getModelGroupItem_Args()
  {
    return (EAttribute)modelGroupItemEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getModelNormalItem()
  {
    return modelNormalItemEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getModelBinding()
  {
    return modelBindingEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getModelBinding_Type()
  {
    return (EAttribute)modelBindingEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getModelBinding_Configuration()
  {
    return (EAttribute)modelBindingEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getModelGroupFunction()
  {
    return modelGroupFunctionEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ItemsFactory getItemsFactory()
  {
    return (ItemsFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    itemModelEClass = createEClass(ITEM_MODEL);
    createEReference(itemModelEClass, ITEM_MODEL__ITEMS);

    modelItemEClass = createEClass(MODEL_ITEM);
    createEAttribute(modelItemEClass, MODEL_ITEM__NAME);
    createEAttribute(modelItemEClass, MODEL_ITEM__LABEL);
    createEAttribute(modelItemEClass, MODEL_ITEM__ICON);
    createEReference(modelItemEClass, MODEL_ITEM__GROUPS);
    createEReference(modelItemEClass, MODEL_ITEM__BINDINGS);
    createEAttribute(modelItemEClass, MODEL_ITEM__TYPE);

    modelGroupItemEClass = createEClass(MODEL_GROUP_ITEM);
    createEAttribute(modelGroupItemEClass, MODEL_GROUP_ITEM__FUNCTION);
    createEAttribute(modelGroupItemEClass, MODEL_GROUP_ITEM__ARGS);

    modelNormalItemEClass = createEClass(MODEL_NORMAL_ITEM);

    modelBindingEClass = createEClass(MODEL_BINDING);
    createEAttribute(modelBindingEClass, MODEL_BINDING__TYPE);
    createEAttribute(modelBindingEClass, MODEL_BINDING__CONFIGURATION);

    // Create enums
    modelGroupFunctionEEnum = createEEnum(MODEL_GROUP_FUNCTION);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    modelGroupItemEClass.getESuperTypes().add(this.getModelItem());
    modelNormalItemEClass.getESuperTypes().add(this.getModelItem());

    // Initialize classes and features; add operations and parameters
    initEClass(itemModelEClass, ItemModel.class, "ItemModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getItemModel_Items(), this.getModelItem(), null, "items", null, 0, -1, ItemModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(modelItemEClass, ModelItem.class, "ModelItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getModelItem_Name(), ecorePackage.getEString(), "name", null, 0, 1, ModelItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getModelItem_Label(), ecorePackage.getEString(), "label", null, 0, 1, ModelItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getModelItem_Icon(), ecorePackage.getEString(), "icon", null, 0, 1, ModelItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getModelItem_Groups(), this.getModelGroupItem(), null, "groups", null, 0, -1, ModelItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getModelItem_Bindings(), this.getModelBinding(), null, "bindings", null, 0, -1, ModelItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getModelItem_Type(), ecorePackage.getEString(), "type", null, 0, 1, ModelItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(modelGroupItemEClass, ModelGroupItem.class, "ModelGroupItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getModelGroupItem_Function(), this.getModelGroupFunction(), "function", null, 0, 1, ModelGroupItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getModelGroupItem_Args(), ecorePackage.getEString(), "args", null, 0, -1, ModelGroupItem.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(modelNormalItemEClass, ModelNormalItem.class, "ModelNormalItem", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(modelBindingEClass, ModelBinding.class, "ModelBinding", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getModelBinding_Type(), ecorePackage.getEString(), "type", null, 0, 1, ModelBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getModelBinding_Configuration(), ecorePackage.getEString(), "configuration", null, 0, 1, ModelBinding.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(modelGroupFunctionEEnum, ModelGroupFunction.class, "ModelGroupFunction");
    addEEnumLiteral(modelGroupFunctionEEnum, ModelGroupFunction.AND);
    addEEnumLiteral(modelGroupFunctionEEnum, ModelGroupFunction.OR);
    addEEnumLiteral(modelGroupFunctionEEnum, ModelGroupFunction.AVG);
    addEEnumLiteral(modelGroupFunctionEEnum, ModelGroupFunction.MAX);
    addEEnumLiteral(modelGroupFunctionEEnum, ModelGroupFunction.MIN);

    // Create resource
    createResource(eNS_URI);
  }

} //ItemsPackageImpl
