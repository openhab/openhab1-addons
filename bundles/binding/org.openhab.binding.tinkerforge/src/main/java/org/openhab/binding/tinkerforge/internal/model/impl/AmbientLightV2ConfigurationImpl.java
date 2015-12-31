/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.openhab.binding.tinkerforge.internal.model.AmbientLightV2Configuration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ambient Light V2 Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.AmbientLightV2ConfigurationImpl#getIlluminanceRange <em>Illuminance Range</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.AmbientLightV2ConfigurationImpl#getIntegrationTime <em>Integration Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AmbientLightV2ConfigurationImpl extends TFBaseConfigurationImpl implements AmbientLightV2Configuration
{
  /**
   * The default value of the '{@link #getIlluminanceRange() <em>Illuminance Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIlluminanceRange()
   * @generated
   * @ordered
   */
  protected static final short ILLUMINANCE_RANGE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getIlluminanceRange() <em>Illuminance Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIlluminanceRange()
   * @generated
   * @ordered
   */
  protected short illuminanceRange = ILLUMINANCE_RANGE_EDEFAULT;

  /**
   * The default value of the '{@link #getIntegrationTime() <em>Integration Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIntegrationTime()
   * @generated
   * @ordered
   */
  protected static final short INTEGRATION_TIME_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getIntegrationTime() <em>Integration Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIntegrationTime()
   * @generated
   * @ordered
   */
  protected short integrationTime = INTEGRATION_TIME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AmbientLightV2ConfigurationImpl()
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
    return ModelPackage.Literals.AMBIENT_LIGHT_V2_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getIlluminanceRange()
  {
    return illuminanceRange;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIlluminanceRange(short newIlluminanceRange)
  {
    short oldIlluminanceRange = illuminanceRange;
    illuminanceRange = newIlluminanceRange;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__ILLUMINANCE_RANGE, oldIlluminanceRange, illuminanceRange));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getIntegrationTime()
  {
    return integrationTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIntegrationTime(short newIntegrationTime)
  {
    short oldIntegrationTime = integrationTime;
    integrationTime = newIntegrationTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__INTEGRATION_TIME, oldIntegrationTime, integrationTime));
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
      case ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__ILLUMINANCE_RANGE:
        return getIlluminanceRange();
      case ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__INTEGRATION_TIME:
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
      case ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__ILLUMINANCE_RANGE:
        setIlluminanceRange((Short)newValue);
        return;
      case ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__INTEGRATION_TIME:
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
      case ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__ILLUMINANCE_RANGE:
        setIlluminanceRange(ILLUMINANCE_RANGE_EDEFAULT);
        return;
      case ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__INTEGRATION_TIME:
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
      case ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__ILLUMINANCE_RANGE:
        return illuminanceRange != ILLUMINANCE_RANGE_EDEFAULT;
      case ModelPackage.AMBIENT_LIGHT_V2_CONFIGURATION__INTEGRATION_TIME:
        return integrationTime != INTEGRATION_TIME_EDEFAULT;
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
    result.append(" (illuminanceRange: ");
    result.append(illuminanceRange);
    result.append(", integrationTime: ");
    result.append(integrationTime);
    result.append(')');
    return result.toString();
  }

} //AmbientLightV2ConfigurationImpl
