/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickDC;
import java.math.BigDecimal;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBrick DC</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getMaxVelocity <em>Max Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getMinVelocity <em>Min Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getTargetvelocity <em>Targetvelocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getCurrentVelocity <em>Current Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getPwmFrequency <em>Pwm Frequency</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getDriveMode <em>Drive Mode</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.MDecimalValue> org.openhab.binding.tinkerforge.internal.model.ProgrammableSwitchActor org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.MTinkerBrickDC> org.openhab.binding.tinkerforge.internal.model.MoveActor org.openhab.binding.tinkerforge.internal.model.SetPointActor<org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration> org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface MBrickDC extends MSensor<DecimalValue>, ProgrammableSwitchActor, MDevice<BrickDC>, MoveActor, SetPointActor<TFBrickDCConfiguration>, CallbackListener
{
  /**
   * Returns the value of the '<em><b>Threshold</b></em>' attribute.
   * The default value is <code>"10"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Threshold</em>' attribute.
   * @see #setThreshold(BigDecimal)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_Threshold()
   * @model default="10" unique="false"
   * @generated
   */
  BigDecimal getThreshold();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getThreshold <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Threshold</em>' attribute.
   * @see #getThreshold()
   * @generated
   */
  void setThreshold(BigDecimal value);

  /**
   * Returns the value of the '<em><b>Max Velocity</b></em>' attribute.
   * The default value is <code>"32767"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Max Velocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Max Velocity</em>' attribute.
   * @see #setMaxVelocity(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_MaxVelocity()
   * @model default="32767" unique="false"
   * @generated
   */
  Short getMaxVelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getMaxVelocity <em>Max Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Max Velocity</em>' attribute.
   * @see #getMaxVelocity()
   * @generated
   */
  void setMaxVelocity(Short value);

  /**
   * Returns the value of the '<em><b>Min Velocity</b></em>' attribute.
   * The default value is <code>"-32767"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Min Velocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Min Velocity</em>' attribute.
   * @see #setMinVelocity(Short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_MinVelocity()
   * @model default="-32767" unique="false"
   * @generated
   */
  Short getMinVelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getMinVelocity <em>Min Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Min Velocity</em>' attribute.
   * @see #getMinVelocity()
   * @generated
   */
  void setMinVelocity(Short value);

  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"brick_dc"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_DeviceType()
   * @model default="brick_dc" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Velocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Velocity</em>' attribute.
   * @see #setVelocity(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_Velocity()
   * @model unique="false"
   * @generated
   */
  short getVelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getVelocity <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Velocity</em>' attribute.
   * @see #getVelocity()
   * @generated
   */
  void setVelocity(short value);

  /**
   * Returns the value of the '<em><b>Targetvelocity</b></em>' attribute.
   * The default value is <code>"0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Targetvelocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Targetvelocity</em>' attribute.
   * @see #setTargetvelocity(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_Targetvelocity()
   * @model default="0" unique="false"
   * @generated
   */
  short getTargetvelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getTargetvelocity <em>Targetvelocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Targetvelocity</em>' attribute.
   * @see #getTargetvelocity()
   * @generated
   */
  void setTargetvelocity(short value);

  /**
   * Returns the value of the '<em><b>Current Velocity</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Current Velocity</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Current Velocity</em>' attribute.
   * @see #setCurrentVelocity(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_CurrentVelocity()
   * @model unique="false"
   * @generated
   */
  short getCurrentVelocity();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getCurrentVelocity <em>Current Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Current Velocity</em>' attribute.
   * @see #getCurrentVelocity()
   * @generated
   */
  void setCurrentVelocity(short value);

  /**
   * Returns the value of the '<em><b>Acceleration</b></em>' attribute.
   * The default value is <code>"10000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Acceleration</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Acceleration</em>' attribute.
   * @see #setAcceleration(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_Acceleration()
   * @model default="10000" unique="false"
   * @generated
   */
  int getAcceleration();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getAcceleration <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Acceleration</em>' attribute.
   * @see #getAcceleration()
   * @generated
   */
  void setAcceleration(int value);

  /**
   * Returns the value of the '<em><b>Pwm Frequency</b></em>' attribute.
   * The default value is <code>"15000"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pwm Frequency</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pwm Frequency</em>' attribute.
   * @see #setPwmFrequency(int)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_PwmFrequency()
   * @model default="15000" unique="false"
   * @generated
   */
  int getPwmFrequency();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getPwmFrequency <em>Pwm Frequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pwm Frequency</em>' attribute.
   * @see #getPwmFrequency()
   * @generated
   */
  void setPwmFrequency(int value);

  /**
   * Returns the value of the '<em><b>Drive Mode</b></em>' attribute.
   * The default value is <code>"BRAKE"</code>.
   * The literals are from the enumeration {@link org.openhab.binding.tinkerforge.internal.model.DCDriveMode}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Drive Mode</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Drive Mode</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.DCDriveMode
   * @see #setDriveMode(DCDriveMode)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickDC_DriveMode()
   * @model default="BRAKE" unique="false"
   * @generated
   */
  DCDriveMode getDriveMode();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickDC#getDriveMode <em>Drive Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Drive Mode</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.DCDriveMode
   * @see #getDriveMode()
   * @generated
   */
  void setDriveMode(DCDriveMode value);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
   * @generated
   */
  void init();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model unique="false" velocityUnique="false" accelerationUnique="false" drivemodeUnique="false"
   * @generated
   */
  boolean setSpeed(Short velocity, int acceleration, String drivemode);

} // MBrickDC
