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
import org.openhab.binding.tinkerforge.internal.model.MBrickletIndustrialDigitalIn4;
import org.openhab.binding.tinkerforge.internal.model.MIndustrialDigitalIn;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletIndustrialDigitalIn4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MIndustrial Digital In</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.4.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MIndustrialDigitalInImpl#getSensorValue <em>Sensor Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MIndustrialDigitalInImpl extends MinimalEObjectImpl.Container implements MIndustrialDigitalIn
{
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
   * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensorValue()
   * @generated
   * @ordered
   */
  protected HighLowValue sensorValue;

  private short inNum;

private InterruptListener interruptListener;

private int mask;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MIndustrialDigitalInImpl()
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
    return ModelPackage.Literals.MINDUSTRIAL_DIGITAL_IN;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletIndustrialDigitalIn4 getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK) return null;
    return (MBrickletIndustrialDigitalIn4)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletIndustrialDigitalIn4 newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletIndustrialDigitalIn4 newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK, newMbrick, newMbrick));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HighLowValue getSensorValue()
  {
    return sensorValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSensorValue(HighLowValue newSensorValue)
  {
    HighLowValue oldSensorValue = sensorValue;
    sensorValue = newSensorValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MINDUSTRIAL_DIGITAL_IN__SENSOR_VALUE, oldSensorValue, sensorValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public HighLowValue fetchSensorValue()
  {
	  HighLowValue value = HighLowValue.UNDEF;
	  try {
		value = extractValue(getMbrick().getTinkerforgeDevice().getValue());
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
   * @generated NOT
   */
  public void init()
  {
	    setEnabledA(new AtomicBoolean());
		logger = LoggerFactory.getLogger(MIndustrialDigitalInImpl.class);
		inNum = Short.parseShort(String.valueOf(subId.charAt(subId.length() - 1)));
		mask = 0001 << inNum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public void enable() {
		setSensorValue(HighLowValue.UNDEF);
		MBrickletIndustrialDigitalIn4 bricklet = getMbrick();
		if (bricklet == null) {
			logger.error("{} No brick found for Digital4In: {} ",
					LoggerConstants.TFINIT, subId);
		} else {
			BrickletIndustrialDigitalIn4 brickletIndustrialDigitalIn4 = bricklet
					.getTinkerforgeDevice();
			interruptListener = new InterruptListener();
			brickletIndustrialDigitalIn4
					.addInterruptListener(interruptListener);
			setSensorValue(fetchSensorValue());
		}
	}

  /**
  *
  * @generated NOT
  */
	private HighLowValue extractValue(int valueMask) {
		HighLowValue value = HighLowValue.UNDEF;
		if ((valueMask & mask) == mask) {
			value = HighLowValue.HIGH;
		} else {
			value = HighLowValue.LOW;
		}
		return value;
	}

	/**
	 * 
	 * @generated NOT
	 */
	private class InterruptListener implements
			BrickletIndustrialDigitalIn4.InterruptListener {
		@Override
		public void interrupt(int interruptMask, int valueMask) {
			if ((interruptMask & mask) == mask) {
				setSensorValue(extractValue(valueMask));
			}
		}
	}
  
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void disable()
  {
	  getMbrick().getTinkerforgeDevice().removeInterruptListener(interruptListener);
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletIndustrialDigitalIn4)otherEnd, msgs);
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER:
        return getLogger();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID:
        return getUid();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID:
        return getSubId();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        return getMbrick();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SENSOR_VALUE:
        return getSensorValue();
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        setMbrick((MBrickletIndustrialDigitalIn4)newValue);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SENSOR_VALUE:
        setSensorValue((HighLowValue)newValue);
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        setMbrick((MBrickletIndustrialDigitalIn4)null);
        return;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SENSOR_VALUE:
        setSensorValue((HighLowValue)null);
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__MBRICK:
        return getMbrick() != null;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SENSOR_VALUE:
        return sensorValue != null;
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
    if (baseClass == MSensor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MINDUSTRIAL_DIGITAL_IN__SENSOR_VALUE: return ModelPackage.MSENSOR__SENSOR_VALUE;
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
    if (baseClass == MSensor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSENSOR__SENSOR_VALUE: return ModelPackage.MINDUSTRIAL_DIGITAL_IN__SENSOR_VALUE;
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
    if (baseClass == MSensor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MSENSOR___FETCH_SENSOR_VALUE: return ModelPackage.MINDUSTRIAL_DIGITAL_IN___FETCH_SENSOR_VALUE;
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
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN___FETCH_SENSOR_VALUE:
        return fetchSensorValue();
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN___INIT:
        init();
        return null;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN___ENABLE:
        enable();
        return null;
      case ModelPackage.MINDUSTRIAL_DIGITAL_IN___DISABLE:
        disable();
        return null;
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
    result.append(" (logger: ");
    result.append(logger);
    result.append(", uid: ");
    result.append(uid);
    result.append(", enabledA: ");
    result.append(enabledA);
    result.append(", subId: ");
    result.append(subId);
    result.append(", sensorValue: ");
    result.append(sensorValue);
    result.append(')');
    return result.toString();
  }

} //MIndustrialDigitalInImpl
