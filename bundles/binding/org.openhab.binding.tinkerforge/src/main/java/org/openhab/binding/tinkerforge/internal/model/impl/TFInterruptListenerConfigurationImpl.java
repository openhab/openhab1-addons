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
import org.openhab.binding.tinkerforge.internal.model.TFInterruptListenerConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TF Interrupt Listener Configuration</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.4.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFInterruptListenerConfigurationImpl#getDebouncePeriod <em>Debounce Period</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TFInterruptListenerConfigurationImpl extends MinimalEObjectImpl.Container implements TFInterruptListenerConfiguration
{
  /**
   * The default value of the '{@link #getDebouncePeriod() <em>Debounce Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDebouncePeriod()
   * @generated
   * @ordered
   */
  protected static final long DEBOUNCE_PERIOD_EDEFAULT = 0L;
  /**
   * The cached value of the '{@link #getDebouncePeriod() <em>Debounce Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDebouncePeriod()
   * @generated
   * @ordered
   */
  protected long debouncePeriod = DEBOUNCE_PERIOD_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TFInterruptListenerConfigurationImpl()
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
    return ModelPackage.Literals.TF_INTERRUPT_LISTENER_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public long getDebouncePeriod()
  {
    return debouncePeriod;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDebouncePeriod(long newDebouncePeriod)
  {
    long oldDebouncePeriod = debouncePeriod;
    debouncePeriod = newDebouncePeriod;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_INTERRUPT_LISTENER_CONFIGURATION__DEBOUNCE_PERIOD, oldDebouncePeriod, debouncePeriod));
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
      case ModelPackage.TF_INTERRUPT_LISTENER_CONFIGURATION__DEBOUNCE_PERIOD:
        return getDebouncePeriod();
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
      case ModelPackage.TF_INTERRUPT_LISTENER_CONFIGURATION__DEBOUNCE_PERIOD:
        setDebouncePeriod((Long)newValue);
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
      case ModelPackage.TF_INTERRUPT_LISTENER_CONFIGURATION__DEBOUNCE_PERIOD:
        setDebouncePeriod(DEBOUNCE_PERIOD_EDEFAULT);
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
      case ModelPackage.TF_INTERRUPT_LISTENER_CONFIGURATION__DEBOUNCE_PERIOD:
        return debouncePeriod != DEBOUNCE_PERIOD_EDEFAULT;
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
    result.append(" (debouncePeriod: ");
    result.append(debouncePeriod);
    result.append(')');
    return result.toString();
  }

} //TFInterruptListenerConfigurationImpl
