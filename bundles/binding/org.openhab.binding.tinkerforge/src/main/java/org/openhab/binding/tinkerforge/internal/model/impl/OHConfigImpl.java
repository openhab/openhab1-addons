/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.OHConfig;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>OH Config</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.OHConfigImpl#getOhTfDevices <em>Oh Tf Devices</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OHConfigImpl extends MinimalEObjectImpl.Container implements OHConfig
{
  /**
   * The cached value of the '{@link #getOhTfDevices() <em>Oh Tf Devices</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOhTfDevices()
   * @generated
   * @ordered
   */
  protected EList<OHTFDevice<?, ?>> ohTfDevices;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OHConfigImpl()
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
    return ModelPackage.Literals.OH_CONFIG;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<OHTFDevice<?, ?>> getOhTfDevices()
  {
    if (ohTfDevices == null)
    {
      ohTfDevices = new EObjectContainmentWithInverseEList<OHTFDevice<?, ?>>(OHTFDevice.class, this, ModelPackage.OH_CONFIG__OH_TF_DEVICES, ModelPackage.OHTF_DEVICE__OH_CONFIG);
    }
    return ohTfDevices;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public OHTFDevice<?, ?> getConfigByTFId(String uid, String subid) {
		EList<OHTFDevice<?, ?>> _ohTfDevices = getOhTfDevices();
		for (final OHTFDevice<?, ?> ohTfDevice : _ohTfDevices) {
			String _uid = ohTfDevice.getUid();
			if (_uid.equals(uid)) {
				if (subid == null && ohTfDevice.getSubid() == null) {
					return ohTfDevice;
				} else {
					String _subid = ohTfDevice.getSubid();
					if (_subid != null && _subid.equals(subid)) {
						return ohTfDevice;
					}
				}
			}
		}
		return null;
	}

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public OHTFDevice<?, ?> getConfigByOHId(String ohid)
  {
		EList<OHTFDevice<?, ?>> _ohTfDevices = getOhTfDevices();
		for (final OHTFDevice<?, ?> ohTfDevice : _ohTfDevices)
    {
      String _ohid = ohTfDevice.getOhid();
      if (_ohid.equals(ohid))
    	  return ohTfDevice;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.OH_CONFIG__OH_TF_DEVICES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getOhTfDevices()).basicAdd(otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.OH_CONFIG__OH_TF_DEVICES:
        return ((InternalEList<?>)getOhTfDevices()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case ModelPackage.OH_CONFIG__OH_TF_DEVICES:
        return getOhTfDevices();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ModelPackage.OH_CONFIG__OH_TF_DEVICES:
        getOhTfDevices().clear();
        getOhTfDevices().addAll((Collection<? extends OHTFDevice<?, ?>>)newValue);
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
      case ModelPackage.OH_CONFIG__OH_TF_DEVICES:
        getOhTfDevices().clear();
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
      case ModelPackage.OH_CONFIG__OH_TF_DEVICES:
        return ohTfDevices != null && !ohTfDevices.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException
  {
    switch (operationID)
    {
      case ModelPackage.OH_CONFIG___GET_CONFIG_BY_TF_ID__STRING_STRING:
        return getConfigByTFId((String)arguments.get(0), (String)arguments.get(1));
      case ModelPackage.OH_CONFIG___GET_CONFIG_BY_OH_ID__STRING:
        return getConfigByOHId((String)arguments.get(0));
    }
    return super.eInvoke(operationID, arguments);
  }

} //OHConfigImpl
