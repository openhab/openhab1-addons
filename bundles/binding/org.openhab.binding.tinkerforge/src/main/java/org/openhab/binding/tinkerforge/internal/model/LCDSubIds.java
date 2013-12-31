/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>LCD Sub Ids</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLCDSubIds()
 * @model
 * @generated
 */
public enum LCDSubIds implements Enumerator
{
  /**
   * The '<em><b>BACKLIGHT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BACKLIGHT_VALUE
   * @generated
   * @ordered
   */
  BACKLIGHT(0, "BACKLIGHT", "BACKLIGHT"),

  /**
   * The '<em><b>BUTTON0</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BUTTON0_VALUE
   * @generated
   * @ordered
   */
  BUTTON0(0, "BUTTON0", "BUTTON0"),

  /**
   * The '<em><b>BUTTON1</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BUTTON1_VALUE
   * @generated
   * @ordered
   */
  BUTTON1(0, "BUTTON1", "BUTTON1"),

  /**
   * The '<em><b>BUTTON2</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BUTTON2_VALUE
   * @generated
   * @ordered
   */
  BUTTON2(0, "BUTTON2", "BUTTON2"),

  /**
   * The '<em><b>BUTTON3</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BUTTON3_VALUE
   * @generated
   * @ordered
   */
  BUTTON3(0, "BUTTON3", "BUTTON3");

  /**
   * The '<em><b>BACKLIGHT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>BACKLIGHT</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #BACKLIGHT
   * @model
   * @generated
   * @ordered
   */
  public static final int BACKLIGHT_VALUE = 0;

  /**
   * The '<em><b>BUTTON0</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>BUTTON0</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #BUTTON0
   * @model
   * @generated
   * @ordered
   */
  public static final int BUTTON0_VALUE = 0;

  /**
   * The '<em><b>BUTTON1</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>BUTTON1</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #BUTTON1
   * @model
   * @generated
   * @ordered
   */
  public static final int BUTTON1_VALUE = 0;

  /**
   * The '<em><b>BUTTON2</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>BUTTON2</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #BUTTON2
   * @model
   * @generated
   * @ordered
   */
  public static final int BUTTON2_VALUE = 0;

  /**
   * The '<em><b>BUTTON3</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>BUTTON3</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #BUTTON3
   * @model
   * @generated
   * @ordered
   */
  public static final int BUTTON3_VALUE = 0;

  /**
   * An array of all the '<em><b>LCD Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final LCDSubIds[] VALUES_ARRAY =
    new LCDSubIds[]
    {
      BACKLIGHT,
      BUTTON0,
      BUTTON1,
      BUTTON2,
      BUTTON3,
    };

  /**
   * A public read-only list of all the '<em><b>LCD Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<LCDSubIds> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>LCD Sub Ids</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static LCDSubIds get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      LCDSubIds result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>LCD Sub Ids</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static LCDSubIds getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      LCDSubIds result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>LCD Sub Ids</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static LCDSubIds get(int value)
  {
    switch (value)
    {
      case BACKLIGHT_VALUE: return BACKLIGHT;
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
  private LCDSubIds(int value, String name, String literal)
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
  
} //LCDSubIds
