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
import org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TFIO Sensor Configuration</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.4.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFIOSensorConfigurationImpl#isPullUpResistorEnabled <em>Pull Up Resistor Enabled</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TFIOSensorConfigurationImpl extends MinimalEObjectImpl.Container implements TFIOSensorConfiguration
{
  /**
   * The default value of the '{@link #isPullUpResistorEnabled() <em>Pull Up Resistor Enabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPullUpResistorEnabled()
   * @generated
   * @ordered
   */
  protected static final boolean PULL_UP_RESISTOR_ENABLED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isPullUpResistorEnabled() <em>Pull Up Resistor Enabled</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPullUpResistorEnabled()
   * @generated
   * @ordered
   */
  protected boolean pullUpResistorEnabled = PULL_UP_RESISTOR_ENABLED_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TFIOSensorConfigurationImpl()
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
    return ModelPackage.Literals.TFIO_SENSOR_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isPullUpResistorEnabled()
  {
    return pullUpResistorEnabled;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPullUpResistorEnabled(boolean newPullUpResistorEnabled)
  {
    boolean oldPullUpResistorEnabled = pullUpResistorEnabled;
    pullUpResistorEnabled = newPullUpResistorEnabled;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TFIO_SENSOR_CONFIGURATION__PULL_UP_RESISTOR_ENABLED, oldPullUpResistorEnabled, pullUpResistorEnabled));
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
      case ModelPackage.TFIO_SENSOR_CONFIGURATION__PULL_UP_RESISTOR_ENABLED:
        return isPullUpResistorEnabled();
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
      case ModelPackage.TFIO_SENSOR_CONFIGURATION__PULL_UP_RESISTOR_ENABLED:
        setPullUpResistorEnabled((Boolean)newValue);
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
      case ModelPackage.TFIO_SENSOR_CONFIGURATION__PULL_UP_RESISTOR_ENABLED:
        setPullUpResistorEnabled(PULL_UP_RESISTOR_ENABLED_EDEFAULT);
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
      case ModelPackage.TFIO_SENSOR_CONFIGURATION__PULL_UP_RESISTOR_ENABLED:
        return pullUpResistorEnabled != PULL_UP_RESISTOR_ENABLED_EDEFAULT;
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
    result.append(" (pullUpResistorEnabled: ");
    result.append(pullUpResistorEnabled);
    result.append(')');
    return result.toString();
  }

} //TFIOSensorConfigurationImpl
