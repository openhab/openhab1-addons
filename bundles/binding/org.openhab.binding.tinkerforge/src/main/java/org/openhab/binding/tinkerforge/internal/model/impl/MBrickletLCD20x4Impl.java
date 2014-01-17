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
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLCD20x4;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Backlight;
import org.openhab.binding.tinkerforge.internal.model.MLCD20x4Button;
import org.openhab.binding.tinkerforge.internal.model.MLCDSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTextActor;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletLCD20x4;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc --> 
 * An implementation of the model object '<em><b>MBricklet LCD2 0x4</b></em>'. 
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getLogger <em>Logger</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getUid <em>Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getEnabledA <em>Enabled A</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getTinkerforgeDevice <em>Tinkerforge Device</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getIpConnection <em>Ip Connection</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getConnectedUid <em>Connected Uid</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getPosition <em>Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getDeviceIdentifier <em>Device Identifier</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getName <em>Name</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getBrickd <em>Brickd</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getText <em>Text</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getMsubdevices <em>Msubdevices</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getPositionPrefix <em>Position Prefix</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getPositonSuffix <em>Positon Suffix</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#isDisplayErrors <em>Display Errors</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLCD20x4Impl#getErrorPrefix <em>Error Prefix</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MBrickletLCD20x4Impl extends MinimalEObjectImpl.Container
		implements MBrickletLCD20x4 {
	/**
   * The default value of the '{@link #getLogger() <em>Logger</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getLogger()
   * @generated
   * @ordered
   */
	protected static final Logger LOGGER_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getLogger() <em>Logger</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getLogger()
   * @generated
   * @ordered
   */
	protected Logger logger = LOGGER_EDEFAULT;

	/**
	 * The default value of the '{@link #getUid() <em>Uid</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUid()
	 * @generated
	 * @ordered
	 */
	protected static final String UID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUid() <em>Uid</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getUid()
	 * @generated
	 * @ordered
	 */
	protected String uid = UID_EDEFAULT;

	/**
   * The default value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getEnabledA()
   * @generated
   * @ordered
   */
	protected static final AtomicBoolean ENABLED_A_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getEnabledA()
   * @generated
   * @ordered
   */
	protected AtomicBoolean enabledA = ENABLED_A_EDEFAULT;

	/**
   * The cached value of the '{@link #getTinkerforgeDevice() <em>Tinkerforge Device</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getTinkerforgeDevice()
   * @generated
   * @ordered
   */
	protected BrickletLCD20x4 tinkerforgeDevice;

	/**
   * The default value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getIpConnection()
   * @generated
   * @ordered
   */
	protected static final IPConnection IP_CONNECTION_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getIpConnection()
   * @generated
   * @ordered
   */
	protected IPConnection ipConnection = IP_CONNECTION_EDEFAULT;

	/**
   * The default value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getConnectedUid()
   * @generated
   * @ordered
   */
	protected static final String CONNECTED_UID_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getConnectedUid()
   * @generated
   * @ordered
   */
	protected String connectedUid = CONNECTED_UID_EDEFAULT;

	/**
   * The default value of the '{@link #getPosition() <em>Position</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getPosition()
   * @generated
   * @ordered
   */
	protected static final char POSITION_EDEFAULT = '\u0000';

	/**
   * The cached value of the '{@link #getPosition() <em>Position</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getPosition()
   * @generated
   * @ordered
   */
	protected char position = POSITION_EDEFAULT;

	/**
   * The default value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getDeviceIdentifier()
   * @generated
   * @ordered
   */
	protected static final int DEVICE_IDENTIFIER_EDEFAULT = 0;

	/**
   * The cached value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getDeviceIdentifier()
   * @generated
   * @ordered
   */
	protected int deviceIdentifier = DEVICE_IDENTIFIER_EDEFAULT;

	/**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
	protected static final String NAME_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
	protected String name = NAME_EDEFAULT;

	/**
   * The default value of the '{@link #getText() <em>Text</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getText()
   * @generated
   * @ordered
   */
	protected static final String TEXT_EDEFAULT = null;

	/**
   * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getText()
   * @generated
   * @ordered
   */
	protected String text = TEXT_EDEFAULT;

	/**
   * The cached value of the '{@link #getMsubdevices() <em>Msubdevices</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMsubdevices()
   * @generated
   * @ordered
   */
  protected EList<MLCDSubDevice> msubdevices;

  /**
   * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDeviceType()
   * @generated
   * @ordered
   */
  protected static final String DEVICE_TYPE_EDEFAULT = "bricklet_LCD20x4";

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
   * The default value of the '{@link #getPositionPrefix() <em>Position Prefix</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getPositionPrefix()
   * @generated
   * @ordered
   */
	protected static final String POSITION_PREFIX_EDEFAULT = "TFNUM<";

	/**
   * The cached value of the '{@link #getPositionPrefix() <em>Position Prefix</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getPositionPrefix()
   * @generated
   * @ordered
   */
	protected String positionPrefix = POSITION_PREFIX_EDEFAULT;

	/**
   * The default value of the '{@link #getPositonSuffix() <em>Positon Suffix</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getPositonSuffix()
   * @generated
   * @ordered
   */
	protected static final String POSITON_SUFFIX_EDEFAULT = ">";

	/**
   * The cached value of the '{@link #getPositonSuffix() <em>Positon Suffix</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #getPositonSuffix()
   * @generated
   * @ordered
   */
	protected String positonSuffix = POSITON_SUFFIX_EDEFAULT;

	/**
   * The default value of the '{@link #isDisplayErrors() <em>Display Errors</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #isDisplayErrors()
   * @generated
   * @ordered
   */
	protected static final boolean DISPLAY_ERRORS_EDEFAULT = true;

	/**
   * The cached value of the '{@link #isDisplayErrors() <em>Display Errors</em>}' attribute.
   * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
   * @see #isDisplayErrors()
   * @generated
   * @ordered
   */
	protected boolean displayErrors = DISPLAY_ERRORS_EDEFAULT;

	/**
   * The default value of the '{@link #getErrorPrefix() <em>Error Prefix</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getErrorPrefix()
   * @generated
   * @ordered
   */
	protected static final String ERROR_PREFIX_EDEFAULT = "openhab Error:";

	/**
   * The cached value of the '{@link #getErrorPrefix() <em>Error Prefix</em>}' attribute.
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @see #getErrorPrefix()
   * @generated
   * @ordered
   */
	protected String errorPrefix = ERROR_PREFIX_EDEFAULT;

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	protected MBrickletLCD20x4Impl() {
    super();
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	protected EClass eStaticClass() {
    return ModelPackage.Literals.MBRICKLET_LCD2_0X4;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public Logger getLogger() {
    return logger;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setLogger(Logger newLogger) {
    Logger oldLogger = logger;
    logger = newLogger;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__LOGGER, oldLogger, logger));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public String getUid() {
    return uid;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setUid(String newUid) {
    String oldUid = uid;
    uid = newUid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__UID, oldUid, uid));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public AtomicBoolean getEnabledA() {
    return enabledA;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setEnabledA(AtomicBoolean newEnabledA) {
    AtomicBoolean oldEnabledA = enabledA;
    enabledA = newEnabledA;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__ENABLED_A, oldEnabledA, enabledA));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public BrickletLCD20x4 getTinkerforgeDevice() {
    return tinkerforgeDevice;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setTinkerforgeDevice(BrickletLCD20x4 newTinkerforgeDevice) {
    BrickletLCD20x4 oldTinkerforgeDevice = tinkerforgeDevice;
    tinkerforgeDevice = newTinkerforgeDevice;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__TINKERFORGE_DEVICE, oldTinkerforgeDevice, tinkerforgeDevice));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public IPConnection getIpConnection() {
    return ipConnection;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setIpConnection(IPConnection newIpConnection) {
    IPConnection oldIpConnection = ipConnection;
    ipConnection = newIpConnection;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__IP_CONNECTION, oldIpConnection, ipConnection));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public String getConnectedUid() {
    return connectedUid;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setConnectedUid(String newConnectedUid) {
    String oldConnectedUid = connectedUid;
    connectedUid = newConnectedUid;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__CONNECTED_UID, oldConnectedUid, connectedUid));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public char getPosition() {
    return position;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setPosition(char newPosition) {
    char oldPosition = position;
    position = newPosition;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__POSITION, oldPosition, position));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public int getDeviceIdentifier() {
    return deviceIdentifier;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setDeviceIdentifier(int newDeviceIdentifier) {
    int oldDeviceIdentifier = deviceIdentifier;
    deviceIdentifier = newDeviceIdentifier;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__DEVICE_IDENTIFIER, oldDeviceIdentifier, deviceIdentifier));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public String getName() {
    return name;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setName(String newName) {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__NAME, oldName, name));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public MBrickd getBrickd() {
    if (eContainerFeatureID() != ModelPackage.MBRICKLET_LCD2_0X4__BRICKD) return null;
    return (MBrickd)eContainer();
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public NotificationChain basicSetBrickd(MBrickd newBrickd,
			NotificationChain msgs) {
    msgs = eBasicSetContainer((InternalEObject)newBrickd, ModelPackage.MBRICKLET_LCD2_0X4__BRICKD, msgs);
    return msgs;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setBrickd(MBrickd newBrickd) {
    if (newBrickd != eInternalContainer() || (eContainerFeatureID() != ModelPackage.MBRICKLET_LCD2_0X4__BRICKD && newBrickd != null))
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__BRICKD, newBrickd, newBrickd));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public String getText() {
    return text;
  }

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	private void displayError(String msg) {
		if (displayErrors) {
			try {
				tinkerforgeDevice.clearDisplay();
				tinkerforgeDevice.writeLine((short) 0, (short) 0, errorPrefix);
				tinkerforgeDevice.writeLine((short) 1, (short) 0, msg);
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void setText(String newText) {
		String oldText = text;
		text = newText;
		// TODO check if there are changes
		logger.debug("setText: " + newText);
		try {
			if (text == null || text.equals(""))
				tinkerforgeDevice.clearDisplay();
			else if (text.startsWith(positionPrefix)) {
				int indexOfSuffix = text.indexOf(positonSuffix);
				if (indexOfSuffix == -1
						|| indexOfSuffix > positionPrefix.length() + 3) {
					logger.error("no valid suffix found");
					displayError("no valid suffix");
				} else {

					try {
						short lineNum = (short) Integer.parseInt(text
								.substring(positionPrefix.length(),
										positionPrefix.length() + 1));
						short inLinePostition = (short) Integer.parseInt(text
								.substring(positionPrefix.length() + 1,
										indexOfSuffix));
						if (lineNum < 0 || lineNum > 3) {
							logger.error("line number must have a value from 0 - 3");
							displayError("faulty line number");
						} else if (inLinePostition < 0 || inLinePostition > 19) {
							logger.error("position must have a value from 0 - 19");
							displayError("faulty position number");
						} else {
							String text2show = text
									.substring(indexOfSuffix + 1);
							if (text2show.length() == 0)
								text2show = "                    "; // TODO
																	// clear
																	// line
							tinkerforgeDevice.writeLine(lineNum,
									inLinePostition, text2show);
						}
					} catch (NumberFormatException e) {
						logger.error("found erroneous line numbers: ", e);
						displayError("faulty numbers");
					}
				}
			} else {
				tinkerforgeDevice.clearDisplay();
				int lineSize = 19;
				int lineNum = 0;
				for (int i = 0; i < text.length(); i += lineSize) {
					if (lineNum > 3)
						break;
					tinkerforgeDevice.writeLine(
							(short) lineNum,
							(short) 0,
							text.substring(i,
									Math.min(text.length(), i + lineSize)));
					lineNum++;
				}

			}
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET,
						ModelPackage.MBRICKLET_LCD2_0X4__TEXT, oldText, text));

		} catch (TimeoutException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
		} catch (NotConnectedException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
		}   	
	}

	/**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<MLCDSubDevice> getMsubdevices()
  {
    if (msubdevices == null)
    {
      msubdevices = new EObjectContainmentWithInverseEList<MLCDSubDevice>(MSubDevice.class, this, ModelPackage.MBRICKLET_LCD2_0X4__MSUBDEVICES, ModelPackage.MSUB_DEVICE__MBRICK);
    }
    return msubdevices;
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
   * @generated
   */
	public String getPositionPrefix() {
    return positionPrefix;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setPositionPrefix(String newPositionPrefix) {
    String oldPositionPrefix = positionPrefix;
    positionPrefix = newPositionPrefix;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__POSITION_PREFIX, oldPositionPrefix, positionPrefix));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public String getPositonSuffix() {
    return positonSuffix;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setPositonSuffix(String newPositonSuffix) {
    String oldPositonSuffix = positonSuffix;
    positonSuffix = newPositonSuffix;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__POSITON_SUFFIX, oldPositonSuffix, positonSuffix));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public boolean isDisplayErrors() {
    return displayErrors;
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public void setDisplayErrors(boolean newDisplayErrors) {
    boolean oldDisplayErrors = displayErrors;
    displayErrors = newDisplayErrors;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LCD2_0X4__DISPLAY_ERRORS, oldDisplayErrors, displayErrors));
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	public String getErrorPrefix() {
    return errorPrefix;
  }

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void init() {
		setEnabledA(new AtomicBoolean());
		logger = LoggerFactory.getLogger(MBrickletLCD20x4Impl.class);
	}

	/**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void initSubDevices()
  {
		ModelFactory factory = ModelFactory.eINSTANCE;
		for (int i = 0; i < 4; i++) {
			MLCD20x4Button mButton = factory.createMLCD20x4Button();
			mButton.setUid(uid);
			String subId = "button" + String.valueOf(i);
			logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, subId);
			mButton.setSubId(subId);
			mButton.init();
			mButton.setMbrick(this);
		}
		MLCD20x4Backlight backlight = factory.createMLCD20x4Backlight();
		backlight.setUid(uid);
		String subId = "backlight";
		logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, subId);
		backlight.setSubId(subId);
		backlight.init();
		backlight.setMbrick(this);
  }

  /**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NOT
	 */
	public void enable() {
		tinkerforgeDevice = new BrickletLCD20x4(uid, ipConnection);
		try {
			tinkerforgeDevice.clearDisplay();
		} catch (TimeoutException e) {
			TinkerforgeErrorHandler.handleError(this,
					TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
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
	public void disable() {
		tinkerforgeDevice = null;
	}

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@SuppressWarnings("unchecked")
  @Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_LCD2_0X4__BRICKD:
        if (eInternalContainer() != null)
          msgs = eBasicRemoveFromContainer(msgs);
        return basicSetBrickd((MBrickd)otherEnd, msgs);
      case ModelPackage.MBRICKLET_LCD2_0X4__MSUBDEVICES:
        return ((InternalEList<InternalEObject>)(InternalEList<?>)getMsubdevices()).basicAdd(otherEnd, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd,
			int featureID, NotificationChain msgs) {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_LCD2_0X4__BRICKD:
        return basicSetBrickd(null, msgs);
      case ModelPackage.MBRICKLET_LCD2_0X4__MSUBDEVICES:
        return ((InternalEList<?>)getMsubdevices()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(
			NotificationChain msgs) {
    switch (eContainerFeatureID())
    {
      case ModelPackage.MBRICKLET_LCD2_0X4__BRICKD:
        return eInternalContainer().eInverseRemove(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_LCD2_0X4__LOGGER:
        return getLogger();
      case ModelPackage.MBRICKLET_LCD2_0X4__UID:
        return getUid();
      case ModelPackage.MBRICKLET_LCD2_0X4__ENABLED_A:
        return getEnabledA();
      case ModelPackage.MBRICKLET_LCD2_0X4__TINKERFORGE_DEVICE:
        return getTinkerforgeDevice();
      case ModelPackage.MBRICKLET_LCD2_0X4__IP_CONNECTION:
        return getIpConnection();
      case ModelPackage.MBRICKLET_LCD2_0X4__CONNECTED_UID:
        return getConnectedUid();
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITION:
        return getPosition();
      case ModelPackage.MBRICKLET_LCD2_0X4__DEVICE_IDENTIFIER:
        return getDeviceIdentifier();
      case ModelPackage.MBRICKLET_LCD2_0X4__NAME:
        return getName();
      case ModelPackage.MBRICKLET_LCD2_0X4__BRICKD:
        return getBrickd();
      case ModelPackage.MBRICKLET_LCD2_0X4__TEXT:
        return getText();
      case ModelPackage.MBRICKLET_LCD2_0X4__MSUBDEVICES:
        return getMsubdevices();
      case ModelPackage.MBRICKLET_LCD2_0X4__DEVICE_TYPE:
        return getDeviceType();
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITION_PREFIX:
        return getPositionPrefix();
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITON_SUFFIX:
        return getPositonSuffix();
      case ModelPackage.MBRICKLET_LCD2_0X4__DISPLAY_ERRORS:
        return isDisplayErrors();
      case ModelPackage.MBRICKLET_LCD2_0X4__ERROR_PREFIX:
        return getErrorPrefix();
    }
    return super.eGet(featureID, resolve, coreType);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@SuppressWarnings("unchecked")
  @Override
	public void eSet(int featureID, Object newValue) {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_LCD2_0X4__LOGGER:
        setLogger((Logger)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__UID:
        setUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__ENABLED_A:
        setEnabledA((AtomicBoolean)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletLCD20x4)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__IP_CONNECTION:
        setIpConnection((IPConnection)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__CONNECTED_UID:
        setConnectedUid((String)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITION:
        setPosition((Character)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__DEVICE_IDENTIFIER:
        setDeviceIdentifier((Integer)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__NAME:
        setName((String)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__BRICKD:
        setBrickd((MBrickd)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__TEXT:
        setText((String)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__MSUBDEVICES:
        getMsubdevices().clear();
        getMsubdevices().addAll((Collection<? extends MLCDSubDevice>)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITION_PREFIX:
        setPositionPrefix((String)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITON_SUFFIX:
        setPositonSuffix((String)newValue);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__DISPLAY_ERRORS:
        setDisplayErrors((Boolean)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	public void eUnset(int featureID) {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_LCD2_0X4__LOGGER:
        setLogger(LOGGER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__UID:
        setUid(UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__ENABLED_A:
        setEnabledA(ENABLED_A_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__TINKERFORGE_DEVICE:
        setTinkerforgeDevice((BrickletLCD20x4)null);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__IP_CONNECTION:
        setIpConnection(IP_CONNECTION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__CONNECTED_UID:
        setConnectedUid(CONNECTED_UID_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITION:
        setPosition(POSITION_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__DEVICE_IDENTIFIER:
        setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__NAME:
        setName(NAME_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__BRICKD:
        setBrickd((MBrickd)null);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__TEXT:
        setText(TEXT_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__MSUBDEVICES:
        getMsubdevices().clear();
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITION_PREFIX:
        setPositionPrefix(POSITION_PREFIX_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITON_SUFFIX:
        setPositonSuffix(POSITON_SUFFIX_EDEFAULT);
        return;
      case ModelPackage.MBRICKLET_LCD2_0X4__DISPLAY_ERRORS:
        setDisplayErrors(DISPLAY_ERRORS_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	public boolean eIsSet(int featureID) {
    switch (featureID)
    {
      case ModelPackage.MBRICKLET_LCD2_0X4__LOGGER:
        return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
      case ModelPackage.MBRICKLET_LCD2_0X4__UID:
        return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
      case ModelPackage.MBRICKLET_LCD2_0X4__ENABLED_A:
        return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
      case ModelPackage.MBRICKLET_LCD2_0X4__TINKERFORGE_DEVICE:
        return tinkerforgeDevice != null;
      case ModelPackage.MBRICKLET_LCD2_0X4__IP_CONNECTION:
        return IP_CONNECTION_EDEFAULT == null ? ipConnection != null : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
      case ModelPackage.MBRICKLET_LCD2_0X4__CONNECTED_UID:
        return CONNECTED_UID_EDEFAULT == null ? connectedUid != null : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITION:
        return position != POSITION_EDEFAULT;
      case ModelPackage.MBRICKLET_LCD2_0X4__DEVICE_IDENTIFIER:
        return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
      case ModelPackage.MBRICKLET_LCD2_0X4__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case ModelPackage.MBRICKLET_LCD2_0X4__BRICKD:
        return getBrickd() != null;
      case ModelPackage.MBRICKLET_LCD2_0X4__TEXT:
        return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
      case ModelPackage.MBRICKLET_LCD2_0X4__MSUBDEVICES:
        return msubdevices != null && !msubdevices.isEmpty();
      case ModelPackage.MBRICKLET_LCD2_0X4__DEVICE_TYPE:
        return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITION_PREFIX:
        return POSITION_PREFIX_EDEFAULT == null ? positionPrefix != null : !POSITION_PREFIX_EDEFAULT.equals(positionPrefix);
      case ModelPackage.MBRICKLET_LCD2_0X4__POSITON_SUFFIX:
        return POSITON_SUFFIX_EDEFAULT == null ? positonSuffix != null : !POSITON_SUFFIX_EDEFAULT.equals(positonSuffix);
      case ModelPackage.MBRICKLET_LCD2_0X4__DISPLAY_ERRORS:
        return displayErrors != DISPLAY_ERRORS_EDEFAULT;
      case ModelPackage.MBRICKLET_LCD2_0X4__ERROR_PREFIX:
        return ERROR_PREFIX_EDEFAULT == null ? errorPrefix != null : !ERROR_PREFIX_EDEFAULT.equals(errorPrefix);
    }
    return super.eIsSet(featureID);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
    if (baseClass == MTextActor.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICKLET_LCD2_0X4__TEXT: return ModelPackage.MTEXT_ACTOR__TEXT;
        default: return -1;
      }
    }
    if (baseClass == MSubDeviceHolder.class)
    {
      switch (derivedFeatureID)
      {
        case ModelPackage.MBRICKLET_LCD2_0X4__MSUBDEVICES: return ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES;
        default: return -1;
      }
    }
    return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
    if (baseClass == MTextActor.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MTEXT_ACTOR__TEXT: return ModelPackage.MBRICKLET_LCD2_0X4__TEXT;
        default: return -1;
      }
    }
    if (baseClass == MSubDeviceHolder.class)
    {
      switch (baseFeatureID)
      {
        case ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES: return ModelPackage.MBRICKLET_LCD2_0X4__MSUBDEVICES;
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
    if (baseClass == MTextActor.class)
    {
      switch (baseOperationID)
      {
        default: return -1;
      }
    }
    if (baseClass == MSubDeviceHolder.class)
    {
      switch (baseOperationID)
      {
        case ModelPackage.MSUB_DEVICE_HOLDER___INIT_SUB_DEVICES: return ModelPackage.MBRICKLET_LCD2_0X4___INIT_SUB_DEVICES;
        default: return -1;
      }
    }
    return super.eDerivedOperationID(baseOperationID, baseClass);
  }

  /**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments)
			throws InvocationTargetException {
    switch (operationID)
    {
      case ModelPackage.MBRICKLET_LCD2_0X4___INIT:
        init();
        return null;
      case ModelPackage.MBRICKLET_LCD2_0X4___INIT_SUB_DEVICES:
        initSubDevices();
        return null;
      case ModelPackage.MBRICKLET_LCD2_0X4___ENABLE:
        enable();
        return null;
      case ModelPackage.MBRICKLET_LCD2_0X4___DISABLE:
        disable();
        return null;
    }
    return super.eInvoke(operationID, arguments);
  }

	/**
   * <!-- begin-user-doc --> <!-- end-user-doc -->
   * @generated
   */
	@Override
	public String toString() {
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
    result.append(", text: ");
    result.append(text);
    result.append(", deviceType: ");
    result.append(deviceType);
    result.append(", positionPrefix: ");
    result.append(positionPrefix);
    result.append(", positonSuffix: ");
    result.append(positonSuffix);
    result.append(", displayErrors: ");
    result.append(displayErrors);
    result.append(", errorPrefix: ");
    result.append(errorPrefix);
    result.append(')');
    return result.toString();
  }

} // MBrickletLCD20x4Impl
