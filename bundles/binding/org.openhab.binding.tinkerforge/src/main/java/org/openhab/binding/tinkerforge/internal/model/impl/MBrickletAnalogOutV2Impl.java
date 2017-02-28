/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
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
import org.openhab.binding.tinkerforge.internal.config.DeviceOptions;
import org.openhab.binding.tinkerforge.internal.model.ConfigOptsDimmable;
import org.openhab.binding.tinkerforge.internal.model.DimmableActor;
import org.openhab.binding.tinkerforge.internal.model.DimmableConfiguration;
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickd;
import org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogOutV2;
import org.openhab.binding.tinkerforge.internal.model.MDevice;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.PercentTypeActor;
import org.openhab.binding.tinkerforge.internal.model.SetPointActor;
import org.openhab.binding.tinkerforge.internal.tools.Tools;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.PercentValue;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.PercentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletAnalogOutV2;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBricklet Analog Out V2</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getSensorValue <em>Sensor
 * Value</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getLogger <em>Logger</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getUid <em>Uid</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#isPoll <em>Poll</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getEnabledA <em>Enabled
 * A</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getTinkerforgeDevice
 * <em>Tinkerforge Device</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getIpConnection <em>Ip
 * Connection</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getConnectedUid <em>Connected
 * Uid</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getPosition
 * <em>Position</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getDeviceIdentifier
 * <em>Device Identifier</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getName <em>Name</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getBrickd <em>Brickd</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getTfConfig <em>Tf
 * Config</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getMinValue <em>Min
 * Value</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getMaxValue <em>Max
 * Value</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getPercentValue <em>Percent
 * Value</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getDeviceType <em>Device
 * Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getMinValueDevice <em>Min
 * Value Device</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletAnalogOutV2Impl#getMaxValueDevice <em>Max
 * Value Device</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MBrickletAnalogOutV2Impl extends MinimalEObjectImpl.Container implements MBrickletAnalogOutV2 {
    /**
     * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getSensorValue()
     * @generated
     * @ordered
     */
    protected DecimalValue sensorValue;

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
    protected BrickletAnalogOutV2 tinkerforgeDevice;

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
    protected DimmableConfiguration tfConfig;

    /**
     * The default value of the '{@link #getMinValue() <em>Min Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMinValue()
     * @generated
     * @ordered
     */
    protected static final BigDecimal MIN_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMinValue() <em>Min Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMinValue()
     * @generated
     * @ordered
     */
    protected BigDecimal minValue = MIN_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getMaxValue() <em>Max Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMaxValue()
     * @generated
     * @ordered
     */
    protected static final BigDecimal MAX_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getMaxValue() <em>Max Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMaxValue()
     * @generated
     * @ordered
     */
    protected BigDecimal maxValue = MAX_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getPercentValue() <em>Percent Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getPercentValue()
     * @generated
     * @ordered
     */
    protected static final PercentValue PERCENT_VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getPercentValue() <em>Percent Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getPercentValue()
     * @generated
     * @ordered
     */
    protected PercentValue percentValue = PERCENT_VALUE_EDEFAULT;

    /**
     * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDeviceType()
     * @generated
     * @ordered
     */
    protected static final String DEVICE_TYPE_EDEFAULT = "bricklet_analog_out_v2";

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
     * The default value of the '{@link #getMinValueDevice() <em>Min Value Device</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMinValueDevice()
     * @generated
     * @ordered
     */
    protected static final BigDecimal MIN_VALUE_DEVICE_EDEFAULT = new BigDecimal("0");

    /**
     * The cached value of the '{@link #getMinValueDevice() <em>Min Value Device</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMinValueDevice()
     * @generated
     * @ordered
     */
    protected BigDecimal minValueDevice = MIN_VALUE_DEVICE_EDEFAULT;

    /**
     * The default value of the '{@link #getMaxValueDevice() <em>Max Value Device</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMaxValueDevice()
     * @generated
     * @ordered
     */
    protected static final BigDecimal MAX_VALUE_DEVICE_EDEFAULT = new BigDecimal("12000");

    /**
     * The cached value of the '{@link #getMaxValueDevice() <em>Max Value Device</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMaxValueDevice()
     * @generated
     * @ordered
     */
    protected BigDecimal maxValueDevice = MAX_VALUE_DEVICE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected MBrickletAnalogOutV2Impl() {
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
        return ModelPackage.Literals.MBRICKLET_ANALOG_OUT_V2;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public DecimalValue getSensorValue() {
        return sensorValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setSensorValue(DecimalValue newSensorValue) {
        DecimalValue oldSensorValue = sensorValue;
        sensorValue = newSensorValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__SENSOR_VALUE,
                    oldSensorValue, sensorValue));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__LOGGER,
                    oldLogger, logger));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__UID, oldUid,
                    uid));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__POLL, oldPoll,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__ENABLED_A,
                    oldEnabledA, enabledA));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BrickletAnalogOutV2 getTinkerforgeDevice() {
        return tinkerforgeDevice;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setTinkerforgeDevice(BrickletAnalogOutV2 newTinkerforgeDevice) {
        BrickletAnalogOutV2 oldTinkerforgeDevice = tinkerforgeDevice;
        tinkerforgeDevice = newTinkerforgeDevice;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.MBRICKLET_ANALOG_OUT_V2__TINKERFORGE_DEVICE, oldTinkerforgeDevice, tinkerforgeDevice));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__IP_CONNECTION,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__CONNECTED_UID,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__POSITION,
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
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.MBRICKLET_ANALOG_OUT_V2__DEVICE_IDENTIFIER, oldDeviceIdentifier, deviceIdentifier));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__NAME, oldName,
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
        if (eContainerFeatureID() != ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD)
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
        if (eContainerFeatureID() != ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD)
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
        msgs = eBasicSetContainer((InternalEObject) newBrickd, ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD, msgs);
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
                || (eContainerFeatureID() != ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD && newBrickd != null)) {
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD,
                    newBrickd, newBrickd));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public DimmableConfiguration getTfConfig() {
        return tfConfig;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetTfConfig(DimmableConfiguration newTfConfig, NotificationChain msgs) {
        DimmableConfiguration oldTfConfig = tfConfig;
        tfConfig = newTfConfig;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG, oldTfConfig, newTfConfig);
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
    public void setTfConfig(DimmableConfiguration newTfConfig) {
        if (newTfConfig != tfConfig) {
            NotificationChain msgs = null;
            if (tfConfig != null)
                msgs = ((InternalEObject) tfConfig).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG, null, msgs);
            if (newTfConfig != null)
                msgs = ((InternalEObject) newTfConfig).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG, null, msgs);
            msgs = basicSetTfConfig(newTfConfig, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG,
                    newTfConfig, newTfConfig));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BigDecimal getMinValue() {
        return minValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMinValue(BigDecimal newMinValue) {
        BigDecimal oldMinValue = minValue;
        minValue = newMinValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE,
                    oldMinValue, minValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BigDecimal getMaxValue() {
        return maxValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMaxValue(BigDecimal newMaxValue) {
        BigDecimal oldMaxValue = maxValue;
        maxValue = newMaxValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE,
                    oldMaxValue, maxValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public PercentValue getPercentValue() {
        return percentValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setPercentValue(PercentValue newPercentValue) {
        PercentValue oldPercentValue = percentValue;
        percentValue = newPercentValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_ANALOG_OUT_V2__PERCENT_VALUE,
                    oldPercentValue, percentValue));
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
    public BigDecimal getMinValueDevice() {
        return minValueDevice;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMinValueDevice(BigDecimal newMinValueDevice) {
        BigDecimal oldMinValueDevice = minValueDevice;
        minValueDevice = newMinValueDevice;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE_DEVICE, oldMinValueDevice, minValueDevice));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BigDecimal getMaxValueDevice() {
        return maxValueDevice;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMaxValueDevice(BigDecimal newMaxValueDevice) {
        BigDecimal oldMaxValueDevice = maxValueDevice;
        maxValueDevice = newMaxValueDevice;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE_DEVICE, oldMaxValueDevice, maxValueDevice));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setValue(BigDecimal newValue, DeviceOptions opts) {
        logger.debug("setValue  {}", newValue);
        setPoint(newValue.intValue());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void setValue(PercentType newValue, DeviceOptions opts) {
        BigDecimal abs = maxValue.subtract(minValue);
        int newOut = abs.divide(new BigDecimal(100)).multiply(newValue.toBigDecimal()).add(minValue).intValue();
        logger.debug("setValue percentType {}", newOut);
        setPoint(newOut);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void dimm(IncreaseDecreaseType increaseDecrease, DeviceOptions opts) {
        Short step = Tools.getShortOpt(ConfigOptsDimmable.STEP.toString(), opts);
        if (step == null) {
            logger.error("dimmer option step is missing, items configuration has to be fixed!");
            return;
        }
        Integer newValue = null;
        if (increaseDecrease.equals(IncreaseDecreaseType.INCREASE)) {
            newValue = this.sensorValue.intValue() + step;
            if (newValue > maxValue.intValue()) {
                newValue = maxValue.intValue();
            }
        } else if (increaseDecrease.equals(IncreaseDecreaseType.DECREASE)) {
            newValue = this.sensorValue.intValue() - step;
            if (newValue > maxValue.intValue()) {
                newValue = maxValue.intValue();
            }
        }
        logger.debug("dimm increase decrease setting value {}", newValue);
        setPoint(newValue);
    }

    private boolean setPoint(Integer voltage) {
        try {
            if (voltage == null) {
                logger.error("voltage must not be null");
                return false;
            }
            if (voltage < minValue.intValue()) {
                logger.error("won't set voltage to value {} which is lower than minValue {}", voltage, minValue);
                return false;
            }
            if (voltage > maxValue.intValue()) {
                logger.error("won't set voltage to value {} which is higher than maxValue {}", voltage, maxValue);
                return false;
            }
            logger.debug("setting new voltage {}", voltage);
            tinkerforgeDevice.setOutputVoltage(voltage);
            fetchSensorValue();
            return true;
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
        return false;
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
        logger = LoggerFactory.getLogger(MBrickletAnalogOutV2Impl.class);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void enable() {
        minValue = minValueDevice;
        maxValue = maxValueDevice;
        if (tfConfig != null) {
            logger.debug("found tfConfig");
            if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("minValue"))) {
                if (tfConfig.getMinValue().compareTo(minValue) == 1) {
                    minValue = tfConfig.getMinValue();
                }
            }
            if (tfConfig.eIsSet(tfConfig.eClass().getEStructuralFeature("maxValue"))) {
                if (tfConfig.getMinValue().compareTo(maxValue) == -1) {
                    maxValue = tfConfig.getMaxValue();
                }
            }
        }
        tinkerforgeDevice = new BrickletAnalogOutV2(getUid(), getIpConnection());
        fetchSensorValue();
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
     * @generated NOT
     */
    @Override
    public void fetchSensorValue() {
        try {
            int value = tinkerforgeDevice.getOutputVoltage();
            setSensorValue(Tools.calculate(value));
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
    public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD:
                if (eInternalContainer() != null)
                    msgs = eBasicRemoveFromContainer(msgs);
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
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD:
                return basicSetBrickd(null, msgs);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG:
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
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD:
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
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__SENSOR_VALUE:
                return getSensorValue();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__LOGGER:
                return getLogger();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__UID:
                return getUid();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POLL:
                return isPoll();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__ENABLED_A:
                return getEnabledA();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TINKERFORGE_DEVICE:
                return getTinkerforgeDevice();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__IP_CONNECTION:
                return getIpConnection();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__CONNECTED_UID:
                return getConnectedUid();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POSITION:
                return getPosition();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__DEVICE_IDENTIFIER:
                return getDeviceIdentifier();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__NAME:
                return getName();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD:
                if (resolve)
                    return getBrickd();
                return basicGetBrickd();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG:
                return getTfConfig();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE:
                return getMinValue();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE:
                return getMaxValue();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__PERCENT_VALUE:
                return getPercentValue();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__DEVICE_TYPE:
                return getDeviceType();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE_DEVICE:
                return getMinValueDevice();
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE_DEVICE:
                return getMaxValueDevice();
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
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__SENSOR_VALUE:
                setSensorValue((DecimalValue) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__LOGGER:
                setLogger((Logger) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__UID:
                setUid((String) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POLL:
                setPoll((Boolean) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__ENABLED_A:
                setEnabledA((AtomicBoolean) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TINKERFORGE_DEVICE:
                setTinkerforgeDevice((BrickletAnalogOutV2) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__IP_CONNECTION:
                setIpConnection((IPConnection) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__CONNECTED_UID:
                setConnectedUid((String) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POSITION:
                setPosition((Character) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__DEVICE_IDENTIFIER:
                setDeviceIdentifier((Integer) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__NAME:
                setName((String) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD:
                setBrickd((MBrickd) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG:
                setTfConfig((DimmableConfiguration) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE:
                setMinValue((BigDecimal) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE:
                setMaxValue((BigDecimal) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__PERCENT_VALUE:
                setPercentValue((PercentValue) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE_DEVICE:
                setMinValueDevice((BigDecimal) newValue);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE_DEVICE:
                setMaxValueDevice((BigDecimal) newValue);
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
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__SENSOR_VALUE:
                setSensorValue((DecimalValue) null);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__LOGGER:
                setLogger(LOGGER_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__UID:
                setUid(UID_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POLL:
                setPoll(POLL_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__ENABLED_A:
                setEnabledA(ENABLED_A_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TINKERFORGE_DEVICE:
                setTinkerforgeDevice((BrickletAnalogOutV2) null);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__IP_CONNECTION:
                setIpConnection(IP_CONNECTION_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__CONNECTED_UID:
                setConnectedUid(CONNECTED_UID_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POSITION:
                setPosition(POSITION_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__DEVICE_IDENTIFIER:
                setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__NAME:
                setName(NAME_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD:
                setBrickd((MBrickd) null);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG:
                setTfConfig((DimmableConfiguration) null);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE:
                setMinValue(MIN_VALUE_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE:
                setMaxValue(MAX_VALUE_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__PERCENT_VALUE:
                setPercentValue(PERCENT_VALUE_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE_DEVICE:
                setMinValueDevice(MIN_VALUE_DEVICE_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE_DEVICE:
                setMaxValueDevice(MAX_VALUE_DEVICE_EDEFAULT);
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
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__SENSOR_VALUE:
                return sensorValue != null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__LOGGER:
                return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__UID:
                return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POLL:
                return poll != POLL_EDEFAULT;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__ENABLED_A:
                return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TINKERFORGE_DEVICE:
                return tinkerforgeDevice != null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__IP_CONNECTION:
                return IP_CONNECTION_EDEFAULT == null ? ipConnection != null
                        : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__CONNECTED_UID:
                return CONNECTED_UID_EDEFAULT == null ? connectedUid != null
                        : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POSITION:
                return position != POSITION_EDEFAULT;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__DEVICE_IDENTIFIER:
                return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD:
                return basicGetBrickd() != null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG:
                return tfConfig != null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE:
                return MIN_VALUE_EDEFAULT == null ? minValue != null : !MIN_VALUE_EDEFAULT.equals(minValue);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE:
                return MAX_VALUE_EDEFAULT == null ? maxValue != null : !MAX_VALUE_EDEFAULT.equals(maxValue);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__PERCENT_VALUE:
                return PERCENT_VALUE_EDEFAULT == null ? percentValue != null
                        : !PERCENT_VALUE_EDEFAULT.equals(percentValue);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__DEVICE_TYPE:
                return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE_DEVICE:
                return MIN_VALUE_DEVICE_EDEFAULT == null ? minValueDevice != null
                        : !MIN_VALUE_DEVICE_EDEFAULT.equals(minValueDevice);
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE_DEVICE:
                return MAX_VALUE_DEVICE_EDEFAULT == null ? maxValueDevice != null
                        : !MAX_VALUE_DEVICE_EDEFAULT.equals(maxValueDevice);
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
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__LOGGER:
                    return ModelPackage.MBASE_DEVICE__LOGGER;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__UID:
                    return ModelPackage.MBASE_DEVICE__UID;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POLL:
                    return ModelPackage.MBASE_DEVICE__POLL;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__ENABLED_A:
                    return ModelPackage.MBASE_DEVICE__ENABLED_A;
                default:
                    return -1;
            }
        }
        if (baseClass == MDevice.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TINKERFORGE_DEVICE:
                    return ModelPackage.MDEVICE__TINKERFORGE_DEVICE;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__IP_CONNECTION:
                    return ModelPackage.MDEVICE__IP_CONNECTION;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__CONNECTED_UID:
                    return ModelPackage.MDEVICE__CONNECTED_UID;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__POSITION:
                    return ModelPackage.MDEVICE__POSITION;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__DEVICE_IDENTIFIER:
                    return ModelPackage.MDEVICE__DEVICE_IDENTIFIER;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__NAME:
                    return ModelPackage.MDEVICE__NAME;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD:
                    return ModelPackage.MDEVICE__BRICKD;
                default:
                    return -1;
            }
        }
        if (baseClass == MTFConfigConsumer.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG:
                    return ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG;
                default:
                    return -1;
            }
        }
        if (baseClass == DimmableActor.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE:
                    return ModelPackage.DIMMABLE_ACTOR__MIN_VALUE;
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE:
                    return ModelPackage.DIMMABLE_ACTOR__MAX_VALUE;
                default:
                    return -1;
            }
        }
        if (baseClass == PercentTypeActor.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_ANALOG_OUT_V2__PERCENT_VALUE:
                    return ModelPackage.PERCENT_TYPE_ACTOR__PERCENT_VALUE;
                default:
                    return -1;
            }
        }
        if (baseClass == SetPointActor.class) {
            switch (derivedFeatureID) {
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
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__LOGGER;
                case ModelPackage.MBASE_DEVICE__UID:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__UID;
                case ModelPackage.MBASE_DEVICE__POLL:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__POLL;
                case ModelPackage.MBASE_DEVICE__ENABLED_A:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__ENABLED_A;
                default:
                    return -1;
            }
        }
        if (baseClass == MDevice.class) {
            switch (baseFeatureID) {
                case ModelPackage.MDEVICE__TINKERFORGE_DEVICE:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__TINKERFORGE_DEVICE;
                case ModelPackage.MDEVICE__IP_CONNECTION:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__IP_CONNECTION;
                case ModelPackage.MDEVICE__CONNECTED_UID:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__CONNECTED_UID;
                case ModelPackage.MDEVICE__POSITION:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__POSITION;
                case ModelPackage.MDEVICE__DEVICE_IDENTIFIER:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__DEVICE_IDENTIFIER;
                case ModelPackage.MDEVICE__NAME:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__NAME;
                case ModelPackage.MDEVICE__BRICKD:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__BRICKD;
                default:
                    return -1;
            }
        }
        if (baseClass == MTFConfigConsumer.class) {
            switch (baseFeatureID) {
                case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__TF_CONFIG;
                default:
                    return -1;
            }
        }
        if (baseClass == DimmableActor.class) {
            switch (baseFeatureID) {
                case ModelPackage.DIMMABLE_ACTOR__MIN_VALUE:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__MIN_VALUE;
                case ModelPackage.DIMMABLE_ACTOR__MAX_VALUE:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__MAX_VALUE;
                default:
                    return -1;
            }
        }
        if (baseClass == PercentTypeActor.class) {
            switch (baseFeatureID) {
                case ModelPackage.PERCENT_TYPE_ACTOR__PERCENT_VALUE:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2__PERCENT_VALUE;
                default:
                    return -1;
            }
        }
        if (baseClass == SetPointActor.class) {
            switch (baseFeatureID) {
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
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2___INIT;
                case ModelPackage.MBASE_DEVICE___ENABLE:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2___ENABLE;
                case ModelPackage.MBASE_DEVICE___DISABLE:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2___DISABLE;
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
        if (baseClass == DimmableActor.class) {
            switch (baseOperationID) {
                case ModelPackage.DIMMABLE_ACTOR___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS;
                default:
                    return -1;
            }
        }
        if (baseClass == PercentTypeActor.class) {
            switch (baseOperationID) {
                case ModelPackage.PERCENT_TYPE_ACTOR___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS;
                default:
                    return -1;
            }
        }
        if (baseClass == SetPointActor.class) {
            switch (baseOperationID) {
                case ModelPackage.SET_POINT_ACTOR___SET_VALUE__BIGDECIMAL_DEVICEOPTIONS:
                    return ModelPackage.MBRICKLET_ANALOG_OUT_V2___SET_VALUE__BIGDECIMAL_DEVICEOPTIONS;
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
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2___SET_VALUE__BIGDECIMAL_DEVICEOPTIONS:
                setValue((BigDecimal) arguments.get(0), (DeviceOptions) arguments.get(1));
                return null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2___SET_VALUE__PERCENTTYPE_DEVICEOPTIONS:
                setValue((PercentType) arguments.get(0), (DeviceOptions) arguments.get(1));
                return null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2___DIMM__INCREASEDECREASETYPE_DEVICEOPTIONS:
                dimm((IncreaseDecreaseType) arguments.get(0), (DeviceOptions) arguments.get(1));
                return null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2___INIT:
                init();
                return null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2___ENABLE:
                enable();
                return null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2___DISABLE:
                disable();
                return null;
            case ModelPackage.MBRICKLET_ANALOG_OUT_V2___FETCH_SENSOR_VALUE:
                fetchSensorValue();
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
        result.append(" (sensorValue: ");
        result.append(sensorValue);
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
        result.append(", minValue: ");
        result.append(minValue);
        result.append(", maxValue: ");
        result.append(maxValue);
        result.append(", percentValue: ");
        result.append(percentValue);
        result.append(", deviceType: ");
        result.append(deviceType);
        result.append(", minValueDevice: ");
        result.append(minValueDevice);
        result.append(", maxValueDevice: ");
        result.append(maxValueDevice);
        result.append(')');
        return result.toString();
    }

} // MBrickletAnalogOutV2Impl
