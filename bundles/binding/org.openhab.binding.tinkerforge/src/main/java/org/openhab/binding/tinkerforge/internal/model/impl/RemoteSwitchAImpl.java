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
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickletRemoteSwitch;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchA;
import org.openhab.binding.tinkerforge.internal.model.RemoteSwitchAConfiguration;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletRemoteSwitch;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remote Switch A</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getSwitchState <em>Switch State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getSubId <em>Sub Id</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getMbrick <em>Mbrick</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getHouseCode <em>House Code</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getReceiverCode <em>Receiver Code</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.RemoteSwitchAImpl#getRepeats <em>Repeats</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RemoteSwitchAImpl extends MinimalEObjectImpl.Container implements RemoteSwitchA
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
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected RemoteSwitchAConfiguration tfConfig;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "remote_switch_a";

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
   * The default value of the '{@link #getHouseCode() <em>House Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHouseCode()
   * @generated
   * @ordered
   */
  protected static final Short HOUSE_CODE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getHouseCode() <em>House Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHouseCode()
   * @generated
   * @ordered
   */
  protected Short houseCode = HOUSE_CODE_EDEFAULT;

  /**
   * The default value of the '{@link #getReceiverCode() <em>Receiver Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReceiverCode()
   * @generated
   * @ordered
   */
  protected static final Short RECEIVER_CODE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getReceiverCode() <em>Receiver Code</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getReceiverCode()
   * @generated
   * @ordered
   */
  protected Short receiverCode = RECEIVER_CODE_EDEFAULT;

  /**
   * The default value of the '{@link #getRepeats() <em>Repeats</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepeats()
   * @generated
   * @ordered
   */
  protected static final Short REPEATS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRepeats() <em>Repeats</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRepeats()
   * @generated
   * @ordered
   */
  protected Short repeats = REPEATS_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RemoteSwitchAImpl()
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
    return ModelPackage.Literals.REMOTE_SWITCH_A;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__SWITCH_STATE, oldSwitchState, switchState));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__ENABLED_A, oldEnabledA, enabledA));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__SUB_ID, oldSubId, subId));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickletRemoteSwitch getMbrick()
  {
    if (eContainerFeatureID() != ModelPackage.REMOTE_SWITCH_A__MBRICK) return null;
    return (MBrickletRemoteSwitch)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMbrick(MBrickletRemoteSwitch newMbrick, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newMbrick, ModelPackage.REMOTE_SWITCH_A__MBRICK, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMbrick(MBrickletRemoteSwitch newMbrick)
  {
    if (newMbrick != eInternalContainer() || (eContainerFeatureID() != ModelPackage.REMOTE_SWITCH_A__MBRICK && newMbrick != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__MBRICK, newMbrick, newMbrick));
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
  public Short getHouseCode()
  {
    return houseCode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setHouseCode(Short newHouseCode)
  {
    Short oldHouseCode = houseCode;
    houseCode = newHouseCode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__HOUSE_CODE, oldHouseCode, houseCode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getReceiverCode()
  {
    return receiverCode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setReceiverCode(Short newReceiverCode)
  {
    Short oldReceiverCode = receiverCode;
    receiverCode = newReceiverCode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__RECEIVER_CODE, oldReceiverCode, receiverCode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getRepeats()
  {
    return repeats;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRepeats(Short newRepeats)
  {
    Short oldRepeats = repeats;
    repeats = newRepeats;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__REPEATS, oldRepeats, repeats));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RemoteSwitchAConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(RemoteSwitchAConfiguration newTfConfig, NotificationChain msgs)
  {
    RemoteSwitchAConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(RemoteSwitchAConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.REMOTE_SWITCH_A__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.REMOTE_SWITCH_A__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.REMOTE_SWITCH_A__TF_CONFIG, newTfConfig, newTfConfig));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(RemoteSwitchAImpl.class);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void enable() {
    logger.debug("{} enable called on RemoteSwitchA", LoggerConstants.TFINIT);
    boolean houseCodeFound = false;
    boolean receiverCodeFound = false;
    if (tfConfig != null) {
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("houseCode"))) {
        setHouseCode(tfConfig.getHouseCode());
        houseCodeFound = true;
      } else {
        logger.error("{} houseCode not configured for subid {}", LoggerConstants.TFINITSUB,
            getSubId());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("receiverCode"))) {
        setReceiverCode(tfConfig.getReceiverCode());
        receiverCodeFound = true;
      } else {
        logger.error("{} receiverCode not configured for subid {}", LoggerConstants.TFINITSUB,
            getSubId());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("repeats"))) {
        setRepeats(tfConfig.getRepeats());
      }
    }
    if (tfConfig == null || !houseCodeFound || !receiverCodeFound) {
      logger.error("{} missing configuration for subid {} device will not work",
          LoggerConstants.TFINITSUB, getSubId());
    }
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
   * @generated NOT
   */
  public void turnSwitch(OnOffValue state) {
    if (state == OnOffValue.UNDEF) {
      logger.warn("got undef state, nothing to be done");
      return;
    }
    if (getHouseCode() != null && getReceiverCode() != null) {
      short switchTo =
          state == OnOffValue.ON
              ? BrickletRemoteSwitch.SWITCH_TO_ON
              : BrickletRemoteSwitch.SWITCH_TO_OFF;
      try {
        if (getRepeats() != null){
          getMbrick().getTinkerforgeDevice().setRepeats(getRepeats());
        }
        getMbrick().getTinkerforgeDevice().switchSocketA(getHouseCode(), getReceiverCode(),
            switchTo);
        setSwitchState(state);
      } catch (TimeoutException e) {
        TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
      } catch (NotConnectedException e) {
        TinkerforgeErrorHandler.handleError(this,
            TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
      }
    } else {
      logger.error("{} missing configuration for subid {} device will not switch",
          LoggerConstants.TFINITSUB, getSubId());
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public OnOffValue fetchSwitchState()
  {
    return getSwitchState();
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
      case ModelPackage.REMOTE_SWITCH_A__MBRICK:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetMbrick((MBrickletRemoteSwitch)otherEnd, msgs);
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
      case ModelPackage.REMOTE_SWITCH_A__MBRICK:
        return basicSetMbrick(null, msgs);
      case ModelPackage.REMOTE_SWITCH_A__TF_CONFIG:
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
      case ModelPackage.REMOTE_SWITCH_A__MBRICK:
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
      case ModelPackage.REMOTE_SWITCH_A__SWITCH_STATE:
        return getSwitchState();
      case ModelPackage.REMOTE_SWITCH_A__LOGGER:
        return getLogger();
      case ModelPackage.REMOTE_SWITCH_A__UID:
        return getUid();
      case ModelPackage.REMOTE_SWITCH_A__ENABLED_A:
        return getEnabledA();
      case ModelPackage.REMOTE_SWITCH_A__SUB_ID:
        return getSubId();
      case ModelPackage.REMOTE_SWITCH_A__MBRICK:
        return getMbrick();
      case ModelPackage.REMOTE_SWITCH_A__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.REMOTE_SWITCH_A__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.REMOTE_SWITCH_A__HOUSE_CODE:
        return getHouseCode();
      case ModelPackage.REMOTE_SWITCH_A__RECEIVER_CODE:
        return getReceiverCode();
      case ModelPackage.REMOTE_SWITCH_A__REPEATS:
        return getRepeats();
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
      case ModelPackage.REMOTE_SWITCH_A__SWITCH_STATE:
        setSwitchState((OnOffValue)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_A__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_A__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_A__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_A__SUB_ID:
        setSubId((String)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_A__MBRICK:
        setMbrick((MBrickletRemoteSwitch)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_A__TF_CONFIG:
        setTfConfig((RemoteSwitchAConfiguration)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_A__HOUSE_CODE:
        setHouseCode((Short)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_A__RECEIVER_CODE:
        setReceiverCode((Short)newValue);
        return;
      case ModelPackage.REMOTE_SWITCH_A__REPEATS:
        setRepeats((Short)newValue);
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
      case ModelPackage.REMOTE_SWITCH_A__SWITCH_STATE:
        setSwitchState(SWITCH_STATE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_A__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_A__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_A__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_A__SUB_ID:
        setSubId(SUB_ID_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_A__MBRICK:
        setMbrick((MBrickletRemoteSwitch)null);
        return;
      case ModelPackage.REMOTE_SWITCH_A__TF_CONFIG:
        setTfConfig((RemoteSwitchAConfiguration)null);
        return;
      case ModelPackage.REMOTE_SWITCH_A__HOUSE_CODE:
        setHouseCode(HOUSE_CODE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_A__RECEIVER_CODE:
        setReceiverCode(RECEIVER_CODE_EDEFAULT);
        return;
      case ModelPackage.REMOTE_SWITCH_A__REPEATS:
        setRepeats(REPEATS_EDEFAULT);
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
      case ModelPackage.REMOTE_SWITCH_A__SWITCH_STATE:
        return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
      case ModelPackage.REMOTE_SWITCH_A__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.REMOTE_SWITCH_A__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.REMOTE_SWITCH_A__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.REMOTE_SWITCH_A__SUB_ID:
        return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
      case ModelPackage.REMOTE_SWITCH_A__MBRICK:
        return getMbrick() != null;
      case ModelPackage.REMOTE_SWITCH_A__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.REMOTE_SWITCH_A__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.REMOTE_SWITCH_A__HOUSE_CODE:
        return HOUSE_CODE_EDEFAULT == null ? houseCode != null : !HOUSE_CODE_EDEFAULT.equals(houseCode);
      case ModelPackage.REMOTE_SWITCH_A__RECEIVER_CODE:
        return RECEIVER_CODE_EDEFAULT == null ? receiverCode != null : !RECEIVER_CODE_EDEFAULT.equals(receiverCode);
      case ModelPackage.REMOTE_SWITCH_A__REPEATS:
        return REPEATS_EDEFAULT == null ? repeats != null : !REPEATS_EDEFAULT.equals(repeats);
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
        case ModelPackage.REMOTE_SWITCH_A__LOGGER: return ModelPackage.MBASE_DEVICE__LOGGER;
        case ModelPackage.REMOTE_SWITCH_A__UID: return ModelPackage.MBASE_DEVICE__UID;
        case ModelPackage.REMOTE_SWITCH_A__ENABLED_A: return ModelPackage.MBASE_DEVICE__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.REMOTE_SWITCH_A__SUB_ID: return ModelPackage.MSUB_DEVICE__SUB_ID;
        case ModelPackage.REMOTE_SWITCH_A__MBRICK: return ModelPackage.MSUB_DEVICE__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.REMOTE_SWITCH_A__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
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
        case ModelPackage.MBASE_DEVICE__LOGGER: return ModelPackage.REMOTE_SWITCH_A__LOGGER;
        case ModelPackage.MBASE_DEVICE__UID: return ModelPackage.REMOTE_SWITCH_A__UID;
        case ModelPackage.MBASE_DEVICE__ENABLED_A: return ModelPackage.REMOTE_SWITCH_A__ENABLED_A;
        default: return -1;
      }
    }
    if (baseClass == MSubDevice.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSUB_DEVICE__SUB_ID: return ModelPackage.REMOTE_SWITCH_A__SUB_ID;
        case ModelPackage.MSUB_DEVICE__MBRICK: return ModelPackage.REMOTE_SWITCH_A__MBRICK;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.REMOTE_SWITCH_A__TF_CONFIG;
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
        case ModelPackage.MBASE_DEVICE___INIT: return ModelPackage.REMOTE_SWITCH_A___INIT;
        case ModelPackage.MBASE_DEVICE___ENABLE: return ModelPackage.REMOTE_SWITCH_A___ENABLE;
        case ModelPackage.MBASE_DEVICE___DISABLE: return ModelPackage.REMOTE_SWITCH_A___DISABLE;
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
      case ModelPackage.REMOTE_SWITCH_A___INIT:
        init();
        return null;
      case ModelPackage.REMOTE_SWITCH_A___ENABLE:
        enable();
        return null;
      case ModelPackage.REMOTE_SWITCH_A___DISABLE:
        disable();
        return null;
      case ModelPackage.REMOTE_SWITCH_A___TURN_SWITCH__ONOFFVALUE:
        turnSwitch((OnOffValue)arguments.get(0));
        return null;
      case ModelPackage.REMOTE_SWITCH_A___FETCH_SWITCH_STATE:
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
    result.append(", houseCode: ");
    result.append(houseCode);
    result.append(", receiverCode: ");
    result.append(receiverCode);
    result.append(", repeats: ");
    result.append(repeats);
    result.append(')');
    return result.toString();
  }

} //RemoteSwitchAImpl
