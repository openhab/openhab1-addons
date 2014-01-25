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
import org.openhab.binding.tinkerforge.internal.model.DigitalSensor;
import org.openhab.binding.tinkerforge.internal.model.GenericDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIO16;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletIO16;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Digital Sensor</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.4.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getGenericDeviceId <em>Generic Device Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#isPullUpResistorEnabled <em>Pull Up Resistor Enabled</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getPort <em>Port</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorImpl#getPin <em>Pin</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DigitalSensorImpl extends MinimalEObjectImpl.Container implements DigitalSensor
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
   * The default value of the '{@link #getGenericDeviceId() <em>Generic Device Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGenericDeviceId()
   * @generated
   * @ordered
   */
  protected static final String GENERIC_DEVICE_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getGenericDeviceId() <em>Generic Device Id</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getGenericDeviceId()
   * @generated
   * @ordered
   */
  protected String genericDeviceId = GENERIC_DEVICE_ID_EDEFAULT;

  /**
   * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensorValue()
   * @generated
   * @ordered
   */
  protected HighLowValue sensorValue;

  /**
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected TFIOSensorConfiguration tfConfig;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "iosensor";

  /**
   * The cached value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected String deviceType = DEVICE_TYPE_EDEFAULT;

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
   * The default value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected static final char PORT_EDEFAULT = '\u0000';

  /**
   * The cached value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected char port = PORT_EDEFAULT;

  /**
   * The default value of the '{@link #getPin() <em>Pin</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPin()
   * @generated
   * @ordered
   */
  protected static final int PIN_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getPin() <em>Pin</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPin()
   * @generated
   * @ordered
   */
  protected int pin = PIN_EDEFAULT;

private int mask;

