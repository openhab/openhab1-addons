/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
 * A representation of the literals of the enumeration '<em><b>Joystick Sub Ids</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getJoystickSubIds()
 * @model
 * @generated
 */
public enum JoystickSubIds implements Enumerator
{
  /**
   * The '<em><b>Joystick xposition</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #JOYSTICK_XPOSITION_VALUE
   * @generated
   * @ordered
   */
  JOYSTICK_XPOSITION(0, "joystick_xposition", "joystick_xposition"),

  /**
   * The '<em><b>Joystick yposition</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #JOYSTICK_YPOSITION_VALUE
   * @generated
   * @ordered
   */
  JOYSTICK_YPOSITION(0, "joystick_yposition", "joystick_yposition"),

  /**
   * The '<em><b>Joystick button</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #JOYSTICK_BUTTON_VALUE
   * @generated
   * @ordered
   */
  JOYSTICK_BUTTON(0, "joystick_button", "joystick_button");

  /**
   * The '<em><b>Joystick xposition</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Joystick xposition</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #JOYSTICK_XPOSITION
   * @model name="joystick_xposition"
   * @generated
   * @ordered
   */
  public static final int JOYSTICK_XPOSITION_VALUE = 0;

  /**
   * The '<em><b>Joystick yposition</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Joystick yposition</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #JOYSTICK_YPOSITION
   * @model name="joystick_yposition"
   * @generated
   * @ordered
   */
  public static final int JOYSTICK_YPOSITION_VALUE = 0;

  /**
   * The '<em><b>Joystick button</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Joystick button</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #JOYSTICK_BUTTON
   * @model name="joystick_button"
   * @generated
   * @ordered
   */
  public static final int JOYSTICK_BUTTON_VALUE = 0;

  /**
   * An array of all the '<em><b>Joystick Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final JoystickSubIds[] VALUES_ARRAY =
    new JoystickSubIds[]
    {
      JOYSTICK_XPOSITION,
      JOYSTICK_YPOSITION,
      JOYSTICK_BUTTON,
    };

  /**
   * A public read-only list of all the '<em><b>Joystick Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<JoystickSubIds> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Joystick Sub Ids</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static JoystickSubIds get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      JoystickSubIds result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Joystick Sub Ids</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static JoystickSubIds getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      JoystickSubIds result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Joystick Sub Ids</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static JoystickSubIds get(int value)
  {
    switch (value)
    {
      case JOYSTICK_XPOSITION_VALUE: return JOYSTICK_XPOSITION;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private JoystickSubIds(int value, String name, String literal)
  {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLiteral()
  {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    return literal;
  }
  
} //JoystickSubIds
