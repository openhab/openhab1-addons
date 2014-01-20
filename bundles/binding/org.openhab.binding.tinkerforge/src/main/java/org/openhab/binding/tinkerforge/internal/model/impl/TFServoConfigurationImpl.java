/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFServoConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TF Servo Configuration</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl#getServoVoltage <em>Servo Voltage</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl#getPulseWidthMin <em>Pulse Width Min</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl#getPulseWidthMax <em>Pulse Width Max</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl#getPeriod <em>Period</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFServoConfigurationImpl#getOutputVoltage <em>Output Voltage</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TFServoConfigurationImpl extends MinimalEObjectImpl.Container implements TFServoConfiguration
{
  /**
   * The default value of the '{@link #getVelocity() <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocity()
   * @generated
   * @ordered
   */
  protected static final int VELOCITY_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getVelocity() <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocity()
   * @generated
   * @ordered
   */
  protected int velocity = VELOCITY_EDEFAULT;

  /**
   * The default value of the '{@link #getAcceleration() <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAcceleration()
   * @generated
   * @ordered
   */
  protected static final int ACCELERATION_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getAcceleration() <em>Acceleration</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAcceleration()
   * @generated
   * @ordered
   */
  protected int acceleration = ACCELERATION_EDEFAULT;

  /**
   * The default value of the '{@link #getServoVoltage() <em>Servo Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServoVoltage()
   * @generated
   * @ordered
   */
  protected static final int SERVO_VOLTAGE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getServoVoltage() <em>Servo Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getServoVoltage()
   * @generated
   * @ordered
   */
  protected int servoVoltage = SERVO_VOLTAGE_EDEFAULT;

  /**
   * The default value of the '{@link #getPulseWidthMin() <em>Pulse Width Min</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPulseWidthMin()
   * @generated
   * @ordered
   */
  protected static final int PULSE_WIDTH_MIN_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getPulseWidthMin() <em>Pulse Width Min</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPulseWidthMin()
   * @generated
   * @ordered
   */
  protected int pulseWidthMin = PULSE_WIDTH_MIN_EDEFAULT;

  /**
   * The default value of the '{@link #getPulseWidthMax() <em>Pulse Width Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPulseWidthMax()
   * @generated
   * @ordered
   */
  protected static final int PULSE_WIDTH_MAX_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getPulseWidthMax() <em>Pulse Width Max</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPulseWidthMax()
   * @generated
   * @ordered
   */
  protected int pulseWidthMax = PULSE_WIDTH_MAX_EDEFAULT;

  /**
   * The default value of the '{@link #getPeriod() <em>Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPeriod()
   * @generated
   * @ordered
   */
  protected static final int PERIOD_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getPeriod() <em>Period</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPeriod()
   * @generated
   * @ordered
   */
  protected int period = PERIOD_EDEFAULT;

  /**
   * The default value of the '{@link #getOutputVoltage() <em>Output Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutputVoltage()
   * @generated
   * @ordered
   */
  protected static final int OUTPUT_VOLTAGE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getOutputVoltage() <em>Output Voltage</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOutputVoltage()
   * @generated
   * @ordered
   */
  protected int outputVoltage = OUTPUT_VOLTAGE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TFServoConfigurationImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return ModelPackage.Literals.TF_SERVO_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getVelocity()
  {
    return velocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVelocity(int newVelocity)
  {
    int oldVelocity = velocity;
    velocity = newVelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_SERVO_CONFIGURATION__VELOCITY, oldVelocity, velocity));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getAcceleration()
  {
    return acceleration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAcceleration(int newAcceleration)
  {
    int oldAcceleration = acceleration;
    acceleration = newAcceleration;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_SERVO_CONFIGURATION__ACCELERATION, oldAcceleration, acceleration));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getServoVoltage()
  {
    return servoVoltage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setServoVoltage(int newServoVoltage)
  {
    int oldServoVoltage = servoVoltage;
    servoVoltage = newServoVoltage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_SERVO_CONFIGURATION__SERVO_VOLTAGE, oldServoVoltage, servoVoltage));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPulseWidthMin()
  {
    return pulseWidthMin;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPulseWidthMin(int newPulseWidthMin)
  {
    int oldPulseWidthMin = pulseWidthMin;
    pulseWidthMin = newPulseWidthMin;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MIN, oldPulseWidthMin, pulseWidthMin));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPulseWidthMax()
  {
    return pulseWidthMax;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPulseWidthMax(int newPulseWidthMax)
  {
    int oldPulseWidthMax = pulseWidthMax;
    pulseWidthMax = newPulseWidthMax;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MAX, oldPulseWidthMax, pulseWidthMax));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPeriod()
  {
    return period;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPeriod(int newPeriod)
  {
    int oldPeriod = period;
    period = newPeriod;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_SERVO_CONFIGURATION__PERIOD, oldPeriod, period));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getOutputVoltage()
  {
    return outputVoltage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOutputVoltage(int newOutputVoltage)
  {
    int oldOutputVoltage = outputVoltage;
    outputVoltage = newOutputVoltage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_SERVO_CONFIGURATION__OUTPUT_VOLTAGE, oldOutputVoltage, outputVoltage));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case ModelPackage.TF_SERVO_CONFIGURATION__VELOCITY:
        return getVelocity();
      case ModelPackage.TF_SERVO_CONFIGURATION__ACCELERATION:
        return getAcceleration();
      case ModelPackage.TF_SERVO_CONFIGURATION__SERVO_VOLTAGE:
        return getServoVoltage();
      case ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MIN:
        return getPulseWidthMin();
      case ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MAX:
        return getPulseWidthMax();
      case ModelPackage.TF_SERVO_CONFIGURATION__PERIOD:
        return getPeriod();
      case ModelPackage.TF_SERVO_CONFIGURATION__OUTPUT_VOLTAGE:
        return getOutputVoltage();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case ModelPackage.TF_SERVO_CONFIGURATION__VELOCITY:
        setVelocity((Integer)newValue);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__ACCELERATION:
        setAcceleration((Integer)newValue);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__SERVO_VOLTAGE:
        setServoVoltage((Integer)newValue);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MIN:
        setPulseWidthMin((Integer)newValue);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MAX:
        setPulseWidthMax((Integer)newValue);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__PERIOD:
        setPeriod((Integer)newValue);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__OUTPUT_VOLTAGE:
        setOutputVoltage((Integer)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case ModelPackage.TF_SERVO_CONFIGURATION__VELOCITY:
        setVelocity(VELOCITY_EDEFAULT);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__ACCELERATION:
        setAcceleration(ACCELERATION_EDEFAULT);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__SERVO_VOLTAGE:
        setServoVoltage(SERVO_VOLTAGE_EDEFAULT);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MIN:
        setPulseWidthMin(PULSE_WIDTH_MIN_EDEFAULT);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MAX:
        setPulseWidthMax(PULSE_WIDTH_MAX_EDEFAULT);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__PERIOD:
        setPeriod(PERIOD_EDEFAULT);
        return;
      case ModelPackage.TF_SERVO_CONFIGURATION__OUTPUT_VOLTAGE:
        setOutputVoltage(OUTPUT_VOLTAGE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case ModelPackage.TF_SERVO_CONFIGURATION__VELOCITY:
        return velocity != VELOCITY_EDEFAULT;
      case ModelPackage.TF_SERVO_CONFIGURATION__ACCELERATION:
        return acceleration != ACCELERATION_EDEFAULT;
      case ModelPackage.TF_SERVO_CONFIGURATION__SERVO_VOLTAGE:
        return servoVoltage != SERVO_VOLTAGE_EDEFAULT;
      case ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MIN:
        return pulseWidthMin != PULSE_WIDTH_MIN_EDEFAULT;
      case ModelPackage.TF_SERVO_CONFIGURATION__PULSE_WIDTH_MAX:
        return pulseWidthMax != PULSE_WIDTH_MAX_EDEFAULT;
      case ModelPackage.TF_SERVO_CONFIGURATION__PERIOD:
        return period != PERIOD_EDEFAULT;
      case ModelPackage.TF_SERVO_CONFIGURATION__OUTPUT_VOLTAGE:
        return outputVoltage != OUTPUT_VOLTAGE_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (velocity: ");
    result.append(velocity);
    result.append(", acceleration: ");
    result.append(acceleration);
    result.append(", servoVoltage: ");
    result.append(servoVoltage);
    result.append(", pulseWidthMin: ");
    result.append(pulseWidthMin);
    result.append(", pulseWidthMax: ");
    result.append(pulseWidthMax);
    result.append(", period: ");
    result.append(period);
    result.append(", outputVoltage: ");
    result.append(outputVoltage);
    result.append(')');
    return result.toString();
  }

} //TFServoConfigurationImpl
