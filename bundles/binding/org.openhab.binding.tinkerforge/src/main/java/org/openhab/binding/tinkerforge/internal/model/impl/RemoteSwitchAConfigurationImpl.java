/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchAConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remote Switch AConfiguration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAConfigurationImpl#getHouseCode <em>House Code</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAConfigurationImpl#getReceiverCode <em>Receiver Code</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAConfigurationImpl#getRepeats <em>Repeats</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RemoteSwitchAConfigurationImpl extends MinimalEObjectImpl.Container implements RemoteSwitchAConfiguration
{
  /**
   * The default value of the '{@link #getHouseCode() <em>House Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHouseCode()
   * @generated
   * @ordered
   */
  protected static final Short HOUSE_CODE_EDEFAULT = null;
  /**
   * The cached value of the '{@link #getHouseCode() <em>House Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHouseCode()
   * @generated
   * @ordered
   */
  protected Short houseCode = HOUSE_CODE_EDEFAULT;
  /**
   * The default value of the '{@link #getReceiverCode() <em>Receiver Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReceiverCode()
   * @generated
   * @ordered
   */
  protected static final Short RECEIVER_CODE_EDEFAULT = null;
  /**
   * The cached value of the '{@link #getReceiverCode() <em>Receiver Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReceiverCode()
   * @generated
   * @ordered
   */
  protected Short receiverCode = RECEIVER_CODE_EDEFAULT;

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
  protected RemoteSwitchAConfigurationImpl()
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
    return ModelPackage.Literals.REMOTE_SWITCH_ACONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getHouseCode()
  {
    return houseCode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setHouseCode(Short newHouseCode)
  {
    Short oldHouseCode = houseCode;
    houseCode = newHouseCode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_ACONFIGURATION__HOUSE_CODE, oldHouseCode, houseCode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getReceiverCode()
  {
    return receiverCode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setReceiverCode(Short newReceiverCode)
  {
    Short oldReceiverCode = receiverCode;
    receiverCode = newReceiverCode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_ACONFIGURATION__RECEIVER_CODE, oldReceiverCode, receiverCode));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_ACONFIGURATION__REPEATS, oldRepeats, repeats));
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
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__HOUSE_CODE:
        return getHouseCode();
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__RECEIVER_CODE:
        return getReceiverCode();
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__REPEATS:
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
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__HOUSE_CODE:
        setHouseCode((Short)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__RECEIVER_CODE:
        setReceiverCode((Short)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__REPEATS:
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
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__HOUSE_CODE:
        setHouseCode(HOUSE_CODE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__RECEIVER_CODE:
        setReceiverCode(RECEIVER_CODE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__REPEATS:
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
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__HOUSE_CODE:
        return HOUSE_CODE_EDEFAULT == null ? houseCode != null : !HOUSE_CODE_EDEFAULT.equals(houseCode);
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__RECEIVER_CODE:
        return RECEIVER_CODE_EDEFAULT == null ? receiverCode != null : !RECEIVER_CODE_EDEFAULT.equals(receiverCode);
      case ModelPackage.REMOTE_SWITCH_ACONFIGURATION__REPEATS:
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
    result.append(" (houseCode: ");
    result.append(houseCode);
    result.append(", receiverCode: ");
    result.append(receiverCode);
    result.append(", repeats: ");
    result.append(repeats);
    result.append(')');
    return result.toString();
  }

} //RemoteSwitchAConfigurationImpl
