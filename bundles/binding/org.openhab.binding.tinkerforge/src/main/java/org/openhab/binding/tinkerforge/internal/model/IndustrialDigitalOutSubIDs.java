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
 * A representation of the literals of the enumeration '<em><b>Industrial Digital Out Sub IDs</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getIndustrialDigitalOutSubIDs()
 * @model
 * @generated
 */
public enum IndustrialDigitalOutSubIDs implements Enumerator
{
  /**
   * The '<em><b>OUT0</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OUT0_VALUE
   * @generated
   * @ordered
   */
  OUT0(0, "OUT0", "OUT0"),

  /**
   * The '<em><b>OUT1</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OUT1_VALUE
   * @generated
   * @ordered
   */
  OUT1(0, "OUT1", "OUT1"),

  /**
   * The '<em><b>OUT2</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OUT2_VALUE
   * @generated
   * @ordered
   */
  OUT2(0, "OUT2", "OUT2"),

  /**
   * The '<em><b>OUT3</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OUT3_VALUE
   * @generated
   * @ordered
   */
  OUT3(0, "OUT3", "OUT3");

  /**
   * The '<em><b>OUT0</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>OUT0</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #OUT0
   * @model
   * @generated
   * @ordered
   */
  public static final int OUT0_VALUE = 0;

  /**
   * The '<em><b>OUT1</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>OUT1</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #OUT1
   * @model
   * @generated
   * @ordered
   */
  public static final int OUT1_VALUE = 0;

  /**
   * The '<em><b>OUT2</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>OUT2</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #OUT2
   * @model
   * @generated
   * @ordered
   */
  public static final int OUT2_VALUE = 0;

  /**
   * The '<em><b>OUT3</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>OUT3</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #OUT3
   * @model
   * @generated
   * @ordered
   */
  public static final int OUT3_VALUE = 0;

  /**
   * An array of all the '<em><b>Industrial Digital Out Sub IDs</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final IndustrialDigitalOutSubIDs[] VALUES_ARRAY =
    new IndustrialDigitalOutSubIDs[]
    {
      OUT0,
      OUT1,
      OUT2,
      OUT3,
    };

  /**
   * A public read-only list of all the '<em><b>Industrial Digital Out Sub IDs</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<IndustrialDigitalOutSubIDs> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Industrial Digital Out Sub IDs</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialDigitalOutSubIDs get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      IndustrialDigitalOutSubIDs result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Industrial Digital Out Sub IDs</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialDigitalOutSubIDs getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      IndustrialDigitalOutSubIDs result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Industrial Digital Out Sub IDs</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialDigitalOutSubIDs get(int value)
  {
    switch (value)
    {
      case OUT0_VALUE: return OUT0;
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
  private IndustrialDigitalOutSubIDs(int value, String name, String literal)
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
  
} //IndustrialDigitalOutSubIDs
