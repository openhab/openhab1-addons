/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
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
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.openhab.binding.tinkerforge.internal.LoggerConstants;
import org.openhab.binding.tinkerforge.internal.TinkerforgeErrorHandler;
import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.model.LEDGroup;
import org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletLEDStrip;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.tools.LedList;
import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.types.HSBValue;
import org.openhab.core.library.types.HSBType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletLEDStrip;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBricklet LED Strip</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.5.0
 *        <!-- end-user-doc -->
 *        <p>
 *        The following features are implemented:
 *        </p>
 *        <ul>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getColor
 *        <em>Color</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getLogger
 *        <em>Logger</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getUid <em>Uid</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#isPoll
 *        <em>Poll</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getEnabledA <em>Enabled
 *        A</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getTinkerforgeDevice
 *        <em>Tinkerforge Device</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getIpConnection <em>Ip
 *        Connection</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getConnectedUid
 *        <em>Connected Uid</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getPosition
 *        <em>Position</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getDeviceIdentifier
 *        <em>Device Identifier</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getName
 *        <em>Name</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getBrickd
 *        <em>Brickd</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getTfConfig <em>Tf
 *        Config</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getMsubdevices
 *        <em>Msubdevices</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getDeviceType <em>Device
 *        Type</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletLEDStripImpl#getColorMapping <em>Color
 *        Mapping</em>}</li>
 *        </ul>
 *
 * @generated
 */
public class MBrickletLEDStripImpl extends MinimalEObjectImpl.Container implements MBrickletLEDStrip {
    /**
     * The default value of the '{@link #getColor() <em>Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getColor()
     * @generated
     * @ordered
     */
    protected static final HSBValue COLOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getColor() <em>Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getColor()
     * @generated
     * @ordered
     */
    protected HSBValue color = COLOR_EDEFAULT;

    private static final String LEDS = "leds";

    private static final String COLOR_MAPPING = "colorMapping";

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
    protected BrickletLEDStrip tinkerforgeDevice;

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
     * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getTfConfig()
     * @generated
     * @ordered
     */
    protected LEDStripConfiguration tfConfig;

    /**
     * The cached value of the '{@link #getMsubdevices() <em>Msubdevices</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMsubdevices()
     * @generated
     * @ordered
     */
    protected EList<LEDGroup> msubdevices;

    /**
     * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDeviceType()
     * @generated
     * @ordered
     */
    protected static final String DEVICE_TYPE_EDEFAULT = "bricklet_ledstrip";

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
     * The default value of the '{@link #getColorMapping() <em>Color Mapping</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getColorMapping()
     * @generated
     * @ordered
     */
    protected static final String COLOR_MAPPING_EDEFAULT = "rgb";

