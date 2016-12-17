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
 * A representation of the literals of the enumeration '<em><b>Brick Stepper Sub Ids</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getBrickStepperSubIds()
 * @model
 * @generated
 */
public enum BrickStepperSubIds implements Enumerator {
    /**
     * The '<em><b>Stepper drive</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_DRIVE_VALUE
     * @generated
     * @ordered
     */
    STEPPER_DRIVE(0, "stepper_drive", "stepper_drive"),

    /**
     * The '<em><b>Stepper velocity</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_VELOCITY_VALUE
     * @generated
     * @ordered
     */
    STEPPER_VELOCITY(0, "stepper_velocity", "stepper_velocity"),

    /**
     * The '<em><b>Stepper current</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_CURRENT_VALUE
     * @generated
     * @ordered
     */
    STEPPER_CURRENT(0, "stepper_current", "stepper_current"),

    /**
     * The '<em><b>Stepper position</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_POSITION_VALUE
     * @generated
     * @ordered
     */
    STEPPER_POSITION(0, "stepper_position", "stepper_position"),

    /**
     * The '<em><b>Stepper steps</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_STEPS_VALUE
     * @generated
     * @ordered
     */
    STEPPER_STEPS(0, "stepper_steps", "stepper_steps"),

    /**
     * The '<em><b>Stepper stack voltage</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_STACK_VOLTAGE_VALUE
     * @generated
     * @ordered
     */
    STEPPER_STACK_VOLTAGE(0, "stepper_stack_voltage", "stepper_stack_voltage"),

    /**
     * The '<em><b>Stepper external voltage</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_EXTERNAL_VOLTAGE_VALUE
     * @generated
     * @ordered
     */
    STEPPER_EXTERNAL_VOLTAGE(0, "stepper_external_voltage", "stepper_external_voltage"),

    /**
     * The '<em><b>Stepper consumption</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_CONSUMPTION_VALUE
     * @generated
     * @ordered
     */
    STEPPER_CONSUMPTION(0, "stepper_consumption", "stepper_consumption"),

    /**
     * The '<em><b>Stepper under voltage</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_UNDER_VOLTAGE_VALUE
     * @generated
     * @ordered
     */
    STEPPER_UNDER_VOLTAGE(0, "stepper_under_voltage", "stepper_under_voltage"),

    /**
     * The '<em><b>Stepper state</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_STATE_VALUE
     * @generated
     * @ordered
     */
    STEPPER_STATE(0, "stepper_state", "stepper_state"),

    /**
     * The '<em><b>Stepper chip temperature</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_CHIP_TEMPERATURE_VALUE
     * @generated
     * @ordered
     */
    STEPPER_CHIP_TEMPERATURE(0, "stepper_chip_temperature", "stepper_chip_temperature"),

    /**
     * The '<em><b>Stepper led</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_LED_VALUE
     * @generated
     * @ordered
     */
    STEPPER_LED(0, "stepper_led", "stepper_led");

    /**
     * The '<em><b>Stepper drive</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper drive</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_DRIVE
     * @model name="stepper_drive"
     * @generated
     * @ordered
     */
    public static final int STEPPER_DRIVE_VALUE = 0;

    /**
     * The '<em><b>Stepper velocity</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper velocity</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_VELOCITY
     * @model name="stepper_velocity"
     * @generated
     * @ordered
     */
    public static final int STEPPER_VELOCITY_VALUE = 0;

    /**
     * The '<em><b>Stepper current</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper current</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_CURRENT
     * @model name="stepper_current"
     * @generated
     * @ordered
     */
    public static final int STEPPER_CURRENT_VALUE = 0;

    /**
     * The '<em><b>Stepper position</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper position</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_POSITION
     * @model name="stepper_position"
     * @generated
     * @ordered
     */
    public static final int STEPPER_POSITION_VALUE = 0;

    /**
     * The '<em><b>Stepper steps</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper steps</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_STEPS
     * @model name="stepper_steps"
     * @generated
     * @ordered
     */
    public static final int STEPPER_STEPS_VALUE = 0;

    /**
     * The '<em><b>Stepper stack voltage</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper stack voltage</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_STACK_VOLTAGE
     * @model name="stepper_stack_voltage"
     * @generated
     * @ordered
     */
    public static final int STEPPER_STACK_VOLTAGE_VALUE = 0;

    /**
     * The '<em><b>Stepper external voltage</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper external voltage</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_EXTERNAL_VOLTAGE
     * @model name="stepper_external_voltage"
     * @generated
     * @ordered
     */
    public static final int STEPPER_EXTERNAL_VOLTAGE_VALUE = 0;

    /**
     * The '<em><b>Stepper consumption</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper consumption</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_CONSUMPTION
     * @model name="stepper_consumption"
     * @generated
     * @ordered
     */
    public static final int STEPPER_CONSUMPTION_VALUE = 0;

    /**
     * The '<em><b>Stepper under voltage</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper under voltage</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_UNDER_VOLTAGE
     * @model name="stepper_under_voltage"
     * @generated
     * @ordered
     */
    public static final int STEPPER_UNDER_VOLTAGE_VALUE = 0;

    /**
     * The '<em><b>Stepper state</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper state</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_STATE
     * @model name="stepper_state"
     * @generated
     * @ordered
     */
    public static final int STEPPER_STATE_VALUE = 0;

    /**
     * The '<em><b>Stepper chip temperature</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper chip temperature</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_CHIP_TEMPERATURE
     * @model name="stepper_chip_temperature"
     * @generated
     * @ordered
     */
    public static final int STEPPER_CHIP_TEMPERATURE_VALUE = 0;

    /**
     * The '<em><b>Stepper led</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stepper led</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @see #STEPPER_LED
     * @model name="stepper_led"
     * @generated
     * @ordered
     */
    public static final int STEPPER_LED_VALUE = 0;

    /**
     * An array of all the '<em><b>Brick Stepper Sub Ids</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    private static final BrickStepperSubIds[] VALUES_ARRAY = new BrickStepperSubIds[] { STEPPER_DRIVE, STEPPER_VELOCITY,
            STEPPER_CURRENT, STEPPER_POSITION, STEPPER_STEPS, STEPPER_STACK_VOLTAGE, STEPPER_EXTERNAL_VOLTAGE,
            STEPPER_CONSUMPTION, STEPPER_UNDER_VOLTAGE, STEPPER_STATE, STEPPER_CHIP_TEMPERATURE, STEPPER_LED, };

    /**
     * A public read-only list of all the '<em><b>Brick Stepper Sub Ids</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public static final List<BrickStepperSubIds> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Brick Stepper Sub Ids</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static BrickStepperSubIds get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            BrickStepperSubIds result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Brick Stepper Sub Ids</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static BrickStepperSubIds getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            BrickStepperSubIds result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Brick Stepper Sub Ids</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static BrickStepperSubIds get(int value) {
        switch (value) {
            case STEPPER_DRIVE_VALUE:
                return STEPPER_DRIVE;
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
    private BrickStepperSubIds(int value, String name, String literal) {
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
    public int getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
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

} // BrickStepperSubIds
