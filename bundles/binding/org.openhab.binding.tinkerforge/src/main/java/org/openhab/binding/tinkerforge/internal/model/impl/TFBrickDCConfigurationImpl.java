/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import java.math.BigDecimal;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration;
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TF Brick DC Configuration</b></em>'.
 *
 * @author Theo Weiss
 * @since 1.3.0
 *        <!-- end-user-doc -->
 *        <p>
 *        The following features are implemented:
 *        </p>
 *        <ul>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getThreshold
 *        <em>Threshold</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getCallbackPeriod
 *        <em>Callback Period</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getVelocity
 *        <em>Velocity</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getAcceleration
 *        <em>Acceleration</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getPwmFrequency
 *        <em>Pwm Frequency</em>}</li>
 *        <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getDriveMode
 *        <em>Drive Mode</em>}</li>
 *        </ul>
 *
 * @generated
 */
public class TFBrickDCConfigurationImpl extends DimmableConfigurationImpl implements TFBrickDCConfiguration {
    /**
     * The default value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getThreshold()
     * @generated
     * @ordered
     */
    protected static final BigDecimal THRESHOLD_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getThreshold()
     * @generated
     * @ordered
     */
    protected BigDecimal threshold = THRESHOLD_EDEFAULT;

    /**
     * The default value of the '{@link #getCallbackPeriod() <em>Callback Period</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getCallbackPeriod()
     * @generated
     * @ordered
     */
    protected static final int CALLBACK_PERIOD_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getCallbackPeriod() <em>Callback Period</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getCallbackPeriod()
     * @generated
     * @ordered
     */
    protected int callbackPeriod = CALLBACK_PERIOD_EDEFAULT;

    /**
     * The default value of the '{@link #getVelocity() <em>Velocity</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getVelocity()
     * @generated
     * @ordered
     */
    protected static final short VELOCITY_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getVelocity() <em>Velocity</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getVelocity()
     * @generated
     * @ordered
     */
    protected short velocity = VELOCITY_EDEFAULT;

    /**
     * The default value of the '{@link #getAcceleration() <em>Acceleration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getAcceleration()
     * @generated
     * @ordered
     */
    protected static final int ACCELERATION_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getAcceleration() <em>Acceleration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getAcceleration()
     * @generated
     * @ordered
     */
    protected int acceleration = ACCELERATION_EDEFAULT;

    /**
     * The default value of the '{@link #getPwmFrequency() <em>Pwm Frequency</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getPwmFrequency()
     * @generated
     * @ordered
     */
    protected static final int PWM_FREQUENCY_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getPwmFrequency() <em>Pwm Frequency</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getPwmFrequency()
     * @generated
     * @ordered
     */
    protected int pwmFrequency = PWM_FREQUENCY_EDEFAULT;

