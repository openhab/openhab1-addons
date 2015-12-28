/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.BrickletColorConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bricklet Color Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletColorConfigurationImpl#getGain <em>Gain</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletColorConfigurationImpl#getIntegrationTime <em>Integration Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BrickletColorConfigurationImpl extends MinimalEObjectImpl.Container implements BrickletColorConfiguration
{
  /**
   * The default value of the '{@link #getGain() <em>Gain</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGain()
   * @generated
   * @ordered
   */
  protected static final Short GAIN_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getGain() <em>Gain</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGain()
   * @generated
   * @ordered
   */
  protected Short gain = GAIN_EDEFAULT;

  /**
   * The default value of the '{@link #getIntegrationTime() <em>Integration Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIntegrationTime()
   * @generated
   * @ordered
   */
  protected static final Short INTEGRATION_TIME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getIntegrationTime() <em>Integration Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIntegrationTime()
   * @generated
   * @ordered
   */
  protected Short integrationTime = INTEGRATION_TIME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BrickletColorConfigurationImpl()
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
    return ModelPackage.Literals.BRICKLET_COLOR_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getGain()
  {
    return gain;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGain(Short newGain)
  {
    Short oldGain = gain;
    gain = newGain;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_COLOR_CONFIGURATION__GAIN, oldGain, gain));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getIntegrationTime()
  {
    return integrationTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIntegrationTime(Short newIntegrationTime)
  {
    Short oldIntegrationTime = integrationTime;
    integrationTime = newIntegrationTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_COLOR_CONFIGURATION__INTEGRATION_TIME, oldIntegrationTime, integrationTime));
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
      case ModelPackage.BRICKLET_COLOR_CONFIGURATION__GAIN:
        return getGain();
      case ModelPackage.BRICKLET_COLOR_CONFIGURATION__INTEGRATION_TIME:
        return getIntegrationTime();
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
      case ModelPackage.BRICKLET_COLOR_CONFIGURATION__GAIN:
        setGain((Short)newValue);
        return;
      case ModelPackage.BRICKLET_COLOR_CONFIGURATION__INTEGRATION_TIME:
        setIntegrationTime((Short)newValue);
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
      case ModelPackage.BRICKLET_COLOR_CONFIGURATION__GAIN:
        setGain(GAIN_EDEFAULT);
        return;
      case ModelPackage.BRICKLET_COLOR_CONFIGURATION__INTEGRATION_TIME:
        setIntegrationTime(INTEGRATION_TIME_EDEFAULT);
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
      case ModelPackage.BRICKLET_COLOR_CONFIGURATION__GAIN:
        return GAIN_EDEFAULT == null ? gain != null : !GAIN_EDEFAULT.equals(gain);
      case ModelPackage.BRICKLET_COLOR_CONFIGURATION__INTEGRATION_TIME:
        return INTEGRATION_TIME_EDEFAULT == null ? integrationTime != null : !INTEGRATION_TIME_EDEFAULT.equals(integrationTime);
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
    result.append(" (gain: ");
    result.append(gain);
    result.append(", integrationTime: ");
    result.append(integrationTime);
    result.append(')');
    return result.toString();
  }

} //BrickletColorConfigurationImpl
