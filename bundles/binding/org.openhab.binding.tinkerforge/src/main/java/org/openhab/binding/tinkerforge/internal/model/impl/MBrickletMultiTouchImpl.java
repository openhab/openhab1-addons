/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import com.tinkerforge.BrickletMultiTouch;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.openhab.binding.tinkerforge.internal.model.BrickletMultiTouchConfiguration;
import org.openhab.binding.tinkerforge.internal.LoggerConstants;
import org.openhab.binding.tinkerforge.internal.TinkerforgeErrorHandler;
import org.openhab.binding.tinkerforge.internal.model.Electrode;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletMultiTouch;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.MultiTouchDevice;
import org.openhab.binding.tinkerforge.internal.model.Proximity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBricklet Multi Touch</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getTinkerforgeDevice <em>Tinkerforge Device</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getConnectedUid <em>Connected Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getPosition <em>Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getDeviceIdentifier <em>Device Identifier</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getBrickd <em>Brickd</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getMsubdevices <em>Msubdevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getRecalibrate <em>Recalibrate</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletMultiTouchImpl#getSensitivity <em>Sensitivity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MBrickletMultiTouchImpl extends MinimalEObjectImpl.Container implements MBrickletMultiTouch
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
   * The cached value of the '{@link #getTinkerforgeDevice() <em>Tinkerforge Device</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTinkerforgeDevice()
   * @generated
   * @ordered
   */
  protected BrickletMultiTouch tinkerforgeDevice;

  /**
   * The default value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIpConnection()
   * @generated
   * @ordered
   */
  protected static final IPConnection IP_CONNECTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIpConnection()
   * @generated
   * @ordered
   */
  protected IPConnection ipConnection = IP_CONNECTION_EDEFAULT;

  /**
   * The default value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectedUid()
   * @generated
   * @ordered
   */
  protected static final String CONNECTED_UID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectedUid()
   * @generated
   * @ordered
   */
  protected String connectedUid = CONNECTED_UID_EDEFAULT;

  /**
   * The default value of the '{@link #getPosition() <em>Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPosition()
   * @generated
   * @ordered
   */
  protected static final char POSITION_EDEFAULT = '\u0000';

  /**
   * The cached value of the '{@link #getPosition() <em>Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPosition()
   * @generated
   * @ordered
   */
  protected char position = POSITION_EDEFAULT;

  /**
   * The default value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceIdentifier()
   * @generated
   * @ordered
   */
  protected static final int DEVICE_IDENTIFIER_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceIdentifier()
   * @generated
   * @ordered
   */
  protected int deviceIdentifier = DEVICE_IDENTIFIER_EDEFAULT;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getMsubdevices() <em>Msubdevices</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMsubdevices()
   * @generated
   * @ordered
   */
  protected EList<MultiTouchDevice> msubdevices;

  /**
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected BrickletMultiTouchConfiguration tfConfig;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "bricklet_multitouch";

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
   * The default value of the '{@link #getRecalibrate() <em>Recalibrate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRecalibrate()
   * @generated
   * @ordered
   */
  protected static final Boolean RECALIBRATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getRecalibrate() <em>Recalibrate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRecalibrate()
   * @generated
   * @ordered
   */
  protected Boolean recalibrate = RECALIBRATE_EDEFAULT;

  /**
   * The default value of the '{@link #getSensitivity() <em>Sensitivity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensitivity()
   * @generated
   * @ordered
   */
  protected static final Short SENSITIVITY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSensitivity() <em>Sensitivity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensitivity()
   * @generated
   * @ordered
   */
  protected Short sensitivity = SENSITIVITY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MBrickletMultiTouchImpl()
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
    return ModelPackage.Literals.MBRICKLET_MULTI_TOUCH;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__ENABLED_A, oldEnabledA, enabledA));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletMultiTouch getTinkerforgeDevice()
  {
    return tinkerforgeDevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTinkerforgeDevice(BrickletMultiTouch newTinkerforgeDevice)
  {
    BrickletMultiTouch oldTinkerforgeDevice = tinkerforgeDevice;
    tinkerforgeDevice = newTinkerforgeDevice;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__TINKERFORGE_DEVICE, oldTinkerforgeDevice, tinkerforgeDevice));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IPConnection getIpConnection()
  {
    return ipConnection;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIpConnection(IPConnection newIpConnection)
  {
    IPConnection oldIpConnection = ipConnection;
    ipConnection = newIpConnection;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__IP_CONNECTION, oldIpConnection, ipConnection));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getConnectedUid()
  {
    return connectedUid;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setConnectedUid(String newConnectedUid)
  {
    String oldConnectedUid = connectedUid;
    connectedUid = newConnectedUid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__CONNECTED_UID, oldConnectedUid, connectedUid));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public char getPosition()
  {
    return position;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPosition(char newPosition)
  {
    char oldPosition = position;
    position = newPosition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__POSITION, oldPosition, position));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getDeviceIdentifier()
  {
    return deviceIdentifier;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDeviceIdentifier(int newDeviceIdentifier)
  {
    int oldDeviceIdentifier = deviceIdentifier;
    deviceIdentifier = newDeviceIdentifier;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__DEVICE_IDENTIFIER, oldDeviceIdentifier, deviceIdentifier));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickd getBrickd()
  {
    if (eContainerFeatureID() != ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD) return null;
    return (MBrickd)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBrickd(MBrickd newBrickd, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newBrickd, ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBrickd(MBrickd newBrickd)
  {
    if (newBrickd != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD && newBrickd != null))
    {
      if (EcoreUtil.isAncestor(this, newBrickd))
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
        msgs = eBasicRemoveFromContainer(msgs);
      if (newBrickd != null)
        msgs = ((InternalEObject)newBrickd).eInverseAdd(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class, msgs);
      msgs = basicSetBrickd(newBrickd, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD, newBrickd, newBrickd));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<MultiTouchDevice> getMsubdevices()
  {
    if (msubdevices == null)
    {
      msubdevices = new EObjectContainmentWithInverseEList<MultiTouchDevice>(MSubDevice.class, this, ModelPackage.MBRICKLET_MULTI_TOUCH__MSUBDEVICES, ModelPackage.MSUB_DEVICE__MBRICK);
    }
    return msubdevices;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletMultiTouchConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(BrickletMultiTouchConfiguration newTfConfig, NotificationChain msgs)
  {
    BrickletMultiTouchConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(BrickletMultiTouchConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG, newTfConfig, newTfConfig));
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
  public Boolean getRecalibrate()
  {
    return recalibrate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRecalibrate(Boolean newRecalibrate)
  {
    Boolean oldRecalibrate = recalibrate;
    recalibrate = newRecalibrate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__RECALIBRATE, oldRecalibrate, recalibrate));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getSensitivity()
  {
    return sensitivity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSensitivity(Short newSensitivity)
  {
    Short oldSensitivity = sensitivity;
    sensitivity = newSensitivity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_MULTI_TOUCH__SENSITIVITY, oldSensitivity, sensitivity));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void initSubDevices() {
    ModelFactory factory = ModelFactory.eINSTANCE;
    for (int i = 0; i < 12; i++) {
      Electrode electrode = factory.createElectrode();
      electrode.setUid(getUid());
      String subId = "electrode" + i;
      electrode.setSubId(subId);
      electrode.setPin(i);
      electrode.init();
      electrode.setMbrick(this);
      logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, subId);
    }
    Proximity proximity = factory.createProximity();
    proximity.setUid(getUid());
    String subId = "proximity";
    proximity.setSubId(subId);
    proximity.setPin(12);
    proximity.init();
    proximity.setMbrick(this);
    logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, subId);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(MBrickletMultiTouchImpl.class);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void enable() {
    if (tfConfig != null) {
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("sensitivity"))) {
        setSensitivity(tfConfig.getSensitivity());
        logger.debug("{} MultiTouchDevice uid {} setSensitivity {}", LoggerConstants.TFINIT,
            getUid(), getSensitivity());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("recalibrate"))) {
        setRecalibrate(tfConfig.getRecalibrate());
        logger.debug("{} MultiTouchDevice uid {} setRecalibrate {}", LoggerConstants.TFINIT,
            getUid(), getRecalibrate());
      }
    }

    tinkerforgeDevice = new BrickletMultiTouch(getUid(), getIpConnection());
    try {
      if (getSensitivity() != null) {
        tinkerforgeDevice.setElectrodeSensitivity(getSensitivity());
      }
      if (getRecalibrate() != null && getRecalibrate()){
        tinkerforgeDevice.recalibrate();
      }
      // enable all electrodes by default
      tinkerforgeDevice.setElectrodeConfig(8191);
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
  public void disable()
  {
    tinkerforgeDevice = null;
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
      case ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetBrickd((MBrickd)otherEnd, msgs);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__MSUBDEVICES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getMsubdevices()).basicAdd(otherEnd, msgs);
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
      case ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD:
        return basicSetBrickd(null, msgs);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__MSUBDEVICES:
        return ((InternalEList<?>)getMsubdevices()).basicRemove(otherEnd, msgs);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG:
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
      case ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD:
        return eInternalContainer().eInverseRemove(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class, msgs);
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
      case ModelPackage.MBRICKLET_MULTI_TOUCH__LOGGER:
        return getLogger();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__UID:
        return getUid();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__TINKERFORGE_DEVICE:
        return getTinkerforgeDevice();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__IP_CONNECTION:
        return getIpConnection();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__CONNECTED_UID:
        return getConnectedUid();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__POSITION:
        return getPosition();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__DEVICE_IDENTIFIER:
        return getDeviceIdentifier();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__NAME:
        return getName();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD:
        return getBrickd();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__MSUBDEVICES:
        return getMsubdevices();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__RECALIBRATE:
        return getRecalibrate();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__SENSITIVITY:
        return getSensitivity();
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
      case ModelPackage.MBRICKLET_MULTI_TOUCH__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletMultiTouch)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__IP_CONNECTION:
        setIpConnection((IPConnection)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__CONNECTED_UID:
        setConnectedUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__POSITION:
        setPosition((Character)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__DEVICE_IDENTIFIER:
        setDeviceIdentifier((Integer)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__NAME:
        setName((String)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD:
        setBrickd((MBrickd)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__MSUBDEVICES:
        getMsubdevices().clear();
        getMsubdevices().addAll((Collection<? extends MultiTouchDevice>)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG:
        setTfConfig((BrickletMultiTouchConfiguration)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__RECALIBRATE:
        setRecalibrate((Boolean)newValue);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__SENSITIVITY:
        setSensitivity((Short)newValue);
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
      case ModelPackage.MBRICKLET_MULTI_TOUCH__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletMultiTouch)null);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__IP_CONNECTION:
        setIpConnection(IP_CONNECTION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__CONNECTED_UID:
        setConnectedUid(CONNECTED_UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__POSITION:
        setPosition(POSITION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__DEVICE_IDENTIFIER:
        setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD:
        setBrickd((MBrickd)null);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__MSUBDEVICES:
        getMsubdevices().clear();
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG:
        setTfConfig((BrickletMultiTouchConfiguration)null);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__RECALIBRATE:
        setRecalibrate(RECALIBRATE_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__SENSITIVITY:
        setSensitivity(SENSITIVITY_EDEFAULT);
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
      case ModelPackage.MBRICKLET_MULTI_TOUCH__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__TINKERFORGE_DEVICE:
        return tinkerforgeDevice != null;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__IP_CONNECTION:
        return IP_CONNECTION_EDEFAULT == null ? ipConnection != null : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__CONNECTED_UID:
        return CONNECTED_UID_EDEFAULT == null ? connectedUid != null : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__POSITION:
        return position != POSITION_EDEFAULT;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__DEVICE_IDENTIFIER:
        return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__BRICKD:
        return getBrickd() != null;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__MSUBDEVICES:
        return msubdevices != null && !msubdevices.isEmpty();
      case ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.MBRICKLET_MULTI_TOUCH__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__RECALIBRATE:
        return RECALIBRATE_EDEFAULT == null ? recalibrate != null : !RECALIBRATE_EDEFAULT.equals(recalibrate);
      case ModelPackage.MBRICKLET_MULTI_TOUCH__SENSITIVITY:
        return SENSITIVITY_EDEFAULT == null ? sensitivity != null : !SENSITIVITY_EDEFAULT.equals(sensitivity);
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
    if (baseClass == MSubDeviceHolder.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICKLET_MULTI_TOUCH__MSUBDEVICES: return ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
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
    if (baseClass == MSubDeviceHolder.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES: return ModelPackage.MBRICKLET_MULTI_TOUCH__MSUBDEVICES;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.MBRICKLET_MULTI_TOUCH__TF_CONFIG;
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
    if (baseClass == MSubDeviceHolder.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MSUB_DEVICE_HOLDER___INIT_SUB_DEVICES: return ModelPackage.MBRICKLET_MULTI_TOUCH___INIT_SUB_DEVICES;
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
      case ModelPackage.MBRICKLET_MULTI_TOUCH___INIT_SUB_DEVICES:
        initSubDevices();
        return null;
      case ModelPackage.MBRICKLET_MULTI_TOUCH___INIT:
        init();
        return null;
      case ModelPackage.MBRICKLET_MULTI_TOUCH___ENABLE:
        enable();
        return null;
      case ModelPackage.MBRICKLET_MULTI_TOUCH___DISABLE:
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
    result.append(", tinkerforgeDevice: ");
    result.append(tinkerforgeDevice);
    result.append(", ipConnection: ");
    result.append(ipConnection);
    result.append(", connectedUid: ");
    result.append(connectedUid);
    result.append(", position: ");
    result.append(position);
    result.append(", deviceIdentifier: ");
    result.append(deviceIdentifier);
    result.append(", name: ");
    result.append(name);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", recalibrate: ");
    result.append(recalibrate);
    result.append(", sensitivity: ");
    result.append(sensitivity);
    result.append(')');
    return result.toString();
  }

} //MBrickletMultiTouchImpl
