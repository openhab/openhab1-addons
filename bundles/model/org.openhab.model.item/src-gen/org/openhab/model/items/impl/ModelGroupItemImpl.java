/**
 * <copyright>
 * </copyright>
 *

 */
package org.openhab.model.items.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.openhab.model.items.ItemsPackage;
import org.openhab.model.items.ModelGroupFunction;
import org.openhab.model.items.ModelGroupItem;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Group Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.model.items.impl.ModelGroupItemImpl#getFunction <em>Function</em>}</li>
 *   <li>{@link org.openhab.model.items.impl.ModelGroupItemImpl#getActiveState <em>Active State</em>}</li>
 *   <li>{@link org.openhab.model.items.impl.ModelGroupItemImpl#getPassiveState <em>Passive State</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelGroupItemImpl extends ModelItemImpl implements ModelGroupItem
{
  /**
   * The default value of the '{@link #getFunction() <em>Function</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFunction()
   * @generated
   * @ordered
   */
  protected static final ModelGroupFunction FUNCTION_EDEFAULT = ModelGroupFunction.AND;

  /**
   * The cached value of the '{@link #getFunction() <em>Function</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFunction()
   * @generated
   * @ordered
   */
  protected ModelGroupFunction function = FUNCTION_EDEFAULT;

  /**
   * The default value of the '{@link #getActiveState() <em>Active State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActiveState()
   * @generated
   * @ordered
   */
  protected static final String ACTIVE_STATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getActiveState() <em>Active State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getActiveState()
   * @generated
   * @ordered
   */
  protected String activeState = ACTIVE_STATE_EDEFAULT;

  /**
   * The default value of the '{@link #getPassiveState() <em>Passive State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPassiveState()
   * @generated
   * @ordered
   */
  protected static final String PASSIVE_STATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPassiveState() <em>Passive State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPassiveState()
   * @generated
   * @ordered
   */
  protected String passiveState = PASSIVE_STATE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ModelGroupItemImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ItemsPackage.Literals.MODEL_GROUP_ITEM;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ModelGroupFunction getFunction()
  {
    return function;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFunction(ModelGroupFunction newFunction)
  {
    ModelGroupFunction oldFunction = function;
    function = newFunction == null ? FUNCTION_EDEFAULT : newFunction;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ItemsPackage.MODEL_GROUP_ITEM__FUNCTION, oldFunction, function));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getActiveState()
  {
    return activeState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setActiveState(String newActiveState)
  {
    String oldActiveState = activeState;
    activeState = newActiveState;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ItemsPackage.MODEL_GROUP_ITEM__ACTIVE_STATE, oldActiveState, activeState));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getPassiveState()
  {
    return passiveState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPassiveState(String newPassiveState)
  {
    String oldPassiveState = passiveState;
    passiveState = newPassiveState;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ItemsPackage.MODEL_GROUP_ITEM__PASSIVE_STATE, oldPassiveState, passiveState));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ItemsPackage.MODEL_GROUP_ITEM__FUNCTION:
        return getFunction();
      case ItemsPackage.MODEL_GROUP_ITEM__ACTIVE_STATE:
        return getActiveState();
      case ItemsPackage.MODEL_GROUP_ITEM__PASSIVE_STATE:
        return getPassiveState();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ItemsPackage.MODEL_GROUP_ITEM__FUNCTION:
        setFunction((ModelGroupFunction)newValue);
        return;
      case ItemsPackage.MODEL_GROUP_ITEM__ACTIVE_STATE:
        setActiveState((String)newValue);
        return;
      case ItemsPackage.MODEL_GROUP_ITEM__PASSIVE_STATE:
        setPassiveState((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ItemsPackage.MODEL_GROUP_ITEM__FUNCTION:
        setFunction(FUNCTION_EDEFAULT);
        return;
      case ItemsPackage.MODEL_GROUP_ITEM__ACTIVE_STATE:
        setActiveState(ACTIVE_STATE_EDEFAULT);
        return;
      case ItemsPackage.MODEL_GROUP_ITEM__PASSIVE_STATE:
        setPassiveState(PASSIVE_STATE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ItemsPackage.MODEL_GROUP_ITEM__FUNCTION:
        return function != FUNCTION_EDEFAULT;
      case ItemsPackage.MODEL_GROUP_ITEM__ACTIVE_STATE:
        return ACTIVE_STATE_EDEFAULT == null ? activeState != null : !ACTIVE_STATE_EDEFAULT.equals(activeState);
      case ItemsPackage.MODEL_GROUP_ITEM__PASSIVE_STATE:
        return PASSIVE_STATE_EDEFAULT == null ? passiveState != null : !PASSIVE_STATE_EDEFAULT.equals(passiveState);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (function: ");
    result.append(function);
    result.append(", activeState: ");
    result.append(activeState);
    result.append(", passiveState: ");
    result.append(passiveState);
    result.append(')');
    return result.toString();
  }

} //ModelGroupItemImpl
