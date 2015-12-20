/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import com.tinkerforge.BrickletLaserRangeFinder;
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

import org.openhab.binding.tinkerforge.internal.LoggerConstants;
import org.openhab.binding.tinkerforge.internal.TinkerforgeErrorHandler;
import org.openhab.binding.tinkerforge.internal.model.LaserRangeFinderConfiguration;
import org.openhab.binding.tinkerforge.internal.model.LaserRangeFinderDevice;
import org.openhab.binding.tinkerforge.internal.model.LaserRangeFinderDistance;
import org.openhab.binding.tinkerforge.internal.model.LaserRangeFinderLaser;
import org.openhab.binding.tinkerforge.internal.model.LaserRangeFinderVelocity;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBricklet Laser Range Finder</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#isPoll <em>Poll</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getTinkerforgeDevice <em>Tinkerforge Device</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getConnectedUid <em>Connected Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getPosition <em>Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getDeviceIdentifier <em>Device Identifier</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getBrickd <em>Brickd</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getMsubdevices <em>Msubdevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getTfConfig <em>Tf Config</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getDistanceAverageLength <em>Distance Average Length</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getVelocityAverageLength <em>Velocity Average Length</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLaserRangeFinderImpl#getEnableLaserOnStartup <em>Enable Laser On Startup</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MBrickletLaserRangeFinderImpl extends MinimalEObjectImpl.Container implements MBrickletLaserRangeFinder
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
  protected BrickletLaserRangeFinder tinkerforgeDevice;

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
  protected EList<LaserRangeFinderDevice> msubdevices;

  /**
   * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTfConfig()
   * @generated
   * @ordered
   */
  protected LaserRangeFinderConfiguration tfConfig;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "bricklet_laser_range_finder";

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
   * The default value of the '{@link #getDistanceAverageLength() <em>Distance Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDistanceAverageLength()
   * @generated
   * @ordered
   */
  protected static final short DISTANCE_AVERAGE_LENGTH_EDEFAULT = 10;

  /**
   * The cached value of the '{@link #getDistanceAverageLength() <em>Distance Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDistanceAverageLength()
   * @generated
   * @ordered
   */
  protected short distanceAverageLength = DISTANCE_AVERAGE_LENGTH_EDEFAULT;

  /**
   * The default value of the '{@link #getVelocityAverageLength() <em>Velocity Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocityAverageLength()
   * @generated
   * @ordered
   */
  protected static final short VELOCITY_AVERAGE_LENGTH_EDEFAULT = 10;

  /**
   * The cached value of the '{@link #getVelocityAverageLength() <em>Velocity Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocityAverageLength()
   * @generated
   * @ordered
   */
  protected short velocityAverageLength = VELOCITY_AVERAGE_LENGTH_EDEFAULT;

  /**
   * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMode()
   * @generated
   * @ordered
   */
  protected static final short MODE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMode()
   * @generated
   * @ordered
   */
  protected short mode = MODE_EDEFAULT;

  /**
   * The default value of the '{@link #getEnableLaserOnStartup() <em>Enable Laser On Startup</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnableLaserOnStartup()
   * @generated
   * @ordered
   */
  protected static final Boolean ENABLE_LASER_ON_STARTUP_EDEFAULT = Boolean.TRUE;

  /**
   * The cached value of the '{@link #getEnableLaserOnStartup() <em>Enable Laser On Startup</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnableLaserOnStartup()
   * @generated
   * @ordered
   */
  protected Boolean enableLaserOnStartup = ENABLE_LASER_ON_STARTUP_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MBrickletLaserRangeFinderImpl()
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
    return ModelPackage.Literals.MBRICKLET_LASER_RANGE_FINDER;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__UID, oldUid, uid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POLL, oldPoll, poll));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLED_A, oldEnabledA, enabledA));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BrickletLaserRangeFinder getTinkerforgeDevice()
  {
    return tinkerforgeDevice;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTinkerforgeDevice(BrickletLaserRangeFinder newTinkerforgeDevice)
  {
    BrickletLaserRangeFinder oldTinkerforgeDevice = tinkerforgeDevice;
    tinkerforgeDevice = newTinkerforgeDevice;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TINKERFORGE_DEVICE, oldTinkerforgeDevice, tinkerforgeDevice));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__IP_CONNECTION, oldIpConnection, ipConnection));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__CONNECTED_UID, oldConnectedUid, connectedUid));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POSITION, oldPosition, position));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DEVICE_IDENTIFIER, oldDeviceIdentifier, deviceIdentifier));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MBrickd getBrickd()
  {
    if (eContainerFeatureID() != ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD) return null;
    return (MBrickd)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBrickd(MBrickd newBrickd, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newBrickd, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBrickd(MBrickd newBrickd)
  {
    if (newBrickd != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD && newBrickd != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD, newBrickd, newBrickd));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<LaserRangeFinderDevice> getMsubdevices()
  {
    if (msubdevices == null)
    {
      msubdevices = new EObjectContainmentWithInverseEList<LaserRangeFinderDevice>(MSubDevice.class, this, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MSUBDEVICES, ModelPackage.MSUB_DEVICE__MBRICK);
    }
    return msubdevices;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LaserRangeFinderConfiguration getTfConfig()
  {
    return tfConfig;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTfConfig(LaserRangeFinderConfiguration newTfConfig, NotificationChain msgs)
  {
    LaserRangeFinderConfiguration oldTfConfig = tfConfig;
    tfConfig = newTfConfig;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG, oldTfConfig, newTfConfig);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTfConfig(LaserRangeFinderConfiguration newTfConfig)
  {
    if (newTfConfig != tfConfig)
    {
      NotificationChain msgs = null;
      if (tfConfig != null)
        msgs = ((InternalEObject)tfConfig).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG, null, msgs);
      if (newTfConfig != null)
        msgs = ((InternalEObject)newTfConfig).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG, null, msgs);
      msgs = basicSetTfConfig(newTfConfig, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG, newTfConfig, newTfConfig));
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
  public short getDistanceAverageLength()
  {
    return distanceAverageLength;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDistanceAverageLength(short newDistanceAverageLength)
  {
    short oldDistanceAverageLength = distanceAverageLength;
    distanceAverageLength = newDistanceAverageLength;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DISTANCE_AVERAGE_LENGTH, oldDistanceAverageLength, distanceAverageLength));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getVelocityAverageLength()
  {
    return velocityAverageLength;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVelocityAverageLength(short newVelocityAverageLength)
  {
    short oldVelocityAverageLength = velocityAverageLength;
    velocityAverageLength = newVelocityAverageLength;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__VELOCITY_AVERAGE_LENGTH, oldVelocityAverageLength, velocityAverageLength));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getMode()
  {
    return mode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMode(short newMode)
  {
    short oldMode = mode;
    mode = newMode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MODE, oldMode, mode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Boolean getEnableLaserOnStartup()
  {
    return enableLaserOnStartup;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEnableLaserOnStartup(Boolean newEnableLaserOnStartup)
  {
    Boolean oldEnableLaserOnStartup = enableLaserOnStartup;
    enableLaserOnStartup = newEnableLaserOnStartup;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLE_LASER_ON_STARTUP, oldEnableLaserOnStartup, enableLaserOnStartup));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void initSubDevices()
  {
    ModelFactory factory = ModelFactory.eINSTANCE;
    LaserRangeFinderDistance distance = factory.createLaserRangeFinderDistance();
    distance.setUid(getUid());
    String subIdDistance = "distance";
    logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, subIdDistance);
    distance.setSubId(subIdDistance);
    distance.init();
    distance.setMbrick(this);
    
    LaserRangeFinderVelocity velocity = factory.createLaserRangeFinderVelocity();
    velocity.setUid(getUid());
    String subIdVelocity = "velocity";
    logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, subIdVelocity);
    velocity.setSubId(subIdVelocity);
    velocity.init();
    velocity.setMbrick(this);
    
    LaserRangeFinderLaser laser = factory.createLaserRangeFinderLaser();
    laser.setUid(getUid());
    String subIdLaser = "laser";
    logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, subIdLaser);
    laser.setSubId(subIdLaser);
    laser.init();
    laser.setMbrick(this);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void init()
  {
    setEnabledA(new AtomicBoolean());
    logger = LoggerFactory.getLogger(MBrickletLaserRangeFinderImpl.class);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void enable()
  {
    if (tfConfig != null) {
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("distanceAverageLength"))) {
        Short distanceAverageLength = tfConfig.getDistanceAverageLength();
        logger.debug("distanceAverageLength {}", distanceAverageLength);
        setDistanceAverageLength(distanceAverageLength);
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("velocityAverageLength"))) {
        Short velocityAverageLength = tfConfig.getVelocityAverageLength();
        logger.debug("velocityAverageLength {}", velocityAverageLength);
        setVelocityAverageLength(velocityAverageLength);
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("mode"))) {
        Short mode = tfConfig.getMode();
        logger.debug("mode {}", mode);
        setMode(mode);
      }
      if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("enableLaserOnStartup"))) {
        boolean enableLaser = tfConfig.getEnableLaserOnStartup();
        logger.debug("enable laser on startup {}", enableLaser);
        setEnableLaserOnStartup(enableLaser);
      } else {
        boolean enableLaser = tfConfig.getEnableLaserOnStartup();
        logger.debug("enable laser on startup {}", enableLaser);
      }
    }
    try {
      tinkerforgeDevice = new BrickletLaserRangeFinder(getUid(), getIpConnection());
      tinkerforgeDevice.setMode(getMode());
      tinkerforgeDevice.setMovingAverage(getDistanceAverageLength(), getVelocityAverageLength());
      if (getEnableLaserOnStartup()){
        logger.debug("enabling laser");
        tinkerforgeDevice.enableLaser();
      }
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
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetBrickd((MBrickd)otherEnd, msgs);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MSUBDEVICES:
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
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD:
        return basicSetBrickd(null, msgs);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MSUBDEVICES:
        return ((InternalEList<?>)getMsubdevices()).basicRemove(otherEnd, msgs);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG:
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
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD:
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
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__LOGGER:
        return getLogger();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__UID:
        return getUid();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POLL:
        return isPoll();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TINKERFORGE_DEVICE:
        return getTinkerforgeDevice();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__IP_CONNECTION:
        return getIpConnection();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__CONNECTED_UID:
        return getConnectedUid();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POSITION:
        return getPosition();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DEVICE_IDENTIFIER:
        return getDeviceIdentifier();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__NAME:
        return getName();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD:
        return getBrickd();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MSUBDEVICES:
        return getMsubdevices();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG:
        return getTfConfig();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DISTANCE_AVERAGE_LENGTH:
        return getDistanceAverageLength();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__VELOCITY_AVERAGE_LENGTH:
        return getVelocityAverageLength();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MODE:
        return getMode();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLE_LASER_ON_STARTUP:
        return getEnableLaserOnStartup();
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
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POLL:
        setPoll((Boolean)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletLaserRangeFinder)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__IP_CONNECTION:
        setIpConnection((IPConnection)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__CONNECTED_UID:
        setConnectedUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POSITION:
        setPosition((Character)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DEVICE_IDENTIFIER:
        setDeviceIdentifier((Integer)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__NAME:
        setName((String)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD:
        setBrickd((MBrickd)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MSUBDEVICES:
        getMsubdevices().clear();
        getMsubdevices().addAll((Collection<? extends LaserRangeFinderDevice>)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG:
        setTfConfig((LaserRangeFinderConfiguration)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DISTANCE_AVERAGE_LENGTH:
        setDistanceAverageLength((Short)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__VELOCITY_AVERAGE_LENGTH:
        setVelocityAverageLength((Short)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MODE:
        setMode((Short)newValue);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLE_LASER_ON_STARTUP:
        setEnableLaserOnStartup((Boolean)newValue);
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
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POLL:
        setPoll(POLL_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletLaserRangeFinder)null);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__IP_CONNECTION:
        setIpConnection(IP_CONNECTION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__CONNECTED_UID:
        setConnectedUid(CONNECTED_UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POSITION:
        setPosition(POSITION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DEVICE_IDENTIFIER:
        setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD:
        setBrickd((MBrickd)null);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MSUBDEVICES:
        getMsubdevices().clear();
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG:
        setTfConfig((LaserRangeFinderConfiguration)null);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DISTANCE_AVERAGE_LENGTH:
        setDistanceAverageLength(DISTANCE_AVERAGE_LENGTH_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__VELOCITY_AVERAGE_LENGTH:
        setVelocityAverageLength(VELOCITY_AVERAGE_LENGTH_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MODE:
        setMode(MODE_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLE_LASER_ON_STARTUP:
        setEnableLaserOnStartup(ENABLE_LASER_ON_STARTUP_EDEFAULT);
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
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POLL:
        return poll != POLL_EDEFAULT;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TINKERFORGE_DEVICE:
        return tinkerforgeDevice != null;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__IP_CONNECTION:
        return IP_CONNECTION_EDEFAULT == null ? ipConnection != null : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__CONNECTED_UID:
        return CONNECTED_UID_EDEFAULT == null ? connectedUid != null : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__POSITION:
        return position != POSITION_EDEFAULT;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DEVICE_IDENTIFIER:
        return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__BRICKD:
        return getBrickd() != null;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MSUBDEVICES:
        return msubdevices != null && !msubdevices.isEmpty();
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG:
        return tfConfig != null;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__DISTANCE_AVERAGE_LENGTH:
        return distanceAverageLength != DISTANCE_AVERAGE_LENGTH_EDEFAULT;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__VELOCITY_AVERAGE_LENGTH:
        return velocityAverageLength != VELOCITY_AVERAGE_LENGTH_EDEFAULT;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MODE:
        return mode != MODE_EDEFAULT;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__ENABLE_LASER_ON_STARTUP:
        return ENABLE_LASER_ON_STARTUP_EDEFAULT == null ? enableLaserOnStartup != null : !ENABLE_LASER_ON_STARTUP_EDEFAULT.equals(enableLaserOnStartup);
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
        case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MSUBDEVICES: return ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG: return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
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
        case ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES: return ModelPackage.MBRICKLET_LASER_RANGE_FINDER__MSUBDEVICES;
        default: return -1;
      }
    }
    if (baseClass == MTFConfigConsumer.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG: return ModelPackage.MBRICKLET_LASER_RANGE_FINDER__TF_CONFIG;
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
        case ModelPackage.MSUB_DEVICE_HOLDER___INIT_SUB_DEVICES: return ModelPackage.MBRICKLET_LASER_RANGE_FINDER___INIT_SUB_DEVICES;
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
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER___INIT_SUB_DEVICES:
        initSubDevices();
        return null;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER___INIT:
        init();
        return null;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER___ENABLE:
        enable();
        return null;
      case ModelPackage.MBRICKLET_LASER_RANGE_FINDER___DISABLE:
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
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", distanceAverageLength: ");
    result.append(distanceAverageLength);
    result.append(", velocityAverageLength: ");
    result.append(velocityAverageLength);
    result.append(", mode: ");
    result.append(mode);
    result.append(", enableLaserOnStartup: ");
    result.append(enableLaserOnStartup);
    result.append(')');
    return result.toString();
  }

} //MBrickletLaserRangeFinderImpl
