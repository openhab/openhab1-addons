/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchCConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remote Switch CConfiguration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchCConfigurationImpl#getSystemCode <em>System Code</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchCConfigurationImpl#getDeviceCode <em>Device Code</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchCConfigurationImpl#getRepeats <em>Repeats</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RemoteSwitchCConfigurationImpl extends MinimalEObjectImpl.Container implements RemoteSwitchCConfiguration
{
  /**
   * The default value of the '{@link #getSystemCode() <em>System Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSystemCode()
   * @generated
   * @ordered
   */
  protected static final String SYSTEM_CODE_EDEFAULT = null;
  /**
   * The cached value of the '{@link #getSystemCode() <em>System Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSystemCode()
   * @generated
   * @ordered
   */
  protected String systemCode = SYSTEM_CODE_EDEFAULT;
  /**
   * The default value of the '{@link #getDeviceCode() <em>Device Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceCode()
   * @generated
   * @ordered
   */
  protected static final Short DEVICE_CODE_EDEFAULT = null;
  /**
   * The cached value of the '{@link #getDeviceCode() <em>Device Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceCode()
   * @generated
   * @ordered
   */
  protected Short deviceCode = DEVICE_CODE_EDEFAULT;

  /**
   * The default value of the '{@link #getRepeats() <em>Repeats</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepeats()
   * @generated
   * @ordered
   */
  protected static final Short REPEATS_EDEFAULT = null;
  /**
   * The cached value of the '{@link #getRepeats() <em>Repeats</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepeats()
   * @generated
   * @ordered
   */
  protected Short repeats = REPEATS_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RemoteSwitchCConfigurationImpl()
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
    return ModelPackage.Literals.REMOTE_SWITCH_CCONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSystemCode()
  {
    return systemCode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSystemCode(String newSystemCode)
  {
    String oldSystemCode = systemCode;
    systemCode = newSystemCode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_CCONFIGURATION__SYSTEM_CODE, oldSystemCode, systemCode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getDeviceCode()
  {
    return deviceCode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDeviceCode(Short newDeviceCode)
  {
    Short oldDeviceCode = deviceCode;
    deviceCode = newDeviceCode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_CCONFIGURATION__DEVICE_CODE, oldDeviceCode, deviceCode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getRepeats()
  {
    return repeats;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRepeats(Short newRepeats)
  {
    Short oldRepeats = repeats;
    repeats = newRepeats;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_CCONFIGURATION__REPEATS, oldRepeats, repeats));
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
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__SYSTEM_CODE:
        return getSystemCode();
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__DEVICE_CODE:
        return getDeviceCode();
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__REPEATS:
        return getRepeats();
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
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__SYSTEM_CODE:
        setSystemCode((String)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__DEVICE_CODE:
        setDeviceCode((Short)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__REPEATS:
        setRepeats((Short)newValue);
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
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__SYSTEM_CODE:
        setSystemCode(SYSTEM_CODE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__DEVICE_CODE:
        setDeviceCode(DEVICE_CODE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__REPEATS:
        setRepeats(REPEATS_EDEFAULT);
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
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__SYSTEM_CODE:
        return SYSTEM_CODE_EDEFAULT == null ? systemCode != null : !SYSTEM_CODE_EDEFAULT.equals(systemCode);
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__DEVICE_CODE:
        return DEVICE_CODE_EDEFAULT == null ? deviceCode != null : !DEVICE_CODE_EDEFAULT.equals(deviceCode);
      case ModelPackage.REMOTE_SWITCH_CCONFIGURATION__REPEATS:
        return REPEATS_EDEFAULT == null ? repeats != null : !REPEATS_EDEFAULT.equals(repeats);
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
    result.append(" (systemCode: ");
    result.append(systemCode);
    result.append(", deviceCode: ");
    result.append(deviceCode);
    result.append(", repeats: ");
    result.append(repeats);
    result.append(')');
    return result.toString();
  }

} //RemoteSwitchCConfigurationImpl
