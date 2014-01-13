/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Servo Sub IDs</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getServoSubIDs()
 * @model
 * @generated
 */
public enum ServoSubIDs implements Enumerator
{
  /**
   * The '<em><b>SERVO0</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SERVO0_VALUE
   * @generated
   * @ordered
   */
  SERVO0(0, "SERVO0", "SERVO0"),

  /**
   * The '<em><b>SERVO1</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SERVO1_VALUE
   * @generated
   * @ordered
   */
  SERVO1(0, "SERVO1", "SERVO1"),

  /**
   * The '<em><b>SERVO2</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SERVO2_VALUE
   * @generated
   * @ordered
   */
  SERVO2(0, "SERVO2", "SERVO2"),

  /**
   * The '<em><b>SERVO3</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SERVO3_VALUE
   * @generated
   * @ordered
   */
  SERVO3(0, "SERVO3", "SERVO3"),

  /**
   * The '<em><b>SERVO4</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SERVO4_VALUE
   * @generated
   * @ordered
   */
  SERVO4(0, "SERVO4", "SERVO4"),

  /**
   * The '<em><b>SERVO5</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SERVO5_VALUE
   * @generated
   * @ordered
   */
  SERVO5(0, "SERVO5", "SERVO5"),

  /**
   * The '<em><b>SERVO6</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #SERVO6_VALUE
   * @generated
   * @ordered
   */
  SERVO6(0, "SERVO6", "SERVO6");

  /**
   * The '<em><b>SERVO0</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>SERVO0</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SERVO0
   * @model
   * @generated
   * @ordered
   */
  public static final int SERVO0_VALUE = 0;

  /**
   * The '<em><b>SERVO1</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>SERVO1</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SERVO1
   * @model
   * @generated
   * @ordered
   */
  public static final int SERVO1_VALUE = 0;

  /**
   * The '<em><b>SERVO2</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>SERVO2</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SERVO2
   * @model
   * @generated
   * @ordered
   */
  public static final int SERVO2_VALUE = 0;

  /**
   * The '<em><b>SERVO3</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>SERVO3</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SERVO3
   * @model
   * @generated
   * @ordered
   */
  public static final int SERVO3_VALUE = 0;

  /**
   * The '<em><b>SERVO4</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>SERVO4</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SERVO4
   * @model
   * @generated
   * @ordered
   */
  public static final int SERVO4_VALUE = 0;

  /**
   * The '<em><b>SERVO5</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>SERVO5</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SERVO5
   * @model
   * @generated
   * @ordered
   */
  public static final int SERVO5_VALUE = 0;

  /**
   * The '<em><b>SERVO6</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>SERVO6</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #SERVO6
   * @model
   * @generated
   * @ordered
   */
  public static final int SERVO6_VALUE = 0;

  /**
   * An array of all the '<em><b>Servo Sub IDs</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final ServoSubIDs[] VALUES_ARRAY =
    new ServoSubIDs[]
    {
      SERVO0,
      SERVO1,
      SERVO2,
      SERVO3,
      SERVO4,
      SERVO5,
      SERVO6,
    };

  /**
   * A public read-only list of all the '<em><b>Servo Sub IDs</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<ServoSubIDs> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Servo Sub IDs</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ServoSubIDs get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ServoSubIDs result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Servo Sub IDs</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ServoSubIDs getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ServoSubIDs result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Servo Sub IDs</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static ServoSubIDs get(int value)
  {
    switch (value)
    {
      case SERVO0_VALUE: return SERVO0;
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
  private ServoSubIDs(int value, String name, String literal)
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
  
} //ServoSubIDs
