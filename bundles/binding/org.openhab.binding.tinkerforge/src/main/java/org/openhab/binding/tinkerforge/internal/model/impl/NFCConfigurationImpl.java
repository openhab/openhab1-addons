/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.NFCConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>NFC Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.NFCConfigurationImpl#isClearValuesOnError <em>Clear
 * Values On Error</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.NFCConfigurationImpl#isResetOldValues <em>Reset Old
 * Values</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.NFCConfigurationImpl#isTriggeredScan <em>Triggered
 * Scan</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.NFCConfigurationImpl#getDelayAfterScan <em>Delay After
 * Scan</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NFCConfigurationImpl extends MinimalEObjectImpl.Container implements NFCConfiguration {
    /**
     * The default value of the '{@link #isClearValuesOnError() <em>Clear Values On Error</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isClearValuesOnError()
     * @generated
     * @ordered
     */
    protected static final boolean CLEAR_VALUES_ON_ERROR_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isClearValuesOnError() <em>Clear Values On Error</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isClearValuesOnError()
     * @generated
     * @ordered
     */
    protected boolean clearValuesOnError = CLEAR_VALUES_ON_ERROR_EDEFAULT;

    /**
     * The default value of the '{@link #isResetOldValues() <em>Reset Old Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isResetOldValues()
     * @generated
     * @ordered
     */
    protected static final boolean RESET_OLD_VALUES_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isResetOldValues() <em>Reset Old Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isResetOldValues()
     * @generated
     * @ordered
     */
    protected boolean resetOldValues = RESET_OLD_VALUES_EDEFAULT;

    /**
     * The default value of the '{@link #isTriggeredScan() <em>Triggered Scan</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isTriggeredScan()
     * @generated
     * @ordered
     */
    protected static final boolean TRIGGERED_SCAN_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isTriggeredScan() <em>Triggered Scan</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isTriggeredScan()
     * @generated
     * @ordered
     */
    protected boolean triggeredScan = TRIGGERED_SCAN_EDEFAULT;

    /**
     * The default value of the '{@link #getDelayAfterScan() <em>Delay After Scan</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDelayAfterScan()
     * @generated
     * @ordered
     */
    protected static final int DELAY_AFTER_SCAN_EDEFAULT = 3000;

    /**
     * The cached value of the '{@link #getDelayAfterScan() <em>Delay After Scan</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDelayAfterScan()
     * @generated
     * @ordered
     */
    protected int delayAfterScan = DELAY_AFTER_SCAN_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected NFCConfigurationImpl() {
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
        return ModelPackage.Literals.NFC_CONFIGURATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isClearValuesOnError() {
        return clearValuesOnError;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setClearValuesOnError(boolean newClearValuesOnError) {
        boolean oldClearValuesOnError = clearValuesOnError;
        clearValuesOnError = newClearValuesOnError;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.NFC_CONFIGURATION__CLEAR_VALUES_ON_ERROR,
                    oldClearValuesOnError, clearValuesOnError));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isResetOldValues() {
        return resetOldValues;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setResetOldValues(boolean newResetOldValues) {
        boolean oldResetOldValues = resetOldValues;
        resetOldValues = newResetOldValues;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.NFC_CONFIGURATION__RESET_OLD_VALUES,
                    oldResetOldValues, resetOldValues));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isTriggeredScan() {
        return triggeredScan;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setTriggeredScan(boolean newTriggeredScan) {
        boolean oldTriggeredScan = triggeredScan;
        triggeredScan = newTriggeredScan;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.NFC_CONFIGURATION__TRIGGERED_SCAN,
                    oldTriggeredScan, triggeredScan));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getDelayAfterScan() {
        return delayAfterScan;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setDelayAfterScan(int newDelayAfterScan) {
        int oldDelayAfterScan = delayAfterScan;
        delayAfterScan = newDelayAfterScan;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.NFC_CONFIGURATION__DELAY_AFTER_SCAN,
                    oldDelayAfterScan, delayAfterScan));
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
            case ModelPackage.NFC_CONFIGURATION__CLEAR_VALUES_ON_ERROR:
                return isClearValuesOnError();
            case ModelPackage.NFC_CONFIGURATION__RESET_OLD_VALUES:
                return isResetOldValues();
            case ModelPackage.NFC_CONFIGURATION__TRIGGERED_SCAN:
                return isTriggeredScan();
            case ModelPackage.NFC_CONFIGURATION__DELAY_AFTER_SCAN:
                return getDelayAfterScan();
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
            case ModelPackage.NFC_CONFIGURATION__CLEAR_VALUES_ON_ERROR:
                setClearValuesOnError((Boolean) newValue);
                return;
            case ModelPackage.NFC_CONFIGURATION__RESET_OLD_VALUES:
                setResetOldValues((Boolean) newValue);
                return;
            case ModelPackage.NFC_CONFIGURATION__TRIGGERED_SCAN:
                setTriggeredScan((Boolean) newValue);
                return;
            case ModelPackage.NFC_CONFIGURATION__DELAY_AFTER_SCAN:
                setDelayAfterScan((Integer) newValue);
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
            case ModelPackage.NFC_CONFIGURATION__CLEAR_VALUES_ON_ERROR:
                setClearValuesOnError(CLEAR_VALUES_ON_ERROR_EDEFAULT);
                return;
            case ModelPackage.NFC_CONFIGURATION__RESET_OLD_VALUES:
                setResetOldValues(RESET_OLD_VALUES_EDEFAULT);
                return;
            case ModelPackage.NFC_CONFIGURATION__TRIGGERED_SCAN:
                setTriggeredScan(TRIGGERED_SCAN_EDEFAULT);
                return;
            case ModelPackage.NFC_CONFIGURATION__DELAY_AFTER_SCAN:
                setDelayAfterScan(DELAY_AFTER_SCAN_EDEFAULT);
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
            case ModelPackage.NFC_CONFIGURATION__CLEAR_VALUES_ON_ERROR:
                return clearValuesOnError != CLEAR_VALUES_ON_ERROR_EDEFAULT;
            case ModelPackage.NFC_CONFIGURATION__RESET_OLD_VALUES:
                return resetOldValues != RESET_OLD_VALUES_EDEFAULT;
            case ModelPackage.NFC_CONFIGURATION__TRIGGERED_SCAN:
                return triggeredScan != TRIGGERED_SCAN_EDEFAULT;
            case ModelPackage.NFC_CONFIGURATION__DELAY_AFTER_SCAN:
                return delayAfterScan != DELAY_AFTER_SCAN_EDEFAULT;
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
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (clearValuesOnError: ");
        result.append(clearValuesOnError);
        result.append(", resetOldValues: ");
        result.append(resetOldValues);
        result.append(", triggeredScan: ");
        result.append(triggeredScan);
        result.append(", delayAfterScan: ");
        result.append(delayAfterScan);
        result.append(')');
        return result.toString();
    }

} // NFCConfigurationImpl
