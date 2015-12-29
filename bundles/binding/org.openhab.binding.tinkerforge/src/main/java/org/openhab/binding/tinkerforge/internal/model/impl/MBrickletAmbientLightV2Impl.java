/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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
import org.openhab.binding.tinkerforge.internal.config.ConfigurationException;
import org.openhab.binding.tinkerforge.internal.model.AmbientLightV2Configuration;
import org.openhab.binding.tinkerforge.internal.model.CallbackListener;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletAmbientLightV2;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBricklet Ambient Light V2</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getTinkerforgeDevice <em>Tinkerforge Device</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getConnectedUid <em>Connected Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getPosition <em>Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getDeviceIdentifier <em>Device Identifier</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getBrickd <em>Brickd</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getSensorValue <em>Sensor Value</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getCallbackPeriod <em>Callback Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getIlluminanceRange <em>Illuminance Range</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAmbientLightV2Impl#getIntegrationTime <em>Integration Time</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MBrickletAmbientLightV2Impl extends MinimalEObjectImpl.Container implements MBrickletAmbientLightV2
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
   * The cached value of the '{@link #getTinkerforgeDevice() <em>Tinkerforge Device</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTinkerforgeDevice()
   * @generated
   * @ordered
   */
  protected BrickletAmbientLightV2 tinkerforgeDevice;

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
   * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSensorValue()
   * @generated
   * @ordered
   */
  protected DecimalValue sensorValue;

  /**
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected AmbientLightV2Configuration tfConfig;

  /**
   * The default value of the '{@link #getCallbackPeriod() <em>Callback Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCallbackPeriod()
   * @generated
   * @ordered
   */
  protected static final long CALLBACK_PERIOD_EDEFAULT = 1000L;

  /**
   * The cached value of the '{@link #getCallbackPeriod() <em>Callback Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCallbackPeriod()
   * @generated
   * @ordered
   */
  protected long callbackPeriod = CALLBACK_PERIOD_EDEFAULT;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "bricklet_ambient_lightv2";

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
   * The default value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getThreshold()
   * @generated
   * @ordered
   */
  protected static final BigDecimal THRESHOLD_EDEFAULT = new BigDecimal("1");

  /**
   * The cached value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getThreshold()
   * @generated
   * @ordered
   */
  protected BigDecimal threshold = THRESHOLD_EDEFAULT;

  /**
   * The default value of the '{@link #getIlluminanceRange() <em>Illuminance Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIlluminanceRange()
   * @generated
   * @ordered
   */
  protected static final short ILLUMINANCE_RANGE_EDEFAULT = 3;

  /**
   * The cached value of the '{@link #getIlluminanceRange() <em>Illuminance Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIlluminanceRange()
   * @generated
   * @ordered
   */
  protected short illuminanceRange = ILLUMINANCE_RANGE_EDEFAULT;

  /**
   * The default value of the '{@link #getIntegrationTime() <em>Integration Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIntegrationTime()
   * @generated
   * @ordered
   */
  protected static final short INTEGRATION_TIME_EDEFAULT = 3;

  /**
   * The cached value of the '{@link #getIntegrationTime() <em>Integration Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIntegrationTime()
   * @generated
   * @ordered
   */
  protected short integrationTime = INTEGRATION_TIME_EDEFAULT;

  private IlluminanceListener listener;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MBrickletAmbientLightV2Impl()
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
    return ModelPackage.Literals.MBRICKLET_AMBIENT_LIGHT_V2;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ENABLED_A, oldEnabledA, enabledA));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletAmbientLightV2 getTinkerforgeDevice()
  {
    return tinkerforgeDevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTinkerforgeDevice(BrickletAmbientLightV2 newTinkerforgeDevice)
  {
    BrickletAmbientLightV2 oldTinkerforgeDevice = tinkerforgeDevice;
    tinkerforgeDevice = newTinkerforgeDevice;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TINKERFORGE_DEVICE, oldTinkerforgeDevice, tinkerforgeDevice));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__IP_CONNECTION, oldIpConnection, ipConnection));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CONNECTED_UID, oldConnectedUid, connectedUid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POSITION, oldPosition, position));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__DEVICE_IDENTIFIER, oldDeviceIdentifier, deviceIdentifier));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickd getBrickd()
  {
    if (eContainerFeatureID() != ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD) return null;
    return (MBrickd)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBrickd(MBrickd newBrickd, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newBrickd, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBrickd(MBrickd newBrickd)
  {
    if (newBrickd != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD && newBrickd != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD, newBrickd, newBrickd));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DecimalValue getSensorValue()
  {
    return sensorValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSensorValue(DecimalValue newSensorValue)
  {
    DecimalValue oldSensorValue = sensorValue;
    sensorValue = newSensorValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__SENSOR_VALUE, oldSensorValue, sensorValue));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AmbientLightV2Configuration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(AmbientLightV2Configuration newTfConfig, NotificationChain msgs)
  {
    AmbientLightV2Configuration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(AmbientLightV2Configuration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG, newTfConfig, newTfConfig));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public long getCallbackPeriod()
  {
    return callbackPeriod;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCallbackPeriod(long newCallbackPeriod)
  {
    long oldCallbackPeriod = callbackPeriod;
    callbackPeriod = newCallbackPeriod;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CALLBACK_PERIOD, oldCallbackPeriod, callbackPeriod));
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
  public BigDecimal getThreshold()
  {
    return threshold;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setThreshold(BigDecimal newThreshold)
  {
    BigDecimal oldThreshold = threshold;
    threshold = newThreshold;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__THRESHOLD, oldThreshold, threshold));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getIlluminanceRange()
  {
    return illuminanceRange;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIlluminanceRange(short newIlluminanceRange)
  {
    short oldIlluminanceRange = illuminanceRange;
    illuminanceRange = newIlluminanceRange;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ILLUMINANCE_RANGE, oldIlluminanceRange, illuminanceRange));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getIntegrationTime()
  {
    return integrationTime;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIntegrationTime(short newIntegrationTime)
  {
    short oldIntegrationTime = integrationTime;
    integrationTime = newIntegrationTime;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__INTEGRATION_TIME, oldIntegrationTime, integrationTime));
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(MBrickletAmbientLightV2Impl.class);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated NOT
   */
  public void fetchSensorValue()
  {
    try {
      long illuminance = tinkerforgeDevice.getIlluminance();
      DecimalValue value = Tools.calculate10(illuminance);
      setSensorValue(value);
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
  public void enable()
  {
    List<Integer> possibleIlluminanceValues = Arrays.asList(new Integer[] {0, 1, 2, 3, 4, 5, 6});
    List<Integer> possibleIntegrationTimeValues =
        Arrays.asList(new Integer[] {0, 1, 2, 3, 4, 5, 6, 7});
    if (tfConfig != null) {
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("threshold"))) {
        logger.debug("threshold {}", tfConfig.getThreshold());
        setThreshold(tfConfig.getThreshold());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("callbackPeriod"))) {
        logger.debug("callbackPeriod {}", tfConfig.getCallbackPeriod());
        setCallbackPeriod(tfConfig.getCallbackPeriod());
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("illuminanceRange"))) {
        short illuminanceRange = tfConfig.getIlluminanceRange();
        logger.debug("illuminanceRange {}", illuminanceRange);
        if (!possibleIlluminanceValues.contains(illuminanceRange)) {
          logger.error("invalid illuminanceRange value: {}", illuminanceRange);
          throw new ConfigurationException("invalid illuminanceRange value: " + illuminanceRange);
        }
        setIlluminanceRange(illuminanceRange);
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("integrationTime"))) {
        short integrationTime = tfConfig.getIntegrationTime();
        logger.debug("integrationTime {}", integrationTime);
        if (!possibleIntegrationTimeValues.contains(integrationTime)) {
          logger.error("invalid integrationTime value: {}", integrationTime);
          throw new ConfigurationException("invalid integrationTime value: " + integrationTime);
        }
        setIntegrationTime(integrationTime);
      }
    }
    tinkerforgeDevice = new BrickletAmbientLightV2(getUid(), getIpConnection());
    try {
      tinkerforgeDevice.setIlluminanceCallbackPeriod(callbackPeriod);
      tinkerforgeDevice.setConfiguration(getIlluminanceRange(), getIntegrationTime());
      listener = new IlluminanceListener();
      tinkerforgeDevice.addIlluminanceListener(listener);
      fetchSensorValue();
    } catch (TimeoutException e) {
      TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
    } catch (NotConnectedException e) {
      TinkerforgeErrorHandler.handleError(this,
          TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
    }
  }

  private class IlluminanceListener implements BrickletAmbientLightV2.IlluminanceListener {

    @Override
    public void illuminance(long newIlluminance) {
      DecimalValue newValue = Tools.calculate10(newIlluminance);
      logger.trace("{} got new value {}", LoggerConstants.TFMODELUPDATE, newValue);
      if (newValue.compareTo(getSensorValue(), getThreshold()) != 0) {
        logger.trace("{} setting new value {}", LoggerConstants.TFMODELUPDATE, newValue);
        setSensorValue(newValue);
      } else {
        logger.trace("{} omitting new value {}", LoggerConstants.TFMODELUPDATE, newValue);
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
      tinkerforgeDevice.removeIlluminanceListener(listener);
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
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetBrickd((MBrickd)otherEnd, msgs);
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
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD:
        return basicSetBrickd(null, msgs);
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG:
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
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD:
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
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__LOGGER:
        return getLogger();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__UID:
        return getUid();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POLL:
        return isPoll();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TINKERFORGE_DEVICE:
        return getTinkerforgeDevice();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__IP_CONNECTION:
        return getIpConnection();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CONNECTED_UID:
        return getConnectedUid();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POSITION:
        return getPosition();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__DEVICE_IDENTIFIER:
        return getDeviceIdentifier();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__NAME:
        return getName();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD:
        return getBrickd();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__SENSOR_VALUE:
        return getSensorValue();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CALLBACK_PERIOD:
        return getCallbackPeriod();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__THRESHOLD:
        return getThreshold();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ILLUMINANCE_RANGE:
        return getIlluminanceRange();
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__INTEGRATION_TIME:
        return getIntegrationTime();
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
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletAmbientLightV2)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__IP_CONNECTION:
        setIpConnection((IPConnection)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CONNECTED_UID:
        setConnectedUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POSITION:
        setPosition((Character)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__DEVICE_IDENTIFIER:
        setDeviceIdentifier((Integer)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__NAME:
        setName((String)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD:
        setBrickd((MBrickd)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__SENSOR_VALUE:
        setSensorValue((DecimalValue)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG:
        setTfConfig((AmbientLightV2Configuration)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CALLBACK_PERIOD:
        setCallbackPeriod((Long)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__THRESHOLD:
        setThreshold((BigDecimal)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ILLUMINANCE_RANGE:
        setIlluminanceRange((Short)newValue);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__INTEGRATION_TIME:
        setIntegrationTime((Short)newValue);
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
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletAmbientLightV2)null);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__IP_CONNECTION:
        setIpConnection(IP_CONNECTION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CONNECTED_UID:
        setConnectedUid(CONNECTED_UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POSITION:
        setPosition(POSITION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__DEVICE_IDENTIFIER:
        setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD:
        setBrickd((MBrickd)null);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__SENSOR_VALUE:
        setSensorValue((DecimalValue)null);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG:
        setTfConfig((AmbientLightV2Configuration)null);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CALLBACK_PERIOD:
        setCallbackPeriod(CALLBACK_PERIOD_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__THRESHOLD:
        setThreshold(THRESHOLD_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ILLUMINANCE_RANGE:
        setIlluminanceRange(ILLUMINANCE_RANGE_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__INTEGRATION_TIME:
        setIntegrationTime(INTEGRATION_TIME_EDEFAULT);
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
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TINKERFORGE_DEVICE:
        return tinkerforgeDevice != null;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__IP_CONNECTION:
        return IP_CONNECTION_EDEFAULT == null ? ipConnection != null : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CONNECTED_UID:
        return CONNECTED_UID_EDEFAULT == null ? connectedUid != null : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__POSITION:
        return position != POSITION_EDEFAULT;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__DEVICE_IDENTIFIER:
        return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__BRICKD:
        return getBrickd() != null;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__SENSOR_VALUE:
        return sensorValue != null;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CALLBACK_PERIOD:
        return callbackPeriod != CALLBACK_PERIOD_EDEFAULT;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__THRESHOLD:
        return THRESHOLD_EDEFAULT == null ? threshold != null : !THRESHOLD_EDEFAULT.equals(threshold);
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__ILLUMINANCE_RANGE:
        return illuminanceRange != ILLUMINANCE_RANGE_EDEFAULT;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__INTEGRATION_TIME:
        return integrationTime != INTEGRATION_TIME_EDEFAULT;
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
        case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__SENSOR_VALUE: return ModelPackage.MSENSOR__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == CallbackListener.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CALLBACK_PERIOD: return ModelPackage.CALLBACK_LISTENER__CALLBACK_PERIOD;
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
        case ModelPackage.MSENSOR__SENSOR_VALUE: return ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__SENSOR_VALUE;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__TF_CONFIG;
        default: return -1;
      }
    }
    if (baseClass == CallbackListener.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.CALLBACK_LISTENER__CALLBACK_PERIOD: return ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2__CALLBACK_PERIOD;
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
        case ModelPackage.MSENSOR___FETCH_SENSOR_VALUE: return ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2___FETCH_SENSOR_VALUE;
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
    if (baseClass == CallbackListener.class)
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
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2___INIT:
        init();
        return null;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2___FETCH_SENSOR_VALUE:
        fetchSensorValue();
        return null;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2___ENABLE:
        enable();
        return null;
      case ModelPackage.MBRICKLET_AMBIENT_LIGHT_V2___DISABLE:
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
    result.append(", sensorValue: ");
    result.append(sensorValue);
    result.append(", callbackPeriod: ");
    result.append(callbackPeriod);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", threshold: ");
    result.append(threshold);
    result.append(", illuminanceRange: ");
    result.append(illuminanceRange);
    result.append(", integrationTime: ");
    result.append(integrationTime);
    result.append(')');
    return result.toString();
  }

} //MBrickletAmbientLightV2Impl
