/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LED Strip Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDStripConfigurationImpl#getChiptype <em>Chiptype</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDStripConfigurationImpl#getFrameduration <em>Frameduration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDStripConfigurationImpl#getClockfrequency <em>Clockfrequency</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LEDStripConfigurationImpl extends MinimalEObjectImpl.Container implements LEDStripConfiguration
{
  /**
   * The default value of the '{@link #getChiptype() <em>Chiptype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChiptype()
   * @generated
   * @ordered
   */
  protected static final String CHIPTYPE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getChiptype() <em>Chiptype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChiptype()
   * @generated
   * @ordered
   */
  protected String chiptype = CHIPTYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getFrameduration() <em>Frameduration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFrameduration()
   * @generated
   * @ordered
   */
  protected static final Integer FRAMEDURATION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFrameduration() <em>Frameduration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFrameduration()
   * @generated
   * @ordered
   */
  protected Integer frameduration = FRAMEDURATION_EDEFAULT;

  /**
   * The default value of the '{@link #getClockfrequency() <em>Clockfrequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClockfrequency()
   * @generated
   * @ordered
   */
  protected static final Long CLOCKFREQUENCY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getClockfrequency() <em>Clockfrequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getClockfrequency()
   * @generated
   * @ordered
   */
  protected Long clockfrequency = CLOCKFREQUENCY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LEDStripConfigurationImpl()
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
    return ModelPackage.Literals.LED_STRIP_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getChiptype()
  {
    return chiptype;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setChiptype(String newChiptype)
  {
    String oldChiptype = chiptype;
    chiptype = newChiptype;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_STRIP_CONFIGURATION__CHIPTYPE, oldChiptype, chiptype));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Integer getFrameduration()
  {
    return frameduration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFrameduration(Integer newFrameduration)
  {
    Integer oldFrameduration = frameduration;
    frameduration = newFrameduration;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_STRIP_CONFIGURATION__FRAMEDURATION, oldFrameduration, frameduration));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Long getClockfrequency()
  {
    return clockfrequency;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setClockfrequency(Long newClockfrequency)
  {
    Long oldClockfrequency = clockfrequency;
    clockfrequency = newClockfrequency;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_STRIP_CONFIGURATION__CLOCKFREQUENCY, oldClockfrequency, clockfrequency));
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
      case ModelPackage.LED_STRIP_CONFIGURATION__CHIPTYPE:
        return getChiptype();
      case ModelPackage.LED_STRIP_CONFIGURATION__FRAMEDURATION:
        return getFrameduration();
      case ModelPackage.LED_STRIP_CONFIGURATION__CLOCKFREQUENCY:
        return getClockfrequency();
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
      case ModelPackage.LED_STRIP_CONFIGURATION__CHIPTYPE:
        setChiptype((String)newValue);
        return;
      case ModelPackage.LED_STRIP_CONFIGURATION__FRAMEDURATION:
        setFrameduration((Integer)newValue);
        return;
      case ModelPackage.LED_STRIP_CONFIGURATION__CLOCKFREQUENCY:
        setClockfrequency((Long)newValue);
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
      case ModelPackage.LED_STRIP_CONFIGURATION__CHIPTYPE:
        setChiptype(CHIPTYPE_EDEFAULT);
        return;
      case ModelPackage.LED_STRIP_CONFIGURATION__FRAMEDURATION:
        setFrameduration(FRAMEDURATION_EDEFAULT);
        return;
      case ModelPackage.LED_STRIP_CONFIGURATION__CLOCKFREQUENCY:
        setClockfrequency(CLOCKFREQUENCY_EDEFAULT);
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
      case ModelPackage.LED_STRIP_CONFIGURATION__CHIPTYPE:
        return CHIPTYPE_EDEFAULT == null ? chiptype != null : !CHIPTYPE_EDEFAULT.equals(chiptype);
      case ModelPackage.LED_STRIP_CONFIGURATION__FRAMEDURATION:
        return FRAMEDURATION_EDEFAULT == null ? frameduration != null : !FRAMEDURATION_EDEFAULT.equals(frameduration);
      case ModelPackage.LED_STRIP_CONFIGURATION__CLOCKFREQUENCY:
        return CLOCKFREQUENCY_EDEFAULT == null ? clockfrequency != null : !CLOCKFREQUENCY_EDEFAULT.equals(clockfrequency);
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
    result.append(" (chiptype: ");
    result.append(chiptype);
    result.append(", frameduration: ");
    result.append(frameduration);
    result.append(", clockfrequency: ");
    result.append(clockfrequency);
    result.append(')');
    return result.toString();
  }

} //LEDStripConfigurationImpl
