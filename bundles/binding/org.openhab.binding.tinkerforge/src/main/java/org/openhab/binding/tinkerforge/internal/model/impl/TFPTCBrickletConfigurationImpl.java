/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFPTCBrickletConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TFPTC Bricklet Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFPTCBrickletConfigurationImpl#getNoiseRejectionFilter
 * <em>Noise Rejection Filter</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFPTCBrickletConfigurationImpl#getWireMode <em>Wire
 * Mode</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TFPTCBrickletConfigurationImpl extends MinimalEObjectImpl.Container implements TFPTCBrickletConfiguration {
    /**
     * The default value of the '{@link #getNoiseRejectionFilter() <em>Noise Rejection Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getNoiseRejectionFilter()
     * @generated
     * @ordered
     */
    protected static final Short NOISE_REJECTION_FILTER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNoiseRejectionFilter() <em>Noise Rejection Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getNoiseRejectionFilter()
     * @generated
     * @ordered
     */
    protected Short noiseRejectionFilter = NOISE_REJECTION_FILTER_EDEFAULT;

    /**
     * The default value of the '{@link #getWireMode() <em>Wire Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getWireMode()
     * @generated
     * @ordered
     */
    protected static final Short WIRE_MODE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getWireMode() <em>Wire Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getWireMode()
     * @generated
     * @ordered
     */
    protected Short wireMode = WIRE_MODE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected TFPTCBrickletConfigurationImpl() {
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
        return ModelPackage.Literals.TFPTC_BRICKLET_CONFIGURATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Short getNoiseRejectionFilter() {
        return noiseRejectionFilter;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setNoiseRejectionFilter(Short newNoiseRejectionFilter) {
        Short oldNoiseRejectionFilter = noiseRejectionFilter;
        noiseRejectionFilter = newNoiseRejectionFilter;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.TFPTC_BRICKLET_CONFIGURATION__NOISE_REJECTION_FILTER, oldNoiseRejectionFilter,
                    noiseRejectionFilter));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Short getWireMode() {
        return wireMode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void setWireMode(Short newWireMode) {
        Short oldWireMode = wireMode;
        wireMode = newWireMode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TFPTC_BRICKLET_CONFIGURATION__WIRE_MODE,
                    oldWireMode, wireMode));
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
            case ModelPackage.TFPTC_BRICKLET_CONFIGURATION__NOISE_REJECTION_FILTER:
                return getNoiseRejectionFilter();
            case ModelPackage.TFPTC_BRICKLET_CONFIGURATION__WIRE_MODE:
                return getWireMode();
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
            case ModelPackage.TFPTC_BRICKLET_CONFIGURATION__NOISE_REJECTION_FILTER:
                setNoiseRejectionFilter((Short) newValue);
                return;
            case ModelPackage.TFPTC_BRICKLET_CONFIGURATION__WIRE_MODE:
                setWireMode((Short) newValue);
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
            case ModelPackage.TFPTC_BRICKLET_CONFIGURATION__NOISE_REJECTION_FILTER:
                setNoiseRejectionFilter(NOISE_REJECTION_FILTER_EDEFAULT);
                return;
            case ModelPackage.TFPTC_BRICKLET_CONFIGURATION__WIRE_MODE:
                setWireMode(WIRE_MODE_EDEFAULT);
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
            case ModelPackage.TFPTC_BRICKLET_CONFIGURATION__NOISE_REJECTION_FILTER:
                return NOISE_REJECTION_FILTER_EDEFAULT == null ? noiseRejectionFilter != null
                        : !NOISE_REJECTION_FILTER_EDEFAULT.equals(noiseRejectionFilter);
            case ModelPackage.TFPTC_BRICKLET_CONFIGURATION__WIRE_MODE:
                return WIRE_MODE_EDEFAULT == null ? wireMode != null : !WIRE_MODE_EDEFAULT.equals(wireMode);
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
        result.append(" (noiseRejectionFilter: ");
        result.append(noiseRejectionFilter);
        result.append(", wireMode: ");
        result.append(wireMode);
        result.append(')');
        return result.toString();
    }

} // TFPTCBrickletConfigurationImpl
