/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
import org.openhab.binding.tinkerforge.internal.model.Ecosystem;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.HighLowValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickDC;
import com.tinkerforge.BrickServo;
import com.tinkerforge.BrickletAccelerometer;
import com.tinkerforge.BrickletAmbientLight;
import com.tinkerforge.BrickletAmbientLightV2;
import com.tinkerforge.BrickletAnalogIn;
import com.tinkerforge.BrickletAnalogInV2;
import com.tinkerforge.BrickletBarometer;
import com.tinkerforge.BrickletColor;
import com.tinkerforge.BrickletDistanceIR;
import com.tinkerforge.BrickletDistanceUS;
import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletDualRelay;
import com.tinkerforge.BrickletDustDetector;
import com.tinkerforge.BrickletHallEffect;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletIO16;
import com.tinkerforge.BrickletIO4;
import com.tinkerforge.BrickletIndustrialDigitalIn4;
import com.tinkerforge.BrickletIndustrialDigitalOut4;
import com.tinkerforge.BrickletIndustrialDual020mA;
import com.tinkerforge.BrickletIndustrialDualAnalogIn;
import com.tinkerforge.BrickletIndustrialQuadRelay;
import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.BrickletLEDStrip;
import com.tinkerforge.BrickletLaserRangeFinder;
import com.tinkerforge.BrickletLinearPoti;
import com.tinkerforge.BrickletLoadCell;
import com.tinkerforge.BrickletMoisture;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletMultiTouch;
import com.tinkerforge.BrickletPTC;
import com.tinkerforge.BrickletPiezoSpeaker;
import com.tinkerforge.BrickletRemoteSwitch;
import com.tinkerforge.BrickletRotaryEncoder;
import com.tinkerforge.BrickletSegmentDisplay4x7;
import com.tinkerforge.BrickletSolidStateRelay;
import com.tinkerforge.BrickletSoundIntensity;
import com.tinkerforge.BrickletTemperature;
import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.BrickletTilt;
import com.tinkerforge.BrickletVoltageCurrent;
import com.tinkerforge.CryptoException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBrickd</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getHost <em>Host</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getPort <em>Port</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getAuthkey <em>Authkey</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getIsConnected <em>Is Connected</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#isAutoReconnect <em>Auto Reconnect</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#isReconnected <em>Reconnected</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getConnectedCounter <em>Connected Counter</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getTimeout <em>Timeout</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getMdevices <em>Mdevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickdImpl#getEcosystem <em>Ecosystem</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MBrickdImpl extends MinimalEObjectImpl.Container implements MBrickd
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
   * The default value of the '{@link #getHost() <em>Host</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHost()
   * @generated
   * @ordered
   */
  protected static final String HOST_EDEFAULT = "localhost";

  /**
   * The cached value of the '{@link #getHost() <em>Host</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHost()
   * @generated
   * @ordered
   */
  protected String host = HOST_EDEFAULT;

  /**
   * The default value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected static final int PORT_EDEFAULT = 4223;

  /**
   * The cached value of the '{@link #getPort() <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPort()
   * @generated
   * @ordered
   */
  protected int port = PORT_EDEFAULT;

  /**
   * The default value of the '{@link #getAuthkey() <em>Authkey</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAuthkey()
   * @generated
   * @ordered
   */
  protected static final String AUTHKEY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getAuthkey() <em>Authkey</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAuthkey()
   * @generated
   * @ordered
   */
  protected String authkey = AUTHKEY_EDEFAULT;

  /**
   * The default value of the '{@link #getIsConnected() <em>Is Connected</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIsConnected()
   * @generated
   * @ordered
   */
  protected static final HighLowValue IS_CONNECTED_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getIsConnected() <em>Is Connected</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIsConnected()
   * @generated
   * @ordered
   */
  protected HighLowValue isConnected = IS_CONNECTED_EDEFAULT;

  /**
   * The default value of the '{@link #isAutoReconnect() <em>Auto Reconnect</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAutoReconnect()
   * @generated
   * @ordered
   */
  protected static final boolean AUTO_RECONNECT_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isAutoReconnect() <em>Auto Reconnect</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAutoReconnect()
   * @generated
   * @ordered
   */
  protected boolean autoReconnect = AUTO_RECONNECT_EDEFAULT;

  /**
   * The default value of the '{@link #isReconnected() <em>Reconnected</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isReconnected()
   * @generated
   * @ordered
   */
  protected static final boolean RECONNECTED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isReconnected() <em>Reconnected</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isReconnected()
   * @generated
   * @ordered
   */
  protected boolean reconnected = RECONNECTED_EDEFAULT;

  /**
   * The default value of the '{@link #getConnectedCounter() <em>Connected Counter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectedCounter()
   * @generated
   * @ordered
   */
  protected static final DecimalValue CONNECTED_COUNTER_EDEFAULT = (DecimalValue)ModelFactory.eINSTANCE.createFromString(ModelPackage.eINSTANCE.getMDecimalValue(), "0");

  /**
   * The cached value of the '{@link #getConnectedCounter() <em>Connected Counter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConnectedCounter()
   * @generated
   * @ordered
   */
  protected DecimalValue connectedCounter = CONNECTED_COUNTER_EDEFAULT;

  /**
   * The default value of the '{@link #getTimeout() <em>Timeout</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTimeout()
   * @generated
   * @ordered
   */
  protected static final int TIMEOUT_EDEFAULT = 2500;

  /**
   * The cached value of the '{@link #getTimeout() <em>Timeout</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTimeout()
   * @generated
   * @ordered
   */
  protected int timeout = TIMEOUT_EDEFAULT;

  /**
   * The cached value of the '{@link #getMdevices() <em>Mdevices</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMdevices()
   * @generated
   * @ordered
   */
  protected EList<MDevice<?>> mdevices;

  private Thread connectThread;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MBrickdImpl()
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
    return ModelPackage.Literals.MBRICKD;
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__LOGGER, oldLogger, logger));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__IP_CONNECTION, oldIpConnection, ipConnection));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getHost()
  {
    return host;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setHost(String newHost)
  {
    String oldHost = host;
    host = newHost;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__HOST, oldHost, host));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPort()
  {
    return port;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPort(int newPort)
  {
    int oldPort = port;
    port = newPort;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__PORT, oldPort, port));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getAuthkey()
  {
    return authkey;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAuthkey(String newAuthkey)
  {
    String oldAuthkey = authkey;
    authkey = newAuthkey;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__AUTHKEY, oldAuthkey, authkey));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public HighLowValue getIsConnected()
  {
    return isConnected;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsConnected(HighLowValue newIsConnected)
  {
    HighLowValue oldIsConnected = isConnected;
    isConnected = newIsConnected;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__IS_CONNECTED, oldIsConnected, isConnected));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isAutoReconnect()
  {
    return autoReconnect;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAutoReconnect(boolean newAutoReconnect)
  {
    boolean oldAutoReconnect = autoReconnect;
    autoReconnect = newAutoReconnect;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__AUTO_RECONNECT, oldAutoReconnect, autoReconnect));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isReconnected()
  {
    return reconnected;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setReconnected(boolean newReconnected)
  {
    boolean oldReconnected = reconnected;
    reconnected = newReconnected;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__RECONNECTED, oldReconnected, reconnected));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DecimalValue getConnectedCounter()
  {
    return connectedCounter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setConnectedCounter(DecimalValue newConnectedCounter)
  {
    DecimalValue oldConnectedCounter = connectedCounter;
    connectedCounter = newConnectedCounter;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__CONNECTED_COUNTER, oldConnectedCounter, connectedCounter));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getTimeout()
  {
    return timeout;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTimeout(int newTimeout)
  {
    int oldTimeout = timeout;
    timeout = newTimeout;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__TIMEOUT, oldTimeout, timeout));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<MDevice<?>> getMdevices()
  {
    if (mdevices == null)
    {
      mdevices = new EObjectContainmentWithInverseEList<MDevice<?>>(MDevice.class, this, ModelPackage.MBRICKD__MDEVICES, ModelPackage.MDEVICE__BRICKD);
    }
    return mdevices;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Ecosystem getEcosystem()
  {
    if (eContainerFeatureID() != ModelPackage.MBRICKD__ECOSYSTEM) return null;
    return (Ecosystem)eContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEcosystem(Ecosystem newEcosystem, NotificationChain msgs)
  {
    msgs = eBasicSetContainer((InternalEObject)newEcosystem, ModelPackage.MBRICKD__ECOSYSTEM, msgs);
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEcosystem(Ecosystem newEcosystem)
  {
    if (newEcosystem != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MBRICKD__ECOSYSTEM && newEcosystem != null))
    {
      if (EcoreUtil.isAncestor(this, newEcosystem))
        throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
      NotificationChain msgs = null;
      if (eInternalContainer() != null)
        msgs = eBasicRemoveFromContainer(msgs);
      if (newEcosystem != null)
        msgs = ((InternalEObject)newEcosystem).eInverseAdd(this, ModelPackage.ECOSYSTEM__MBRICKDS, Ecosystem.class, msgs);
      msgs = basicSetEcosystem(newEcosystem, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKD__ECOSYSTEM, newEcosystem, newEcosystem));
  }

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void connect() {
		// Create connection to brickd
		final IPConnection ipcon = new IPConnection();
		setIpConnection(ipcon);

		ipConnection.setTimeout(timeout);
		ipConnection.setAutoReconnect(autoReconnect);
		ipConnection.addConnectedListener(new ConnectedListener(ipcon));
		ipConnection.addDisconnectedListener(new DisconnectedListener());
		ipConnection.addEnumerateListener(new EnumerateListener());
		//makeConnect();
		makeConnectThread();
		logger.trace("{} After connect call", LoggerConstants.TFINIT);
	}

  // /**
  // * @see makeConnectThread()
  // *
  // * @generated NOT
  // */
  // private void makeConnect() {
  // try {
  // logger.debug(
  // "trying to establish connection to {}:{}", host, port);
  // ipConnection.connect(getHost(), getPort());
  // } catch (AlreadyConnectedException e) {
  // logger.debug("connect successful: {}:{}", host,
  // port);
  // } catch (ConnectException e) {
  // // lets try it endless: don't set connected to true
  // logger.debug(
  // "connect failed with ConnectionException: {}:{}", host,
  // port);
  // } catch (UnknownHostException e) {
  // logger.error("fatal error: {}", e);
  // } catch (IOException e) {
  // logger.error("connect failed with IOException {}", e);
  // }
  // }

	  /**
   * Connects the ipConnection to the brickd. A thread is used to retry the connection in case of a
   * ConnectExpeption. This is as workaround for an issue in the IpConnection api: If autoReconnect
   * is chosen the reconnect does not work for the connect method call. This call must anyway be
   * successful. Only later disconnects are handled by autoReconnect. If this issue is solved in the
   * upstream api, the makeConnect method of this should be preferred.
   * 
   * @generated NOT
   */
  private void makeConnectThread() {
    connectThread = new Thread() {
      boolean connected = false;
      boolean fatalError = false;

      @Override
      public void run() {
        while (!connected && !fatalError && !isInterrupted()) {
          try {
            logger.trace("trying to establish connection to {}:{}", host, port);
            ipConnection.connect(getHost(), getPort());
          } catch (AlreadyConnectedException e) {
            logger.trace("connect successful: {}:{}", host, port);
            connected = true;
          } catch (ConnectException e) {
            // lets try it endless: don't set connected to true
            logger.debug("connect failed with ConnectionException: {}:{}", host, port);
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e1) {
              logger.debug("connect interrupt recieved: {}:{}", host, port);
              interrupt();
            }
          } catch (UnknownHostException e) {
            // TODO use TinkerforeExceptionHandler
            logger.error("fatal error: {}", e);
            fatalError = true;
          } catch (IOException e) {
            logger.error("connect failed with IOException {}", e);
            try {
              Thread.sleep(1000);
            } catch (InterruptedException e1) {
              logger.debug("connect interrupt recieved: {}:{}", host, port);
              interrupt();
            }
          }
        }
      }
    };
    connectThread.setDaemon(true);
    connectThread.start();
  }

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
  private class ConnectedListener implements IPConnection.ConnectedListener {
    IPConnection ipcon;

    public ConnectedListener(IPConnection ipcon) {
      super();
      this.ipcon = ipcon;
    }

    @Override
    public void connected(short connectReason) {
      logger.debug("{} Connected listener was called.", LoggerConstants.TFINIT);
      if (connectReason == IPConnection.CONNECT_REASON_AUTO_RECONNECT) {
        setReconnected(true);
      }
      try {
        if (authkey != null && !authkey.equals("")) {
          ipcon.authenticate(authkey);
        }
        ipcon.enumerate();
      } catch (TimeoutException e) {
        TinkerforgeErrorHandler.handleError(getLogger(),
            TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
      } catch (CryptoException e) {
        TinkerforgeErrorHandler.handleError(getLogger(),
            TinkerforgeErrorHandler.TF_NOT_CRYPTO_EXCEPTION, e);
      } catch (NotConnectedException e) {
        TinkerforgeErrorHandler.handleError(getLogger(),
            TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
      }
      setIsConnected(HighLowValue.HIGH);
      Integer counternew = connectedCounter.intValue() + 1;
      setConnectedCounter(new DecimalValue(counternew));
    }
  }

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	
	private Lock modelLock = new ReentrantLock(true);
	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	private class DisconnectedListener implements
			IPConnection.DisconnectedListener {

		private String connectReasonString;

		@Override
		public void disconnected(short connectReason) {
			modelLock.lock();
			try {
				setIsConnected(HighLowValue.LOW);
				ArrayList<String> deviceUidList = new ArrayList<String>();
				for (MDevice<?> mDevice : mdevices) {
					deviceUidList.add(mDevice.getUid());
				}
				for (String uid : deviceUidList) {
					removeDevice(uid);
				}
				switch (connectReason) {
				case 0:
					connectReasonString = "request";
					break;
				case 1:
					connectReasonString = "unresolvable problem";
					break;
				case 2:
					connectReasonString = "shutdown";
					break;
				default:
					break;
				}
				logger.debug("disconnected listener was called, caused by: {}",
						connectReasonString);
			} finally {
				modelLock.unlock();
			}
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	private class EnumerateListener implements IPConnection.EnumerateListener {

		@Override
		public void enumerate(String uid, String connectedUid, char position,
				short[] hardwareVersion, short[] firmwareVersion,
				int deviceIdentifier, short enumerationType) {
			logger.debug("{} EnumerateListener was called, type {}",
					LoggerConstants.TFINIT, enumerationType);
			modelLock.lock();
			try {
				if (enumerationType == IPConnection.ENUMERATION_TYPE_DISCONNECTED)
					removeDevice(uid);
				else
					addDevice(uid, connectedUid, deviceIdentifier);
			} finally {
				modelLock.unlock();
			}
		}

	}

	/**
	 * Removes devices which are no longer available. If the device has
	 * sub devices these are removed before removing the device. This gives the
	 * model adapter a chance to handle sub device removal as well.
	 * 
	 * @generated NOT
	 */
	private void removeDevice(String uid) {
		MDevice<?> device = (MDevice<?>) getDevice(uid);
		if (device instanceof MSubDeviceHolder<?>){
			logger.debug("{} removing all subdevices", LoggerConstants.TFINIT);
			@SuppressWarnings("unchecked")
			MSubDeviceHolder<MSubDevice<?>> mSubDeviceHolder = (MSubDeviceHolder<MSubDevice<?>>) device;
			mSubDeviceHolder.getMsubdevices().clear();
		}
		if (device != null) {
			EcoreUtil.remove(device);
		}
		//getMdevices().remove(device);
		logger.debug("{} removeDevice called for uid: {}", LoggerConstants.TFINIT, uid);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	@SuppressWarnings("unchecked")
	private void addDevice(String uid, String connectedUid, int deviceIdentifier) {
		logger.debug("{} addDevice called for uid: {}", LoggerConstants.TFINIT, uid);
		if (getDevice(uid) != null) {
			logger.debug("{} device already exists. uid: {}", LoggerConstants.TFINIT, uid);
		} else {
			ModelFactory factory = ModelFactory.eINSTANCE;
			MDevice<?> mDevice = null;
			if (deviceIdentifier == BrickletTemperature.DEVICE_IDENTIFIER) {
				logger.debug("{} addDevice temperature", LoggerConstants.TFINIT);
				mDevice = factory.createMBrickletTemperature();
				mDevice.setDeviceIdentifier(BrickletTemperature.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickletHumidity.DEVICE_IDENTIFIER) {
				logger.debug("{} addDevice humidity", LoggerConstants.TFINIT);
				mDevice = factory.createMBrickletHumidity();
				mDevice.setDeviceIdentifier(BrickletHumidity.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickServo.DEVICE_IDENTIFIER){
				logger.debug("{} addDevice BrickServo", LoggerConstants.TFINIT);
				mDevice = factory.createMBrickServo();
				mDevice.setDeviceIdentifier(BrickServo.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickletDualRelay.DEVICE_IDENTIFIER){
				logger.debug("{} addDevice BrickletDualRelayBricklet", LoggerConstants.TFINIT);
				mDevice = factory.createMDualRelayBricklet();
				mDevice.setDeviceIdentifier(BrickletDualRelay.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickletIndustrialQuadRelay.DEVICE_IDENTIFIER){
				logger.debug("{} addDevice BrickletIndustrialQuadRelayBricklet", LoggerConstants.TFINIT);
				mDevice = factory.createMIndustrialQuadRelayBricklet();
				mDevice.setDeviceIdentifier(BrickletIndustrialQuadRelay.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickletBarometer.DEVICE_IDENTIFIER){
				logger.debug("{} addDevice BrickletBarometer", LoggerConstants.TFINIT);
				mDevice = factory.createMBrickletBarometer();
				mDevice.setDeviceIdentifier(BrickletBarometer.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickletAmbientLight.DEVICE_IDENTIFIER){
				logger.debug("{} addDevice AmbientLight", LoggerConstants.TFINIT);
				mDevice = factory.createMBrickletAmbientLight();
				mDevice.setDeviceIdentifier(BrickletAmbientLight.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletAmbientLightV2.DEVICE_IDENTIFIER){
              logger.debug("{} addDevice AmbientLighV2", LoggerConstants.TFINIT);
              mDevice = factory.createMBrickletAmbientLightV2();
              mDevice.setDeviceIdentifier(BrickletAmbientLightV2.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickletDistanceIR.DEVICE_IDENTIFIER){
				logger.debug("{} addDevice DistanceIR", LoggerConstants.TFINIT);
				mDevice = factory.createMBrickletDistanceIR();
				mDevice.setDeviceIdentifier(BrickletDistanceIR.DEVICE_IDENTIFIER);		
			} else if (deviceIdentifier == BrickDC.DEVICE_IDENTIFIER) {
				logger.debug("{} addDevice BrickDC", LoggerConstants.TFINIT);
				mDevice = factory.createMBrickDC();
				mDevice.setDeviceIdentifier(BrickDC.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickletLCD20x4.DEVICE_IDENTIFIER){
				logger.debug("addDevice BrickletLCD20x4");
				mDevice = factory.createMBrickletLCD20x4();
				mDevice.setDeviceIdentifier(BrickletLCD20x4.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickletIndustrialDigitalIn4.DEVICE_IDENTIFIER){
				logger.debug("addDevice BrickletIndustrialDigitalIn4");
				mDevice = factory.createMBrickletIndustrialDigitalIn4();
				mDevice.setDeviceIdentifier(BrickletIndustrialDigitalIn4.DEVICE_IDENTIFIER);
			} else if (deviceIdentifier == BrickletIO16.DEVICE_IDENTIFIER){
				logger.debug("addDevice BrickletIO16");
				mDevice = factory.createMBrickletIO16();
				mDevice.setDeviceIdentifier(BrickletIO16.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletRemoteSwitch.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletRemoteSwitch");
              mDevice = factory.createMBrickletRemoteSwitch();
              mDevice.setDeviceIdentifier(BrickletRemoteSwitch.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletMotionDetector.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletMotionDetector");
              mDevice = factory.createMBrickletMotionDetector();
              mDevice.setDeviceIdentifier(BrickletMotionDetector.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletMultiTouch.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletMultiTouch");
              mDevice = factory.createMBrickletMultiTouch();
              mDevice.setDeviceIdentifier(BrickletMultiTouch.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletTemperatureIR.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletTemperatureIR");
              mDevice = factory.createMBrickletTemperatureIR();
              mDevice.setDeviceIdentifier(BrickletTemperatureIR.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletSoundIntensity.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletSoundIntensity");
              mDevice = factory.createMBrickletSoundIntensity();
              mDevice.setDeviceIdentifier(BrickletSoundIntensity.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletMoisture.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletMoisture");
              mDevice = factory.createMBrickletMoisture();
              mDevice.setDeviceIdentifier(BrickletMoisture.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletDistanceUS.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletDistanceUS");
              mDevice = factory.createMBrickletDistanceUS();
              mDevice.setDeviceIdentifier(BrickletDistanceUS.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletVoltageCurrent.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletVoltageCurrent");
              mDevice = factory.createMBrickletVoltageCurrent();
              mDevice.setDeviceIdentifier(BrickletVoltageCurrent.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletTilt.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletTilt");
              mDevice = factory.createMBrickletTilt();
              mDevice.setDeviceIdentifier(BrickletTilt.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletIO4.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletIO4");
              mDevice = factory.createMBrickletIO4();
              mDevice.setDeviceIdentifier(BrickletIO4.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletHallEffect.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletHallEffect");
              mDevice = factory.createMBrickletHallEffect();
              mDevice.setDeviceIdentifier(BrickletHallEffect.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletIndustrialDigitalOut4.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletIndustrilaDigitalOut4");
              mDevice = factory.createMBrickletIndustrialDigitalOut4();
              mDevice.setDeviceIdentifier(BrickletIndustrialDigitalOut4.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletSegmentDisplay4x7.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletSegmentDisplay4x7");
              mDevice = factory.createMBrickletSegmentDisplay4x7();
              mDevice.setDeviceIdentifier(BrickletSegmentDisplay4x7.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletLEDStrip.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletLEDStrip");
              mDevice = factory.createMBrickletLEDStrip();
              mDevice.setDeviceIdentifier(BrickletLEDStrip.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletJoystick.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletJoystick");
              mDevice = factory.createMBrickletJoystick();
              mDevice.setDeviceIdentifier(BrickletJoystick.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletLinearPoti.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletLinearPoti");
              mDevice = factory.createMBrickletLinearPoti();
              mDevice.setDeviceIdentifier(BrickletLinearPoti.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletDualButton.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletDualButton");
              mDevice = factory.createMBrickletDualButton();
              mDevice.setDeviceIdentifier(BrickletDualButton.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletPTC.DEVICE_IDENTIFIER) {
              logger.debug("addDevice BrickletPTC");
              mDevice = factory.createMBrickletPTC();
              mDevice.setDeviceIdentifier(BrickletPTC.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletIndustrialDual020mA.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletIndustrialDual020mA");
              mDevice = factory.createMBrickletIndustrialDual020mA();
              mDevice.setDeviceIdentifier(BrickletIndustrialDual020mA.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletSolidStateRelay.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletSolidStateRelay");
              mDevice = factory.createMBrickletSolidStateRelay();
              mDevice.setDeviceIdentifier(BrickletSolidStateRelay.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletPiezoSpeaker.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletPiezoSpeaker");
              mDevice = factory.createMBrickletPiezoSpeaker();
              mDevice.setDeviceIdentifier(BrickletPiezoSpeaker.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletRotaryEncoder.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletRotaryEncoder");
              mDevice = factory.createMBrickletRotaryEncoder();
              mDevice.setDeviceIdentifier(BrickletRotaryEncoder.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletDustDetector.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletDustDetector");
              mDevice = factory.createMBrickletDustDetector();
              mDevice.setDeviceIdentifier(BrickletDustDetector.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletLoadCell.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletLoadCell");
              mDevice = factory.createMBrickletLoadCell();
              mDevice.setDeviceIdentifier(BrickletLoadCell.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletColor.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletColor");
              mDevice = factory.createMBrickletColor();
              mDevice.setDeviceIdentifier(BrickletColor.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletIndustrialDualAnalogIn.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletIndustrialDualAnalogIn");
              mDevice = factory.createMBrickletIndustrialDualAnalogIn();
              mDevice.setDeviceIdentifier(BrickletIndustrialDualAnalogIn.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletAnalogIn.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletAnalogIn");
              mDevice = factory.createMBrickletAnalogIn();
              mDevice.setDeviceIdentifier(BrickletAnalogIn.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletAnalogInV2.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletAnalogInV2");
              mDevice = factory.createMBrickletAnalogInV2();
              mDevice.setDeviceIdentifier(BrickletAnalogInV2.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletLaserRangeFinder.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletLaserRangeFinder");
              mDevice = factory.createMBrickletLaserRangeFinder();
              mDevice.setDeviceIdentifier(BrickletLaserRangeFinder.DEVICE_IDENTIFIER);
            } else if (deviceIdentifier == BrickletAccelerometer.DEVICE_IDENTIFIER){
              logger.debug("addDevice BrickletAccelerometer");
              mDevice = factory.createMBrickletAccelerometer();
              mDevice.setDeviceIdentifier(BrickletAccelerometer.DEVICE_IDENTIFIER);
            }
			if (mDevice != null) {
				mDevice.setIpConnection(getIpConnection());
				logger.debug("{} addDevice uid: {}", LoggerConstants.TFINIT, uid);
				mDevice.setUid(uid);
				mDevice.setConnectedUid(connectedUid);
				mDevice.init();
				mDevice.setBrickd(this); // be aware this triggers the notifier
				if (mDevice instanceof MSubDeviceHolder){
					logger.debug("{} initSubDevices uid: {}", LoggerConstants.TFINIT, uid);
					((MSubDeviceHolder<MSubDevice<?>>)mDevice).initSubDevices();
				}
			}
		}
	}

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated NOT
   */
	public void disconnect() {
		try {
			ipConnection.disconnect();
			if (connectThread != null)
				connectThread.interrupt();
		} catch (NotConnectedException e) {
			TinkerforgeErrorHandler.handleError(logger,
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
	  logger = LoggerFactory.getLogger(MBrickdImpl.class);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public MBaseDevice getDevice(String uid)
  {
    EList<MDevice<?>> _mdevices = getMdevices();
    for (final MBaseDevice mdevice : _mdevices)
    {
      String _uid = mdevice.getUid();
      if (_uid.equals(uid))
      {
        return mdevice;
      }
    }
    return null;
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
      case ModelPackage.MBRICKD__MDEVICES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getMdevices()).basicAdd(otherEnd, msgs);
      case ModelPackage.MBRICKD__ECOSYSTEM:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetEcosystem((Ecosystem)otherEnd, msgs);
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
      case ModelPackage.MBRICKD__MDEVICES:
        return ((InternalEList<?>)getMdevices()).basicRemove(otherEnd, msgs);
      case ModelPackage.MBRICKD__ECOSYSTEM:
        return basicSetEcosystem(null, msgs);
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
      case ModelPackage.MBRICKD__ECOSYSTEM:
        return eInternalContainer().eInverseRemove(this, ModelPackage.ECOSYSTEM__MBRICKDS, Ecosystem.class, msgs);
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
      case ModelPackage.MBRICKD__LOGGER:
        return getLogger();
      case ModelPackage.MBRICKD__IP_CONNECTION:
        return getIpConnection();
      case ModelPackage.MBRICKD__HOST:
        return getHost();
      case ModelPackage.MBRICKD__PORT:
        return getPort();
      case ModelPackage.MBRICKD__AUTHKEY:
        return getAuthkey();
      case ModelPackage.MBRICKD__IS_CONNECTED:
        return getIsConnected();
      case ModelPackage.MBRICKD__AUTO_RECONNECT:
        return isAutoReconnect();
      case ModelPackage.MBRICKD__RECONNECTED:
        return isReconnected();
      case ModelPackage.MBRICKD__CONNECTED_COUNTER:
        return getConnectedCounter();
      case ModelPackage.MBRICKD__TIMEOUT:
        return getTimeout();
      case ModelPackage.MBRICKD__MDEVICES:
        return getMdevices();
      case ModelPackage.MBRICKD__ECOSYSTEM:
        return getEcosystem();
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
      case ModelPackage.MBRICKD__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MBRICKD__IP_CONNECTION:
        setIpConnection((IPConnection)newValue);
        return;
      case ModelPackage.MBRICKD__HOST:
        setHost((String)newValue);
        return;
      case ModelPackage.MBRICKD__PORT:
        setPort((Integer)newValue);
        return;
      case ModelPackage.MBRICKD__AUTHKEY:
        setAuthkey((String)newValue);
        return;
      case ModelPackage.MBRICKD__IS_CONNECTED:
        setIsConnected((HighLowValue)newValue);
        return;
      case ModelPackage.MBRICKD__AUTO_RECONNECT:
        setAutoReconnect((Boolean)newValue);
        return;
      case ModelPackage.MBRICKD__RECONNECTED:
        setReconnected((Boolean)newValue);
        return;
      case ModelPackage.MBRICKD__CONNECTED_COUNTER:
        setConnectedCounter((DecimalValue)newValue);
        return;
      case ModelPackage.MBRICKD__TIMEOUT:
        setTimeout((Integer)newValue);
        return;
      case ModelPackage.MBRICKD__MDEVICES:
        getMdevices().clear();
        getMdevices().addAll((Collection<? extends MDevice<?>>)newValue);
        return;
      case ModelPackage.MBRICKD__ECOSYSTEM:
        setEcosystem((Ecosystem)newValue);
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
      case ModelPackage.MBRICKD__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__IP_CONNECTION:
        setIpConnection(IP_CONNECTION_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__HOST:
        setHost(HOST_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__PORT:
        setPort(PORT_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__AUTHKEY:
        setAuthkey(AUTHKEY_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__IS_CONNECTED:
        setIsConnected(IS_CONNECTED_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__AUTO_RECONNECT:
        setAutoReconnect(AUTO_RECONNECT_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__RECONNECTED:
        setReconnected(RECONNECTED_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__CONNECTED_COUNTER:
        setConnectedCounter(CONNECTED_COUNTER_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__TIMEOUT:
        setTimeout(TIMEOUT_EDEFAULT);
        return;
      case ModelPackage.MBRICKD__MDEVICES:
        getMdevices().clear();
        return;
      case ModelPackage.MBRICKD__ECOSYSTEM:
        setEcosystem((Ecosystem)null);
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
      case ModelPackage.MBRICKD__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MBRICKD__IP_CONNECTION:
        return IP_CONNECTION_EDEFAULT == null ? ipConnection != null : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
      case ModelPackage.MBRICKD__HOST:
        return HOST_EDEFAULT == null ? host != null : !HOST_EDEFAULT.equals(host);
      case ModelPackage.MBRICKD__PORT:
        return port != PORT_EDEFAULT;
      case ModelPackage.MBRICKD__AUTHKEY:
        return AUTHKEY_EDEFAULT == null ? authkey != null : !AUTHKEY_EDEFAULT.equals(authkey);
      case ModelPackage.MBRICKD__IS_CONNECTED:
        return IS_CONNECTED_EDEFAULT == null ? isConnected != null : !IS_CONNECTED_EDEFAULT.equals(isConnected);
      case ModelPackage.MBRICKD__AUTO_RECONNECT:
        return autoReconnect != AUTO_RECONNECT_EDEFAULT;
      case ModelPackage.MBRICKD__RECONNECTED:
        return reconnected != RECONNECTED_EDEFAULT;
      case ModelPackage.MBRICKD__CONNECTED_COUNTER:
        return CONNECTED_COUNTER_EDEFAULT == null ? connectedCounter != null : !CONNECTED_COUNTER_EDEFAULT.equals(connectedCounter);
      case ModelPackage.MBRICKD__TIMEOUT:
        return timeout != TIMEOUT_EDEFAULT;
      case ModelPackage.MBRICKD__MDEVICES:
        return mdevices != null && !mdevices.isEmpty();
      case ModelPackage.MBRICKD__ECOSYSTEM:
        return getEcosystem() != null;
    }
    return super.eIsSet(featureID);
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
      case ModelPackage.MBRICKD___CONNECT:
        connect();
        return null;
      case ModelPackage.MBRICKD___DISCONNECT:
        disconnect();
        return null;
      case ModelPackage.MBRICKD___INIT:
        init();
        return null;
      case ModelPackage.MBRICKD___GET_DEVICE__STRING:
        return getDevice((String)arguments.get(0));
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
    result.append(", ipConnection: ");
    result.append(ipConnection);
    result.append(", host: ");
    result.append(host);
    result.append(", port: ");
    result.append(port);
    result.append(", authkey: ");
    result.append(authkey);
    result.append(", isConnected: ");
    result.append(isConnected);
    result.append(", autoReconnect: ");
    result.append(autoReconnect);
    result.append(", reconnected: ");
    result.append(reconnected);
    result.append(", connectedCounter: ");
    result.append(connectedCounter);
    result.append(", timeout: ");
    result.append(timeout);
    result.append(')');
    return result.toString();
  }

} //MBrickdImpl
