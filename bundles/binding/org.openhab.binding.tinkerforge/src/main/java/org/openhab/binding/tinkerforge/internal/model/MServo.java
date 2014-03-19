/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;



/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MServo</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMin <em>Pulse Width Min</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMax <em>Pulse Width Max</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPeriod <em>Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getOutputVoltage <em>Output Voltage</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getServoCurrentPosition <em>Servo Current Position</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MServo#getServoDestinationPosition <em>Servo Destination Position</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo()
 * @model
 * @generated
 */
public interface MServo extends MInSwitchActor, MSubDevice<MBrickServo>, MTFConfigConsumer<TFServoConfiguration>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"servo"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_DeviceType()
   * @model default="servo" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Velocity</b></em>' attribute.
   * The default value is <code>"30000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Velocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Velocity</em>' attribute.
   * @see #setVelocity(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_Velocity()
   * @model default="30000" unique="false"
   * @generated
   */
  int getVelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getVelocity <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Velocity</em>' attribute.
   * @see #getVelocity()
   * @generated
   */
  void setVelocity(int value);

  /**
   * Returns the value of the '<em><b>Acceleration</b></em>' attribute.
   * The default value is <code>"30000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Acceleration</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Acceleration</em>' attribute.
   * @see #setAcceleration(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_Acceleration()
   * @model default="30000" unique="false"
   * @generated
   */
  int getAcceleration();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getAcceleration <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Acceleration</em>' attribute.
   * @see #getAcceleration()
   * @generated
   */
  void setAcceleration(int value);

  /**
   * Returns the value of the '<em><b>Pulse Width Min</b></em>' attribute.
   * The default value is <code>"1000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pulse Width Min</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pulse Width Min</em>' attribute.
   * @see #setPulseWidthMin(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_PulseWidthMin()
   * @model default="1000" unique="false"
   * @generated
   */
  int getPulseWidthMin();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMin <em>Pulse Width Min</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pulse Width Min</em>' attribute.
   * @see #getPulseWidthMin()
   * @generated
   */
  void setPulseWidthMin(int value);

  /**
   * Returns the value of the '<em><b>Pulse Width Max</b></em>' attribute.
   * The default value is <code>"2000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pulse Width Max</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pulse Width Max</em>' attribute.
   * @see #setPulseWidthMax(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_PulseWidthMax()
   * @model default="2000" unique="false"
   * @generated
   */
  int getPulseWidthMax();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPulseWidthMax <em>Pulse Width Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pulse Width Max</em>' attribute.
   * @see #getPulseWidthMax()
   * @generated
   */
  void setPulseWidthMax(int value);

  /**
   * Returns the value of the '<em><b>Period</b></em>' attribute.
   * The default value is <code>"19500"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Period</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Period</em>' attribute.
   * @see #setPeriod(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_Period()
   * @model default="19500" unique="false"
   * @generated
   */
  int getPeriod();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getPeriod <em>Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Period</em>' attribute.
   * @see #getPeriod()
   * @generated
   */
  void setPeriod(int value);

  /**
   * Returns the value of the '<em><b>Output Voltage</b></em>' attribute.
   * The default value is <code>"5000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Output Voltage</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Output Voltage</em>' attribute.
   * @see #setOutputVoltage(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_OutputVoltage()
   * @model default="5000" unique="false"
   * @generated
   */
  int getOutputVoltage();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getOutputVoltage <em>Output Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Output Voltage</em>' attribute.
   * @see #getOutputVoltage()
   * @generated
   */
  void setOutputVoltage(int value);

  /**
   * Returns the value of the '<em><b>Servo Current Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Servo Current Position</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Servo Current Position</em>' attribute.
   * @see #setServoCurrentPosition(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_ServoCurrentPosition()
   * @model unique="false"
   * @generated
   */
  short getServoCurrentPosition();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getServoCurrentPosition <em>Servo Current Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Servo Current Position</em>' attribute.
   * @see #getServoCurrentPosition()
   * @generated
   */
  void setServoCurrentPosition(short value);

  /**
   * Returns the value of the '<em><b>Servo Destination Position</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Servo Destination Position</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Servo Destination Position</em>' attribute.
   * @see #setServoDestinationPosition(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMServo_ServoDestinationPosition()
   * @model unique="false"
   * @generated
   */
  short getServoDestinationPosition();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MServo#getServoDestinationPosition <em>Servo Destination Position</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Servo Destination Position</em>' attribute.
   * @see #getServoDestinationPosition()
   * @generated
   */
  void setServoDestinationPosition(short value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
   * @generated
   */
  void init();

} // MServo
