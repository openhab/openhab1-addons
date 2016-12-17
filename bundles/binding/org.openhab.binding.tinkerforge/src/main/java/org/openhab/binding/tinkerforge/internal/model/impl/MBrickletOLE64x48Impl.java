/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
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
import org.openhab.binding.tinkerforge.internal.TinkerforgeErrorHandler;
import org.openhab.binding.tinkerforge.internal.model.BrickletOLEDConfiguration;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletOLE64x48;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.MTextActor;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.tools.LinePositionText;
import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.tools.Tools.LinePositionParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletOLED64x48;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBricklet OLE6 4x48</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getPositionPrefix <em>Position
 * Prefix</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getPositionSuffix <em>Position
 * Suffix</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getContrast <em>Contrast</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#isInvert <em>Invert</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getLogger <em>Logger</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getUid <em>Uid</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#isPoll <em>Poll</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getEnabledA <em>Enabled A</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getTinkerforgeDevice
 * <em>Tinkerforge Device</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getIpConnection <em>Ip
 * Connection</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getConnectedUid <em>Connected
 * Uid</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getPosition <em>Position</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getDeviceIdentifier <em>Device
 * Identifier</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getName <em>Name</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getBrickd <em>Brickd</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getText <em>Text</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getTfConfig <em>Tf Config</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletOLE64x48Impl#getDeviceType <em>Device
 * Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MBrickletOLE64x48Impl extends MinimalEObjectImpl.Container implements MBrickletOLE64x48 {
    /**
     * The default value of the '{@link #getPositionPrefix() <em>Position Prefix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getPositionPrefix()
     * @generated
     * @ordered
     */
    protected static final String POSITION_PREFIX_EDEFAULT = "TFNUM<";

    /**
     * The cached value of the '{@link #getPositionPrefix() <em>Position Prefix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getPositionPrefix()
     * @generated
     * @ordered
     */
    protected String positionPrefix = POSITION_PREFIX_EDEFAULT;

    /**
     * The default value of the '{@link #getPositionSuffix() <em>Position Suffix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getPositionSuffix()
     * @generated
     * @ordered
     */
    protected static final String POSITION_SUFFIX_EDEFAULT = ">";

    /**
     * The cached value of the '{@link #getPositionSuffix() <em>Position Suffix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getPositionSuffix()
     * @generated
     * @ordered
     */
    protected String positionSuffix = POSITION_SUFFIX_EDEFAULT;

    /**
     * The default value of the '{@link #getContrast() <em>Contrast</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getContrast()
     * @generated
     * @ordered
     */
    protected static final short CONTRAST_EDEFAULT = 143;

    /**
     * The cached value of the '{@link #getContrast() <em>Contrast</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getContrast()
     * @generated
     * @ordered
     */
    protected short contrast = CONTRAST_EDEFAULT;

    /**
     * The default value of the '{@link #isInvert() <em>Invert</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isInvert()
     * @generated
     * @ordered
     */
    protected static final boolean INVERT_EDEFAULT = false;

    private static final short maxColumn = 63;

    private static final short maxLine = 5;

    /**
     * The cached value of the '{@link #isInvert() <em>Invert</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isInvert()
     * @generated
     * @ordered
     */
    protected boolean invert = INVERT_EDEFAULT;

    /**
     * The default value of the '{@link #getLogger() <em>Logger</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getLogger()
     * @generated
     * @ordered
     */
    protected static final Logger LOGGER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLogger() <em>Logger</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getLogger()
     * @generated
     * @ordered
     */
    protected Logger logger = LOGGER_EDEFAULT;

    /**
     * The default value of the '{@link #getUid() <em>Uid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getUid()
     * @generated
     * @ordered
     */
    protected static final String UID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getUid() <em>Uid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getUid()
     * @generated
     * @ordered
     */
    protected String uid = UID_EDEFAULT;

    /**
     * The default value of the '{@link #isPoll() <em>Poll</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isPoll()
     * @generated
     * @ordered
     */
    protected static final boolean POLL_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isPoll() <em>Poll</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #isPoll()
     * @generated
     * @ordered
     */
    protected boolean poll = POLL_EDEFAULT;

    /**
     * The default value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getEnabledA()
     * @generated
     * @ordered
     */
    protected static final AtomicBoolean ENABLED_A_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getEnabledA() <em>Enabled A</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getEnabledA()
     * @generated
     * @ordered
     */
    protected AtomicBoolean enabledA = ENABLED_A_EDEFAULT;

    /**
     * The cached value of the '{@link #getTinkerforgeDevice() <em>Tinkerforge Device</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getTinkerforgeDevice()
     * @generated
     * @ordered
     */
    protected BrickletOLED64x48 tinkerforgeDevice;

    /**
     * The default value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getIpConnection()
     * @generated
     * @ordered
     */
    protected static final IPConnection IP_CONNECTION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getIpConnection() <em>Ip Connection</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getIpConnection()
     * @generated
     * @ordered
     */
    protected IPConnection ipConnection = IP_CONNECTION_EDEFAULT;

    /**
     * The default value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getConnectedUid()
     * @generated
     * @ordered
     */
    protected static final String CONNECTED_UID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getConnectedUid() <em>Connected Uid</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getConnectedUid()
     * @generated
     * @ordered
     */
    protected String connectedUid = CONNECTED_UID_EDEFAULT;

    /**
     * The default value of the '{@link #getPosition() <em>Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getPosition()
     * @generated
     * @ordered
     */
    protected static final char POSITION_EDEFAULT = '\u0000';

    /**
     * The cached value of the '{@link #getPosition() <em>Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getPosition()
     * @generated
     * @ordered
     */
    protected char position = POSITION_EDEFAULT;

    /**
     * The default value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getDeviceIdentifier()
     * @generated
     * @ordered
     */
    protected static final int DEVICE_IDENTIFIER_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getDeviceIdentifier() <em>Device Identifier</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getDeviceIdentifier()
     * @generated
     * @ordered
     */
    protected int deviceIdentifier = DEVICE_IDENTIFIER_EDEFAULT;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getText() <em>Text</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getText()
     * @generated
     * @ordered
     */
    protected static final String TEXT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getText() <em>Text</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getText()
     * @generated
     * @ordered
     */
    protected String text = TEXT_EDEFAULT;

    /**
     * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getTfConfig()
     * @generated
     * @ordered
     */
    protected BrickletOLEDConfiguration tfConfig;

    /**
     * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getDeviceType()
     * @generated
     * @ordered
     */
    protected static final String DEVICE_TYPE_EDEFAULT = "bricklet_oled64x48";

    /**
     * The cached value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getDeviceType()
     * @generated
     * @ordered
     */
    protected String deviceType = DEVICE_TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    protected MBrickletOLE64x48Impl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.MBRICKLET_OLE6_4X48;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Logger getLogger() {
        return logger;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setLogger(Logger newLogger) {
        Logger oldLogger = logger;
        logger = newLogger;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__LOGGER, oldLogger,
                    logger));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getUid() {
        return uid;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setUid(String newUid) {
        String oldUid = uid;
        uid = newUid;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__UID, oldUid, uid));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isPoll() {
        return poll;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPoll(boolean newPoll) {
        boolean oldPoll = poll;
        poll = newPoll;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__POLL, oldPoll,
                    poll));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AtomicBoolean getEnabledA() {
        return enabledA;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setEnabledA(AtomicBoolean newEnabledA) {
        AtomicBoolean oldEnabledA = enabledA;
        enabledA = newEnabledA;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__ENABLED_A,
                    oldEnabledA, enabledA));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public BrickletOLED64x48 getTinkerforgeDevice() {
        return tinkerforgeDevice;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTinkerforgeDevice(BrickletOLED64x48 newTinkerforgeDevice) {
        BrickletOLED64x48 oldTinkerforgeDevice = tinkerforgeDevice;
        tinkerforgeDevice = newTinkerforgeDevice;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__TINKERFORGE_DEVICE,
                    oldTinkerforgeDevice, tinkerforgeDevice));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IPConnection getIpConnection() {
        return ipConnection;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIpConnection(IPConnection newIpConnection) {
        IPConnection oldIpConnection = ipConnection;
        ipConnection = newIpConnection;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__IP_CONNECTION,
                    oldIpConnection, ipConnection));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getConnectedUid() {
        return connectedUid;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setConnectedUid(String newConnectedUid) {
        String oldConnectedUid = connectedUid;
        connectedUid = newConnectedUid;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__CONNECTED_UID,
                    oldConnectedUid, connectedUid));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public char getPosition() {
        return position;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPosition(char newPosition) {
        char oldPosition = position;
        position = newPosition;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__POSITION,
                    oldPosition, position));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int getDeviceIdentifier() {
        return deviceIdentifier;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDeviceIdentifier(int newDeviceIdentifier) {
        int oldDeviceIdentifier = deviceIdentifier;
        deviceIdentifier = newDeviceIdentifier;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__DEVICE_IDENTIFIER,
                    oldDeviceIdentifier, deviceIdentifier));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__NAME, oldName,
                    name));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MBrickd getBrickd() {
        if (eContainerFeatureID() != ModelPackage.MBRICKLET_OLE6_4X48__BRICKD) {
            return null;
        }
        return (MBrickd) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public MBrickd basicGetBrickd() {
        if (eContainerFeatureID() != ModelPackage.MBRICKLET_OLE6_4X48__BRICKD) {
            return null;
        }
        return (MBrickd) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetBrickd(MBrickd newBrickd, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newBrickd, ModelPackage.MBRICKLET_OLE6_4X48__BRICKD, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setBrickd(MBrickd newBrickd) {
        if (newBrickd != eInternalContainer()
                || (eContainerFeatureID() != ModelPackage.MBRICKLET_OLE6_4X48__BRICKD && newBrickd != null)) {
            if (EcoreUtil.isAncestor(this, newBrickd)) {
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            }
            NotificationChain msgs = null;
            if (eInternalContainer() != null) {
                msgs = eBasicRemoveFromContainer(msgs);
            }
            if (newBrickd != null) {
                msgs = ((InternalEObject) newBrickd).eInverseAdd(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class,
                        msgs);
            }
            msgs = basicSetBrickd(newBrickd, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__BRICKD, newBrickd,
                    newBrickd));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getText() {
        return text;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setText(String newText) {
        String oldText = text;
        text = newText;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__TEXT, oldText,
                    text));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public BrickletOLEDConfiguration getTfConfig() {
        return tfConfig;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetTfConfig(BrickletOLEDConfiguration newTfConfig, NotificationChain msgs) {
        BrickletOLEDConfiguration oldTfConfig = tfConfig;
        tfConfig = newTfConfig;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG, oldTfConfig, newTfConfig);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTfConfig(BrickletOLEDConfiguration newTfConfig) {
        if (newTfConfig != tfConfig) {
            NotificationChain msgs = null;
            if (tfConfig != null) {
                msgs = ((InternalEObject) tfConfig).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG, null, msgs);
            }
            if (newTfConfig != null) {
                msgs = ((InternalEObject) newTfConfig).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG, null, msgs);
            }
            msgs = basicSetTfConfig(newTfConfig, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG,
                    newTfConfig, newTfConfig));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getPositionPrefix() {
        return positionPrefix;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPositionPrefix(String newPositionPrefix) {
        String oldPositionPrefix = positionPrefix;
        positionPrefix = newPositionPrefix;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__POSITION_PREFIX,
                    oldPositionPrefix, positionPrefix));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getPositionSuffix() {
        return positionSuffix;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPositionSuffix(String newPositionSuffix) {
        String oldPositionSuffix = positionSuffix;
        positionSuffix = newPositionSuffix;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__POSITION_SUFFIX,
                    oldPositionSuffix, positionSuffix));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public short getContrast() {
        return contrast;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setContrast(short newContrast) {
        short oldContrast = contrast;
        contrast = newContrast;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__CONTRAST,
                    oldContrast, contrast));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean isInvert() {
        return invert;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setInvert(boolean newInvert) {
        boolean oldInvert = invert;
        invert = newInvert;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_OLE6_4X48__INVERT, oldInvert,
                    invert));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void clear() {
        clear((short) 0, maxColumn, (short) 0, maxLine);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void clear(short columnFrom, short columnTo, short rowFrom, short rowTo) {
        if (columnFrom < 0 || columnFrom > maxColumn) {
            logger.error("start column must have a value from 0 to {}", maxColumn);
            return;
        }
        if (columnTo < 0 || columnTo > maxColumn) {
            logger.error("end column must have a value from 0 to {}", maxColumn);
            return;
        }
        if (rowFrom < 0 || rowFrom > maxLine) {
            logger.error("start row must have a value from 0 to {}", maxLine);
            return;
        }
        if (rowTo < 0 || rowTo > maxLine) {
            logger.error("end row must have a value from 0 to {}", maxLine);
            return;
        }
        try {
            tinkerforgeDevice.newWindow(columnFrom, columnTo, rowFrom, rowTo);
            tinkerforgeDevice.clearDisplay();
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void writeLine(short line, short position, String text) {
        if (position < 0 || position > maxColumn) {
            logger.error("position must have a value from 0 to {}", maxColumn);
            return;
        }
        if (line < 0 || line > maxLine) {
            logger.error("line must have a value from 0 to {}", maxLine);
            return;
        }
        try {
            tinkerforgeDevice.writeLine(line, position, text);
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void simpleGauge(int angle) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void simpleGauge(int min, int max, int value) {
        // TODO: implement this method
        // Ensure that you remove @generated or mark it @generated NOT
        throw new UnsupportedOperationException();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void write(String linePositionText) {
        try {
            LinePositionText linePostion = Tools.parseLinePostion(linePositionText, positionPrefix, positionSuffix, 1,
                    2, maxLine, maxColumn);
            tinkerforgeDevice.writeLine(linePostion.getLine(), linePostion.getPosition(), linePostion.getText());
        } catch (LinePositionParseException e) {
            logger.error("faulty input text", e);
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void init() {
        setEnabledA(new AtomicBoolean());
        logger = LoggerFactory.getLogger(MBrickletOLE64x48Impl.class);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void enable() {
        if (tfConfig != null) {
            if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("contrast"))) {
                Short contrast = tfConfig.getContrast();
                logger.debug("contrast {}", contrast);
                setContrast(contrast);
            }
            if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("invert"))) {
                boolean invert = tfConfig.isInvert();
                logger.debug("invert {}", invert);
                setInvert(invert);
            }
        }
        try {
            tinkerforgeDevice = new BrickletOLED64x48(getUid(), getIpConnection());
            tinkerforgeDevice.setDisplayConfiguration(getContrast(), isInvert());
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void disable() {
        tinkerforgeDevice = null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case ModelPackage.MBRICKLET_OLE6_4X48__BRICKD:
                if (eInternalContainer() != null) {
                    msgs = eBasicRemoveFromContainer(msgs);
                }
                return basicSetBrickd((MBrickd) otherEnd, msgs);
        }
        return super.eInverseAdd(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case ModelPackage.MBRICKLET_OLE6_4X48__BRICKD:
                return basicSetBrickd(null, msgs);
            case ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG:
                return basicSetTfConfig(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
        switch (eContainerFeatureID()) {
            case ModelPackage.MBRICKLET_OLE6_4X48__BRICKD:
                return eInternalContainer().eInverseRemove(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class, msgs);
        }
        return super.eBasicRemoveFromContainerFeature(msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION_PREFIX:
                return getPositionPrefix();
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION_SUFFIX:
                return getPositionSuffix();
            case ModelPackage.MBRICKLET_OLE6_4X48__CONTRAST:
                return getContrast();
            case ModelPackage.MBRICKLET_OLE6_4X48__INVERT:
                return isInvert();
            case ModelPackage.MBRICKLET_OLE6_4X48__LOGGER:
                return getLogger();
            case ModelPackage.MBRICKLET_OLE6_4X48__UID:
                return getUid();
            case ModelPackage.MBRICKLET_OLE6_4X48__POLL:
                return isPoll();
            case ModelPackage.MBRICKLET_OLE6_4X48__ENABLED_A:
                return getEnabledA();
            case ModelPackage.MBRICKLET_OLE6_4X48__TINKERFORGE_DEVICE:
                return getTinkerforgeDevice();
            case ModelPackage.MBRICKLET_OLE6_4X48__IP_CONNECTION:
                return getIpConnection();
            case ModelPackage.MBRICKLET_OLE6_4X48__CONNECTED_UID:
                return getConnectedUid();
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION:
                return getPosition();
            case ModelPackage.MBRICKLET_OLE6_4X48__DEVICE_IDENTIFIER:
                return getDeviceIdentifier();
            case ModelPackage.MBRICKLET_OLE6_4X48__NAME:
                return getName();
            case ModelPackage.MBRICKLET_OLE6_4X48__BRICKD:
                if (resolve) {
                    return getBrickd();
                }
                return basicGetBrickd();
            case ModelPackage.MBRICKLET_OLE6_4X48__TEXT:
                return getText();
            case ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG:
                return getTfConfig();
            case ModelPackage.MBRICKLET_OLE6_4X48__DEVICE_TYPE:
                return getDeviceType();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION_PREFIX:
                setPositionPrefix((String) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION_SUFFIX:
                setPositionSuffix((String) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__CONTRAST:
                setContrast((Short) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__INVERT:
                setInvert((Boolean) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__LOGGER:
                setLogger((Logger) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__UID:
                setUid((String) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__POLL:
                setPoll((Boolean) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__ENABLED_A:
                setEnabledA((AtomicBoolean) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__TINKERFORGE_DEVICE:
                setTinkerforgeDevice((BrickletOLED64x48) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__IP_CONNECTION:
                setIpConnection((IPConnection) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__CONNECTED_UID:
                setConnectedUid((String) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION:
                setPosition((Character) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__DEVICE_IDENTIFIER:
                setDeviceIdentifier((Integer) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__NAME:
                setName((String) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__BRICKD:
                setBrickd((MBrickd) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__TEXT:
                setText((String) newValue);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG:
                setTfConfig((BrickletOLEDConfiguration) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION_PREFIX:
                setPositionPrefix(POSITION_PREFIX_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION_SUFFIX:
                setPositionSuffix(POSITION_SUFFIX_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__CONTRAST:
                setContrast(CONTRAST_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__INVERT:
                setInvert(INVERT_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__LOGGER:
                setLogger(LOGGER_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__UID:
                setUid(UID_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__POLL:
                setPoll(POLL_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__ENABLED_A:
                setEnabledA(ENABLED_A_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__TINKERFORGE_DEVICE:
                setTinkerforgeDevice((BrickletOLED64x48) null);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__IP_CONNECTION:
                setIpConnection(IP_CONNECTION_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__CONNECTED_UID:
                setConnectedUid(CONNECTED_UID_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION:
                setPosition(POSITION_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__DEVICE_IDENTIFIER:
                setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__NAME:
                setName(NAME_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__BRICKD:
                setBrickd((MBrickd) null);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__TEXT:
                setText(TEXT_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG:
                setTfConfig((BrickletOLEDConfiguration) null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION_PREFIX:
                return POSITION_PREFIX_EDEFAULT == null ? positionPrefix != null
                        : !POSITION_PREFIX_EDEFAULT.equals(positionPrefix);
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION_SUFFIX:
                return POSITION_SUFFIX_EDEFAULT == null ? positionSuffix != null
                        : !POSITION_SUFFIX_EDEFAULT.equals(positionSuffix);
            case ModelPackage.MBRICKLET_OLE6_4X48__CONTRAST:
                return contrast != CONTRAST_EDEFAULT;
            case ModelPackage.MBRICKLET_OLE6_4X48__INVERT:
                return invert != INVERT_EDEFAULT;
            case ModelPackage.MBRICKLET_OLE6_4X48__LOGGER:
                return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
            case ModelPackage.MBRICKLET_OLE6_4X48__UID:
                return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
            case ModelPackage.MBRICKLET_OLE6_4X48__POLL:
                return poll != POLL_EDEFAULT;
            case ModelPackage.MBRICKLET_OLE6_4X48__ENABLED_A:
                return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
            case ModelPackage.MBRICKLET_OLE6_4X48__TINKERFORGE_DEVICE:
                return tinkerforgeDevice != null;
            case ModelPackage.MBRICKLET_OLE6_4X48__IP_CONNECTION:
                return IP_CONNECTION_EDEFAULT == null ? ipConnection != null
                        : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
            case ModelPackage.MBRICKLET_OLE6_4X48__CONNECTED_UID:
                return CONNECTED_UID_EDEFAULT == null ? connectedUid != null
                        : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
            case ModelPackage.MBRICKLET_OLE6_4X48__POSITION:
                return position != POSITION_EDEFAULT;
            case ModelPackage.MBRICKLET_OLE6_4X48__DEVICE_IDENTIFIER:
                return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
            case ModelPackage.MBRICKLET_OLE6_4X48__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case ModelPackage.MBRICKLET_OLE6_4X48__BRICKD:
                return basicGetBrickd() != null;
            case ModelPackage.MBRICKLET_OLE6_4X48__TEXT:
                return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
            case ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG:
                return tfConfig != null;
            case ModelPackage.MBRICKLET_OLE6_4X48__DEVICE_TYPE:
                return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
        if (baseClass == MBaseDevice.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_OLE6_4X48__LOGGER:
                    return ModelPackage.MBASE_DEVICE__LOGGER;
                case ModelPackage.MBRICKLET_OLE6_4X48__UID:
                    return ModelPackage.MBASE_DEVICE__UID;
                case ModelPackage.MBRICKLET_OLE6_4X48__POLL:
                    return ModelPackage.MBASE_DEVICE__POLL;
                case ModelPackage.MBRICKLET_OLE6_4X48__ENABLED_A:
                    return ModelPackage.MBASE_DEVICE__ENABLED_A;
                default:
                    return -1;
            }
        }
        if (baseClass == MDevice.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_OLE6_4X48__TINKERFORGE_DEVICE:
                    return ModelPackage.MDEVICE__TINKERFORGE_DEVICE;
                case ModelPackage.MBRICKLET_OLE6_4X48__IP_CONNECTION:
                    return ModelPackage.MDEVICE__IP_CONNECTION;
                case ModelPackage.MBRICKLET_OLE6_4X48__CONNECTED_UID:
                    return ModelPackage.MDEVICE__CONNECTED_UID;
                case ModelPackage.MBRICKLET_OLE6_4X48__POSITION:
                    return ModelPackage.MDEVICE__POSITION;
                case ModelPackage.MBRICKLET_OLE6_4X48__DEVICE_IDENTIFIER:
                    return ModelPackage.MDEVICE__DEVICE_IDENTIFIER;
                case ModelPackage.MBRICKLET_OLE6_4X48__NAME:
                    return ModelPackage.MDEVICE__NAME;
                case ModelPackage.MBRICKLET_OLE6_4X48__BRICKD:
                    return ModelPackage.MDEVICE__BRICKD;
                default:
                    return -1;
            }
        }
        if (baseClass == MTextActor.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_OLE6_4X48__TEXT:
                    return ModelPackage.MTEXT_ACTOR__TEXT;
                default:
                    return -1;
            }
        }
        if (baseClass == MTFConfigConsumer.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG:
                    return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
                default:
                    return -1;
            }
        }
        return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
        if (baseClass == MBaseDevice.class) {
            switch (baseFeatureID) {
                case ModelPackage.MBASE_DEVICE__LOGGER:
                    return ModelPackage.MBRICKLET_OLE6_4X48__LOGGER;
                case ModelPackage.MBASE_DEVICE__UID:
                    return ModelPackage.MBRICKLET_OLE6_4X48__UID;
                case ModelPackage.MBASE_DEVICE__POLL:
                    return ModelPackage.MBRICKLET_OLE6_4X48__POLL;
                case ModelPackage.MBASE_DEVICE__ENABLED_A:
                    return ModelPackage.MBRICKLET_OLE6_4X48__ENABLED_A;
                default:
                    return -1;
            }
        }
        if (baseClass == MDevice.class) {
            switch (baseFeatureID) {
                case ModelPackage.MDEVICE__TINKERFORGE_DEVICE:
                    return ModelPackage.MBRICKLET_OLE6_4X48__TINKERFORGE_DEVICE;
                case ModelPackage.MDEVICE__IP_CONNECTION:
                    return ModelPackage.MBRICKLET_OLE6_4X48__IP_CONNECTION;
                case ModelPackage.MDEVICE__CONNECTED_UID:
                    return ModelPackage.MBRICKLET_OLE6_4X48__CONNECTED_UID;
                case ModelPackage.MDEVICE__POSITION:
                    return ModelPackage.MBRICKLET_OLE6_4X48__POSITION;
                case ModelPackage.MDEVICE__DEVICE_IDENTIFIER:
                    return ModelPackage.MBRICKLET_OLE6_4X48__DEVICE_IDENTIFIER;
                case ModelPackage.MDEVICE__NAME:
                    return ModelPackage.MBRICKLET_OLE6_4X48__NAME;
                case ModelPackage.MDEVICE__BRICKD:
                    return ModelPackage.MBRICKLET_OLE6_4X48__BRICKD;
                default:
                    return -1;
            }
        }
        if (baseClass == MTextActor.class) {
            switch (baseFeatureID) {
                case ModelPackage.MTEXT_ACTOR__TEXT:
                    return ModelPackage.MBRICKLET_OLE6_4X48__TEXT;
                default:
                    return -1;
            }
        }
        if (baseClass == MTFConfigConsumer.class) {
            switch (baseFeatureID) {
                case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG:
                    return ModelPackage.MBRICKLET_OLE6_4X48__TF_CONFIG;
                default:
                    return -1;
            }
        }
        return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
        if (baseClass == MBaseDevice.class) {
            switch (baseOperationID) {
                case ModelPackage.MBASE_DEVICE___INIT:
                    return ModelPackage.MBRICKLET_OLE6_4X48___INIT;
                case ModelPackage.MBASE_DEVICE___ENABLE:
                    return ModelPackage.MBRICKLET_OLE6_4X48___ENABLE;
                case ModelPackage.MBASE_DEVICE___DISABLE:
                    return ModelPackage.MBRICKLET_OLE6_4X48___DISABLE;
                default:
                    return -1;
            }
        }
        if (baseClass == MDevice.class) {
            switch (baseOperationID) {
                default:
                    return -1;
            }
        }
        if (baseClass == MTextActor.class) {
            switch (baseOperationID) {
                case ModelPackage.MTEXT_ACTOR___WRITE__STRING:
                    return ModelPackage.MBRICKLET_OLE6_4X48___WRITE__STRING;
                default:
                    return -1;
            }
        }
        if (baseClass == MTFConfigConsumer.class) {
            switch (baseOperationID) {
                default:
                    return -1;
            }
        }
        return super.eDerivedOperationID(baseOperationID, baseClass);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
        switch (operationID) {
            case ModelPackage.MBRICKLET_OLE6_4X48___WRITE__STRING:
                write((String) arguments.get(0));
                return null;
            case ModelPackage.MBRICKLET_OLE6_4X48___INIT:
                init();
                return null;
            case ModelPackage.MBRICKLET_OLE6_4X48___ENABLE:
                enable();
                return null;
            case ModelPackage.MBRICKLET_OLE6_4X48___DISABLE:
                disable();
                return null;
            case ModelPackage.MBRICKLET_OLE6_4X48___CLEAR:
                clear();
                return null;
            case ModelPackage.MBRICKLET_OLE6_4X48___CLEAR__SHORT_SHORT_SHORT_SHORT:
                clear((Short) arguments.get(0), (Short) arguments.get(1), (Short) arguments.get(2),
                        (Short) arguments.get(3));
                return null;
            case ModelPackage.MBRICKLET_OLE6_4X48___WRITE_LINE__SHORT_SHORT_STRING:
                writeLine((Short) arguments.get(0), (Short) arguments.get(1), (String) arguments.get(2));
                return null;
            case ModelPackage.MBRICKLET_OLE6_4X48___SIMPLE_GAUGE__INT:
                simpleGauge((Integer) arguments.get(0));
                return null;
            case ModelPackage.MBRICKLET_OLE6_4X48___SIMPLE_GAUGE__INT_INT_INT:
                simpleGauge((Integer) arguments.get(0), (Integer) arguments.get(1), (Integer) arguments.get(2));
                return null;
        }
        return super.eInvoke(operationID, arguments);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) {
            return super.toString();
        }

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (positionPrefix: ");
        result.append(positionPrefix);
        result.append(", positionSuffix: ");
        result.append(positionSuffix);
        result.append(", contrast: ");
        result.append(contrast);
        result.append(", invert: ");
        result.append(invert);
        result.append(", logger: ");
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
        result.append(", text: ");
        result.append(text);
        result.append(", deviceType: ");
        result.append(deviceType);
        result.append(')');
        return result.toString();
    }

} // MBrickletOLE64x48Impl
