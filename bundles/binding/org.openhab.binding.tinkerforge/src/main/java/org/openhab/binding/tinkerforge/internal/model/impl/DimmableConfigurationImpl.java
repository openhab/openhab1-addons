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
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.openhab.binding.tinkerforge.internal.model.DimmableConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dimmable Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DimmableConfigurationImpl#getMinValue <em>Min
 * Value</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.DimmableConfigurationImpl#getMaxValue <em>Max
 * Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DimmableConfigurationImpl extends MinimalEObjectImpl.Container implements DimmableConfiguration {
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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected DimmableConfigurationImpl() {
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
        return ModelPackage.Literals.DIMMABLE_CONFIGURATION;
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIMMABLE_CONFIGURATION__MIN_VALUE,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DIMMABLE_CONFIGURATION__MAX_VALUE,
                    oldMaxValue, maxValue));
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
            case ModelPackage.DIMMABLE_CONFIGURATION__MIN_VALUE:
                return getMinValue();
            case ModelPackage.DIMMABLE_CONFIGURATION__MAX_VALUE:
                return getMaxValue();
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
            case ModelPackage.DIMMABLE_CONFIGURATION__MIN_VALUE:
                setMinValue((BigDecimal) newValue);
                return;
            case ModelPackage.DIMMABLE_CONFIGURATION__MAX_VALUE:
                setMaxValue((BigDecimal) newValue);
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
            case ModelPackage.DIMMABLE_CONFIGURATION__MIN_VALUE:
                setMinValue(MIN_VALUE_EDEFAULT);
                return;
            case ModelPackage.DIMMABLE_CONFIGURATION__MAX_VALUE:
                setMaxValue(MAX_VALUE_EDEFAULT);
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
            case ModelPackage.DIMMABLE_CONFIGURATION__MIN_VALUE:
                return MIN_VALUE_EDEFAULT == null ? minValue != null : !MIN_VALUE_EDEFAULT.equals(minValue);
            case ModelPackage.DIMMABLE_CONFIGURATION__MAX_VALUE:
                return MAX_VALUE_EDEFAULT == null ? maxValue != null : !MAX_VALUE_EDEFAULT.equals(maxValue);
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

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (minValue: ");
        result.append(minValue);
        result.append(", maxValue: ");
        result.append(maxValue);
        result.append(')');
        return result.toString();
    }

} // DimmableConfigurationImpl
