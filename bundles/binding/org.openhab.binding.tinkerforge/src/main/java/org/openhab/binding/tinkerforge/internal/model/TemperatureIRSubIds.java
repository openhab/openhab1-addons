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
 * A representation of the literals of the enumeration '<em><b>Temperature IR Sub Ids</b></em>',
 * and utility methods for working with them.
 * 
 * @author Theo Weiss
 * @since 1.5.0
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTemperatureIRSubIds()
 * @model
 * @generated
 */
public enum TemperatureIRSubIds implements Enumerator
{
  /**
   * The '<em><b>OBJECT TEMPERATURE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #OBJECT_TEMPERATURE_VALUE
   * @generated
   * @ordered
   */
  OBJECT_TEMPERATURE(0, "OBJECT_TEMPERATURE", "OBJECT_TEMPERATURE"),

  /**
   * The '<em><b>AMBIENT TEMPERATURE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #AMBIENT_TEMPERATURE_VALUE
   * @generated
   * @ordered
   */
  AMBIENT_TEMPERATURE(0, "AMBIENT_TEMPERATURE", "AMBIENT_TEMPERATURE");

  /**
   * The '<em><b>OBJECT TEMPERATURE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>OBJECT TEMPERATURE</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #OBJECT_TEMPERATURE
   * @model
   * @generated
   * @ordered
   */
  public static final int OBJECT_TEMPERATURE_VALUE = 0;

  /**
   * The '<em><b>AMBIENT TEMPERATURE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>AMBIENT TEMPERATURE</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #AMBIENT_TEMPERATURE
   * @model
   * @generated
   * @ordered
   */
  public static final int AMBIENT_TEMPERATURE_VALUE = 0;

  /**
   * An array of all the '<em><b>Temperature IR Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final TemperatureIRSubIds[] VALUES_ARRAY =
    new TemperatureIRSubIds[]
    {
      OBJECT_TEMPERATURE,
      AMBIENT_TEMPERATURE,
    };

  /**
   * A public read-only list of all the '<em><b>Temperature IR Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<TemperatureIRSubIds> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Temperature IR Sub Ids</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TemperatureIRSubIds get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      TemperatureIRSubIds result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Temperature IR Sub Ids</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TemperatureIRSubIds getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      TemperatureIRSubIds result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Temperature IR Sub Ids</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static TemperatureIRSubIds get(int value)
  {
    switch (value)
    {
      case OBJECT_TEMPERATURE_VALUE: return OBJECT_TEMPERATURE;
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
  private TemperatureIRSubIds(int value, String name, String literal)
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
  
} //TemperatureIRSubIds
