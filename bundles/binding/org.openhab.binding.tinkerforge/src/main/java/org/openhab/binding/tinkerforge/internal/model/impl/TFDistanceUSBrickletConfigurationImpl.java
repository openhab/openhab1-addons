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

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFDistanceUSBrickletConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TF Distance US Bricklet Configuration</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFDistanceUSBrickletConfigurationImpl#getMovingAverage <em>Moving Average</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TFDistanceUSBrickletConfigurationImpl extends TFBaseConfigurationImpl implements TFDistanceUSBrickletConfiguration
{
  /**
   * The default value of the '{@link #getMovingAverage() <em>Moving Average</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMovingAverage()
   * @generated
   * @ordered
   */
  protected static final Short MOVING_AVERAGE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getMovingAverage() <em>Moving Average</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMovingAverage()
   * @generated
   * @ordered
   */
  protected Short movingAverage = MOVING_AVERAGE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TFDistanceUSBrickletConfigurationImpl()
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
    return ModelPackage.Literals.TF_DISTANCE_US_BRICKLET_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getMovingAverage()
  {
    return movingAverage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMovingAverage(Short newMovingAverage)
  {
    Short oldMovingAverage = movingAverage;
    movingAverage = newMovingAverage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_DISTANCE_US_BRICKLET_CONFIGURATION__MOVING_AVERAGE, oldMovingAverage, movingAverage));
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
      case ModelPackage.TF_DISTANCE_US_BRICKLET_CONFIGURATION__MOVING_AVERAGE:
        return getMovingAverage();
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
      case ModelPackage.TF_DISTANCE_US_BRICKLET_CONFIGURATION__MOVING_AVERAGE:
        setMovingAverage((Short)newValue);
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
      case ModelPackage.TF_DISTANCE_US_BRICKLET_CONFIGURATION__MOVING_AVERAGE:
        setMovingAverage(MOVING_AVERAGE_EDEFAULT);
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
      case ModelPackage.TF_DISTANCE_US_BRICKLET_CONFIGURATION__MOVING_AVERAGE:
        return MOVING_AVERAGE_EDEFAULT == null ? movingAverage != null : !MOVING_AVERAGE_EDEFAULT.equals(movingAverage);
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
    result.append(" (movingAverage: ");
    result.append(movingAverage);
    result.append(')');
    return result.toString();
  }

} //TFDistanceUSBrickletConfigurationImpl
