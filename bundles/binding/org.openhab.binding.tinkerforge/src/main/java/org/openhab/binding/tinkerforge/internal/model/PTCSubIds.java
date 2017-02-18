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
 * A representation of the literals of the enumeration '<em><b>PTC Sub Ids</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * 
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getPTCSubIds()
 * @model
 * @generated
 */
public enum PTCSubIds implements Enumerator {
    /**
     * The '<em><b>Ptc temperature</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #PTC_TEMPERATURE_VALUE
     * @generated
     * @ordered
     */
    PTC_TEMPERATURE(0, "ptc_temperature", "ptc_temperature"),

    /**
     * The '<em><b>Ptc resistance</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #PTC_RESISTANCE_VALUE
     * @generated
     * @ordered
     */
    PTC_RESISTANCE(0, "ptc_resistance", "ptc_resistance"),

    /**
     * The '<em><b>Ptc connected</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #PTC_CONNECTED_VALUE
     * @generated
     * @ordered
     */
    PTC_CONNECTED(0, "ptc_connected", "ptc_connected");

    /**
     * The '<em><b>Ptc temperature</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Ptc temperature</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #PTC_TEMPERATURE
     * @model name="ptc_temperature"
     * @generated
     * @ordered
     */
    public static final int PTC_TEMPERATURE_VALUE = 0;

    /**
     * The '<em><b>Ptc resistance</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Ptc resistance</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #PTC_RESISTANCE
     * @model name="ptc_resistance"
     * @generated
     * @ordered
     */
    public static final int PTC_RESISTANCE_VALUE = 0;

    /**
     * The '<em><b>Ptc connected</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Ptc connected</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #PTC_CONNECTED
     * @model name="ptc_connected"
     * @generated
     * @ordered
     */
    public static final int PTC_CONNECTED_VALUE = 0;

    /**
     * An array of all the '<em><b>PTC Sub Ids</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    private static final PTCSubIds[] VALUES_ARRAY = new PTCSubIds[] { PTC_TEMPERATURE, PTC_RESISTANCE, PTC_CONNECTED, };

    /**
     * A public read-only list of all the '<em><b>PTC Sub Ids</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final List<PTCSubIds> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>PTC Sub Ids</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static PTCSubIds get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            PTCSubIds result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>PTC Sub Ids</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static PTCSubIds getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            PTCSubIds result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>PTC Sub Ids</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static PTCSubIds get(int value) {
        switch (value) {
            case PTC_TEMPERATURE_VALUE:
                return PTC_TEMPERATURE;
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
    private PTCSubIds(int value, String name, String literal) {
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

} // PTCSubIds
