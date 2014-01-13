/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Industrial Quad Relay IDs</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getIndustrialQuadRelayIDs()
 * @model
 * @generated
 */
public enum IndustrialQuadRelayIDs implements Enumerator
{
  /**
   * The '<em><b>RELAY0</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RELAY0_VALUE
   * @generated
   * @ordered
   */
  RELAY0(0, "RELAY0", "RELAY0"),

  /**
   * The '<em><b>RELAY1</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RELAY1_VALUE
   * @generated
   * @ordered
   */
  RELAY1(0, "RELAY1", "RELAY1"),

  /**
   * The '<em><b>RELAY2</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RELAY2_VALUE
   * @generated
   * @ordered
   */
  RELAY2(0, "RELAY2", "RELAY2"),

  /**
   * The '<em><b>RELAY3</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RELAY3_VALUE
   * @generated
   * @ordered
   */
  RELAY3(0, "RELAY3", "RELAY3");

  /**
   * The '<em><b>RELAY0</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>RELAY0</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #RELAY0
   * @model
   * @generated
   * @ordered
   */
  public static final int RELAY0_VALUE = 0;

  /**
   * The '<em><b>RELAY1</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>RELAY1</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #RELAY1
   * @model
   * @generated
   * @ordered
   */
  public static final int RELAY1_VALUE = 0;

  /**
   * The '<em><b>RELAY2</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>RELAY2</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #RELAY2
   * @model
   * @generated
   * @ordered
   */
  public static final int RELAY2_VALUE = 0;

  /**
   * The '<em><b>RELAY3</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>RELAY3</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #RELAY3
   * @model
   * @generated
   * @ordered
   */
  public static final int RELAY3_VALUE = 0;

  /**
   * An array of all the '<em><b>Industrial Quad Relay IDs</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final IndustrialQuadRelayIDs[] VALUES_ARRAY =
    new IndustrialQuadRelayIDs[]
    {
      RELAY0,
      RELAY1,
      RELAY2,
      RELAY3,
    };

  /**
   * A public read-only list of all the '<em><b>Industrial Quad Relay IDs</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<IndustrialQuadRelayIDs> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Industrial Quad Relay IDs</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialQuadRelayIDs get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      IndustrialQuadRelayIDs result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Industrial Quad Relay IDs</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialQuadRelayIDs getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      IndustrialQuadRelayIDs result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Industrial Quad Relay IDs</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialQuadRelayIDs get(int value)
  {
    switch (value)
    {
      case RELAY0_VALUE: return RELAY0;
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
  private IndustrialQuadRelayIDs(int value, String name, String literal)
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
  
} //IndustrialQuadRelayIDs
