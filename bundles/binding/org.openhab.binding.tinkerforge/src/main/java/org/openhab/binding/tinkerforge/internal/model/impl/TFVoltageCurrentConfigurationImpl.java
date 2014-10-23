/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFVoltageCurrentConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TF Voltage Current Configuration</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFVoltageCurrentConfigurationImpl#getAveraging <em>Averaging</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFVoltageCurrentConfigurationImpl#getVoltageConversionTime <em>Voltage Conversion Time</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFVoltageCurrentConfigurationImpl#getCurrentConversionTime <em>Current Conversion Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TFVoltageCurrentConfigurationImpl extends MinimalEObjectImpl.Container implements TFVoltageCurrentConfiguration
{
  /**
   * The default value of the '{@link #getAveraging() <em>Averaging</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAveraging()
   * @generated
   * @ordered
   */
  protected static final Short AVERAGING_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getAveraging() <em>Averaging</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAveraging()
   * @generated
   * @ordered
   */
  protected Short averaging = AVERAGING_EDEFAULT;

  /**
   * The default value of the '{@link #getVoltageConversionTime() <em>Voltage Conversion Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVoltageConversionTime()
   * @generated
   * @ordered
   */
  protected static final Short VOLTAGE_CONVERSION_TIME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getVoltageConversionTime() <em>Voltage Conversion Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVoltageConversionTime()
   * @generated
   * @ordered
   */
  protected Short voltageConversionTime = VOLTAGE_CONVERSION_TIME_EDEFAULT;

  /**
   * The default value of the '{@link #getCurrentConversionTime() <em>Current Conversion Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCurrentConversionTime()
   * @generated
   * @ordered
   */
  protected static final Short CURRENT_CONVERSION_TIME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getCurrentConversionTime() <em>Current Conversion Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCurrentConversionTime()
   * @generated
   * @ordered
   */
  protected Short currentConversionTime = CURRENT_CONVERSION_TIME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TFVoltageCurrentConfigurationImpl()
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
    return ModelPackage.Literals.TF_VOLTAGE_CURRENT_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getAveraging()
  {
    return averaging;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAveraging(Short newAveraging)
  {
    Short oldAveraging = averaging;
    averaging = newAveraging;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__AVERAGING, oldAveraging, averaging));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getVoltageConversionTime()
  {
    return voltageConversionTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVoltageConversionTime(Short newVoltageConversionTime)
  {
    Short oldVoltageConversionTime = voltageConversionTime;
    voltageConversionTime = newVoltageConversionTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__VOLTAGE_CONVERSION_TIME, oldVoltageConversionTime, voltageConversionTime));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getCurrentConversionTime()
  {
    return currentConversionTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCurrentConversionTime(Short newCurrentConversionTime)
  {
    Short oldCurrentConversionTime = currentConversionTime;
    currentConversionTime = newCurrentConversionTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__CURRENT_CONVERSION_TIME, oldCurrentConversionTime, currentConversionTime));
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
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__AVERAGING:
        return getAveraging();
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__VOLTAGE_CONVERSION_TIME:
        return getVoltageConversionTime();
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__CURRENT_CONVERSION_TIME:
        return getCurrentConversionTime();
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
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__AVERAGING:
        setAveraging((Short)newValue);
        return;
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__VOLTAGE_CONVERSION_TIME:
        setVoltageConversionTime((Short)newValue);
        return;
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__CURRENT_CONVERSION_TIME:
        setCurrentConversionTime((Short)newValue);
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
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__AVERAGING:
        setAveraging(AVERAGING_EDEFAULT);
        return;
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__VOLTAGE_CONVERSION_TIME:
        setVoltageConversionTime(VOLTAGE_CONVERSION_TIME_EDEFAULT);
        return;
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__CURRENT_CONVERSION_TIME:
        setCurrentConversionTime(CURRENT_CONVERSION_TIME_EDEFAULT);
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
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__AVERAGING:
        return AVERAGING_EDEFAULT == null ? averaging != null : !AVERAGING_EDEFAULT.equals(averaging);
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__VOLTAGE_CONVERSION_TIME:
        return VOLTAGE_CONVERSION_TIME_EDEFAULT == null ? voltageConversionTime != null : !VOLTAGE_CONVERSION_TIME_EDEFAULT.equals(voltageConversionTime);
      case ModelPackage.TF_VOLTAGE_CURRENT_CONFIGURATION__CURRENT_CONVERSION_TIME:
        return CURRENT_CONVERSION_TIME_EDEFAULT == null ? currentConversionTime != null : !CURRENT_CONVERSION_TIME_EDEFAULT.equals(currentConversionTime);
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
    result.append(" (averaging: ");
    result.append(averaging);
    result.append(", voltageConversionTime: ");
    result.append(voltageConversionTime);
    result.append(", currentConversionTime: ");
    result.append(currentConversionTime);
    result.append(')');
    return result.toString();
  }

} //TFVoltageCurrentConfigurationImpl