    /**
     * The cached value of the '{@link #getColorMapping() <em>Color Mapping</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getColorMapping()
     * @generated
     * @ordered
     */
    protected String colorMapping = COLOR_MAPPING_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected MBrickletLEDStripImpl() {
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
        return ModelPackage.Literals.MBRICKLET_LED_STRIP;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public HSBValue getColor() {
        return color;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setColor(HSBValue newColor) {
        HSBValue oldColor = color;
        color = newColor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__COLOR, oldColor,
                    color));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__LOGGER, oldLogger,
                    logger));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__UID, oldUid, uid));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__POLL, oldPoll,
                    poll));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__ENABLED_A,
                    oldEnabledA, enabledA));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BrickletLEDStrip getTinkerforgeDevice() {
        return tinkerforgeDevice;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setTinkerforgeDevice(BrickletLEDStrip newTinkerforgeDevice) {
        BrickletLEDStrip oldTinkerforgeDevice = tinkerforgeDevice;
        tinkerforgeDevice = newTinkerforgeDevice;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__TINKERFORGE_DEVICE,
                    oldTinkerforgeDevice, tinkerforgeDevice));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__IP_CONNECTION,
                    oldIpConnection, ipConnection));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__CONNECTED_UID,
                    oldConnectedUid, connectedUid));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__POSITION,
                    oldPosition, position));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__DEVICE_IDENTIFIER,
                    oldDeviceIdentifier, deviceIdentifier));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__NAME, oldName,
                    name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public MBrickd getBrickd() {
        if (eContainerFeatureID() != ModelPackage.MBRICKLET_LED_STRIP__BRICKD)
            return null;
        return (MBrickd) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public MBrickd basicGetBrickd() {
        if (eContainerFeatureID() != ModelPackage.MBRICKLET_LED_STRIP__BRICKD)
            return null;
        return (MBrickd) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetBrickd(MBrickd newBrickd, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newBrickd, ModelPackage.MBRICKLET_LED_STRIP__BRICKD, msgs);
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
                || (eContainerFeatureID() != ModelPackage.MBRICKLET_LED_STRIP__BRICKD && newBrickd != null)) {
            if (EcoreUtil.isAncestor(this, newBrickd))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newBrickd != null)
                msgs = ((InternalEObject) newBrickd).eInverseAdd(this, ModelPackage.MBRICKD__MDEVICES, MBrickd.class,
                        msgs);
            msgs = basicSetBrickd(newBrickd, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__BRICKD, newBrickd,
                    newBrickd));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public LEDStripConfiguration getTfConfig() {
        return tfConfig;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetTfConfig(LEDStripConfiguration newTfConfig, NotificationChain msgs) {
        LEDStripConfiguration oldTfConfig = tfConfig;
        tfConfig = newTfConfig;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG, oldTfConfig, newTfConfig);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
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
    public void setTfConfig(LEDStripConfiguration newTfConfig) {
        if (newTfConfig != tfConfig) {
            NotificationChain msgs = null;
            if (tfConfig != null)
                msgs = ((InternalEObject) tfConfig).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG, null, msgs);
            if (newTfConfig != null)
                msgs = ((InternalEObject) newTfConfig).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG, null, msgs);
            msgs = basicSetTfConfig(newTfConfig, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG,
                    newTfConfig, newTfConfig));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public EList<LEDGroup> getMsubdevices() {
        if (msubdevices == null) {
            msubdevices = new EObjectContainmentWithInverseEList<LEDGroup>(LEDGroup.class, this,
                    ModelPackage.MBRICKLET_LED_STRIP__MSUBDEVICES, ModelPackage.MSUB_DEVICE__MBRICK) {
                private static final long serialVersionUID = 1L;

                @Override
                public Class<?> getInverseFeatureClass() {
                    return MSubDevice.class;
                }
            };
        }
        return msubdevices;
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
    public String getColorMapping() {
        return colorMapping;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setColorMapping(String newColorMapping) {
        String oldColorMapping = colorMapping;
        colorMapping = newColorMapping;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_LED_STRIP__COLOR_MAPPING,
                    oldColorMapping, colorMapping));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public void initSubDevices() {
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
        logger = LoggerFactory.getLogger(MBrickletLEDStripImpl.class);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public void enable() {
        logger.trace("enabling");
        int chipType = BrickletLEDStrip.CHIP_TYPE_WS2801;
        int frameDuration = 100;
        Long clockFrequency = null;
        tinkerforgeDevice = new BrickletLEDStrip(getUid(), getIpConnection());
        if (tfConfig != null) {
            if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("chiptype"))) {
                String chipTypeString = tfConfig.getChiptype();
                if (chipTypeString.equalsIgnoreCase("ws2801")) {
                    chipType = BrickletLEDStrip.CHIP_TYPE_WS2801;
                } else if (chipTypeString.equalsIgnoreCase("ws2811")) {
                    chipType = BrickletLEDStrip.CHIP_TYPE_WS2811;
                } else if (chipTypeString.equalsIgnoreCase("ws2812")) {
                    chipType = BrickletLEDStrip.CHIP_TYPE_WS2812;
                } else {
                    logger.error("Unknown ChipType {}", chipTypeString);
                    // TODO raise configuration error
                }
            }
            if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("frameduration"))) {
                frameDuration = tfConfig.getFrameduration();
            }
            if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("clockfrequency"))) {
                clockFrequency = tfConfig.getClockfrequency();
            }
            // config Handling for ColorMapping must be before adding the
            // subdevices because subdevices use the ColorMapping
            if (tfConfig.eIsSet(
                    tfConfig.eClass().getEStructuralFeature(ModelPackage.LED_STRIP_CONFIGURATION__COLOR_MAPPING))) {
                colorMapping = tfConfig.getColorMapping();
            }
            if (tfConfig.eIsSet(
                    tfConfig.eClass().getEStructuralFeature(ModelPackage.LED_STRIP_CONFIGURATION__SUB_DEVICES))) {
                String[] subdevices = tfConfig.getSubDevices().trim().split("\\s+");
                for (String subId : subdevices) {
                    addSubdevice(subId);
                }
            }
        }
        logger.debug("chipType is {}", chipType);
        logger.debug("frameDuration is {}", frameDuration);
        logger.debug("colorMapping is {}", colorMapping);
        try {
            tinkerforgeDevice.setChipType(chipType);
            tinkerforgeDevice.setFrameDuration(frameDuration);
            if (clockFrequency != null) {
                logger.debug("clockFrequency is {}", clockFrequency);
                tinkerforgeDevice.setClockFrequency(clockFrequency);
            } else {
                logger.debug("clockFrequency is not set");
            }
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
    }

    private void addSubdevice(String subId) {
        logger.debug("{} add sub device subId {} for uid {}", LoggerConstants.TFINITSUB, subId, getUid());
        LEDGroup ledGroup = ModelFactory.eINSTANCE.createLEDGroup();
        ledGroup.setUid(getUid());
        ledGroup.setSubId(subId);
        ledGroup.init();
        ledGroup.setMbrick(this);
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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public void setSelectedColor(HSBType color, DeviceOptions opts) {
        logger.debug("setSelectedColor called");
        // ColorMapping may be overridden by itemConfiguration
        char[] colorMapping = getColorMapping().toCharArray();
        String leds = null;

        // handle options
        if (opts != null) {
            if (opts.containsKey(COLOR_MAPPING)) {
                logger.debug("custom color mapping {} ", opts.getOption(COLOR_MAPPING));
                colorMapping = opts.getOption(COLOR_MAPPING).toCharArray();
            }
            if (opts.containsKey(LEDS)) {
                leds = opts.getOption(LEDS).trim();
                logger.debug("leds: {}", leds);
            }
        }
        if (leds == null || leds.length() == 0) {
            logger.error("\"leds\" option missing or empty, items configuration has to be fixed!");
            return;
        }

        // get the rgb values from HSBType
        Color rgbColor = color.toColor();
        short red = (short) rgbColor.getRed();
        short green = (short) rgbColor.getGreen();
        short blue = (short) rgbColor.getBlue();
        logger.debug("rgb is: {}:{}:{}", red, green, blue);

        // construct the values for the setRGBValues call
        HashMap<Character, short[]> colorMap = new HashMap<Character, short[]>(3);
        short[] reds = { red, red, red, red, red, red, red, red, red, red, red, red, red, red, red, red };
        short[] greens = { green, green, green, green, green, green, green, green, green, green, green, green, green,
                green, green, green };
        short[] blues = { blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue, blue,
                blue };
        colorMap.put('r', reds);
        colorMap.put('g', greens);
        colorMap.put('b', blues);
        LedList ledList = Tools.parseLedString(leds);

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
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case ModelPackage.MBRICKLET_LED_STRIP__BRICKD:
                if (eInternalContainer() != null)
                    msgs = eBasicRemoveFromContainer(msgs);
                return basicSetBrickd((MBrickd) otherEnd, msgs);
            case ModelPackage.MBRICKLET_LED_STRIP__MSUBDEVICES:
                return ((InternalEList<InternalEObject>) (InternalEList<?>) getMsubdevices()).basicAdd(otherEnd, msgs);
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
            case ModelPackage.MBRICKLET_LED_STRIP__BRICKD:
                return basicSetBrickd(null, msgs);
            case ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG:
                return basicSetTfConfig(null, msgs);
            case ModelPackage.MBRICKLET_LED_STRIP__MSUBDEVICES:
                return ((InternalEList<?>) getMsubdevices()).basicRemove(otherEnd, msgs);
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
            case ModelPackage.MBRICKLET_LED_STRIP__BRICKD:
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
            case ModelPackage.MBRICKLET_LED_STRIP__COLOR:
                return getColor();
            case ModelPackage.MBRICKLET_LED_STRIP__LOGGER:
                return getLogger();
            case ModelPackage.MBRICKLET_LED_STRIP__UID:
                return getUid();
            case ModelPackage.MBRICKLET_LED_STRIP__POLL:
                return isPoll();
            case ModelPackage.MBRICKLET_LED_STRIP__ENABLED_A:
                return getEnabledA();
            case ModelPackage.MBRICKLET_LED_STRIP__TINKERFORGE_DEVICE:
                return getTinkerforgeDevice();
            case ModelPackage.MBRICKLET_LED_STRIP__IP_CONNECTION:
                return getIpConnection();
            case ModelPackage.MBRICKLET_LED_STRIP__CONNECTED_UID:
                return getConnectedUid();
            case ModelPackage.MBRICKLET_LED_STRIP__POSITION:
                return getPosition();
            case ModelPackage.MBRICKLET_LED_STRIP__DEVICE_IDENTIFIER:
                return getDeviceIdentifier();
            case ModelPackage.MBRICKLET_LED_STRIP__NAME:
                return getName();
            case ModelPackage.MBRICKLET_LED_STRIP__BRICKD:
                if (resolve)
                    return getBrickd();
                return basicGetBrickd();
            case ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG:
                return getTfConfig();
            case ModelPackage.MBRICKLET_LED_STRIP__MSUBDEVICES:
                return getMsubdevices();
            case ModelPackage.MBRICKLET_LED_STRIP__DEVICE_TYPE:
                return getDeviceType();
            case ModelPackage.MBRICKLET_LED_STRIP__COLOR_MAPPING:
                return getColorMapping();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case ModelPackage.MBRICKLET_LED_STRIP__COLOR:
                setColor((HSBValue) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__LOGGER:
                setLogger((Logger) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__UID:
                setUid((String) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__POLL:
                setPoll((Boolean) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__ENABLED_A:
                setEnabledA((AtomicBoolean) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__TINKERFORGE_DEVICE:
                setTinkerforgeDevice((BrickletLEDStrip) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__IP_CONNECTION:
                setIpConnection((IPConnection) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__CONNECTED_UID:
                setConnectedUid((String) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__POSITION:
                setPosition((Character) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__DEVICE_IDENTIFIER:
                setDeviceIdentifier((Integer) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__NAME:
                setName((String) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__BRICKD:
                setBrickd((MBrickd) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG:
                setTfConfig((LEDStripConfiguration) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__MSUBDEVICES:
                getMsubdevices().clear();
                getMsubdevices().addAll((Collection<? extends LEDGroup>) newValue);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__COLOR_MAPPING:
                setColorMapping((String) newValue);
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
            case ModelPackage.MBRICKLET_LED_STRIP__COLOR:
                setColor(COLOR_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__LOGGER:
                setLogger(LOGGER_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__UID:
                setUid(UID_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__POLL:
                setPoll(POLL_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__ENABLED_A:
                setEnabledA(ENABLED_A_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__TINKERFORGE_DEVICE:
                setTinkerforgeDevice((BrickletLEDStrip) null);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__IP_CONNECTION:
                setIpConnection(IP_CONNECTION_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__CONNECTED_UID:
                setConnectedUid(CONNECTED_UID_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__POSITION:
                setPosition(POSITION_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__DEVICE_IDENTIFIER:
                setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__NAME:
                setName(NAME_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__BRICKD:
                setBrickd((MBrickd) null);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG:
                setTfConfig((LEDStripConfiguration) null);
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__MSUBDEVICES:
                getMsubdevices().clear();
                return;
            case ModelPackage.MBRICKLET_LED_STRIP__COLOR_MAPPING:
                setColorMapping(COLOR_MAPPING_EDEFAULT);
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
            case ModelPackage.MBRICKLET_LED_STRIP__COLOR:
                return COLOR_EDEFAULT == null ? color != null : !COLOR_EDEFAULT.equals(color);
            case ModelPackage.MBRICKLET_LED_STRIP__LOGGER:
                return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
            case ModelPackage.MBRICKLET_LED_STRIP__UID:
                return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
            case ModelPackage.MBRICKLET_LED_STRIP__POLL:
                return poll != POLL_EDEFAULT;
            case ModelPackage.MBRICKLET_LED_STRIP__ENABLED_A:
                return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
            case ModelPackage.MBRICKLET_LED_STRIP__TINKERFORGE_DEVICE:
                return tinkerforgeDevice != null;
            case ModelPackage.MBRICKLET_LED_STRIP__IP_CONNECTION:
                return IP_CONNECTION_EDEFAULT == null ? ipConnection != null
                        : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
            case ModelPackage.MBRICKLET_LED_STRIP__CONNECTED_UID:
                return CONNECTED_UID_EDEFAULT == null ? connectedUid != null
                        : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
            case ModelPackage.MBRICKLET_LED_STRIP__POSITION:
                return position != POSITION_EDEFAULT;
            case ModelPackage.MBRICKLET_LED_STRIP__DEVICE_IDENTIFIER:
                return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
            case ModelPackage.MBRICKLET_LED_STRIP__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case ModelPackage.MBRICKLET_LED_STRIP__BRICKD:
                return basicGetBrickd() != null;
            case ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG:
                return tfConfig != null;
            case ModelPackage.MBRICKLET_LED_STRIP__MSUBDEVICES:
                return msubdevices != null && !msubdevices.isEmpty();
            case ModelPackage.MBRICKLET_LED_STRIP__DEVICE_TYPE:
                return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
            case ModelPackage.MBRICKLET_LED_STRIP__COLOR_MAPPING:
                return COLOR_MAPPING_EDEFAULT == null ? colorMapping != null
                        : !COLOR_MAPPING_EDEFAULT.equals(colorMapping);
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
                case ModelPackage.MBRICKLET_LED_STRIP__LOGGER:
                    return ModelPackage.MBASE_DEVICE__LOGGER;
                case ModelPackage.MBRICKLET_LED_STRIP__UID:
                    return ModelPackage.MBASE_DEVICE__UID;
                case ModelPackage.MBRICKLET_LED_STRIP__POLL:
                    return ModelPackage.MBASE_DEVICE__POLL;
                case ModelPackage.MBRICKLET_LED_STRIP__ENABLED_A:
                    return ModelPackage.MBASE_DEVICE__ENABLED_A;
                default:
                    return -1;
            }
        }
        if (baseClass == MDevice.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_LED_STRIP__TINKERFORGE_DEVICE:
                    return ModelPackage.MDEVICE__TINKERFORGE_DEVICE;
                case ModelPackage.MBRICKLET_LED_STRIP__IP_CONNECTION:
                    return ModelPackage.MDEVICE__IP_CONNECTION;
                case ModelPackage.MBRICKLET_LED_STRIP__CONNECTED_UID:
                    return ModelPackage.MDEVICE__CONNECTED_UID;
                case ModelPackage.MBRICKLET_LED_STRIP__POSITION:
                    return ModelPackage.MDEVICE__POSITION;
                case ModelPackage.MBRICKLET_LED_STRIP__DEVICE_IDENTIFIER:
                    return ModelPackage.MDEVICE__DEVICE_IDENTIFIER;
                case ModelPackage.MBRICKLET_LED_STRIP__NAME:
                    return ModelPackage.MDEVICE__NAME;
                case ModelPackage.MBRICKLET_LED_STRIP__BRICKD:
                    return ModelPackage.MDEVICE__BRICKD;
                default:
                    return -1;
            }
        }
        if (baseClass == MTFConfigConsumer.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG:
                    return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
                default:
                    return -1;
            }
        }
        if (baseClass == MSubDeviceHolder.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_LED_STRIP__MSUBDEVICES:
                    return ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES;
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
                    return ModelPackage.MBRICKLET_LED_STRIP__LOGGER;
                case ModelPackage.MBASE_DEVICE__UID:
                    return ModelPackage.MBRICKLET_LED_STRIP__UID;
                case ModelPackage.MBASE_DEVICE__POLL:
                    return ModelPackage.MBRICKLET_LED_STRIP__POLL;
                case ModelPackage.MBASE_DEVICE__ENABLED_A:
                    return ModelPackage.MBRICKLET_LED_STRIP__ENABLED_A;
                default:
                    return -1;
            }
        }
        if (baseClass == MDevice.class) {
            switch (baseFeatureID) {
                case ModelPackage.MDEVICE__TINKERFORGE_DEVICE:
                    return ModelPackage.MBRICKLET_LED_STRIP__TINKERFORGE_DEVICE;
                case ModelPackage.MDEVICE__IP_CONNECTION:
                    return ModelPackage.MBRICKLET_LED_STRIP__IP_CONNECTION;
                case ModelPackage.MDEVICE__CONNECTED_UID:
                    return ModelPackage.MBRICKLET_LED_STRIP__CONNECTED_UID;
                case ModelPackage.MDEVICE__POSITION:
                    return ModelPackage.MBRICKLET_LED_STRIP__POSITION;
                case ModelPackage.MDEVICE__DEVICE_IDENTIFIER:
                    return ModelPackage.MBRICKLET_LED_STRIP__DEVICE_IDENTIFIER;
                case ModelPackage.MDEVICE__NAME:
                    return ModelPackage.MBRICKLET_LED_STRIP__NAME;
                case ModelPackage.MDEVICE__BRICKD:
                    return ModelPackage.MBRICKLET_LED_STRIP__BRICKD;
                default:
                    return -1;
            }
        }
        if (baseClass == MTFConfigConsumer.class) {
            switch (baseFeatureID) {
                case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG:
                    return ModelPackage.MBRICKLET_LED_STRIP__TF_CONFIG;
                default:
                    return -1;
            }
        }
        if (baseClass == MSubDeviceHolder.class) {
            switch (baseFeatureID) {
                case ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES:
                    return ModelPackage.MBRICKLET_LED_STRIP__MSUBDEVICES;
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
                    return ModelPackage.MBRICKLET_LED_STRIP___INIT;
                case ModelPackage.MBASE_DEVICE___ENABLE:
                    return ModelPackage.MBRICKLET_LED_STRIP___ENABLE;
                case ModelPackage.MBASE_DEVICE___DISABLE:
                    return ModelPackage.MBRICKLET_LED_STRIP___DISABLE;
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
        if (baseClass == MTFConfigConsumer.class) {
            switch (baseOperationID) {
                default:
                    return -1;
            }
        }
        if (baseClass == MSubDeviceHolder.class) {
            switch (baseOperationID) {
                case ModelPackage.MSUB_DEVICE_HOLDER___INIT_SUB_DEVICES:
                    return ModelPackage.MBRICKLET_LED_STRIP___INIT_SUB_DEVICES;
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
            case ModelPackage.MBRICKLET_LED_STRIP___INIT_SUB_DEVICES:
                initSubDevices();
                return null;
            case ModelPackage.MBRICKLET_LED_STRIP___INIT:
                init();
                return null;
            case ModelPackage.MBRICKLET_LED_STRIP___ENABLE:
                enable();
                return null;
            case ModelPackage.MBRICKLET_LED_STRIP___DISABLE:
                disable();
                return null;
            case ModelPackage.MBRICKLET_LED_STRIP___SET_SELECTED_COLOR__HSBTYPE_DEVICEOPTIONS:
                setSelectedColor((HSBType) arguments.get(0), (DeviceOptions) arguments.get(1));
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
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (color: ");
        result.append(color);
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
        result.append(", deviceType: ");
        result.append(deviceType);
        result.append(", colorMapping: ");
        result.append(colorMapping);
        result.append(')');
        return result.toString();
    }

} // MBrickletLEDStripImpl
