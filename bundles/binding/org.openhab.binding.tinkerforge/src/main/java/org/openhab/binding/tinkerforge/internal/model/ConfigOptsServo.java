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
 * A representation of the literals of the enumeration '<em><b>Config Opts Servo</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * 
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getConfigOptsServo()
 * @model
 * @generated
 */
public enum ConfigOptsServo implements Enumerator {
    /**
     * The '<em><b>VELOCITY</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #VELOCITY_VALUE
     * @generated
     * @ordered
     */
    VELOCITY(0, "VELOCITY", "VELOCITY"),

    /**
     * The '<em><b>ACCELERATION</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #ACCELERATION_VALUE
     * @generated
     * @ordered
     */
    ACCELERATION(0, "ACCELERATION", "ACCELERATION"),

    /**
     * The '<em><b>PULSEWIDTHMIN</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #PULSEWIDTHMIN_VALUE
     * @generated
     * @ordered
     */
    PULSEWIDTHMIN(0, "PULSEWIDTHMIN", "PULSEWIDTHMIN"),

    /**
     * The '<em><b>PULSEWIDTHMAX</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #PULSEWIDTHMAX_VALUE
     * @generated
     * @ordered
     */
    PULSEWIDTHMAX(0, "PULSEWIDTHMAX", "PULSEWIDTHMAX"),

    /**
     * The '<em><b>PERIOD</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #PERIOD_VALUE
     * @generated
     * @ordered
     */
    PERIOD(0, "PERIOD", "PERIOD"),
    /**
     * The '<em><b>POSITION</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #POSITION_VALUE
     * @generated
     * @ordered
     */
    POSITION(0, "POSITION", "POSITION"),
    /**
     * The '<em><b>LEFTPOSITION</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #LEFTPOSITION_VALUE
     * @generated
     * @ordered
     */
    LEFTPOSITION(0, "LEFTPOSITION", "LEFTPOSITION"),
    /**
     * The '<em><b>RIGHTPOSITION</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #RIGHTPOSITION_VALUE
     * @generated
     * @ordered
     */
    RIGHTPOSITION(0, "RIGHTPOSITION", "RIGHTPOSITION");

    /**
     * The '<em><b>VELOCITY</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>VELOCITY</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #VELOCITY
     * @model
     * @generated
     * @ordered
     */
    public static final int VELOCITY_VALUE = 0;

    /**
     * The '<em><b>ACCELERATION</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>ACCELERATION</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #ACCELERATION
     * @model
     * @generated
     * @ordered
     */
    public static final int ACCELERATION_VALUE = 0;

    /**
     * The '<em><b>PULSEWIDTHMIN</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>PULSEWIDTHMIN</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #PULSEWIDTHMIN
     * @model
     * @generated
     * @ordered
     */
    public static final int PULSEWIDTHMIN_VALUE = 0;

    /**
     * The '<em><b>PULSEWIDTHMAX</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>PULSEWIDTHMAX</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #PULSEWIDTHMAX
     * @model
     * @generated
     * @ordered
     */
    public static final int PULSEWIDTHMAX_VALUE = 0;

    /**
     * The '<em><b>PERIOD</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>PERIOD</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #PERIOD
     * @model
     * @generated
     * @ordered
     */
    public static final int PERIOD_VALUE = 0;

    /**
     * The '<em><b>POSITION</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>POSITION</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #POSITION
     * @model
     * @generated
     * @ordered
     */
    public static final int POSITION_VALUE = 0;

    /**
     * The '<em><b>LEFTPOSITION</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>LEFTPOSITION</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #LEFTPOSITION
     * @model
     * @generated
     * @ordered
     */
    public static final int LEFTPOSITION_VALUE = 0;

    /**
     * The '<em><b>RIGHTPOSITION</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>RIGHTPOSITION</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #RIGHTPOSITION
     * @model
     * @generated
     * @ordered
     */
    public static final int RIGHTPOSITION_VALUE = 0;

    /**
     * An array of all the '<em><b>Config Opts Servo</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    private static final ConfigOptsServo[] VALUES_ARRAY = new ConfigOptsServo[] { VELOCITY, ACCELERATION, PULSEWIDTHMIN,
            PULSEWIDTHMAX, PERIOD, POSITION, LEFTPOSITION, RIGHTPOSITION, };

    /**
     * A public read-only list of all the '<em><b>Config Opts Servo</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public static final List<ConfigOptsServo> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Config Opts Servo</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static ConfigOptsServo get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ConfigOptsServo result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Config Opts Servo</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static ConfigOptsServo getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            ConfigOptsServo result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Config Opts Servo</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static ConfigOptsServo get(int value) {
        switch (value) {
            case VELOCITY_VALUE:
                return VELOCITY;
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
    private ConfigOptsServo(int value, String name, String literal) {
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

} // ConfigOptsServo
