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
 * A representation of the literals of the enumeration '<em><b>Config Opts Move</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getConfigOptsMove()
 * @model
 * @generated
 */
public enum ConfigOptsMove implements Enumerator
{
  /**
   * The '<em><b>RIGHTSPEED</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RIGHTSPEED_VALUE
   * @generated
   * @ordered
   */
  RIGHTSPEED(0, "RIGHTSPEED", "RIGHTSPEED"),

  /**
   * The '<em><b>LEFTSPEED</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #LEFTSPEED_VALUE
   * @generated
   * @ordered
   */
  LEFTSPEED(0, "LEFTSPEED", "LEFTSPEED"), /**
   * The '<em><b>ACCELERATION</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ACCELERATION_VALUE
   * @generated
   * @ordered
   */
  ACCELERATION(0, "ACCELERATION", "ACCELERATION"), /**
   * The '<em><b>DRIVEMODE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #DRIVEMODE_VALUE
   * @generated
   * @ordered
   */
  DRIVEMODE(0, "DRIVEMODE", "DRIVEMODE"), /**
   * The '<em><b>PWM</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PWM_VALUE
   * @generated
   * @ordered
   */
  PWM(0, "PWM", "PWM");

  /**
   * The '<em><b>RIGHTSPEED</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>RIGHTSPEED</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #RIGHTSPEED
   * @model
   * @generated
   * @ordered
   */
  public static final int RIGHTSPEED_VALUE = 0;

  /**
   * The '<em><b>LEFTSPEED</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>LEFTSPEED</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #LEFTSPEED
   * @model
   * @generated
   * @ordered
   */
  public static final int LEFTSPEED_VALUE = 0;

  /**
   * The '<em><b>ACCELERATION</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>ACCELERATION</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #ACCELERATION
   * @model
   * @generated
   * @ordered
   */
  public static final int ACCELERATION_VALUE = 0;

  /**
   * The '<em><b>DRIVEMODE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>DRIVEMODE</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #DRIVEMODE
   * @model
   * @generated
   * @ordered
   */
  public static final int DRIVEMODE_VALUE = 0;

  /**
   * The '<em><b>PWM</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>PWM</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #PWM
   * @model
   * @generated
   * @ordered
   */
  public static final int PWM_VALUE = 0;

  /**
   * An array of all the '<em><b>Config Opts Move</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final ConfigOptsMove[] VALUES_ARRAY =
    new ConfigOptsMove[]
    {
      RIGHTSPEED,
      LEFTSPEED,
      ACCELERATION,
      DRIVEMODE,
      PWM,
    };

  /**
   * A public read-only list of all the '<em><b>Config Opts Move</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<ConfigOptsMove> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Config Opts Move</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ConfigOptsMove get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ConfigOptsMove result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Config Opts Move</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ConfigOptsMove getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ConfigOptsMove result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Config Opts Move</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ConfigOptsMove get(int value)
  {
    switch (value)
    {
      case RIGHTSPEED_VALUE: return RIGHTSPEED;
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
  private ConfigOptsMove(int value, String name, String literal)
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
  
} //ConfigOptsMove
