/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Industrial Digital In Sub IDs</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getIndustrialDigitalInSubIDs()
 * @model
 * @generated
 */
public enum IndustrialDigitalInSubIDs implements Enumerator
{
  /**
   * The '<em><b>IN0</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #IN0_VALUE
   * @generated
   * @ordered
   */
  IN0(0, "IN0", "IN0"),

  /**
   * The '<em><b>IN1</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #IN1_VALUE
   * @generated
   * @ordered
   */
  IN1(0, "IN1", "IN1"),

  /**
   * The '<em><b>IN2</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #IN2_VALUE
   * @generated
   * @ordered
   */
  IN2(0, "IN2", "IN2"),

  /**
   * The '<em><b>IN3</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #IN3_VALUE
   * @generated
   * @ordered
   */
  IN3(0, "IN3", "IN3");

  /**
   * The '<em><b>IN0</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>IN0</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #IN0
   * @model
   * @generated
   * @ordered
   */
  public static final int IN0_VALUE = 0;

  /**
   * The '<em><b>IN1</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>IN1</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #IN1
   * @model
   * @generated
   * @ordered
   */
  public static final int IN1_VALUE = 0;

  /**
   * The '<em><b>IN2</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>IN2</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #IN2
   * @model
   * @generated
   * @ordered
   */
  public static final int IN2_VALUE = 0;

  /**
   * The '<em><b>IN3</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>IN3</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #IN3
   * @model
   * @generated
   * @ordered
   */
  public static final int IN3_VALUE = 0;

  /**
   * An array of all the '<em><b>Industrial Digital In Sub IDs</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final IndustrialDigitalInSubIDs[] VALUES_ARRAY =
    new IndustrialDigitalInSubIDs[]
    {
      IN0,
      IN1,
      IN2,
      IN3,
    };

  /**
   * A public read-only list of all the '<em><b>Industrial Digital In Sub IDs</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<IndustrialDigitalInSubIDs> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Industrial Digital In Sub IDs</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialDigitalInSubIDs get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      IndustrialDigitalInSubIDs result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Industrial Digital In Sub IDs</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialDigitalInSubIDs getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      IndustrialDigitalInSubIDs result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Industrial Digital In Sub IDs</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialDigitalInSubIDs get(int value)
  {
    switch (value)
    {
      case IN0_VALUE: return IN0;
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
  private IndustrialDigitalInSubIDs(int value, String name, String literal)
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
  
} //IndustrialDigitalInSubIDs
