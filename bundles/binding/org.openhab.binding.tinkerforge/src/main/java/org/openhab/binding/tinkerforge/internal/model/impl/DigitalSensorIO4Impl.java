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
import org.openhab.binding.tinkerforge.internal.model.DigitalSensorIO4;
import org.openhab.binding.tinkerforge.internal.model.GenericDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickletIO4;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFIOSensorConfiguration;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletIO4;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Digital Sensor IO4</b></em>'.

 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getGenericDeviceId <em>Generic Device Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#isPullUpResistorEnabled <em>Pull Up Resistor Enabled</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DigitalSensorIO4Impl#getPin <em>Pin</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DigitalSensorIO4Impl extends MinimalEObjectImpl.Container implements DigitalSensorIO4
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
   * The default value of the '{@link #isPoll() <em>Poll</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPoll()
   * @generated
   * @ordered
   */
  protected static final boolean POLL_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isPoll() <em>Poll</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isPoll()
   * @generated
   * @ordered
   */
  protected boolean poll = POLL_EDEFAULT;

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
  protected static final String DEVICE_TYPE_EDEFAULT = "io4sensor";

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

  private InterruptListener listener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected DigitalSensorIO4Impl()
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
    return ModelPackage.Literals.DIGITAL_SENSOR_IO4;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__UID, oldUid, uid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isPoll()
  {
    return poll;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPoll(boolean newPoll)
  {
    boolean oldPoll = poll;
    poll = newPoll;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletIO4 getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.DIGITAL_SENSOR_IO4__MBRICK) return null;
    return (MBrickletIO4)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletIO4 newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.DIGITAL_SENSOR_IO4__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletIO4 newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.DIGITAL_SENSOR_IO4__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__MBRICK, newMbrick, newMbrick));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__GENERIC_DEVICE_ID, oldGenericDeviceId, genericDeviceId));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__SENSOR_VALUE, oldSensorValue, sensorValue));
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
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG, oldTfConfig, newTfConfig);
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
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG, newTfConfig, newTfConfig));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__PULL_UP_RESISTOR_ENABLED, oldPullUpResistorEnabled, pullUpResistorEnabled));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIGITAL_SENSOR_IO4__PIN, oldPin, pin));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void fetchSensorValue()
  {
    HighLowValue value = HighLowValue.UNDEF;
    try {
      value = extractValue(getMbrick().getTinkerforgeDevice().getValue());
      setSensorValue(value);
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
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
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(DigitalSensorIO4Impl.class);
    mask = 0001 << getPin();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void enable()
  {
    setSensorValue(HighLowValue.UNDEF);
    if (tfConfig != null) {
      logger.debug("{} found config for DigitalSensor", LoggerConstants.TFINIT);
      setPullUpResistorEnabled(tfConfig.isPullUpResistorEnabled());
      logger.debug("{} pull-up resistor state is {}", LoggerConstants.TFINIT,
          isPullUpResistorEnabled());
    }
    MBrickletIO4 bricklet = getMbrick();
    if (bricklet == null) {
      logger.error("{} No bricklet found for DigitalSensor: {} ", LoggerConstants.TFINIT, subId);
    } else {
      BrickletIO4 brickletIO4 = bricklet.getTinkerforgeDevice();
      try {
        logger.debug("{} setting InterruptListener for DigitalSensorIO4: {} ", LoggerConstants.TFINIT,
            subId);
        listener = new InterruptListener();
        brickletIO4.addInterruptListener(listener);
        brickletIO4.setConfiguration((short) mask, BrickletIO4.DIRECTION_IN,
            isPullUpResistorEnabled());
        fetchSensorValue();
      } catch (TimeoutException e) {
        TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
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
  private class InterruptListener implements BrickletIO4.InterruptListener {

    @Override
    public void interrupt(short interruptMask, short valueMask) {
      logger.debug("{} interruptListner DigitalSensorIO4 called interrupt mask {}, valuemask {}",
          LoggerConstants.TFMODELUPDATE, interruptMask, valueMask);
      if ((interruptMask & mask) == mask) {
        logger.debug("{} interruptListner DigitalSensorIO4 updating", LoggerConstants.TFMODELUPDATE);
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
    if (listener != null) {
      getMbrick().getTinkerforgeDevice().removeInterruptListener(listener);
    }
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
      case ModelPackage.DIGITAL_SENSOR_IO4__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletIO4)otherEnd, msgs);
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
      case ModelPackage.DIGITAL_SENSOR_IO4__MBRICK:
        return basicSetMbrick(null, msgs);
      case ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG:
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
      case ModelPackage.DIGITAL_SENSOR_IO4__MBRICK:
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
      case ModelPackage.DIGITAL_SENSOR_IO4__LOGGER:
        return getLogger();
      case ModelPackage.DIGITAL_SENSOR_IO4__UID:
        return getUid();
      case ModelPackage.DIGITAL_SENSOR_IO4__POLL:
        return isPoll();
      case ModelPackage.DIGITAL_SENSOR_IO4__ENABLED_A:
        return getEnabledA();
      case ModelPackage.DIGITAL_SENSOR_IO4__SUB_ID:
        return getSubId();
      case ModelPackage.DIGITAL_SENSOR_IO4__MBRICK:
        return getMbrick();
      case ModelPackage.DIGITAL_SENSOR_IO4__GENERIC_DEVICE_ID:
        return getGenericDeviceId();
      case ModelPackage.DIGITAL_SENSOR_IO4__SENSOR_VALUE:
        return getSensorValue();
      case ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.DIGITAL_SENSOR_IO4__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.DIGITAL_SENSOR_IO4__PULL_UP_RESISTOR_ENABLED:
        return isPullUpResistorEnabled();
      case ModelPackage.DIGITAL_SENSOR_IO4__PIN:
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
      case ModelPackage.DIGITAL_SENSOR_IO4__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__MBRICK:
        setMbrick((MBrickletIO4)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__GENERIC_DEVICE_ID:
        setGenericDeviceId((String)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__SENSOR_VALUE:
        setSensorValue((HighLowValue)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG:
        setTfConfig((TFIOSensorConfiguration)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__PULL_UP_RESISTOR_ENABLED:
        setPullUpResistorEnabled((Boolean)newValue);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__PIN:
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
      case ModelPackage.DIGITAL_SENSOR_IO4__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__MBRICK:
        setMbrick((MBrickletIO4)null);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__GENERIC_DEVICE_ID:
        setGenericDeviceId(GENERIC_DEVICE_ID_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__SENSOR_VALUE:
        setSensorValue((HighLowValue)null);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG:
        setTfConfig((TFIOSensorConfiguration)null);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__PULL_UP_RESISTOR_ENABLED:
        setPullUpResistorEnabled(PULL_UP_RESISTOR_ENABLED_EDEFAULT);
        return;
      case ModelPackage.DIGITAL_SENSOR_IO4__PIN:
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
      case ModelPackage.DIGITAL_SENSOR_IO4__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.DIGITAL_SENSOR_IO4__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.DIGITAL_SENSOR_IO4__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.DIGITAL_SENSOR_IO4__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.DIGITAL_SENSOR_IO4__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.DIGITAL_SENSOR_IO4__MBRICK:
        return getMbrick() != null;
      case ModelPackage.DIGITAL_SENSOR_IO4__GENERIC_DEVICE_ID:
        return GENERIC_DEVICE_ID_EDEFAULT == null ? genericDeviceId != null : !GENERIC_DEVICE_ID_EDEFAULT.equals(genericDeviceId);
      case ModelPackage.DIGITAL_SENSOR_IO4__SENSOR_VALUE:
        return sensorValue != null;
      case ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.DIGITAL_SENSOR_IO4__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.DIGITAL_SENSOR_IO4__PULL_UP_RESISTOR_ENABLED:
        return pullUpResistorEnabled != PULL_UP_RESISTOR_ENABLED_EDEFAULT;
      case ModelPackage.DIGITAL_SENSOR_IO4__PIN:
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
        case ModelPackage.DIGITAL_SENSOR_IO4__GENERIC_DEVICE_ID: return ModelPackage.GENERIC_DEVICE__GENERIC_DEVICE_ID;
        default: return -1;
      }
    }
    if (baseClass == MSensor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.DIGITAL_SENSOR_IO4__SENSOR_VALUE: return ModelPackage.MSENSOR__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
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
        case ModelPackage.GENERIC_DEVICE__GENERIC_DEVICE_ID: return ModelPackage.DIGITAL_SENSOR_IO4__GENERIC_DEVICE_ID;
        default: return -1;
      }
    }
    if (baseClass == MSensor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSENSOR__SENSOR_VALUE: return ModelPackage.DIGITAL_SENSOR_IO4__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.DIGITAL_SENSOR_IO4__TF_CONFIG;
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
        case ModelPackage.MSENSOR___FETCH_SENSOR_VALUE: return ModelPackage.DIGITAL_SENSOR_IO4___FETCH_SENSOR_VALUE;
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
      case ModelPackage.DIGITAL_SENSOR_IO4___FETCH_SENSOR_VALUE:
        fetchSensorValue();
        return null;
      case ModelPackage.DIGITAL_SENSOR_IO4___INIT:
        init();
        return null;
      case ModelPackage.DIGITAL_SENSOR_IO4___ENABLE:
        enable();
        return null;
      case ModelPackage.DIGITAL_SENSOR_IO4___DISABLE:
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
    result.append(", poll: ");
    result.append(poll);
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
    result.append(", pin: ");
    result.append(pin);
    result.append(')');
    return result.toString();
  }

} //DigitalSensorIO4Impl
