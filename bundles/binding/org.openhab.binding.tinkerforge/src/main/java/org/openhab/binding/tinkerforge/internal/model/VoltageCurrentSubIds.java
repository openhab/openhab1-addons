/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
 * A representation of the literals of the enumeration '<em><b>Voltage Current Sub Ids</b></em>',
 * and utility methods for working with them.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getVoltageCurrentSubIds()
 * @model
 * @generated
 */
public enum VoltageCurrentSubIds implements Enumerator
{
  /**
   * The '<em><b>VOLTAGECURRENT VOLTAGE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #VOLTAGECURRENT_VOLTAGE_VALUE
   * @generated
   * @ordered
   */
  VOLTAGECURRENT_VOLTAGE(0, "VOLTAGECURRENT_VOLTAGE", "VOLTAGECURRENT_VOLTAGE"), /**
   * The '<em><b>VOLTAGECURRENT CURRENT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #VOLTAGECURRENT_CURRENT_VALUE
   * @generated
   * @ordered
   */
  VOLTAGECURRENT_CURRENT(0, "VOLTAGECURRENT_CURRENT", "VOLTAGECURRENT_CURRENT"), /**
   * The '<em><b>VOLTAGECURRENT POWER</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #VOLTAGECURRENT_POWER_VALUE
   * @generated
   * @ordered
   */
  VOLTAGECURRENT_POWER(0, "VOLTAGECURRENT_POWER", "VOLTAGECURRENT_POWER");

  /**
   * The '<em><b>VOLTAGECURRENT VOLTAGE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>VOLTAGECURRENT VOLTAGE</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #VOLTAGECURRENT_VOLTAGE
   * @model
   * @generated
   * @ordered
   */
  public static final int VOLTAGECURRENT_VOLTAGE_VALUE = 0;

  /**
   * The '<em><b>VOLTAGECURRENT CURRENT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>VOLTAGECURRENT CURRENT</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #VOLTAGECURRENT_CURRENT
   * @model
   * @generated
   * @ordered
   */
  public static final int VOLTAGECURRENT_CURRENT_VALUE = 0;

  /**
   * The '<em><b>VOLTAGECURRENT POWER</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>VOLTAGECURRENT POWER</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #VOLTAGECURRENT_POWER
   * @model
   * @generated
   * @ordered
   */
  public static final int VOLTAGECURRENT_POWER_VALUE = 0;

  /**
   * An array of all the '<em><b>Voltage Current Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final VoltageCurrentSubIds[] VALUES_ARRAY =
    new VoltageCurrentSubIds[]
    {
      VOLTAGECURRENT_VOLTAGE,
      VOLTAGECURRENT_CURRENT,
      VOLTAGECURRENT_POWER,
    };

  /**
   * A public read-only list of all the '<em><b>Voltage Current Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<VoltageCurrentSubIds> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Voltage Current Sub Ids</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static VoltageCurrentSubIds get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      VoltageCurrentSubIds result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Voltage Current Sub Ids</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static VoltageCurrentSubIds getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      VoltageCurrentSubIds result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Voltage Current Sub Ids</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static VoltageCurrentSubIds get(int value)
  {
    switch (value)
    {
      case VOLTAGECURRENT_VOLTAGE_VALUE: return VOLTAGECURRENT_VOLTAGE;
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
  private VoltageCurrentSubIds(int value, String name, String literal)
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
  
} //VoltageCurrentSubIds
