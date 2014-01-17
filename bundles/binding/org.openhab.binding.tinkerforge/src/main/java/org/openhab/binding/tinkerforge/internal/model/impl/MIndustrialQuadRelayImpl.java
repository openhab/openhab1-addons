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
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openhab.binding.tinkerforge.internal.LoggerConstants;
import org.openhab.binding.tinkerforge.internal.TinkerforgeErrorHandler;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelay;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialQuadRelayBricklet;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MIndustrial Quad Relay</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.4.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialQuadRelayImpl#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MIndustrialQuadRelayImpl extends MinimalEObjectImpl.Container implements MIndustrialQuadRelay
{
  /**
   * The default value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchState()
   * @generated
   * @ordered
   */
  protected static final OnOffValue SWITCH_STATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchState()
   * @generated
   * @ordered
   */
  protected OnOffValue switchState = SWITCH_STATE_EDEFAULT;

  /**
   * The default value of the '{@link #getLogger() <em>Logger</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLogger()
   * @generated
   * @ordered
   */
  protected static final Logger LOGGER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLogger() <em>Logger</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLogger()
   * @generated
   * @ordered
   */
  protected Logger logger = LOGGER_EDEFAULT;

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
   * The default value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnabledA()
   * @generated
   * @ordered
   */
  protected static final AtomicBoolean ENABLED_A_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnabledA()
   * @generated
   * @ordered
   */
  protected AtomicBoolean enabledA = ENABLED_A_EDEFAULT;

  /**
   * The default value of the '{@link #getSubId() <em>Sub Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubId()
   * @generated
   * @ordered
   */
  protected static final String SUB_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSubId() <em>Sub Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubId()
   * @generated
   * @ordered
   */
  protected String subId = SUB_ID_EDEFAULT;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "industrial_quad_relay";

  /**
   * The cached value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected String deviceType = DEVICE_TYPE_EDEFAULT;

  private short relayNum;

private int mask;

  private static final byte DEFAULT_SELECTION_MASK = 0000000000000001;

  private static final byte OFF_BYTE = 0000000000000000;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MIndustrialQuadRelayImpl()
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
    return ModelPackage.Literals.MINDUSTRIAL_QUAD_RELAY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OnOffValue getSwitchState()
  {
    return switchState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSwitchState(OnOffValue newSwitchState)
  {
    OnOffValue oldSwitchState = switchState;
    switchState = newSwitchState;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_QUAD_RELAY__SWITCH_STATE, oldSwitchState, switchState));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public void turnSwitch(OnOffValue state) {
		logger.debug("turnSwitchState called on: {}",
				MIndustrialQuadRelayBrickletImpl.class);
		try {
			if (state == OnOffValue.OFF) {
				logger.debug("setSwitchValue off");
				getMbrick().getTinkerforgeDevice().setSelectedValues(mask,
						OFF_BYTE);
			} else if (state == OnOffValue.ON) {
				logger.debug("setSwitchState on");
				getMbrick().getTinkerforgeDevice()
						.setSelectedValues(mask, mask);
			} else {
				logger.error("{} unkown switchstate {}",
						LoggerConstants.TFMODELUPDATE, state);
			}
			setSwitchState(state);
		} catch (TimeoutException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
		} catch (NotConnectedException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
		}
	}

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public OnOffValue fetchSwitchState()
  {
	  OnOffValue value = OnOffValue.UNDEF;
	  try {
		int deviceValue = getMbrick().getTinkerforgeDevice().getValue();
		if ((deviceValue & mask) == mask ){
			value = OnOffValue.ON;
		}
		else {
			value = OnOffValue.OFF;
		}
	} catch (TimeoutException e) {
		TinkerforgeErrorHandler.handleError(this,
				TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
	} catch (NotConnectedException e) {
		TinkerforgeErrorHandler.handleError(this,
				TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
	}
	  return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Logger getLogger()
  {
    return logger;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLogger(Logger newLogger)
  {
    Logger oldLogger = logger;
    logger = newLogger;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_QUAD_RELAY__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_QUAD_RELAY__UID, oldUid, uid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AtomicBoolean getEnabledA()
  {
    return enabledA;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEnabledA(AtomicBoolean newEnabledA)
  {
    AtomicBoolean oldEnabledA = enabledA;
    enabledA = newEnabledA;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_QUAD_RELAY__ENABLED_A, oldEnabledA, enabledA));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSubId()
  {
    return subId;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSubId(String newSubId)
  {
    String oldSubId = subId;
    subId = newSubId;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_QUAD_RELAY__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MIndustrialQuadRelayBricklet getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK) return null;
    return (MIndustrialQuadRelayBricklet)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MIndustrialQuadRelayBricklet newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MIndustrialQuadRelayBricklet newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK && newMbrick != null))
    {
      if (EcoreUtil.isAncestor(this, newMbrick))
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
        msgs = eBasicRemoveFromContainer(msgs);
      if (newMbrick != null)
        msgs = ((InternalEObject)newMbrick).eInverseAdd(this, ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES, MSubDeviceHolder.class, msgs);
      msgs = basicSetMbrick(newMbrick, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK, newMbrick, newMbrick));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDeviceType()
  {
    return deviceType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
	  setEnabledA(new AtomicBoolean());
	  logger = LoggerFactory.getLogger(MIndustrialQuadRelay.class);
	  relayNum = Short.parseShort(String.valueOf(subId.charAt(subId.length() -1)));
	  mask = DEFAULT_SELECTION_MASK << relayNum;
	  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public void enable() {
		logger.debug("enable called on MIndustrialQuadRelayImpl");
		setSwitchState(fetchSwitchState());
	}

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void disable()
  {
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
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MIndustrialQuadRelayBricklet)otherEnd, msgs);
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
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK:
        return basicSetMbrick(null, msgs);
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
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK:
        return eInternalContainer().eInverseRemove(this, ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES, MSubDeviceHolder.class, msgs);
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
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__LOGGER:
        return getLogger();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__UID:
        return getUid();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__SUB_ID:
        return getSubId();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK:
        return getMbrick();
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__DEVICE_TYPE:
        return getDeviceType();
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
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK:
        setMbrick((MIndustrialQuadRelayBricklet)newValue);
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
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK:
        setMbrick((MIndustrialQuadRelayBricklet)null);
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
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK:
        return getMbrick() != null;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass)
  {
    if (baseClass == MBaseDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MINDUSTRIAL_QUAD_RELAY__LOGGER: return ModelPackage.MBASE_DEVICE__LOGGER;
        case ModelPackage.MINDUSTRIAL_QUAD_RELAY__UID: return ModelPackage.MBASE_DEVICE__UID;
        case ModelPackage.MINDUSTRIAL_QUAD_RELAY__ENABLED_A: return ModelPackage.MBASE_DEVICE__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MINDUSTRIAL_QUAD_RELAY__SUB_ID: return ModelPackage.MSUB_DEVICE__SUB_ID;
        case ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK: return ModelPackage.MSUB_DEVICE__MBRICK;
        default: return -1;
      }
    }
    return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass)
  {
    if (baseClass == MBaseDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MBASE_DEVICE__LOGGER: return ModelPackage.MINDUSTRIAL_QUAD_RELAY__LOGGER;
        case ModelPackage.MBASE_DEVICE__UID: return ModelPackage.MINDUSTRIAL_QUAD_RELAY__UID;
        case ModelPackage.MBASE_DEVICE__ENABLED_A: return ModelPackage.MINDUSTRIAL_QUAD_RELAY__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSUB_DEVICE__SUB_ID: return ModelPackage.MINDUSTRIAL_QUAD_RELAY__SUB_ID;
        case ModelPackage.MSUB_DEVICE__MBRICK: return ModelPackage.MINDUSTRIAL_QUAD_RELAY__MBRICK;
        default: return -1;
      }
    }
    return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int eDerivedOperationID(int baseOperationID, Class<?> baseClass)
  {
    if (baseClass == MBaseDevice.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MBASE_DEVICE___INIT: return ModelPackage.MINDUSTRIAL_QUAD_RELAY___INIT;
        case ModelPackage.MBASE_DEVICE___ENABLE: return ModelPackage.MINDUSTRIAL_QUAD_RELAY___ENABLE;
        case ModelPackage.MBASE_DEVICE___DISABLE: return ModelPackage.MINDUSTRIAL_QUAD_RELAY___DISABLE;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (baseOperationID)
      {
        default: return -1;
      }
    }
    return super.eDerivedOperationID(baseOperationID, baseClass);
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
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY___INIT:
        init();
        return null;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY___ENABLE:
        enable();
        return null;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY___DISABLE:
        disable();
        return null;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY___TURN_SWITCH__ONOFFVALUE:
        turnSwitch((OnOffValue)arguments.get(0));
        return null;
      case ModelPackage.MINDUSTRIAL_QUAD_RELAY___FETCH_SWITCH_STATE:
        return fetchSwitchState();
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
    result.append(" (switchState: ");
    result.append(switchState);
    result.append(", logger: ");
    result.append(logger);
    result.append(", uid: ");
    result.append(uid);
    result.append(", enabledA: ");
    result.append(enabledA);
    result.append(", subId: ");
    result.append(subId);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(')');
    return result.toString();
  }

} //MIndustrialQuadRelayImpl
