/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Industrial Dual Analog In Sub Ids</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getIndustrialDualAnalogInSubIds()
 * @model
 * @generated
 */
public enum IndustrialDualAnalogInSubIds implements Enumerator
{
  /**
   * The '<em><b>Channel0</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CHANNEL0_VALUE
   * @generated
   * @ordered
   */
  CHANNEL0(0, "channel0", "channel0"),

  /**
   * The '<em><b>Channel1</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CHANNEL1_VALUE
   * @generated
   * @ordered
   */
  CHANNEL1(0, "channel1", "channel1");

  /**
   * The '<em><b>Channel0</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Channel0</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #CHANNEL0
   * @model name="channel0"
   * @generated
   * @ordered
   */
  public static final int CHANNEL0_VALUE = 0;

  /**
   * The '<em><b>Channel1</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Channel1</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #CHANNEL1
   * @model name="channel1"
   * @generated
   * @ordered
   */
  public static final int CHANNEL1_VALUE = 0;

  /**
   * An array of all the '<em><b>Industrial Dual Analog In Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final IndustrialDualAnalogInSubIds[] VALUES_ARRAY =
    new IndustrialDualAnalogInSubIds[]
    {
      CHANNEL0,
      CHANNEL1,
    };

  /**
   * A public read-only list of all the '<em><b>Industrial Dual Analog In Sub Ids</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<IndustrialDualAnalogInSubIds> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Industrial Dual Analog In Sub Ids</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialDualAnalogInSubIds get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      IndustrialDualAnalogInSubIds result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Industrial Dual Analog In Sub Ids</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialDualAnalogInSubIds getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      IndustrialDualAnalogInSubIds result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Industrial Dual Analog In Sub Ids</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static IndustrialDualAnalogInSubIds get(int value)
  {
    switch (value)
    {
      case CHANNEL0_VALUE: return CHANNEL0;
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
  private IndustrialDualAnalogInSubIds(int value, String name, String literal)
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
  
} //IndustrialDualAnalogInSubIds
