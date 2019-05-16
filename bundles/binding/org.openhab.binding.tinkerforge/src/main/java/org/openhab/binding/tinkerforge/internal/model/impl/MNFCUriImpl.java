/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.openhab.binding.tinkerforge.internal.model.MBrickletNFC;
import org.openhab.binding.tinkerforge.internal.model.MNFCNDEFRecordListener;
import org.openhab.binding.tinkerforge.internal.model.MNFCUri;
import org.openhab.binding.tinkerforge.internal.model.MSensor;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.NFCConfiguration;
import org.openhab.binding.tinkerforge.internal.tools.NDEFRecord;
import org.openhab.binding.tinkerforge.internal.tools.NDEFRecord.SmartPosterRecord;
import org.openhab.binding.tinkerforge.internal.tools.NDEFRecord.UriRecord;
import org.openhab.binding.tinkerforge.internal.types.StringValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MNFC Uri</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MNFCUriImpl#getLogger <em>Logger</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MNFCUriImpl#getUid <em>Uid</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MNFCUriImpl#isPoll <em>Poll</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MNFCUriImpl#getEnabledA <em>Enabled A</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MNFCUriImpl#getSubId <em>Sub Id</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MNFCUriImpl#getMbrick <em>Mbrick</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MNFCUriImpl#getSensorValue <em>Sensor Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MNFCUriImpl extends MinimalEObjectImpl.Container implements MNFCUri {
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
     * The default value of the '{@link #getSubId() <em>Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getSubId()
     * @generated
     * @ordered
     */
    protected static final String SUB_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSubId() <em>Sub Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getSubId()
     * @generated
     * @ordered
     */
    protected String subId = SUB_ID_EDEFAULT;

    /**
     * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getSensorValue()
     * @generated
     * @ordered
     */
    protected StringValue sensorValue;

    private NFCConfiguration nfcConfiguration;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    protected MNFCUriImpl() {
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
        return ModelPackage.Literals.MNFC_URI;
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MNFC_URI__LOGGER, oldLogger, logger));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MNFC_URI__UID, oldUid, uid));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MNFC_URI__POLL, oldPoll, poll));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MNFC_URI__ENABLED_A, oldEnabledA,
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
    public String getSubId() {
        return subId;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSubId(String newSubId) {
        String oldSubId = subId;
        subId = newSubId;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MNFC_URI__SUB_ID, oldSubId, subId));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MBrickletNFC getMbrick() {
        if (eContainerFeatureID() != ModelPackage.MNFC_URI__MBRICK) {
            return null;
        }
        return (MBrickletNFC) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public MBrickletNFC basicGetMbrick() {
        if (eContainerFeatureID() != ModelPackage.MNFC_URI__MBRICK) {
            return null;
        }
        return (MBrickletNFC) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetMbrick(MBrickletNFC newMbrick, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newMbrick, ModelPackage.MNFC_URI__MBRICK, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setMbrick(MBrickletNFC newMbrick) {
        if (newMbrick != eInternalContainer()
                || (eContainerFeatureID() != ModelPackage.MNFC_URI__MBRICK && newMbrick != null)) {
            if (EcoreUtil.isAncestor(this, newMbrick)) {
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            }
            NotificationChain msgs = null;
            if (eInternalContainer() != null) {
                msgs = eBasicRemoveFromContainer(msgs);
            }
            if (newMbrick != null) {
                msgs = ((InternalEObject) newMbrick).eInverseAdd(this, ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES,
                        MSubDeviceHolder.class, msgs);
            }
            msgs = basicSetMbrick(newMbrick, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MNFC_URI__MBRICK, newMbrick, newMbrick));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public StringValue getSensorValue() {
        return sensorValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSensorValue(StringValue newSensorValue) {
        StringValue oldSensorValue = sensorValue;
        sensorValue = newSensorValue;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MNFC_URI__SENSOR_VALUE, oldSensorValue,
                    sensorValue));
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void fetchSensorValue() {

    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void handleNDEFRecord(NDEFRecord record) {
        if (record == null && nfcConfiguration.isClearValuesOnError()) {
            setSensorValue(null);
            return;
        }

        if (record == null && !nfcConfiguration.isClearValuesOnError()) {
            logger.error("ignoring reader error");
            return;
        }

        Optional<String> uriValue = getOptionalUriFromRecord(record);
        if (!setUriValueIfPresent(uriValue) && nfcConfiguration.isResetOldValues()) {
            // resets the current state
            setSensorValue(new StringValue(""));
        }
    }

    private boolean setUriValueIfPresent(Optional<String> uriValue) {
        if (uriValue.isPresent()) {
            String value = uriValue.get();
            value = value == null ? "" : value;
            setSensorValue(new StringValue(value));
        }
        return uriValue.isPresent();
    }

    private static Optional<String> getOptionalUriFromRecord(NDEFRecord record) {
        Optional<String> uriValue = Optional.empty();
        if (record instanceof UriRecord) {
            uriValue = Optional.ofNullable(((UriRecord) record).getCompleteUri());
        } else if (record instanceof SmartPosterRecord) {
            SmartPosterRecord spr = (SmartPosterRecord) record;
            String uri = spr.getUriRecord().getCompleteUri();
            uriValue = Optional.ofNullable(uri);
        }
        return uriValue;
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
        logger = LoggerFactory.getLogger(MNFCUriImpl.class);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void enable() {
        nfcConfiguration = getMbrick().getTfConfig();
        setPoll(false);
        getMbrick().addNDEFRecordListener(this);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void disable() {
        getMbrick().removeNDEFRecordListener(this);
        nfcConfiguration = null;
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
            case ModelPackage.MNFC_URI__MBRICK:
                if (eInternalContainer() != null) {
                    msgs = eBasicRemoveFromContainer(msgs);
                }
                return basicSetMbrick((MBrickletNFC) otherEnd, msgs);
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
            case ModelPackage.MNFC_URI__MBRICK:
                return basicSetMbrick(null, msgs);
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
            case ModelPackage.MNFC_URI__MBRICK:
                return eInternalContainer().eInverseRemove(this, ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES,
                        MSubDeviceHolder.class, msgs);
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
            case ModelPackage.MNFC_URI__LOGGER:
                return getLogger();
            case ModelPackage.MNFC_URI__UID:
                return getUid();
            case ModelPackage.MNFC_URI__POLL:
                return isPoll();
            case ModelPackage.MNFC_URI__ENABLED_A:
                return getEnabledA();
            case ModelPackage.MNFC_URI__SUB_ID:
                return getSubId();
            case ModelPackage.MNFC_URI__MBRICK:
                if (resolve) {
                    return getMbrick();
                }
                return basicGetMbrick();
            case ModelPackage.MNFC_URI__SENSOR_VALUE:
                return getSensorValue();
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
            case ModelPackage.MNFC_URI__LOGGER:
                setLogger((Logger) newValue);
                return;
            case ModelPackage.MNFC_URI__UID:
                setUid((String) newValue);
                return;
            case ModelPackage.MNFC_URI__POLL:
                setPoll((Boolean) newValue);
                return;
            case ModelPackage.MNFC_URI__ENABLED_A:
                setEnabledA((AtomicBoolean) newValue);
                return;
            case ModelPackage.MNFC_URI__SUB_ID:
                setSubId((String) newValue);
                return;
            case ModelPackage.MNFC_URI__MBRICK:
                setMbrick((MBrickletNFC) newValue);
                return;
            case ModelPackage.MNFC_URI__SENSOR_VALUE:
                setSensorValue((StringValue) newValue);
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
            case ModelPackage.MNFC_URI__LOGGER:
                setLogger(LOGGER_EDEFAULT);
                return;
            case ModelPackage.MNFC_URI__UID:
                setUid(UID_EDEFAULT);
                return;
            case ModelPackage.MNFC_URI__POLL:
                setPoll(POLL_EDEFAULT);
                return;
            case ModelPackage.MNFC_URI__ENABLED_A:
                setEnabledA(ENABLED_A_EDEFAULT);
                return;
            case ModelPackage.MNFC_URI__SUB_ID:
                setSubId(SUB_ID_EDEFAULT);
                return;
            case ModelPackage.MNFC_URI__MBRICK:
                setMbrick((MBrickletNFC) null);
                return;
            case ModelPackage.MNFC_URI__SENSOR_VALUE:
                setSensorValue((StringValue) null);
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
            case ModelPackage.MNFC_URI__LOGGER:
                return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
            case ModelPackage.MNFC_URI__UID:
                return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
            case ModelPackage.MNFC_URI__POLL:
                return poll != POLL_EDEFAULT;
            case ModelPackage.MNFC_URI__ENABLED_A:
                return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
            case ModelPackage.MNFC_URI__SUB_ID:
                return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
            case ModelPackage.MNFC_URI__MBRICK:
                return basicGetMbrick() != null;
            case ModelPackage.MNFC_URI__SENSOR_VALUE:
                return sensorValue != null;
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
        if (baseClass == MSensor.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MNFC_URI__SENSOR_VALUE:
                    return ModelPackage.MSENSOR__SENSOR_VALUE;
                default:
                    return -1;
            }
        }
        if (baseClass == MNFCNDEFRecordListener.class) {
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
        if (baseClass == MSensor.class) {
            switch (baseFeatureID) {
                case ModelPackage.MSENSOR__SENSOR_VALUE:
                    return ModelPackage.MNFC_URI__SENSOR_VALUE;
                default:
                    return -1;
            }
        }
        if (baseClass == MNFCNDEFRecordListener.class) {
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
        if (baseClass == MSensor.class) {
            switch (baseOperationID) {
                case ModelPackage.MSENSOR___FETCH_SENSOR_VALUE:
                    return ModelPackage.MNFC_URI___FETCH_SENSOR_VALUE;
                default:
                    return -1;
            }
        }
        if (baseClass == MNFCNDEFRecordListener.class) {
            switch (baseOperationID) {
                case ModelPackage.MNFCNDEF_RECORD_LISTENER___HANDLE_NDEF_RECORD__NDEFRECORD:
                    return ModelPackage.MNFC_URI___HANDLE_NDEF_RECORD__NDEFRECORD;
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
            case ModelPackage.MNFC_URI___HANDLE_NDEF_RECORD__NDEFRECORD:
                handleNDEFRecord((NDEFRecord) arguments.get(0));
                return null;
            case ModelPackage.MNFC_URI___FETCH_SENSOR_VALUE:
                fetchSensorValue();
                return null;
            case ModelPackage.MNFC_URI___INIT:
                init();
                return null;
            case ModelPackage.MNFC_URI___ENABLE:
                enable();
                return null;
            case ModelPackage.MNFC_URI___DISABLE:
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
        result.append(", subId: ");
        result.append(subId);
        result.append(", sensorValue: ");
        result.append(sensorValue);
        result.append(')');
        return result.toString();
    }

} // MNFCUriImpl
