/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchBConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remote Switch BConfiguration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBConfigurationImpl#getAddress <em>Address</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBConfigurationImpl#getUnit <em>Unit</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchBConfigurationImpl#getRepeats <em>Repeats</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RemoteSwitchBConfigurationImpl extends MinimalEObjectImpl.Container implements RemoteSwitchBConfiguration
{
  /**
   * The default value of the '{@link #getAddress() <em>Address</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAddress()
   * @generated
   * @ordered
   */
  protected static final Long ADDRESS_EDEFAULT = null;
  /**
   * The cached value of the '{@link #getAddress() <em>Address</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAddress()
   * @generated
   * @ordered
   */
  protected Long address = ADDRESS_EDEFAULT;
  /**
   * The default value of the '{@link #getUnit() <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnit()
   * @generated
   * @ordered
   */
  protected static final Short UNIT_EDEFAULT = null;
  /**
   * The cached value of the '{@link #getUnit() <em>Unit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUnit()
   * @generated
   * @ordered
   */
  protected Short unit = UNIT_EDEFAULT;

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
  protected RemoteSwitchBConfigurationImpl()
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
    return ModelPackage.Literals.REMOTE_SWITCH_BCONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Long getAddress()
  {
    return address;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAddress(Long newAddress)
  {
    Long oldAddress = address;
    address = newAddress;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_BCONFIGURATION__ADDRESS, oldAddress, address));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getUnit()
  {
    return unit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUnit(Short newUnit)
  {
    Short oldUnit = unit;
    unit = newUnit;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_BCONFIGURATION__UNIT, oldUnit, unit));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_BCONFIGURATION__REPEATS, oldRepeats, repeats));
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
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__ADDRESS:
        return getAddress();
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__UNIT:
        return getUnit();
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__REPEATS:
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
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__ADDRESS:
        setAddress((Long)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__UNIT:
        setUnit((Short)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__REPEATS:
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
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__ADDRESS:
        setAddress(ADDRESS_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__UNIT:
        setUnit(UNIT_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__REPEATS:
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
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__ADDRESS:
        return ADDRESS_EDEFAULT == null ? address != null : !ADDRESS_EDEFAULT.equals(address);
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__UNIT:
        return UNIT_EDEFAULT == null ? unit != null : !UNIT_EDEFAULT.equals(unit);
      case ModelPackage.REMOTE_SWITCH_BCONFIGURATION__REPEATS:
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
    result.append(" (address: ");
    result.append(address);
    result.append(", unit: ");
    result.append(unit);
    result.append(", repeats: ");
    result.append(repeats);
    result.append(')');
    return result.toString();
  }

} //RemoteSwitchBConfigurationImpl
