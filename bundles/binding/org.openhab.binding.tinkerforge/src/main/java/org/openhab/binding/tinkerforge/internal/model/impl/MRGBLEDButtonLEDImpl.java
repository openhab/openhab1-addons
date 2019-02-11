/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.awt.Color;
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
import org.openhab.binding.tinkerforge.internal.model.MBaseDevice;
import org.openhab.binding.tinkerforge.internal.model.MBrickletRGBLEDButton;
import org.openhab.binding.tinkerforge.internal.model.MRGBLEDButtonLED;
import org.openhab.binding.tinkerforge.internal.model.MSubDevice;
import org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.types.HSBValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.core.library.types.HSBType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tinkerforge.BrickletRGBLEDButton;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MRGBLED Button LED</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#getSensorValue <em>Sensor
 * Value</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#getSwitchState <em>Switch
 * State</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#getLogger <em>Logger</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#getUid <em>Uid</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#isPoll <em>Poll</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#getEnabledA <em>Enabled
 * A</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#getSubId <em>Sub Id</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#getMbrick <em>Mbrick</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#getDeviceType <em>Device
 * Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.MRGBLEDButtonLEDImpl#getLastSelectedColor <em>Last
 * Selected Color</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MRGBLEDButtonLEDImpl extends MinimalEObjectImpl.Container implements MRGBLEDButtonLED {
    /**
     * The cached value of the '{@link #getSensorValue() <em>Sensor Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getSensorValue()
     * @generated
     * @ordered
     */
    protected HSBValue sensorValue;

    /**
     * The default value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getSwitchState()
     * @generated
     * @ordered
     */
    protected static final OnOffValue SWITCH_STATE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSwitchState() <em>Switch State</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getSwitchState()
     * @generated
     * @ordered
     */
    protected OnOffValue switchState = SWITCH_STATE_EDEFAULT;

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
     * The default value of the '{@link #getDeviceType() <em>Device Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDeviceType()
     * @generated
     * @ordered
     */
    protected static final String DEVICE_TYPE_EDEFAULT = "led";

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
     * The default value of the '{@link #getLastSelectedColor() <em>Last Selected Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getLastSelectedColor()
     * @generated
     * @ordered
     */
    protected static final HSBType LAST_SELECTED_COLOR_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLastSelectedColor() <em>Last Selected Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getLastSelectedColor()
     * @generated
     * @ordered
     */
    protected HSBType lastSelectedColor = LAST_SELECTED_COLOR_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected MRGBLEDButtonLEDImpl() {
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
        return ModelPackage.Literals.MRGBLED_BUTTON_LED;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public HSBValue getSensorValue() {
        return sensorValue;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setSensorValue(HSBValue newSensorValue) {
        HSBValue oldSensorValue = sensorValue;
        sensorValue = newSensorValue;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MRGBLED_BUTTON_LED__SENSOR_VALUE,
                    oldSensorValue, sensorValue));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public OnOffValue getSwitchState() {
        return switchState;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setSwitchState(OnOffValue newSwitchState) {
        OnOffValue oldSwitchState = switchState;
        switchState = newSwitchState;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MRGBLED_BUTTON_LED__SWITCH_STATE,
                    oldSwitchState, switchState));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MRGBLED_BUTTON_LED__LOGGER, oldLogger,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MRGBLED_BUTTON_LED__UID, oldUid, uid));
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MRGBLED_BUTTON_LED__POLL, oldPoll,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MRGBLED_BUTTON_LED__ENABLED_A,
                    oldEnabledA, enabledA));
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
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MRGBLED_BUTTON_LED__SUB_ID, oldSubId,
                    subId));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public MBrickletRGBLEDButton getMbrick() {
        if (eContainerFeatureID() != ModelPackage.MRGBLED_BUTTON_LED__MBRICK)
            return null;
        return (MBrickletRGBLEDButton) eContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public MBrickletRGBLEDButton basicGetMbrick() {
        if (eContainerFeatureID() != ModelPackage.MRGBLED_BUTTON_LED__MBRICK)
            return null;
        return (MBrickletRGBLEDButton) eInternalContainer();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetMbrick(MBrickletRGBLEDButton newMbrick, NotificationChain msgs) {
        msgs = eBasicSetContainer((InternalEObject) newMbrick, ModelPackage.MRGBLED_BUTTON_LED__MBRICK, msgs);
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setMbrick(MBrickletRGBLEDButton newMbrick) {
        if (newMbrick != eInternalContainer()
                || (eContainerFeatureID() != ModelPackage.MRGBLED_BUTTON_LED__MBRICK && newMbrick != null)) {
            if (EcoreUtil.isAncestor(this, newMbrick))
                throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
            NotificationChain msgs = null;
            if (eInternalContainer() != null)
                msgs = eBasicRemoveFromContainer(msgs);
            if (newMbrick != null)
                msgs = ((InternalEObject) newMbrick).eInverseAdd(this, ModelPackage.MSUB_DEVICE_HOLDER__MSUBDEVICES,
                        MSubDeviceHolder.class, msgs);
            msgs = basicSetMbrick(newMbrick, msgs);
            if (msgs != null)
                msgs.dispatch();
        } else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MRGBLED_BUTTON_LED__MBRICK, newMbrick,
                    newMbrick));
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
    public HSBType getLastSelectedColor() {
        return lastSelectedColor;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setLastSelectedColor(HSBType newLastSelectedColor) {
        HSBType oldLastSelectedColor = lastSelectedColor;
        lastSelectedColor = newLastSelectedColor;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MRGBLED_BUTTON_LED__LAST_SELECTED_COLOR,
                    oldLastSelectedColor, lastSelectedColor));
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
        logger = LoggerFactory.getLogger(MRGBLEDButtonLEDImpl.class);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void enable() {
        setSensorValue(new HSBValue(HSBType.BLACK));
        setLastSelectedColor(HSBType.WHITE);
        setSwitchState(OnOffValue.UNDEF);
        fetchSensorValue();
    }

    /*
     * @generated NOT
     */
    private static HSBValue getHSBValueFromBrickletColor(BrickletRGBLEDButton.Color color) {
        return new HSBValue(new HSBType(new Color(color.red, color.green, color.blue)));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void disable() {

    }

    /**
     * Switches the led on or off. If switched off, the last color is saved and if switched back on, the last saved
     * color is used.
     * If switched on and no last color was set, the color white is set.
     *
     * @generated NOT
     */
    @Override
    public void turnSwitch(OnOffValue state) {
        switch (state) {
            case ON:
                setSelectedColor(lastSelectedColor);
                break;
            case OFF:
                if (!sensorValue.getHsbValue().equals(HSBType.BLACK)) {
                    setLastSelectedColor(sensorValue.getHsbValue());
                }
                setSelectedColor(HSBType.BLACK);
                break;
            default:
                break;
        }
    }

    /**
     * @generated NOT
     */
    private void fetchColor() {
        try {
            BrickletRGBLEDButton.Color tmpColor = getMbrick().getTinkerforgeDevice().getColor();
            HSBValue hsbValue = getHSBValueFromBrickletColor(tmpColor);
            if (!isEqualColor(sensorValue.getHsbValue(), hsbValue.getHsbValue())) {
                setSensorValue(hsbValue);
            }
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
    public void setSelectedColor(HSBType color) {
        Color rgb = color.toColor();
        int red = rgb.getRed();
        int green = rgb.getGreen();
        int blue = rgb.getBlue();

        try {
            getMbrick().getTinkerforgeDevice().setColor(red, green, blue);
            setSensorValue(new HSBValue(color));
            setSwitchStateDependingOnColor(color);
        } catch (TimeoutException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_TIMEOUT_EXCEPTION, e);
        } catch (NotConnectedException e) {
            TinkerforgeErrorHandler.handleError(this, TinkerforgeErrorHandler.TF_NOT_CONNECTION_EXCEPTION, e);
        }
    }

    /**
     * fetches the current color and the switch state (led is on or off).
     *
     * @generated NOT
     */
    @Override
    public void fetchSensorValue() {
        fetchColor();
        setSwitchStateDependingOnColor(sensorValue.getHsbValue());
    }

    /**
     * @generated NOT
     */
    private void setSwitchStateDependingOnColor(HSBType type) {
        OnOffValue oldSwitchState = switchState;
        OnOffValue newSwitchState = type == HSBType.BLACK ? OnOffValue.OFF : OnOffValue.ON;
        if (oldSwitchState != newSwitchState) {
            setSwitchState(newSwitchState);
        }
    }

    /**
     * Compares two colors. The equal() method on hsbtype itself wont work.
     *
     * @generated NOT
     */
    private static boolean isEqualColor(HSBType left, HSBType right) {
        Color lc = left.toColor();
        Color rc = right.toColor();
        return lc.getRed() == rc.getRed() && lc.getGreen() == rc.getGreen() && lc.getBlue() == rc.getBlue();
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
            case ModelPackage.MRGBLED_BUTTON_LED__MBRICK:
                if (eInternalContainer() != null)
                    msgs = eBasicRemoveFromContainer(msgs);
                return basicSetMbrick((MBrickletRGBLEDButton) otherEnd, msgs);
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
            case ModelPackage.MRGBLED_BUTTON_LED__MBRICK:
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
            case ModelPackage.MRGBLED_BUTTON_LED__MBRICK:
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
            case ModelPackage.MRGBLED_BUTTON_LED__SENSOR_VALUE:
                return getSensorValue();
            case ModelPackage.MRGBLED_BUTTON_LED__SWITCH_STATE:
                return getSwitchState();
            case ModelPackage.MRGBLED_BUTTON_LED__LOGGER:
                return getLogger();
            case ModelPackage.MRGBLED_BUTTON_LED__UID:
                return getUid();
            case ModelPackage.MRGBLED_BUTTON_LED__POLL:
                return isPoll();
            case ModelPackage.MRGBLED_BUTTON_LED__ENABLED_A:
                return getEnabledA();
            case ModelPackage.MRGBLED_BUTTON_LED__SUB_ID:
                return getSubId();
            case ModelPackage.MRGBLED_BUTTON_LED__MBRICK:
                if (resolve)
                    return getMbrick();
                return basicGetMbrick();
            case ModelPackage.MRGBLED_BUTTON_LED__DEVICE_TYPE:
                return getDeviceType();
            case ModelPackage.MRGBLED_BUTTON_LED__LAST_SELECTED_COLOR:
                return getLastSelectedColor();
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
            case ModelPackage.MRGBLED_BUTTON_LED__SENSOR_VALUE:
                setSensorValue((HSBValue) newValue);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__SWITCH_STATE:
                setSwitchState((OnOffValue) newValue);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__LOGGER:
                setLogger((Logger) newValue);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__UID:
                setUid((String) newValue);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__POLL:
                setPoll((Boolean) newValue);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__ENABLED_A:
                setEnabledA((AtomicBoolean) newValue);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__SUB_ID:
                setSubId((String) newValue);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__MBRICK:
                setMbrick((MBrickletRGBLEDButton) newValue);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__LAST_SELECTED_COLOR:
                setLastSelectedColor((HSBType) newValue);
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
            case ModelPackage.MRGBLED_BUTTON_LED__SENSOR_VALUE:
                setSensorValue((HSBValue) null);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__SWITCH_STATE:
                setSwitchState(SWITCH_STATE_EDEFAULT);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__LOGGER:
                setLogger(LOGGER_EDEFAULT);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__UID:
                setUid(UID_EDEFAULT);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__POLL:
                setPoll(POLL_EDEFAULT);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__ENABLED_A:
                setEnabledA(ENABLED_A_EDEFAULT);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__SUB_ID:
                setSubId(SUB_ID_EDEFAULT);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__MBRICK:
                setMbrick((MBrickletRGBLEDButton) null);
                return;
            case ModelPackage.MRGBLED_BUTTON_LED__LAST_SELECTED_COLOR:
                setLastSelectedColor(LAST_SELECTED_COLOR_EDEFAULT);
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
            case ModelPackage.MRGBLED_BUTTON_LED__SENSOR_VALUE:
                return sensorValue != null;
            case ModelPackage.MRGBLED_BUTTON_LED__SWITCH_STATE:
                return SWITCH_STATE_EDEFAULT == null ? switchState != null : !SWITCH_STATE_EDEFAULT.equals(switchState);
            case ModelPackage.MRGBLED_BUTTON_LED__LOGGER:
                return LOGGER_EDEFAULT == null ? logger != null : !LOGGER_EDEFAULT.equals(logger);
            case ModelPackage.MRGBLED_BUTTON_LED__UID:
                return UID_EDEFAULT == null ? uid != null : !UID_EDEFAULT.equals(uid);
            case ModelPackage.MRGBLED_BUTTON_LED__POLL:
                return poll != POLL_EDEFAULT;
            case ModelPackage.MRGBLED_BUTTON_LED__ENABLED_A:
                return ENABLED_A_EDEFAULT == null ? enabledA != null : !ENABLED_A_EDEFAULT.equals(enabledA);
            case ModelPackage.MRGBLED_BUTTON_LED__SUB_ID:
                return SUB_ID_EDEFAULT == null ? subId != null : !SUB_ID_EDEFAULT.equals(subId);
            case ModelPackage.MRGBLED_BUTTON_LED__MBRICK:
                return basicGetMbrick() != null;
            case ModelPackage.MRGBLED_BUTTON_LED__DEVICE_TYPE:
                return DEVICE_TYPE_EDEFAULT == null ? deviceType != null : !DEVICE_TYPE_EDEFAULT.equals(deviceType);
            case ModelPackage.MRGBLED_BUTTON_LED__LAST_SELECTED_COLOR:
                return LAST_SELECTED_COLOR_EDEFAULT == null ? lastSelectedColor != null
                        : !LAST_SELECTED_COLOR_EDEFAULT.equals(lastSelectedColor);
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
                case ModelPackage.MRGBLED_BUTTON_LED__LOGGER:
                    return ModelPackage.MBASE_DEVICE__LOGGER;
                case ModelPackage.MRGBLED_BUTTON_LED__UID:
                    return ModelPackage.MBASE_DEVICE__UID;
                case ModelPackage.MRGBLED_BUTTON_LED__POLL:
                    return ModelPackage.MBASE_DEVICE__POLL;
                case ModelPackage.MRGBLED_BUTTON_LED__ENABLED_A:
                    return ModelPackage.MBASE_DEVICE__ENABLED_A;
                default:
                    return -1;
            }
        }
        if (baseClass == MSubDevice.class) {
            switch (derivedFeatureID) {
                case ModelPackage.MRGBLED_BUTTON_LED__SUB_ID:
                    return ModelPackage.MSUB_DEVICE__SUB_ID;
                case ModelPackage.MRGBLED_BUTTON_LED__MBRICK:
                    return ModelPackage.MSUB_DEVICE__MBRICK;
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
                    return ModelPackage.MRGBLED_BUTTON_LED__LOGGER;
                case ModelPackage.MBASE_DEVICE__UID:
                    return ModelPackage.MRGBLED_BUTTON_LED__UID;
                case ModelPackage.MBASE_DEVICE__POLL:
                    return ModelPackage.MRGBLED_BUTTON_LED__POLL;
                case ModelPackage.MBASE_DEVICE__ENABLED_A:
                    return ModelPackage.MRGBLED_BUTTON_LED__ENABLED_A;
                default:
                    return -1;
            }
        }
        if (baseClass == MSubDevice.class) {
            switch (baseFeatureID) {
                case ModelPackage.MSUB_DEVICE__SUB_ID:
                    return ModelPackage.MRGBLED_BUTTON_LED__SUB_ID;
                case ModelPackage.MSUB_DEVICE__MBRICK:
                    return ModelPackage.MRGBLED_BUTTON_LED__MBRICK;
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
                    return ModelPackage.MRGBLED_BUTTON_LED___INIT;
                case ModelPackage.MBASE_DEVICE___ENABLE:
                    return ModelPackage.MRGBLED_BUTTON_LED___ENABLE;
                case ModelPackage.MBASE_DEVICE___DISABLE:
                    return ModelPackage.MRGBLED_BUTTON_LED___DISABLE;
                default:
                    return -1;
            }
        }
        if (baseClass == MSubDevice.class) {
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
            case ModelPackage.MRGBLED_BUTTON_LED___INIT:
                init();
                return null;
            case ModelPackage.MRGBLED_BUTTON_LED___ENABLE:
                enable();
                return null;
            case ModelPackage.MRGBLED_BUTTON_LED___DISABLE:
                disable();
                return null;
            case ModelPackage.MRGBLED_BUTTON_LED___TURN_SWITCH__ONOFFVALUE:
                turnSwitch((OnOffValue) arguments.get(0));
                return null;
            case ModelPackage.MRGBLED_BUTTON_LED___SET_SELECTED_COLOR__HSBTYPE:
                setSelectedColor((HSBType) arguments.get(0));
                return null;
            case ModelPackage.MRGBLED_BUTTON_LED___FETCH_SENSOR_VALUE:
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

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (sensorValue: ");
        result.append(sensorValue);
        result.append(", switchState: ");
        result.append(switchState);
        result.append(", logger: ");
        result.append(logger);
        result.append(", uid: ");
        result.append(uid);
        result.append(", poll: ");
        result.append(poll);
        result.append(", enabledA: ");
        result.append(enabledA);
        result.append(", subId: ");
        result.append(subId);
        result.append(", deviceType: ");
        result.append(deviceType);
        result.append(", lastSelectedColor: ");
        result.append(lastSelectedColor);
        result.append(')');
        return result.toString();
    }

} // MRGBLEDButtonLEDImpl
