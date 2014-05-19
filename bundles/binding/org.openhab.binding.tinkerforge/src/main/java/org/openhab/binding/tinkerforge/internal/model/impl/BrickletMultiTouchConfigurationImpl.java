/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

import org.openhab.binding.tinkerforge.internal.model.BrickletMultiTouchConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bricklet Multi Touch Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletMultiTouchConfigurationImpl#getRecalibrate <em>Recalibrate</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletMultiTouchConfigurationImpl#getSensitivity <em>Sensitivity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BrickletMultiTouchConfigurationImpl extends MinimalEObjectImpl.Container implements BrickletMultiTouchConfiguration
{
  /**
   * The default value of the '{@link #getRecalibrate() <em>Recalibrate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRecalibrate()
   * @generated
   * @ordered
   */
  protected static final Boolean RECALIBRATE_EDEFAULT = null;
  /**
   * The cached value of the '{@link #getRecalibrate() <em>Recalibrate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRecalibrate()
   * @generated
   * @ordered
   */
  protected Boolean recalibrate = RECALIBRATE_EDEFAULT;

  /**
   * The default value of the '{@link #getSensitivity() <em>Sensitivity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensitivity()
   * @generated
   * @ordered
   */
  protected static final Short SENSITIVITY_EDEFAULT = null;
  /**
   * The cached value of the '{@link #getSensitivity() <em>Sensitivity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensitivity()
   * @generated
   * @ordered
   */
  protected Short sensitivity = SENSITIVITY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BrickletMultiTouchConfigurationImpl()
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
    return ModelPackage.Literals.BRICKLET_MULTI_TOUCH_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Boolean getRecalibrate()
  {
    return recalibrate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRecalibrate(Boolean newRecalibrate)
  {
    Boolean oldRecalibrate = recalibrate;
    recalibrate = newRecalibrate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__RECALIBRATE, oldRecalibrate, recalibrate));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getSensitivity()
  {
    return sensitivity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSensitivity(Short newSensitivity)
  {
    Short oldSensitivity = sensitivity;
    sensitivity = newSensitivity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__SENSITIVITY, oldSensitivity, sensitivity));
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
      case ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__RECALIBRATE:
        return getRecalibrate();
      case ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__SENSITIVITY:
        return getSensitivity();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__RECALIBRATE:
        setRecalibrate((Boolean)newValue);
        return;
      case ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__SENSITIVITY:
        setSensitivity((Short)newValue);
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
      case ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__RECALIBRATE:
        setRecalibrate(RECALIBRATE_EDEFAULT);
        return;
      case ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__SENSITIVITY:
        setSensitivity(SENSITIVITY_EDEFAULT);
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
      case ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__RECALIBRATE:
        return RECALIBRATE_EDEFAULT == null ? recalibrate != null : !RECALIBRATE_EDEFAULT.equals(recalibrate);
      case ModelPackage.BRICKLET_MULTI_TOUCH_CONFIGURATION__SENSITIVITY:
        return SENSITIVITY_EDEFAULT == null ? sensitivity != null : !SENSITIVITY_EDEFAULT.equals(sensitivity);
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
    result.append(" (recalibrate: ");
    result.append(recalibrate);
    result.append(", sensitivity: ");
    result.append(sensitivity);
    result.append(')');
    return result.toString();
  }

} //BrickletMultiTouchConfigurationImpl
