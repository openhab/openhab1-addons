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
 * A representation of the model object '<em><b>TF Servo Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getServoVoltage <em>Servo Voltage</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPulseWidthMin <em>Pulse Width Min</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPulseWidthMax <em>Pulse Width Max</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPeriod <em>Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getOutputVoltage <em>Output Voltage</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFServoConfiguration()
 * @model
 * @generated
 */
public interface TFServoConfiguration extends TFConfig
{
  /**
   * Returns the value of the '<em><b>Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Velocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Velocity</em>' attribute.
   * @see #setVelocity(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFServoConfiguration_Velocity()
   * @model unique="false"
   * @generated
   */
  int getVelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getVelocity <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Velocity</em>' attribute.
   * @see #getVelocity()
   * @generated
   */
  void setVelocity(int value);

  /**
   * Returns the value of the '<em><b>Acceleration</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Acceleration</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Acceleration</em>' attribute.
   * @see #setAcceleration(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFServoConfiguration_Acceleration()
   * @model unique="false"
   * @generated
   */
  int getAcceleration();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getAcceleration <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Acceleration</em>' attribute.
   * @see #getAcceleration()
   * @generated
   */
  void setAcceleration(int value);

  /**
   * Returns the value of the '<em><b>Servo Voltage</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Servo Voltage</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Servo Voltage</em>' attribute.
   * @see #setServoVoltage(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFServoConfiguration_ServoVoltage()
   * @model unique="false"
   * @generated
   */
  int getServoVoltage();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getServoVoltage <em>Servo Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Servo Voltage</em>' attribute.
   * @see #getServoVoltage()
   * @generated
   */
  void setServoVoltage(int value);

  /**
   * Returns the value of the '<em><b>Pulse Width Min</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pulse Width Min</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pulse Width Min</em>' attribute.
   * @see #setPulseWidthMin(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFServoConfiguration_PulseWidthMin()
   * @model unique="false"
   * @generated
   */
  int getPulseWidthMin();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPulseWidthMin <em>Pulse Width Min</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pulse Width Min</em>' attribute.
   * @see #getPulseWidthMin()
   * @generated
   */
  void setPulseWidthMin(int value);

  /**
   * Returns the value of the '<em><b>Pulse Width Max</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pulse Width Max</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pulse Width Max</em>' attribute.
   * @see #setPulseWidthMax(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFServoConfiguration_PulseWidthMax()
   * @model unique="false"
   * @generated
   */
  int getPulseWidthMax();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPulseWidthMax <em>Pulse Width Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pulse Width Max</em>' attribute.
   * @see #getPulseWidthMax()
   * @generated
   */
  void setPulseWidthMax(int value);

  /**
   * Returns the value of the '<em><b>Period</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Period</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Period</em>' attribute.
   * @see #setPeriod(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFServoConfiguration_Period()
   * @model unique="false"
   * @generated
   */
  int getPeriod();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getPeriod <em>Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Period</em>' attribute.
   * @see #getPeriod()
   * @generated
   */
  void setPeriod(int value);

  /**
   * Returns the value of the '<em><b>Output Voltage</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Output Voltage</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Output Voltage</em>' attribute.
   * @see #setOutputVoltage(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFServoConfiguration_OutputVoltage()
   * @model unique="false"
   * @generated
   */
  int getOutputVoltage();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration#getOutputVoltage <em>Output Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Output Voltage</em>' attribute.
   * @see #getOutputVoltage()
   * @generated
   */
  void setOutputVoltage(int value);

} // TFServoConfiguration
