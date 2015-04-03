/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LED Strip Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getChiptype <em>Chiptype</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getFrameduration <em>Frameduration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getClockfrequency <em>Clockfrequency</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration()
 * @model
 * @generated
 */
public interface LEDStripConfiguration extends TFConfig
{
  /**
   * Returns the value of the '<em><b>Chiptype</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Chiptype</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Chiptype</em>' attribute.
   * @see #setChiptype(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration_Chiptype()
   * @model unique="false"
   * @generated
   */
  String getChiptype();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getChiptype <em>Chiptype</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Chiptype</em>' attribute.
   * @see #getChiptype()
   * @generated
   */
  void setChiptype(String value);

  /**
   * Returns the value of the '<em><b>Frameduration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Frameduration</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Frameduration</em>' attribute.
   * @see #setFrameduration(Integer)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration_Frameduration()
   * @model unique="false"
   * @generated
   */
  Integer getFrameduration();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getFrameduration <em>Frameduration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Frameduration</em>' attribute.
   * @see #getFrameduration()
   * @generated
   */
  void setFrameduration(Integer value);

  /**
   * Returns the value of the '<em><b>Clockfrequency</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Clockfrequency</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Clockfrequency</em>' attribute.
   * @see #setClockfrequency(Long)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLEDStripConfiguration_Clockfrequency()
   * @model unique="false"
   * @generated
   */
  Long getClockfrequency();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.LEDStripConfiguration#getClockfrequency <em>Clockfrequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Clockfrequency</em>' attribute.
   * @see #getClockfrequency()
   * @generated
   */
  void setClockfrequency(Long value);

} // LEDStripConfiguration