    /**
     * The default value of the '{@link #getDriveMode() <em>Drive Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDriveMode()
     * @generated
     * @ordered
     */
    protected static final String DRIVE_MODE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDriveMode() <em>Drive Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDriveMode()
     * @generated
     * @ordered
     */
    protected String driveMode = DRIVE_MODE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected TFBrickDCConfigurationImpl() {
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
        return ModelPackage.Literals.TF_BRICK_DC_CONFIGURATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public BigDecimal getThreshold() {
        return threshold;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setThreshold(BigDecimal newThreshold) {
        BigDecimal oldThreshold = threshold;
        threshold = newThreshold;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__THRESHOLD,
                    oldThreshold, threshold));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public int getCallbackPeriod() {
        return callbackPeriod;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setCallbackPeriod(int newCallbackPeriod) {
        int oldCallbackPeriod = callbackPeriod;
        callbackPeriod = newCallbackPeriod;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.TF_BRICK_DC_CONFIGURATION__CALLBACK_PERIOD, oldCallbackPeriod, callbackPeriod));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public short getVelocity() {
        return velocity;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setVelocity(short newVelocity) {
        short oldVelocity = velocity;
        velocity = newVelocity;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY,
                    oldVelocity, velocity));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public int getAcceleration() {
        return acceleration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setAcceleration(int newAcceleration) {
        int oldAcceleration = acceleration;
        acceleration = newAcceleration;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION,
                    oldAcceleration, acceleration));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public int getPwmFrequency() {
        return pwmFrequency;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setPwmFrequency(int newPwmFrequency) {
        int oldPwmFrequency = pwmFrequency;
        pwmFrequency = newPwmFrequency;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY,
                    oldPwmFrequency, pwmFrequency));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getDriveMode() {
        return driveMode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setDriveMode(String newDriveMode) {
        String oldDriveMode = driveMode;
        driveMode = newDriveMode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE,
                    oldDriveMode, driveMode));
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
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__THRESHOLD:
                return getThreshold();
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__CALLBACK_PERIOD:
                return getCallbackPeriod();
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY:
                return getVelocity();
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION:
                return getAcceleration();
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY:
                return getPwmFrequency();
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE:
                return getDriveMode();
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
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__THRESHOLD:
                setThreshold((BigDecimal) newValue);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__CALLBACK_PERIOD:
                setCallbackPeriod((Integer) newValue);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY:
                setVelocity((Short) newValue);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION:
                setAcceleration((Integer) newValue);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY:
                setPwmFrequency((Integer) newValue);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE:
                setDriveMode((String) newValue);
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
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__THRESHOLD:
                setThreshold(THRESHOLD_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__CALLBACK_PERIOD:
                setCallbackPeriod(CALLBACK_PERIOD_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY:
                setVelocity(VELOCITY_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION:
                setAcceleration(ACCELERATION_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY:
                setPwmFrequency(PWM_FREQUENCY_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE:
                setDriveMode(DRIVE_MODE_EDEFAULT);
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
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__THRESHOLD:
                return THRESHOLD_EDEFAULT == null ? threshold != null : !THRESHOLD_EDEFAULT.equals(threshold);
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__CALLBACK_PERIOD:
                return callbackPeriod != CALLBACK_PERIOD_EDEFAULT;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY:
                return velocity != VELOCITY_EDEFAULT;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION:
                return acceleration != ACCELERATION_EDEFAULT;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY:
                return pwmFrequency != PWM_FREQUENCY_EDEFAULT;
            case ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE:
                return DRIVE_MODE_EDEFAULT == null ? driveMode != null : !DRIVE_MODE_EDEFAULT.equals(driveMode);
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
        if (baseClass == TFBaseConfiguration.class) {
            switch (derivedFeatureID) {
                case ModelPackage.TF_BRICK_DC_CONFIGURATION__THRESHOLD:
                    return ModelPackage.TF_BASE_CONFIGURATION__THRESHOLD;
                case ModelPackage.TF_BRICK_DC_CONFIGURATION__CALLBACK_PERIOD:
                    return ModelPackage.TF_BASE_CONFIGURATION__CALLBACK_PERIOD;
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
        if (baseClass == TFBaseConfiguration.class) {
            switch (baseFeatureID) {
                case ModelPackage.TF_BASE_CONFIGURATION__THRESHOLD:
                    return ModelPackage.TF_BRICK_DC_CONFIGURATION__THRESHOLD;
                case ModelPackage.TF_BASE_CONFIGURATION__CALLBACK_PERIOD:
                    return ModelPackage.TF_BRICK_DC_CONFIGURATION__CALLBACK_PERIOD;
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
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (threshold: ");
        result.append(threshold);
        result.append(", callbackPeriod: ");
        result.append(callbackPeriod);
        result.append(", velocity: ");
        result.append(velocity);
        result.append(", acceleration: ");
        result.append(acceleration);
        result.append(", pwmFrequency: ");
        result.append(pwmFrequency);
        result.append(", driveMode: ");
        result.append(driveMode);
        result.append(')');
        return result.toString();
    }

} // TFBrickDCConfigurationImpl
