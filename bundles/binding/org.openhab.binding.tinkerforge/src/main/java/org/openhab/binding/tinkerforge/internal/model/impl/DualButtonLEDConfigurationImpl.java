/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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

import org.openhab.binding.tinkerforge.internal.model.DualButtonLEDConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dual Button LED Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DualButtonLEDConfigurationImpl#isAutotoggle <em>Autotoggle</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DualButtonLEDConfigurationImpl extends MinimalEObjectImpl.Container implements DualButtonLEDConfiguration
{
  /**
   * The default value of the '{@link #isAutotoggle() <em>Autotoggle</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAutotoggle()
   * @generated
   * @ordered
   */
  protected static final boolean AUTOTOGGLE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isAutotoggle() <em>Autotoggle</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAutotoggle()
   * @generated
   * @ordered
   */
  protected boolean autotoggle = AUTOTOGGLE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DualButtonLEDConfigurationImpl()
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
    return ModelPackage.Literals.DUAL_BUTTON_LED_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isAutotoggle()
  {
    return autotoggle;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAutotoggle(boolean newAutotoggle)
  {
    boolean oldAutotoggle = autotoggle;
    autotoggle = newAutotoggle;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DUAL_BUTTON_LED_CONFIGURATION__AUTOTOGGLE, oldAutotoggle, autotoggle));
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
      case ModelPackage.DUAL_BUTTON_LED_CONFIGURATION__AUTOTOGGLE:
        return isAutotoggle();
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
      case ModelPackage.DUAL_BUTTON_LED_CONFIGURATION__AUTOTOGGLE:
        setAutotoggle((Boolean)newValue);
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
      case ModelPackage.DUAL_BUTTON_LED_CONFIGURATION__AUTOTOGGLE:
        setAutotoggle(AUTOTOGGLE_EDEFAULT);
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
      case ModelPackage.DUAL_BUTTON_LED_CONFIGURATION__AUTOTOGGLE:
        return autotoggle != AUTOTOGGLE_EDEFAULT;
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
    result.append(" (autotoggle: ");
    result.append(autotoggle);
    result.append(')');
    return result.toString();
  }

} //DualButtonLEDConfigurationImpl
