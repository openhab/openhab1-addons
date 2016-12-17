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
import org.openhab.binding.tinkerforge.internal.model.BrickletOLEDConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bricklet OLED Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletOLEDConfigurationImpl#getContrast
 * <em>Contrast</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletOLEDConfigurationImpl#isInvert
 * <em>Invert</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BrickletOLEDConfigurationImpl extends MinimalEObjectImpl.Container implements BrickletOLEDConfiguration {
    /**
     * The default value of the '{@link #getContrast() <em>Contrast</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getContrast()
     * @generated
     * @ordered
     */
    protected static final Short CONTRAST_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getContrast() <em>Contrast</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getContrast()
     * @generated
     * @ordered
     */
    protected Short contrast = CONTRAST_EDEFAULT;

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
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    protected BrickletOLEDConfigurationImpl() {
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
        return ModelPackage.Literals.BRICKLET_OLED_CONFIGURATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Short getContrast() {
        return contrast;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setContrast(Short newContrast) {
        Short oldContrast = contrast;
        contrast = newContrast;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_OLED_CONFIGURATION__CONTRAST,
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
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_OLED_CONFIGURATION__INVERT,
                    oldInvert, invert));
        }
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
            case ModelPackage.BRICKLET_OLED_CONFIGURATION__CONTRAST:
                return getContrast();
            case ModelPackage.BRICKLET_OLED_CONFIGURATION__INVERT:
                return isInvert();
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
            case ModelPackage.BRICKLET_OLED_CONFIGURATION__CONTRAST:
                setContrast((Short) newValue);
                return;
            case ModelPackage.BRICKLET_OLED_CONFIGURATION__INVERT:
                setInvert((Boolean) newValue);
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
            case ModelPackage.BRICKLET_OLED_CONFIGURATION__CONTRAST:
                setContrast(CONTRAST_EDEFAULT);
                return;
            case ModelPackage.BRICKLET_OLED_CONFIGURATION__INVERT:
                setInvert(INVERT_EDEFAULT);
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
            case ModelPackage.BRICKLET_OLED_CONFIGURATION__CONTRAST:
                return CONTRAST_EDEFAULT == null ? contrast != null : !CONTRAST_EDEFAULT.equals(contrast);
            case ModelPackage.BRICKLET_OLED_CONFIGURATION__INVERT:
                return invert != INVERT_EDEFAULT;
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
        if (eIsProxy()) {
            return super.toString();
        }

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (contrast: ");
        result.append(contrast);
        result.append(", invert: ");
        result.append(invert);
        result.append(')');
        return result.toString();
    }

} // BrickletOLEDConfigurationImpl
