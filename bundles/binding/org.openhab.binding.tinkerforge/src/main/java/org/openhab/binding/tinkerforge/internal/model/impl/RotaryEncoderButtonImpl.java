/**
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
import org.openhab.binding.tinkerforge.internal.model.ButtonConfiguration;
import org.openhab.binding.tinkerforge.internal.model.MBrickletRotaryEncoder;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.RotaryEncoderButton;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletRotaryEncoder;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rotary Encoder Button</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RotaryEncoderButtonImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RotaryEncoderButtonImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RotaryEncoderButtonImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RotaryEncoderButtonImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RotaryEncoderButtonImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RotaryEncoderButtonImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RotaryEncoderButtonImpl#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RotaryEncoderButtonImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RotaryEncoderButtonImpl#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RotaryEncoderButtonImpl extends MinimalEObjectImpl.Container implements RotaryEncoderButton
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
   * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensorValue()
   * @generated
   * @ordered
   */
  protected OnOffValue sensorValue;

  /**
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected ButtonConfiguration tfConfig;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "rotary_encoder_button";

  /**
   * The cached value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected String deviceType = DEVICE_TYPE_EDEFAULT;

  private BrickletRotaryEncoder tinkerforgeDevice;

  private boolean tactile;

  private ButtonListener listener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RotaryEncoderButtonImpl()
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
    return ModelPackage.Literals.ROTARY_ENCODER_BUTTON;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ROTARY_ENCODER_BUTTON__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ROTARY_ENCODER_BUTTON__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ROTARY_ENCODER_BUTTON__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ROTARY_ENCODER_BUTTON__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ROTARY_ENCODER_BUTTON__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletRotaryEncoder getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK) return null;
    return (MBrickletRotaryEncoder)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletRotaryEncoder newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletRotaryEncoder newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK, newMbrick, newMbrick));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public OnOffValue getSensorValue()
  {
    return sensorValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSensorValue(OnOffValue newSensorValue)
  {
    OnOffValue oldSensorValue = sensorValue;
    sensorValue = newSensorValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ROTARY_ENCODER_BUTTON__SENSOR_VALUE, oldSensorValue, sensorValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ButtonConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(ButtonConfiguration newTfConfig, NotificationChain msgs)
  {
    ButtonConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(ButtonConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG, newTfConfig, newTfConfig));
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
  public void fetchSensorValue()
  {
    if (tactile || this.sensorValue == null || this.sensorValue == OnOffValue.UNDEF) {
      try {
        OnOffValue value = tinkerforgeDevice.isPressed() ? OnOffValue.ON : OnOffValue.OFF;
        setSensorValue(value);
      } catch (TimeoutException e) {
        TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
      } catch (NotConnectedException e) {
        TinkerforgeErrorHandler.handleError(this,
            TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
      }
    } else {
      // send current state to update the eventbus
      setSensorValue(getSensorValue());
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
    logger = LoggerFactory.getLogger(RotaryEncoderButtonImpl.class);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void enable()
  {
    tinkerforgeDevice = getMbrick().getTinkerforgeDevice();
    tactile = false;
    if (tfConfig != null) {
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("tactile"))) {
        tactile = tfConfig.isTactile();
      }
    }
    logger.trace("tactile is {}", tactile);
    listener = new ButtonListener();
    tinkerforgeDevice.addPressedListener(listener);
    tinkerforgeDevice.addReleasedListener(listener);
    fetchSensorValue();
  }

  private class ButtonListener
      implements
        BrickletRotaryEncoder.PressedListener,
        BrickletRotaryEncoder.ReleasedListener {

    @Override
    public void released() {
      if (tactile) {
        logger.trace("{} released setting new tactile value", LoggerConstants.TFMODELUPDATE);
        setSensorValue(OnOffValue.OFF);
      } else {
        logger.trace("{} released omitting in switch mode", LoggerConstants.TFMODELUPDATE);
      }
    }

    @Override
    public void pressed() {
      if (tactile) {
        logger.trace("{} pressed setting new tactile value {}", LoggerConstants.TFMODELUPDATE,
            OnOffValue.ON);
        setSensorValue(OnOffValue.ON);
      } else {
        // toggle current device state
        OnOffValue newSwitchValue = sensorValue == OnOffValue.ON ? OnOffValue.OFF : OnOffValue.ON;
        logger.trace("pressed switch value changed to {}", newSwitchValue);
        setSensorValue(newSwitchValue);
      }
    }

  }
  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void disable()
  {
    if (listener != null) {
      tinkerforgeDevice.removePressedListener(listener);
      tinkerforgeDevice.removeReleasedListener(listener);
    }
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
      case ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletRotaryEncoder)otherEnd, msgs);
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
      case ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK:
        return basicSetMbrick(null, msgs);
      case ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG:
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
      case ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK:
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
      case ModelPackage.ROTARY_ENCODER_BUTTON__LOGGER:
        return getLogger();
      case ModelPackage.ROTARY_ENCODER_BUTTON__UID:
        return getUid();
      case ModelPackage.ROTARY_ENCODER_BUTTON__POLL:
        return isPoll();
      case ModelPackage.ROTARY_ENCODER_BUTTON__ENABLED_A:
        return getEnabledA();
      case ModelPackage.ROTARY_ENCODER_BUTTON__SUB_ID:
        return getSubId();
      case ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK:
        return getMbrick();
      case ModelPackage.ROTARY_ENCODER_BUTTON__SENSOR_VALUE:
        return getSensorValue();
      case ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.ROTARY_ENCODER_BUTTON__DEVICE_TYPE:
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
      case ModelPackage.ROTARY_ENCODER_BUTTON__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK:
        setMbrick((MBrickletRotaryEncoder)newValue);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__SENSOR_VALUE:
        setSensorValue((OnOffValue)newValue);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG:
        setTfConfig((ButtonConfiguration)newValue);
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
      case ModelPackage.ROTARY_ENCODER_BUTTON__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK:
        setMbrick((MBrickletRotaryEncoder)null);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__SENSOR_VALUE:
        setSensorValue((OnOffValue)null);
        return;
      case ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG:
        setTfConfig((ButtonConfiguration)null);
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
      case ModelPackage.ROTARY_ENCODER_BUTTON__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.ROTARY_ENCODER_BUTTON__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.ROTARY_ENCODER_BUTTON__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.ROTARY_ENCODER_BUTTON__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.ROTARY_ENCODER_BUTTON__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.ROTARY_ENCODER_BUTTON__MBRICK:
        return getMbrick() != null;
      case ModelPackage.ROTARY_ENCODER_BUTTON__SENSOR_VALUE:
        return sensorValue != null;
      case ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.ROTARY_ENCODER_BUTTON__DEVICE_TYPE:
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
    if (baseClass == MSensor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.ROTARY_ENCODER_BUTTON__SENSOR_VALUE: return ModelPackage.MSENSOR__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
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
        case ModelPackage.MSENSOR__SENSOR_VALUE: return ModelPackage.ROTARY_ENCODER_BUTTON__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.ROTARY_ENCODER_BUTTON__TF_CONFIG;
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
        case ModelPackage.MSENSOR___FETCH_SENSOR_VALUE: return ModelPackage.ROTARY_ENCODER_BUTTON___FETCH_SENSOR_VALUE;
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
      case ModelPackage.ROTARY_ENCODER_BUTTON___FETCH_SENSOR_VALUE:
        fetchSensorValue();
        return null;
      case ModelPackage.ROTARY_ENCODER_BUTTON___INIT:
        init();
        return null;
      case ModelPackage.ROTARY_ENCODER_BUTTON___ENABLE:
        enable();
        return null;
      case ModelPackage.ROTARY_ENCODER_BUTTON___DISABLE:
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
    result.append(", sensorValue: ");
    result.append(sensorValue);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(')');
    return result.toString();
  }

} //RotaryEncoderButtonImpl