private InterruptListener interruptListener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DigitalSensorImpl()
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
    return ModelPackage.Literals.DIGITAL_SENSOR;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletIO16 getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.DIGITAL_SENSOR__MBRICK) return null;
    return (MBrickletIO16)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletIO16 newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.DIGITAL_SENSOR__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletIO16 newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.DIGITAL_SENSOR__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__MBRICK, newMbrick, newMbrick));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getGenericDeviceId()
  {
    return genericDeviceId;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setGenericDeviceId(String newGenericDeviceId)
  {
    String oldGenericDeviceId = genericDeviceId;
    genericDeviceId = newGenericDeviceId;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__GENERIC_DEVICE_ID, oldGenericDeviceId, genericDeviceId));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__SENSOR_VALUE, oldSensorValue, sensorValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TFIOSensorConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(TFIOSensorConfiguration newTfConfig, NotificationChain msgs)
  {
    TFIOSensorConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(TFIOSensorConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.DIGITAL_SENSOR__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.DIGITAL_SENSOR__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__TF_CONFIG, newTfConfig, newTfConfig));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__PULL_UP_RESISTOR_ENABLED, oldPullUpResistorEnabled, pullUpResistorEnabled));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public char getPort()
  {
    return port;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPort(char newPort)
  {
    char oldPort = port;
    port = newPort;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__PORT, oldPort, port));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPin()
  {
    return pin;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPin(int newPin)
  {
    int oldPin = pin;
    pin = newPin;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR__PIN, oldPin, pin));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public HighLowValue fetchSensorValue() {
		HighLowValue value = HighLowValue.UNDEF;
		try {
			value = extractValue(getMbrick().getTinkerforgeDevice().getPort(
					getPort()));
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
		logger = LoggerFactory.getLogger(DigitalSensorImpl.class);
		mask = 00000001 << getPin();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
	public void enable() {
		setSensorValue(HighLowValue.UNDEF);
		if (tfConfig != null) {
			logger.debug("{} found config for DigitalSensor",
					LoggerConstants.TFINIT);
			setPullUpResistorEnabled(tfConfig.isPullUpResistorEnabled());
			logger.debug("{} pull-up resistor state is {}",
					LoggerConstants.TFINIT, isPullUpResistorEnabled());
		}
		MBrickletIO16 bricklet = getMbrick();
		if (bricklet == null) {
			logger.error("{} No bricklet found for DigitalSensor: {} ",
					LoggerConstants.TFINIT, subId);
		} else {
			BrickletIO16 brickletIO16 = bricklet.getTinkerforgeDevice();
			try {
				logger.debug(
						"{} setting InterruptListener for DigitalSensor: {} ",
						LoggerConstants.TFINIT, subId);
				interruptListener = new InterruptListener();
				brickletIO16.addInterruptListener(interruptListener);
				brickletIO16.setPortConfiguration(getPort(), (short) mask,
						BrickletIO16.DIRECTION_IN, isPullUpResistorEnabled());
				setSensorValue(fetchSensorValue());
			} catch (TimeoutException e) {
				TinkerforgeErrorHandler.handleError(this,
						TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
			} catch (NotConnectedException e) {
				TinkerforgeErrorHandler.handleError(this,
						TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
			}
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	private class InterruptListener implements BrickletIO16.InterruptListener {

		@Override
		public void interrupt(char port, short interruptMask, short valueMask) {
			logger.debug(
					"{} interruptListner DigitalSensor called interrupt mask {}, valuemask {}",
					LoggerConstants.TFMODELUPDATE, interruptMask, valueMask);
			if (port == getPort() && (interruptMask & mask) == mask) {
				logger.debug("{} interruptListner DigitalSensor updating",
						LoggerConstants.TFMODELUPDATE);
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
      case ModelPackage.DIGITAL_SENSOR__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletIO16)otherEnd, msgs);
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
      case ModelPackage.DIGITAL_SENSOR__MBRICK:
        return basicSetMbrick(null, msgs);
      case ModelPackage.DIGITAL_SENSOR__TF_CONFIG:
        return basicSetTfConfig(null, msgs);
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
      case ModelPackage.DIGITAL_SENSOR__MBRICK:
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
      case ModelPackage.DIGITAL_SENSOR__LOGGER:
        return getLogger();
      case ModelPackage.DIGITAL_SENSOR__UID:
        return getUid();
      case ModelPackage.DIGITAL_SENSOR__ENABLED_A:
        return getEnabledA();
      case ModelPackage.DIGITAL_SENSOR__SUB_ID:
        return getSubId();
      case ModelPackage.DIGITAL_SENSOR__MBRICK:
        return getMbrick();
      case ModelPackage.DIGITAL_SENSOR__GENERIC_DEVICE_ID:
        return getGenericDeviceId();
      case ModelPackage.DIGITAL_SENSOR__SENSOR_VALUE:
        return getSensorValue();
      case ModelPackage.DIGITAL_SENSOR__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.DIGITAL_SENSOR__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.DIGITAL_SENSOR__PULL_UP_RESISTOR_ENABLED:
        return isPullUpResistorEnabled();
      case ModelPackage.DIGITAL_SENSOR__PORT:
        return getPort();
      case ModelPackage.DIGITAL_SENSOR__PIN:
        return getPin();
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
      case ModelPackage.DIGITAL_SENSOR__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__MBRICK:
        setMbrick((MBrickletIO16)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__GENERIC_DEVICE_ID:
        setGenericDeviceId((String)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__SENSOR_VALUE:
        setSensorValue((HighLowValue)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__TF_CONFIG:
        setTfConfig((TFIOSensorConfiguration)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__PULL_UP_RESISTOR_ENABLED:
        setPullUpResistorEnabled((Boolean)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__PORT:
        setPort((Character)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR__PIN:
        setPin((Integer)newValue);
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
      case ModelPackage.DIGITAL_SENSOR__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR__MBRICK:
        setMbrick((MBrickletIO16)null);
        return;
      case ModelPackage.DIGITAL_SENSOR__GENERIC_DEVICE_ID:
        setGenericDeviceId(GENERIC_DEVICE_ID_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR__SENSOR_VALUE:
        setSensorValue((HighLowValue)null);
        return;
      case ModelPackage.DIGITAL_SENSOR__TF_CONFIG:
        setTfConfig((TFIOSensorConfiguration)null);
        return;
      case ModelPackage.DIGITAL_SENSOR__PULL_UP_RESISTOR_ENABLED:
        setPullUpResistorEnabled(PULL_UP_RESISTOR_ENABLED_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR__PORT:
        setPort(PORT_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR__PIN:
        setPin(PIN_EDEFAULT);
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
      case ModelPackage.DIGITAL_SENSOR__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.DIGITAL_SENSOR__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.DIGITAL_SENSOR__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.DIGITAL_SENSOR__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.DIGITAL_SENSOR__MBRICK:
        return getMbrick() != null;
      case ModelPackage.DIGITAL_SENSOR__GENERIC_DEVICE_ID:
        return GENERIC_DEVICE_ID_EDEFAULT == null ? genericDeviceId != null : !GENERIC_DEVICE_ID_EDEFAULT.equals(genericDeviceId);
      case ModelPackage.DIGITAL_SENSOR__SENSOR_VALUE:
        return sensorValue != null;
      case ModelPackage.DIGITAL_SENSOR__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.DIGITAL_SENSOR__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.DIGITAL_SENSOR__PULL_UP_RESISTOR_ENABLED:
        return pullUpResistorEnabled != PULL_UP_RESISTOR_ENABLED_EDEFAULT;
      case ModelPackage.DIGITAL_SENSOR__PORT:
        return port != PORT_EDEFAULT;
      case ModelPackage.DIGITAL_SENSOR__PIN:
        return pin != PIN_EDEFAULT;
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
    if (baseClass == GenericDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.DIGITAL_SENSOR__GENERIC_DEVICE_ID: return ModelPackage.GENERIC_DEVICE__GENERIC_DEVICE_ID;
        default: return -1;
      }
    }
    if (baseClass == MSensor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.DIGITAL_SENSOR__SENSOR_VALUE: return ModelPackage.MSENSOR__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.DIGITAL_SENSOR__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
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
    if (baseClass == GenericDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.GENERIC_DEVICE__GENERIC_DEVICE_ID: return ModelPackage.DIGITAL_SENSOR__GENERIC_DEVICE_ID;
        default: return -1;
      }
    }
    if (baseClass == MSensor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSENSOR__SENSOR_VALUE: return ModelPackage.DIGITAL_SENSOR__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.DIGITAL_SENSOR__TF_CONFIG;
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
    if (baseClass == GenericDevice.class)
    {
      switch (baseOperationID)
      {
        default: return -1;
      }
    }
    if (baseClass == MSensor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MSENSOR___FETCH_SENSOR_VALUE: return ModelPackage.DIGITAL_SENSOR___FETCH_SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
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
      case ModelPackage.DIGITAL_SENSOR___FETCH_SENSOR_VALUE:
        return fetchSensorValue();
      case ModelPackage.DIGITAL_SENSOR___INIT:
        init();
        return null;
      case ModelPackage.DIGITAL_SENSOR___ENABLE:
        enable();
        return null;
      case ModelPackage.DIGITAL_SENSOR___DISABLE:
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
    result.append(", genericDeviceId: ");
    result.append(genericDeviceId);
    result.append(", sensorValue: ");
    result.append(sensorValue);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", pullUpResistorEnabled: ");
    result.append(pullUpResistorEnabled);
    result.append(", port: ");
    result.append(port);
    result.append(", pin: ");
    result.append(pin);
    result.append(')');
    return result.toString();
  }

} //DigitalSensorImpl
