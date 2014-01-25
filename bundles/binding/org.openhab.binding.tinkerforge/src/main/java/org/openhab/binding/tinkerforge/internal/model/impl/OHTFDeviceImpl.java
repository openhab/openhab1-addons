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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.OHConfig;
import org.openhab.binding.tinkerforge.internal.model.OHTFDevice;
import org.openhab.binding.tinkerforge.internal.model.TFConfig;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>OHTF Device</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl#getSubid <em>Subid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl#getOhid <em>Ohid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl#getSubDeviceIds <em>Sub Device Ids</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.OHTFDeviceImpl#getOhConfig <em>Oh Config</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
@SuppressWarnings("rawtypes")
public class OHTFDeviceImpl<TFC extends TFConfig, IDS extends Enum>
		extends
		MinimalEObjectImpl.Container implements OHTFDevice<TFC, IDS>
{
  /**
   * The default value of the '{@link #getUid() <em>Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUid()
   * @generated
   * @ordered
   */
  protected static final String UID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getUid() <em>Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUid()
   * @generated
   * @ordered
   */
  protected String uid = UID_EDEFAULT;

  /**
   * The default value of the '{@link #getSubid() <em>Subid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubid()
   * @generated
   * @ordered
   */
  protected static final String SUBID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSubid() <em>Subid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubid()
   * @generated
   * @ordered
   */
  protected String subid = SUBID_EDEFAULT;

  /**
   * The default value of the '{@link #getOhid() <em>Ohid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOhid()
   * @generated
   * @ordered
   */
  protected static final String OHID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOhid() <em>Ohid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOhid()
   * @generated
   * @ordered
   */
  protected String ohid = OHID_EDEFAULT;

  /**
   * The cached value of the '{@link #getSubDeviceIds() <em>Sub Device Ids</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubDeviceIds()
   * @generated
   * @ordered
   */
  protected EList<IDS> subDeviceIds;

  /**
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected TFC tfConfig;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected OHTFDeviceImpl()
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
    return ModelPackage.Literals.OHTF_DEVICE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getUid()
  {
    return uid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUid(String newUid)
  {
    String oldUid = uid;
    uid = newUid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.OHTF_DEVICE__UID, oldUid, uid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSubid()
  {
    return subid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubid(String newSubid)
  {
    String oldSubid = subid;
    subid = newSubid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.OHTF_DEVICE__SUBID, oldSubid, subid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOhid()
  {
    return ohid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOhid(String newOhid)
  {
    String oldOhid = ohid;
    ohid = newOhid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.OHTF_DEVICE__OHID, oldOhid, ohid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<IDS> getSubDeviceIds()
  {
    if (subDeviceIds == null)
    {
      subDeviceIds = new EDataTypeEList<IDS>(Enum.class, this, ModelPackage.OHTF_DEVICE__SUB_DEVICE_IDS);
    }
    return subDeviceIds;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFC getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(TFC newTfConfig, NotificationChain msgs)
  {
    TFC oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.OHTF_DEVICE__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(TFC newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.OHTF_DEVICE__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.OHTF_DEVICE__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.OHTF_DEVICE__TF_CONFIG, newTfConfig, newTfConfig));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OHConfig getOhConfig()
  {
    if (eContainerFeatureID() != ModelPackage.OHTF_DEVICE__OH_CONFIG) return null;
    return (OHConfig)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOhConfig(OHConfig newOhConfig, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newOhConfig, ModelPackage.OHTF_DEVICE__OH_CONFIG, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOhConfig(OHConfig newOhConfig)
  {
    if (newOhConfig != eInternalContainer() || (eContainerFeatureID() != ModelPackage.OHTF_DEVICE__OH_CONFIG && newOhConfig != null))
    {
      if (EcoreUtil.isAncestor(this, newOhConfig))
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
        msgs = eBasicRemoveFromContainer(msgs);
      if (newOhConfig != null)
        msgs = ((InternalEObject)newOhConfig).eInverseAdd(this, ModelPackage.OH_CONFIG__OH_TF_DEVICES, OHConfig.class, msgs);
      msgs = basicSetOhConfig(newOhConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.OHTF_DEVICE__OH_CONFIG, newOhConfig, newOhConfig));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isValidSubId(String subId)
  {
    OHTFDevice<TFC,IDS> _this = this;
    EList<IDS> _subDeviceIds = _this.getSubDeviceIds();
    for (final IDS sid : _subDeviceIds)
    {
      String _string = sid.toString();
      boolean _equalsIgnoreCase = _string.equalsIgnoreCase(subId);
      if (_equalsIgnoreCase)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case ModelPackage.OHTF_DEVICE__OH_CONFIG:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetOhConfig((OHConfig)otherEnd, msgs);
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
      case ModelPackage.OHTF_DEVICE__TF_CONFIG:
        return basicSetTfConfig(null, msgs);
      case ModelPackage.OHTF_DEVICE__OH_CONFIG:
        return basicSetOhConfig(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs)
  {
    switch (eContainerFeatureID())
    {
      case ModelPackage.OHTF_DEVICE__OH_CONFIG:
        return eInternalContainer().eInverseRemove(this, ModelPackage.OH_CONFIG__OH_TF_DEVICES, OHConfig.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
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
      case ModelPackage.OHTF_DEVICE__UID:
        return getUid();
      case ModelPackage.OHTF_DEVICE__SUBID:
        return getSubid();
      case ModelPackage.OHTF_DEVICE__OHID:
        return getOhid();
      case ModelPackage.OHTF_DEVICE__SUB_DEVICE_IDS:
        return getSubDeviceIds();
      case ModelPackage.OHTF_DEVICE__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.OHTF_DEVICE__OH_CONFIG:
        return getOhConfig();
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
      case ModelPackage.OHTF_DEVICE__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.OHTF_DEVICE__SUBID:
        setSubid((String)newValue);
        return;
      case ModelPackage.OHTF_DEVICE__OHID:
        setOhid((String)newValue);
        return;
      case ModelPackage.OHTF_DEVICE__SUB_DEVICE_IDS:
        getSubDeviceIds().clear();
        getSubDeviceIds().addAll((Collection<? extends IDS>)newValue);
        return;
      case ModelPackage.OHTF_DEVICE__TF_CONFIG:
        setTfConfig((TFC)newValue);
        return;
      case ModelPackage.OHTF_DEVICE__OH_CONFIG:
        setOhConfig((OHConfig)newValue);
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
      case ModelPackage.OHTF_DEVICE__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.OHTF_DEVICE__SUBID:
        setSubid(SUBID_EDEFAULT);
        return;
      case ModelPackage.OHTF_DEVICE__OHID:
        setOhid(OHID_EDEFAULT);
        return;
      case ModelPackage.OHTF_DEVICE__SUB_DEVICE_IDS:
        getSubDeviceIds().clear();
        return;
      case ModelPackage.OHTF_DEVICE__TF_CONFIG:
        setTfConfig((TFC)null);
        return;
      case ModelPackage.OHTF_DEVICE__OH_CONFIG:
        setOhConfig((OHConfig)null);
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
      case ModelPackage.OHTF_DEVICE__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.OHTF_DEVICE__SUBID:
        return SUBID_EDEFAULT == null ? subid != null : !SUBID_EDEFAULT.equals(subid);
      case ModelPackage.OHTF_DEVICE__OHID:
        return OHID_EDEFAULT == null ? ohid != null : !OHID_EDEFAULT.equals(ohid);
      case ModelPackage.OHTF_DEVICE__SUB_DEVICE_IDS:
        return subDeviceIds != null && !subDeviceIds.isEmpty();
      case ModelPackage.OHTF_DEVICE__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.OHTF_DEVICE__OH_CONFIG:
        return getOhConfig() != null;
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
      case ModelPackage.OHTF_DEVICE___IS_VALID_SUB_ID__STRING:
        return isValidSubId((String)arguments.get(0));
    }
    return super.eInvoke(operationID, arguments);
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
    result.append(" (uid: ");
    result.append(uid);
    result.append(", subid: ");
    result.append(subid);
    result.append(", ohid: ");
    result.append(ohid);
    result.append(", subDeviceIds: ");
    result.append(subDeviceIds);
    result.append(')');
    return result.toString();
  }

} //OHTFDeviceImpl
