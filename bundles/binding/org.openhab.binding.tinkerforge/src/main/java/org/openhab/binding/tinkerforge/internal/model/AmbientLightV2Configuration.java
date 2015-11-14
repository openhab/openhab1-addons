/**
 */
package org.openhab.binding.tinkerforge.internal.model;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ambient Light V2 Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.AmbientLightV2Configuration#getIlluminanceRange <em>Illuminance Range</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.AmbientLightV2Configuration#getIntegrationTime <em>Integration Time</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getAmbientLightV2Configuration()
 * @model
 * @generated
 */
public interface AmbientLightV2Configuration extends TFBaseConfiguration
{
  /**
   * Returns the value of the '<em><b>Illuminance Range</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Illuminance Range</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Illuminance Range</em>' attribute.
   * @see #setIlluminanceRange(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getAmbientLightV2Configuration_IlluminanceRange()
   * @model unique="false"
   * @generated
   */
  short getIlluminanceRange();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.AmbientLightV2Configuration#getIlluminanceRange <em>Illuminance Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Illuminance Range</em>' attribute.
   * @see #getIlluminanceRange()
   * @generated
   */
  void setIlluminanceRange(short value);

  /**
   * Returns the value of the '<em><b>Integration Time</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Integration Time</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Integration Time</em>' attribute.
   * @see #setIntegrationTime(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getAmbientLightV2Configuration_IntegrationTime()
   * @model unique="false"
   * @generated
   */
  short getIntegrationTime();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.AmbientLightV2Configuration#getIntegrationTime <em>Integration Time</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Integration Time</em>' attribute.
   * @see #getIntegrationTime()
   * @generated
   */
  void setIntegrationTime(short value);

} // AmbientLightV2Configuration
