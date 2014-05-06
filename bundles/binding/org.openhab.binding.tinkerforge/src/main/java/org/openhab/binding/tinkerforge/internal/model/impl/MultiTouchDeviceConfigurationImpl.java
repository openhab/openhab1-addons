/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.MultiTouchDeviceConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Multi Touch Device Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MultiTouchDeviceConfigurationImpl#getDisableElectrode <em>Disable Electrode</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultiTouchDeviceConfigurationImpl extends MinimalEObjectImpl.Container implements MultiTouchDeviceConfiguration
{
  /**
   * The default value of the '{@link #getDisableElectrode() <em>Disable Electrode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDisableElectrode()
   * @generated
   * @ordered
   */
  protected static final Boolean DISABLE_ELECTRODE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDisableElectrode() <em>Disable Electrode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDisableElectrode()
   * @generated
   * @ordered
   */
  protected Boolean disableElectrode = DISABLE_ELECTRODE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MultiTouchDeviceConfigurationImpl()
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
    return ModelPackage.Literals.MULTI_TOUCH_DEVICE_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Boolean getDisableElectrode()
  {
    return disableElectrode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDisableElectrode(Boolean newDisableElectrode)
  {
    Boolean oldDisableElectrode = disableElectrode;
    disableElectrode = newDisableElectrode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MULTI_TOUCH_DEVICE_CONFIGURATION__DISABLE_ELECTRODE, oldDisableElectrode, disableElectrode));
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
      case ModelPackage.MULTI_TOUCH_DEVICE_CONFIGURATION__DISABLE_ELECTRODE:
        return getDisableElectrode();
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
      case ModelPackage.MULTI_TOUCH_DEVICE_CONFIGURATION__DISABLE_ELECTRODE:
        setDisableElectrode((Boolean)newValue);
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
      case ModelPackage.MULTI_TOUCH_DEVICE_CONFIGURATION__DISABLE_ELECTRODE:
        setDisableElectrode(DISABLE_ELECTRODE_EDEFAULT);
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
      case ModelPackage.MULTI_TOUCH_DEVICE_CONFIGURATION__DISABLE_ELECTRODE:
        return DISABLE_ELECTRODE_EDEFAULT == null ? disableElectrode != null : !DISABLE_ELECTRODE_EDEFAULT.equals(disableElectrode);
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
    result.append(" (disableElectrode: ");
    result.append(disableElectrode);
    result.append(')');
    return result.toString();
  }

} //MultiTouchDeviceConfigurationImpl
