/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Industrial Dual020m ASub Ids</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * 
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getIndustrialDual020mASubIds()
 * @model
 * @generated
 */
public enum IndustrialDual020mASubIds implements Enumerator {
    /**
     * The '<em><b>Sensor0</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #SENSOR0_VALUE
     * @generated
     * @ordered
     */
    SENSOR0(0, "sensor0", "sensor0"),
    /**
     * The '<em><b>Sensor1</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #SENSOR1_VALUE
     * @generated
     * @ordered
     */
    SENSOR1(0, "sensor1", "sensor1");

    /**
     * The '<em><b>Sensor0</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Sensor0</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #SENSOR0
     * @model name="sensor0"
     * @generated
     * @ordered
     */
    public static final int SENSOR0_VALUE = 0;

    /**
     * The '<em><b>Sensor1</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Sensor1</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #SENSOR1
     * @model name="sensor1"
     * @generated
     * @ordered
     */
    public static final int SENSOR1_VALUE = 0;

    /**
     * An array of all the '<em><b>Industrial Dual020m ASub Ids</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    private static final IndustrialDual020mASubIds[] VALUES_ARRAY = new IndustrialDual020mASubIds[] { SENSOR0,
            SENSOR1, };

    /**
     * A public read-only list of all the '<em><b>Industrial Dual020m ASub Ids</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final List<IndustrialDual020mASubIds> VALUES = Collections
            .unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Industrial Dual020m ASub Ids</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static IndustrialDual020mASubIds get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            IndustrialDual020mASubIds result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Industrial Dual020m ASub Ids</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static IndustrialDual020mASubIds getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            IndustrialDual020mASubIds result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Industrial Dual020m ASub Ids</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static IndustrialDual020mASubIds get(int value) {
        switch (value) {
            case SENSOR0_VALUE:
                return SENSOR0;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    private IndustrialDual020mASubIds(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public int getValue() {
        return value;
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
    public String getLiteral() {
        return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }

} // IndustrialDual020mASubIds
