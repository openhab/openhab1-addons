/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.HighLowValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Digital Actor IO16</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.4.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16#getPort <em>Port</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16#getPin <em>Pin</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16#getDefaultState <em>Default State</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16#isKeepOnReconnect <em>Keep On Reconnect</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActorIO16()
 * @model
 * @generated
 */
public interface DigitalActorIO16 extends DigitalActor, IODevice, MTFConfigConsumer<TFIOActorConfiguration>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"io_actuator"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActorIO16_DeviceType()
   * @model default="io_actuator" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Port</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Port</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Port</em>' attribute.
   * @see #setPort(char)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActorIO16_Port()
   * @model unique="false"
   * @generated
   */
  char getPort();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16#getPort <em>Port</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Port</em>' attribute.
   * @see #getPort()
   * @generated
   */
  void setPort(char value);

  /**
   * Returns the value of the '<em><b>Pin</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pin</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pin</em>' attribute.
   * @see #setPin(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActorIO16_Pin()
   * @model unique="false"
   * @generated
   */
  int getPin();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16#getPin <em>Pin</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pin</em>' attribute.
   * @see #getPin()
   * @generated
   */
  void setPin(int value);

  /**
   * Returns the value of the '<em><b>Default State</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Default State</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Default State</em>' attribute.
   * @see #setDefaultState(String)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActorIO16_DefaultState()
   * @model unique="false"
   * @generated
   */
  String getDefaultState();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16#getDefaultState <em>Default State</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Default State</em>' attribute.
   * @see #getDefaultState()
   * @generated
   */
  void setDefaultState(String value);

  /**
   * Returns the value of the '<em><b>Keep On Reconnect</b></em>' attribute.
   * The default value is <code>"false"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Keep On Reconnect</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Keep On Reconnect</em>' attribute.
   * @see #setKeepOnReconnect(boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getDigitalActorIO16_KeepOnReconnect()
   * @model default="false" unique="false"
   * @generated
   */
  boolean isKeepOnReconnect();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.DigitalActorIO16#isKeepOnReconnect <em>Keep On Reconnect</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Keep On Reconnect</em>' attribute.
   * @see #isKeepOnReconnect()
   * @generated
   */
  void setKeepOnReconnect(boolean value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model digitalStateDataType="org.openhab.binding.tinkerforge.internal.model.DigitalValue" digitalStateUnique="false"
   * @generated
   */
  void turnDigital(HighLowValue digitalState);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  void fetchDigitalValue();

} // DigitalActorIO16
