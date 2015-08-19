/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openhab.binding.tinkerforge.internal.TinkerforgeErrorHandler;
import org.openhab.binding.tinkerforge.internal.model.ColorActor;
import org.openhab.binding.tinkerforge.internal.model.LEDGroup;
import org.openhab.binding.tinkerforge.internal.model.LEDGroupConfiguration;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLEDStrip;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MSwitchActor;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.SimpleColorActor;
import org.openhab.binding.tinkerforge.internal.model.SwitchSensor;
import org.openhab.binding.tinkerforge.internal.tools.LedList;
import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.types.HSBValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.core.library.types.HSBType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletLEDStrip;
import com.tinkerforge.BrickletLEDStrip.RGBValues;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LED Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#getColor <em>Color</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LEDGroupImpl#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LEDGroupImpl extends MinimalEObjectImpl.Container implements LEDGroup
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
   * The default value of the '{@link #getColor() <em>Color</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColor()
   * @generated
   * @ordered
   */
  protected static final HSBValue COLOR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getColor() <em>Color</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColor()
   * @generated
   * @ordered
   */
  protected HSBValue color = COLOR_EDEFAULT;

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
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected LEDGroupConfiguration tfConfig;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "ledgroup";

  /**
   * The cached value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected String deviceType = DEVICE_TYPE_EDEFAULT;

  private HSBValue lastONValue;

  private BrickletLEDStrip tinkerforgeDevice;

  private LedList ledList;

  private char[] colorMapping;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LEDGroupImpl()
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
    return ModelPackage.Literals.LED_GROUP;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletLEDStrip getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.LED_GROUP__MBRICK) return null;
    return (MBrickletLEDStrip)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletLEDStrip newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.LED_GROUP__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletLEDStrip newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.LED_GROUP__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__MBRICK, newMbrick, newMbrick));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HSBValue getColor()
  {
    return color;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setColor(HSBValue newColor)
  {
    HSBValue oldColor = color;
    color = newColor;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__COLOR, oldColor, color));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__SWITCH_STATE, oldSwitchState, switchState));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LEDGroupConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(LEDGroupConfiguration newTfConfig, NotificationChain msgs)
  {
    LEDGroupConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(LEDGroupConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.LED_GROUP__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.LED_GROUP__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LED_GROUP__TF_CONFIG, newTfConfig, newTfConfig));
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
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void turnSwitch(OnOffValue state)
  {
    if (state == OnOffValue.OFF) {
      lastONValue = getColor();
      setSelectedColor(HSBType.BLACK);
    } else {
      setSelectedColor(lastONValue.getHsbValue());
    }
    setSwitchState(state);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void fetchSwitchState() {
    if (ledList.getTrackingled() != null) {
      try {
        RGBValues rgbValues = tinkerforgeDevice.getRGBValues(ledList.getTrackingled(), (short) 1);
        short[] r = rgbValues.r;
        short[] g = rgbValues.g;
        short[] b = rgbValues.b;
        if (r[0] == 0 && g[0] == 0 && b[0] == 0) {
          setSwitchState(OnOffValue.OFF);
        } else {
          setSwitchState(OnOffValue.ON);
        }
      } catch (TimeoutException e) {
        TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
      } catch (NotConnectedException e) {
        TinkerforgeErrorHandler.handleError(this,
            TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
      }
    } else {
      setSwitchState(OnOffValue.UNDEF);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void setSelectedColor(HSBType color)
  {
    // get the rgb values from HSBType
    Color rgbColor = color.toColor();
    short red = (short) rgbColor.getRed();
    short green = (short) rgbColor.getGreen();
    short blue = (short) rgbColor.getBlue();
    logger.debug("rgb is: {}:{}:{}", red, green, blue);

    // construct the values for the setRGBValues call
    HashMap<Character, short[]> colorMap = new HashMap<Character, short[]>(3);
    short[] reds = {red, red, red, red, red, red, red, red, red, red, red, red, red, red, red, red};
    short[] greens =
        {green, green, green, green, green, green, green, green, green, green, green, green, green,
            green, green, green};
    short[] blues =
        {blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue,
            blue};
    colorMap.put('r', reds);
    colorMap.put('g', greens);
    colorMap.put('b', blues);

    try {
      if (ledList.hasLedRanges()) {
        Map<Integer, Short> ledRanges = ledList.getLedRanges();
        for (Integer led : ledRanges.keySet()) {
          tinkerforgeDevice.setRGBValues(led, ledRanges.get(led), colorMap.get(colorMapping[0]),
              colorMap.get(colorMapping[1]), colorMap.get(colorMapping[2]));
        }
      }
      if (ledList.hasLeds()) {
        for (Integer led : ledList.getLedNumbers()) {
          tinkerforgeDevice.setRGBValues(led, (short) 1, colorMap.get(colorMapping[0]),
              colorMap.get(colorMapping[1]), colorMap.get(colorMapping[2]));
        }
      }
      setColor(new HSBValue(color));
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
    } catch (NotConnectedException e) {
      TinkerforgeErrorHandler.handleError(this,
          TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(LEDGroupImpl.class);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void enable()
  {
    tinkerforgeDevice = getMbrick().getTinkerforgeDevice();
    colorMapping = getMbrick().getColorMapping().toCharArray();
    logger.debug("colorMapping is {}", colorMapping);
    String ledsstring = null;
    if (tfConfig != null) {
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature(
          ModelPackage.LED_GROUP_CONFIGURATION__LEDS))) {
        ledsstring = tfConfig.getLeds();
      }
    }
    if (ledsstring == null) {
      logger.error("leds configuration is missing");
      // TODO throw configuration exception?
    } else {
      ledList = Tools.parseLedString(ledsstring);
    }
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void disable()
  {
    tinkerforgeDevice = null;
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
      case ModelPackage.LED_GROUP__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletLEDStrip)otherEnd, msgs);
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
      case ModelPackage.LED_GROUP__MBRICK:
        return basicSetMbrick(null, msgs);
      case ModelPackage.LED_GROUP__TF_CONFIG:
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
      case ModelPackage.LED_GROUP__MBRICK:
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
      case ModelPackage.LED_GROUP__LOGGER:
        return getLogger();
      case ModelPackage.LED_GROUP__UID:
        return getUid();
      case ModelPackage.LED_GROUP__POLL:
        return isPoll();
      case ModelPackage.LED_GROUP__ENABLED_A:
        return getEnabledA();
      case ModelPackage.LED_GROUP__SUB_ID:
        return getSubId();
      case ModelPackage.LED_GROUP__MBRICK:
        return getMbrick();
      case ModelPackage.LED_GROUP__COLOR:
        return getColor();
      case ModelPackage.LED_GROUP__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.LED_GROUP__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.LED_GROUP__DEVICE_TYPE:
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
      case ModelPackage.LED_GROUP__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.LED_GROUP__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.LED_GROUP__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.LED_GROUP__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.LED_GROUP__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.LED_GROUP__MBRICK:
        setMbrick((MBrickletLEDStrip)newValue);
        return;
      case ModelPackage.LED_GROUP__COLOR:
        setColor((HSBValue)newValue);
        return;
      case ModelPackage.LED_GROUP__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
      case ModelPackage.LED_GROUP__TF_CONFIG:
        setTfConfig((LEDGroupConfiguration)newValue);
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
      case ModelPackage.LED_GROUP__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.LED_GROUP__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.LED_GROUP__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.LED_GROUP__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.LED_GROUP__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.LED_GROUP__MBRICK:
        setMbrick((MBrickletLEDStrip)null);
        return;
      case ModelPackage.LED_GROUP__COLOR:
        setColor(COLOR_EDEFAULT);
        return;
      case ModelPackage.LED_GROUP__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
      case ModelPackage.LED_GROUP__TF_CONFIG:
        setTfConfig((LEDGroupConfiguration)null);
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
      case ModelPackage.LED_GROUP__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.LED_GROUP__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.LED_GROUP__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.LED_GROUP__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.LED_GROUP__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.LED_GROUP__MBRICK:
        return getMbrick() != null;
      case ModelPackage.LED_GROUP__COLOR:
        return COLOR_EDEFAULT == null ? color != null : !COLOR_EDEFAULT.equals(color);
      case ModelPackage.LED_GROUP__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.LED_GROUP__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.LED_GROUP__DEVICE_TYPE:
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
    if (baseClass == ColorActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.LED_GROUP__COLOR: return ModelPackage.COLOR_ACTOR__COLOR;
        default: return -1;
      }
    }
    if (baseClass == SimpleColorActor.class)
    {
      switch (derivedFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == SwitchSensor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.LED_GROUP__SWITCH_STATE: return ModelPackage.SWITCH_SENSOR__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == MSwitchActor.class)
    {
      switch (derivedFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.LED_GROUP__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
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
    if (baseClass == ColorActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.COLOR_ACTOR__COLOR: return ModelPackage.LED_GROUP__COLOR;
        default: return -1;
      }
    }
    if (baseClass == SimpleColorActor.class)
    {
      switch (baseFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == SwitchSensor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.SWITCH_SENSOR__SWITCH_STATE: return ModelPackage.LED_GROUP__SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == MSwitchActor.class)
    {
      switch (baseFeatureID)
      {
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.LED_GROUP__TF_CONFIG;
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
    if (baseClass == ColorActor.class)
    {
      switch (baseOperationID)
      {
        default: return -1;
      }
    }
    if (baseClass == SimpleColorActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.SIMPLE_COLOR_ACTOR___SET_SELECTED_COLOR__HSBTYPE: return ModelPackage.LED_GROUP___SET_SELECTED_COLOR__HSBTYPE;
        default: return -1;
      }
    }
    if (baseClass == SwitchSensor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.SWITCH_SENSOR___FETCH_SWITCH_STATE: return ModelPackage.LED_GROUP___FETCH_SWITCH_STATE;
        default: return -1;
      }
    }
    if (baseClass == MSwitchActor.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MSWITCH_ACTOR___TURN_SWITCH__ONOFFVALUE: return ModelPackage.LED_GROUP___TURN_SWITCH__ONOFFVALUE;
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
      case ModelPackage.LED_GROUP___TURN_SWITCH__ONOFFVALUE:
        turnSwitch((OnOffValue)arguments.get(0));
        return null;
      case ModelPackage.LED_GROUP___FETCH_SWITCH_STATE:
        fetchSwitchState();
        return null;
      case ModelPackage.LED_GROUP___SET_SELECTED_COLOR__HSBTYPE:
        setSelectedColor((HSBType)arguments.get(0));
        return null;
      case ModelPackage.LED_GROUP___INIT:
        init();
        return null;
      case ModelPackage.LED_GROUP___ENABLE:
        enable();
        return null;
      case ModelPackage.LED_GROUP___DISABLE:
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
    result.append(", color: ");
    result.append(color);
    result.append(", switchState: ");
    result.append(switchState);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(')');
    return result.toString();
  }

} //LEDGroupImpl
