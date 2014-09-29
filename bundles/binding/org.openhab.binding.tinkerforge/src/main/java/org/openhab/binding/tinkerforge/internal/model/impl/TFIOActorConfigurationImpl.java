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
import org.openhab.binding.tinkerforge.internal.model.TFIOActorConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TFIO Actor Configuration</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.4.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFIOActorConfigurationImpl#getDefaultState <em>Default State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFIOActorConfigurationImpl#isKeepOnReconnect <em>Keep On Reconnect</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TFIOActorConfigurationImpl extends MinimalEObjectImpl.Container implements TFIOActorConfiguration
{
  /**
   * The default value of the '{@link #getDefaultState() <em>Default State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultState()
   * @generated
   * @ordered
   */
  protected static final String DEFAULT_STATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDefaultState() <em>Default State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultState()
   * @generated
   * @ordered
   */
  protected String defaultState = DEFAULT_STATE_EDEFAULT;

  /**
   * The default value of the '{@link #isKeepOnReconnect() <em>Keep On Reconnect</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isKeepOnReconnect()
   * @generated
   * @ordered
   */
  protected static final boolean KEEP_ON_RECONNECT_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isKeepOnReconnect() <em>Keep On Reconnect</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isKeepOnReconnect()
   * @generated
   * @ordered
   */
  protected boolean keepOnReconnect = KEEP_ON_RECONNECT_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TFIOActorConfigurationImpl()
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
    return ModelPackage.Literals.TFIO_ACTOR_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDefaultState()
  {
    return defaultState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefaultState(String newDefaultState)
  {
    String oldDefaultState = defaultState;
    defaultState = newDefaultState;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TFIO_ACTOR_CONFIGURATION__DEFAULT_STATE, oldDefaultState, defaultState));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isKeepOnReconnect()
  {
    return keepOnReconnect;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setKeepOnReconnect(boolean newKeepOnReconnect)
  {
    boolean oldKeepOnReconnect = keepOnReconnect;
    keepOnReconnect = newKeepOnReconnect;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TFIO_ACTOR_CONFIGURATION__KEEP_ON_RECONNECT, oldKeepOnReconnect, keepOnReconnect));
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
      case ModelPackage.TFIO_ACTOR_CONFIGURATION__DEFAULT_STATE:
        return getDefaultState();
      case ModelPackage.TFIO_ACTOR_CONFIGURATION__KEEP_ON_RECONNECT:
        return isKeepOnReconnect();
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
      case ModelPackage.TFIO_ACTOR_CONFIGURATION__DEFAULT_STATE:
        setDefaultState((String)newValue);
        return;
      case ModelPackage.TFIO_ACTOR_CONFIGURATION__KEEP_ON_RECONNECT:
        setKeepOnReconnect((Boolean)newValue);
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
      case ModelPackage.TFIO_ACTOR_CONFIGURATION__DEFAULT_STATE:
        setDefaultState(DEFAULT_STATE_EDEFAULT);
        return;
      case ModelPackage.TFIO_ACTOR_CONFIGURATION__KEEP_ON_RECONNECT:
        setKeepOnReconnect(KEEP_ON_RECONNECT_EDEFAULT);
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
      case ModelPackage.TFIO_ACTOR_CONFIGURATION__DEFAULT_STATE:
        return DEFAULT_STATE_EDEFAULT == null ? defaultState != null : !DEFAULT_STATE_EDEFAULT.equals(defaultState);
      case ModelPackage.TFIO_ACTOR_CONFIGURATION__KEEP_ON_RECONNECT:
        return keepOnReconnect != KEEP_ON_RECONNECT_EDEFAULT;
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
    result.append(" (defaultState: ");
    result.append(defaultState);
    result.append(", keepOnReconnect: ");
    result.append(keepOnReconnect);
    result.append(')');
    return result.toString();
  }

} //TFIOActorConfigurationImpl
