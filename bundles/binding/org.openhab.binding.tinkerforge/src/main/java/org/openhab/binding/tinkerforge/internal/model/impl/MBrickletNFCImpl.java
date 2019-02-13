/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import org.openhab.binding.tinkerforge.internal.model.MBrickletNFC;
import org.openhab.binding.tinkerforge.internal.model.MNFCID;
import org.openhab.binding.tinkerforge.internal.model.MNFCNDEFRecordListener;
import org.openhab.binding.tinkerforge.internal.model.MNFCSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MNFCTagInfoListener;
import org.openhab.binding.tinkerforge.internal.model.MNFCText;
import org.openhab.binding.tinkerforge.internal.model.MNFCTrigger;
import org.openhab.binding.tinkerforge.internal.model.MNFCUri;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer;
import org.openhab.binding.tinkerforge.internal.model.ModelFactory;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.NFCConfiguration;
import org.openhab.binding.tinkerforge.internal.tools.NDEFRecord;
import org.openhab.binding.tinkerforge.internal.tools.NDEFRecord.NDEFParseException;
import org.openhab.binding.tinkerforge.internal.tools.NFCTagInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletNFC;
import com.tinkerforge.BrickletNFC.ReaderGetTagID;
import com.tinkerforge.BrickletNFC.ReaderStateChangedListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.StreamOutOfSyncException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MBricklet NFC</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getLogger <em>Logger</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getUid <em>Uid</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#isPoll <em>Poll</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getEnabledA <em>Enabled A</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getTinkerforgeDevice <em>Tinkerforge
 * Device</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getIpConnection <em>Ip
 * Connection</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getConnectedUid <em>Connected
 * Uid</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getPosition <em>Position</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getDeviceIdentifier <em>Device
 * Identifier</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getName <em>Name</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getBrickd <em>Brickd</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getMsubdevices
 * <em>Msubdevices</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getTfConfig <em>Tf Config</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getDeviceType <em>Device
 * Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getTextSubId <em>Text Sub
 * Id</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getUriSubId <em>Uri Sub Id</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getIdSubId <em>Id Sub Id</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MBrickletNFCImpl#getTriggerSubId <em>Trigger Sub
 * Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MBrickletNFCImpl extends MinimalEObjectImpl.Container implements MBrickletNFC {
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
    protected BrickletNFC tinkerforgeDevice;

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
     * The cached value of the '{@link #getMsubdevices() <em>Msubdevices</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getMsubdevices()
     * @generated
     * @ordered
     */
    protected EList<MNFCSubDevice> msubdevices;

    /**
     * The cached value of the '{@link #getTfConfig() <em>Tf Config</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getTfConfig()
     * @generated
     * @ordered
     */
    protected NFCConfiguration tfConfig;

    /**
     * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getDeviceType()
     * @generated
     * @ordered
     */
    protected static final String DEVICE_TYPE_EDEFAULT = "bricklet_nfc";

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
     * The default value of the '{@link #getTextSubId() <em>Text Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getTextSubId()
     * @generated
     * @ordered
     */
    protected static final String TEXT_SUB_ID_EDEFAULT = "text";

    /**
     * The cached value of the '{@link #getTextSubId() <em>Text Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getTextSubId()
     * @generated
     * @ordered
     */
    protected String textSubId = TEXT_SUB_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getUriSubId() <em>Uri Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getUriSubId()
     * @generated
     * @ordered
     */
    protected static final String URI_SUB_ID_EDEFAULT = "uri";

    /**
     * The cached value of the '{@link #getUriSubId() <em>Uri Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getUriSubId()
     * @generated
     * @ordered
     */
    protected String uriSubId = URI_SUB_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getIdSubId() <em>Id Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getIdSubId()
     * @generated
     * @ordered
     */
    protected static final String ID_SUB_ID_EDEFAULT = "id";

    /**
     * The cached value of the '{@link #getIdSubId() <em>Id Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getIdSubId()
     * @generated
     * @ordered
     */
    protected String idSubId = ID_SUB_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getTriggerSubId() <em>Trigger Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getTriggerSubId()
     * @generated
     * @ordered
     */
    protected static final String TRIGGER_SUB_ID_EDEFAULT = "trigger";

    /**
     * The cached value of the '{@link #getTriggerSubId() <em>Trigger Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getTriggerSubId()
     * @generated
     * @ordered
     */
    protected String triggerSubId = TRIGGER_SUB_ID_EDEFAULT;

    private List<MNFCNDEFRecordListener> recordListeners = new ArrayList<>();
    private List<MNFCTagInfoListener> tagInfoListeners = new ArrayList<>();

    private volatile boolean scanTriggered = false;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    protected MBrickletNFCImpl() {
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
        return ModelPackage.Literals.MBRICKLET_NFC;
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__LOGGER, oldLogger,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__UID, oldUid, uid));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__POLL, oldPoll, poll));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__ENABLED_A, oldEnabledA,
                    enabledA));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public BrickletNFC getTinkerforgeDevice() {
        return tinkerforgeDevice;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setTinkerforgeDevice(BrickletNFC newTinkerforgeDevice) {
        BrickletNFC oldTinkerforgeDevice = tinkerforgeDevice;
        tinkerforgeDevice = newTinkerforgeDevice;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__TINKERFORGE_DEVICE,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__IP_CONNECTION,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__CONNECTED_UID,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__POSITION, oldPosition,
                    position));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__DEVICE_IDENTIFIER,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__NAME, oldName, name));
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
        if (eContainerFeatureID() != ModelPackage.MBRICKLET_NFC__BRICKD) {
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
        if (eContainerFeatureID() != ModelPackage.MBRICKLET_NFC__BRICKD) {
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
        msgs = eBasicSetContainer((InternalEObject) newBrickd, ModelPackage.MBRICKLET_NFC__BRICKD, msgs);
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
                || (eContainerFeatureID() != ModelPackage.MBRICKLET_NFC__BRICKD && newBrickd != null)) {
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__BRICKD, newBrickd,
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
    public EList<MNFCSubDevice> getMsubdevices() {
        if (msubdevices == null) {
            msubdevices = new EObjectContainmentWithInverseEList<MNFCSubDevice>(MNFCSubDevice.class, this,
                    ModelPackage.MBRICKLET_NFC__MSUBDEVICES, ModelPackage.MSUB_DEVICE__MBRICK) {
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
    public NFCConfiguration getTfConfig() {
        return tfConfig;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetTfConfig(NFCConfiguration newTfConfig, NotificationChain msgs) {
        NFCConfiguration oldTfConfig = tfConfig;
        tfConfig = newTfConfig;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    ModelPackage.MBRICKLET_NFC__TF_CONFIG, oldTfConfig, newTfConfig);
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
    public void setTfConfig(NFCConfiguration newTfConfig) {
        if (newTfConfig != tfConfig) {
            NotificationChain msgs = null;
            if (tfConfig != null) {
                msgs = ((InternalEObject) tfConfig).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_NFC__TF_CONFIG, null, msgs);
            }
            if (newTfConfig != null) {
                msgs = ((InternalEObject) newTfConfig).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - ModelPackage.MBRICKLET_NFC__TF_CONFIG, null, msgs);
            }
            msgs = basicSetTfConfig(newTfConfig, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MBRICKLET_NFC__TF_CONFIG, newTfConfig,
                    newTfConfig));
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
    public String getTextSubId() {
        return textSubId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getUriSubId() {
        return uriSubId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getIdSubId() {
        return idSubId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getTriggerSubId() {
        return triggerSubId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void addNDEFRecordListener(MNFCNDEFRecordListener listener) {
        if (!recordListeners.contains(listener)) {
            recordListeners.add(listener);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void removeNDEFRecordListener(MNFCNDEFRecordListener listener) {
        recordListeners.remove(listener);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void addTagInfoListener(MNFCTagInfoListener listener) {
        if (!tagInfoListeners.contains(listener)) {
            tagInfoListeners.add(listener);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void removeTagInfoListener(MNFCTagInfoListener listener) {
        tagInfoListeners.remove(listener);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean triggerScan() {
        if (getTfConfig().isTriggeredScan()) {
            return triggerScanIfReady();
        } else {
            logger.error(
                    "to start the scan with a switch, you have to set 'triggeredScan' to 'true' in the tinkerforge.cfg");
        }
        return false;
    }

    private boolean triggerScanIfReady() {
        try {
            scanTriggered = true;
            tinkerforgeDevice.setMode(BrickletNFC.MODE_READER);
            return true;
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
        scanTriggered = false;
        return false;
    }

    private void notifyNDEFRecordListeners(NDEFRecord record) {
        for (MNFCNDEFRecordListener listener : recordListeners) {
            listener.handleNDEFRecord(record);
        }
    }

    private void notifyNFCTagInfoListeners(NFCTagInfo tagInfo) {
        for (MNFCTagInfoListener listener : tagInfoListeners) {
            listener.handleTagInfo(tagInfo);
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void initSubDevices() {
        ModelFactory factory = ModelFactory.eINSTANCE;
        MNFCID nfcid = factory.createMNFCID();
        nfcid.setUid(uid);
        nfcid.setSubId(idSubId);
        logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, idSubId);
        nfcid.init();
        nfcid.setMbrick(this);

        MNFCText nfcText = factory.createMNFCText();
        nfcText.setUid(uid);
        nfcText.setSubId(textSubId);
        logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, textSubId);
        nfcText.init();
        nfcText.setMbrick(this);

        MNFCUri nfcUri = factory.createMNFCUri();
        nfcUri.setUid(uid);
        nfcUri.setSubId(uriSubId);
        logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, uriSubId);
        nfcUri.init();
        nfcUri.setMbrick(this);

        MNFCTrigger nfcTrigger = factory.createMNFCTrigger();
        nfcTrigger.setUid(uid);
        nfcTrigger.setSubId(triggerSubId);
        logger.debug("{} addSubDevice {}", LoggerConstants.TFINIT, triggerSubId);
        nfcTrigger.init();
        nfcTrigger.setMbrick(this);
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
        logger = LoggerFactory.getLogger(MBrickletNFCImpl.class);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void enable() {
        tinkerforgeDevice = new BrickletNFC(getUid(), getIpConnection());
        tinkerforgeDevice.addReaderStateChangedListener(readerStateChangedListener);

        try {
            tinkerforgeDevice.setDetectionLEDConfig(BrickletNFC.DETECTION_LED_CONFIG_OFF);
            // After setting the mode to reader, the nfc device will go to idle and begin with the scanning if
            // triggerScan is set to false
            tinkerforgeDevice.setMode(BrickletNFC.MODE_READER);
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
    }

    private ReaderStateChangedListener readerStateChangedListener = new ReaderStateChangedListener() {
        @Override
        public void readerStateChanged(int state, boolean idle) {
            if (state == BrickletNFC.READER_STATE_IDLE) {
                if (!getTfConfig().isTriggeredScan() || scanTriggered) {
                    scanTriggered = false;
                    requestTagID();
                }
            } else if (state == BrickletNFC.READER_STATE_REQUEST_TAG_ID_READY) {
                readTagID();
                requestNDEF();
            } else if (state == BrickletNFC.READER_STATE_REQUEST_NDEF_READY) {
                readNDEF();
                if (!getTfConfig().isTriggeredScan()) {
                    requestTagIDAfterWait();
                }
            } else if (state == BrickletNFC.READER_STATE_REQUEST_TAG_ID_ERROR) {
                requestTagID();
            } else if (state == BrickletNFC.READER_STATE_REQUEST_NDEF_ERROR) {
                requestTagID();
            }
        }
    };

    private void requestTagIDAfterWait() {
        try {
            Thread.sleep(getTfConfig().getDelayAfterScan());
        } catch (InterruptedException e) {
            logger.error("waiting was interrupted: {}", e.getMessage());
        }
        requestTagID();
    }

    private void readNDEF() {
        try {
            // tinkerforge creates buffer for each read request
            int[] buffer = tinkerforgeDevice.readerReadNDEF();
            if (buffer == null) {
                logger.error("read empty buffer");
            } else {
                NDEFRecord record = NDEFRecord.fromBuffer(buffer);
                tinkerforgeDevice.setDetectionLEDConfig(BrickletNFC.DETECTION_LED_CONFIG_OFF);
                logger.trace("found NDEF Record from type '{}'", record.getType());
                notifyNDEFRecordListeners(record);
            }
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        } catch (StreamOutOfSyncException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_STREAM_OUT_OF_SYNC_EXCPETION, e);
        } catch (NDEFParseException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NDEF_PARSE_EXCEPTION, e);
        }
    }

    private void requestNDEF() {
        try {
            tinkerforgeDevice.readerRequestNDEF();
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
    }

    private boolean requestTagID() {
        try {
            tinkerforgeDevice.setDetectionLEDConfig(BrickletNFC.DETECTION_LED_CONFIG_ON);
            tinkerforgeDevice.readerRequestTagID();
            return true;
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
        return false;
    }

    private void readTagID() {
        try {
            ReaderGetTagID ret = tinkerforgeDevice.readerGetTagID();
            NFCTagInfo tagInfo = NFCTagInfo.fromReaderGetTagID(ret);
            logger.trace("found tag with id '{}'", tagInfo.getTagIdAsHex());
            notifyNFCTagInfoListeners(tagInfo);
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
        tinkerforgeDevice.removeReaderStateChangedListener(readerStateChangedListener);
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
            case ModelPackage.MBRICKLET_NFC__BRICKD:
                if (eInternalContainer() != null) {
                    msgs = eBasicRemoveFromContainer(msgs);
                }
                return basicSetBrickd((MBrickd) otherEnd, msgs);
            case ModelPackage.MBRICKLET_NFC__MSUBDEVICES:
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
            case ModelPackage.MBRICKLET_NFC__BRICKD:
                return basicSetBrickd(null, msgs);
            case ModelPackage.MBRICKLET_NFC__MSUBDEVICES:
                return ((InternalEList<?>) getMsubdevices()).basicRemove(otherEnd, msgs);
            case ModelPackage.MBRICKLET_NFC__TF_CONFIG:
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
            case ModelPackage.MBRICKLET_NFC__BRICKD:
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
            case ModelPackage.MBRICKLET_NFC__LOGGER:
                return getLogger();
            case ModelPackage.MBRICKLET_NFC__UID:
                return getUid();
            case ModelPackage.MBRICKLET_NFC__POLL:
                return isPoll();
            case ModelPackage.MBRICKLET_NFC__ENABLED_A:
                return getEnabledA();
            case ModelPackage.MBRICKLET_NFC__TINKERFORGE_DEVICE:
                return getTinkerforgeDevice();
            case ModelPackage.MBRICKLET_NFC__IP_CONNECTION:
                return getIpConnection();
            case ModelPackage.MBRICKLET_NFC__CONNECTED_UID:
                return getConnectedUid();
            case ModelPackage.MBRICKLET_NFC__POSITION:
                return getPosition();
            case ModelPackage.MBRICKLET_NFC__DEVICE_IDENTIFIER:
                return getDeviceIdentifier();
            case ModelPackage.MBRICKLET_NFC__NAME:
                return getName();
            case ModelPackage.MBRICKLET_NFC__BRICKD:
                if (resolve) {
                    return getBrickd();
                }
                return basicGetBrickd();
            case ModelPackage.MBRICKLET_NFC__MSUBDEVICES:
                return getMsubdevices();
            case ModelPackage.MBRICKLET_NFC__TF_CONFIG:
                return getTfConfig();
            case ModelPackage.MBRICKLET_NFC__DEVICE_TYPE:
                return getDeviceType();
            case ModelPackage.MBRICKLET_NFC__TEXT_SUB_ID:
                return getTextSubId();
            case ModelPackage.MBRICKLET_NFC__URI_SUB_ID:
                return getUriSubId();
            case ModelPackage.MBRICKLET_NFC__ID_SUB_ID:
                return getIdSubId();
            case ModelPackage.MBRICKLET_NFC__TRIGGER_SUB_ID:
                return getTriggerSubId();
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
            case ModelPackage.MBRICKLET_NFC__LOGGER:
                setLogger((Logger) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__UID:
                setUid((String) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__POLL:
                setPoll((Boolean) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__ENABLED_A:
                setEnabledA((AtomicBoolean) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__TINKERFORGE_DEVICE:
                setTinkerforgeDevice((BrickletNFC) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__IP_CONNECTION:
                setIpConnection((IPConnection) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__CONNECTED_UID:
                setConnectedUid((String) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__POSITION:
                setPosition((Character) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__DEVICE_IDENTIFIER:
                setDeviceIdentifier((Integer) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__NAME:
                setName((String) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__BRICKD:
                setBrickd((MBrickd) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__MSUBDEVICES:
                getMsubdevices().clear();
                getMsubdevices().addAll((Collection<? extends MNFCSubDevice>) newValue);
                return;
            case ModelPackage.MBRICKLET_NFC__TF_CONFIG:
                setTfConfig((NFCConfiguration) newValue);
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
            case ModelPackage.MBRICKLET_NFC__LOGGER:
                setLogger(LOGGER_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_NFC__UID:
                setUid(UID_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_NFC__POLL:
                setPoll(POLL_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_NFC__ENABLED_A:
                setEnabledA(ENABLED_A_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_NFC__TINKERFORGE_DEVICE:
                setTinkerforgeDevice((BrickletNFC) null);
                return;
            case ModelPackage.MBRICKLET_NFC__IP_CONNECTION:
                setIpConnection(IP_CONNECTION_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_NFC__CONNECTED_UID:
                setConnectedUid(CONNECTED_UID_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_NFC__POSITION:
                setPosition(POSITION_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_NFC__DEVICE_IDENTIFIER:
                setDeviceIdentifier(DEVICE_IDENTIFIER_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_NFC__NAME:
                setName(NAME_EDEFAULT);
                return;
            case ModelPackage.MBRICKLET_NFC__BRICKD:
                setBrickd((MBrickd) null);
                return;
            case ModelPackage.MBRICKLET_NFC__MSUBDEVICES:
                getMsubdevices().clear();
                return;
            case ModelPackage.MBRICKLET_NFC__TF_CONFIG:
                setTfConfig((NFCConfiguration) null);
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
            case ModelPackage.MBRICKLET_NFC__LOGGER:
                return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
            case ModelPackage.MBRICKLET_NFC__UID:
                return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
            case ModelPackage.MBRICKLET_NFC__POLL:
                return poll != POLL_EDEFAULT;
            case ModelPackage.MBRICKLET_NFC__ENABLED_A:
                return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
            case ModelPackage.MBRICKLET_NFC__TINKERFORGE_DEVICE:
                return tinkerforgeDevice != null;
            case ModelPackage.MBRICKLET_NFC__IP_CONNECTION:
                return IP_CONNECTION_EDEFAULT == null ? ipConnection != null
                        : !IP_CONNECTION_EDEFAULT.equals(ipConnection);
            case ModelPackage.MBRICKLET_NFC__CONNECTED_UID:
                return CONNECTED_UID_EDEFAULT == null ? connectedUid != null
                        : !CONNECTED_UID_EDEFAULT.equals(connectedUid);
            case ModelPackage.MBRICKLET_NFC__POSITION:
                return position != POSITION_EDEFAULT;
            case ModelPackage.MBRICKLET_NFC__DEVICE_IDENTIFIER:
                return deviceIdentifier != DEVICE_IDENTIFIER_EDEFAULT;
            case ModelPackage.MBRICKLET_NFC__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case ModelPackage.MBRICKLET_NFC__BRICKD:
                return basicGetBrickd() != null;
            case ModelPackage.MBRICKLET_NFC__MSUBDEVICES:
                return msubdevices != null && !msubdevices.isEmpty();
            case ModelPackage.MBRICKLET_NFC__TF_CONFIG:
                return tfConfig != null;
            case ModelPackage.MBRICKLET_NFC__DEVICE_TYPE:
                return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
            case ModelPackage.MBRICKLET_NFC__TEXT_SUB_ID:
                return TEXT_SUB_ID_EDEFAULT == null ? textSubId != null : !TEXT_SUB_ID_EDEFAULT.equals(textSubId);
            case ModelPackage.MBRICKLET_NFC__URI_SUB_ID:
                return URI_SUB_ID_EDEFAULT == null ? uriSubId != null : !URI_SUB_ID_EDEFAULT.equals(uriSubId);
            case ModelPackage.MBRICKLET_NFC__ID_SUB_ID:
                return ID_SUB_ID_EDEFAULT == null ? idSubId != null : !ID_SUB_ID_EDEFAULT.equals(idSubId);
            case ModelPackage.MBRICKLET_NFC__TRIGGER_SUB_ID:
                return TRIGGER_SUB_ID_EDEFAULT == null ? triggerSubId != null
                        : !TRIGGER_SUB_ID_EDEFAULT.equals(triggerSubId);
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
        if (baseClass == MSubDeviceHolder.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_NFC__MSUBDEVICES:
                    return ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES;
                default:
                    return -1;
            }
        }
        if (baseClass == MTFConfigConsumer.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MBRICKLET_NFC__TF_CONFIG:
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
        if (baseClass == MSubDeviceHolder.class) {
            switch (baseFeatureID) {
                case ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES:
                    return ModelPackage.MBRICKLET_NFC__MSUBDEVICES;
                default:
                    return -1;
            }
        }
        if (baseClass == MTFConfigConsumer.class) {
            switch (baseFeatureID) {
                case ModelPackage.MTF_CONFIG_CONSUMER__TF_CONFIG:
                    return ModelPackage.MBRICKLET_NFC__TF_CONFIG;
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
        if (baseClass == MSubDeviceHolder.class) {
            switch (baseOperationID) {
                case ModelPackage.MSUB_DEVICE_HOLDER___INIT_SUB_DEVICES:
                    return ModelPackage.MBRICKLET_NFC___INIT_SUB_DEVICES;
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
            case ModelPackage.MBRICKLET_NFC___ADD_NDEF_RECORD_LISTENER__MNFCNDEFRECORDLISTENER:
                addNDEFRecordListener((MNFCNDEFRecordListener) arguments.get(0));
                return null;
            case ModelPackage.MBRICKLET_NFC___REMOVE_NDEF_RECORD_LISTENER__MNFCNDEFRECORDLISTENER:
                removeNDEFRecordListener((MNFCNDEFRecordListener) arguments.get(0));
                return null;
            case ModelPackage.MBRICKLET_NFC___ADD_TAG_INFO_LISTENER__MNFCTAGINFOLISTENER:
                addTagInfoListener((MNFCTagInfoListener) arguments.get(0));
                return null;
            case ModelPackage.MBRICKLET_NFC___REMOVE_TAG_INFO_LISTENER__MNFCTAGINFOLISTENER:
                removeTagInfoListener((MNFCTagInfoListener) arguments.get(0));
                return null;
            case ModelPackage.MBRICKLET_NFC___TRIGGER_SCAN:
                return triggerScan();
            case ModelPackage.MBRICKLET_NFC___INIT_SUB_DEVICES:
                initSubDevices();
                return null;
            case ModelPackage.MBRICKLET_NFC___INIT:
                init();
                return null;
            case ModelPackage.MBRICKLET_NFC___ENABLE:
                enable();
                return null;
            case ModelPackage.MBRICKLET_NFC___DISABLE:
                disable();
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

        StringBuilder result = new StringBuilder(super.toString());
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
        result.append(", textSubId: ");
        result.append(textSubId);
        result.append(", uriSubId: ");
        result.append(uriSubId);
        result.append(", idSubId: ");
        result.append(idSubId);
        result.append(", triggerSubId: ");
        result.append(triggerSubId);
        result.append(')');
        return result.toString();
    }

} // MBrickletNFCImpl
