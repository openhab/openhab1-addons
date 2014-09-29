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
import org.openhab.binding.tinkerforge.internal.model.TFBrickDCConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TF Brick DC Configuration</b></em>'.
 * 
 * @author Theo Weiss
 * @since 1.3.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getVelocity <em>Velocity</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getAcceleration <em>Acceleration</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getPwmFrequency <em>Pwm Frequency</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getDriveMode <em>Drive Mode</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickDCConfigurationImpl#getSwitchOnVelocity <em>Switch On Velocity</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TFBrickDCConfigurationImpl extends MinimalEObjectImpl.Container implements TFBrickDCConfiguration
{
  /**
   * The default value of the '{@link #getVelocity() <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocity()
   * @generated
   * @ordered
   */
  protected static final short VELOCITY_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getVelocity() <em>Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocity()
   * @generated
   * @ordered
   */
  protected short velocity = VELOCITY_EDEFAULT;

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
   * The default value of the '{@link #getPwmFrequency() <em>Pwm Frequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPwmFrequency()
   * @generated
   * @ordered
   */
  protected static final int PWM_FREQUENCY_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getPwmFrequency() <em>Pwm Frequency</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPwmFrequency()
   * @generated
   * @ordered
   */
  protected int pwmFrequency = PWM_FREQUENCY_EDEFAULT;

  /**
   * The default value of the '{@link #getDriveMode() <em>Drive Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDriveMode()
   * @generated
   * @ordered
   */
  protected static final int DRIVE_MODE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getDriveMode() <em>Drive Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDriveMode()
   * @generated
   * @ordered
   */
  protected int driveMode = DRIVE_MODE_EDEFAULT;

  /**
   * The default value of the '{@link #getSwitchOnVelocity() <em>Switch On Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchOnVelocity()
   * @generated
   * @ordered
   */
  protected static final short SWITCH_ON_VELOCITY_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getSwitchOnVelocity() <em>Switch On Velocity</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSwitchOnVelocity()
   * @generated
   * @ordered
   */
  protected short switchOnVelocity = SWITCH_ON_VELOCITY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TFBrickDCConfigurationImpl()
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
    return ModelPackage.Literals.TF_BRICK_DC_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getVelocity()
  {
    return velocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVelocity(short newVelocity)
  {
    short oldVelocity = velocity;
    velocity = newVelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY, oldVelocity, velocity));
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
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION, oldAcceleration, acceleration));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getPwmFrequency()
  {
    return pwmFrequency;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPwmFrequency(int newPwmFrequency)
  {
    int oldPwmFrequency = pwmFrequency;
    pwmFrequency = newPwmFrequency;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY, oldPwmFrequency, pwmFrequency));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getDriveMode()
  {
    return driveMode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDriveMode(int newDriveMode)
  {
    int oldDriveMode = driveMode;
    driveMode = newDriveMode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE, oldDriveMode, driveMode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getSwitchOnVelocity()
  {
    return switchOnVelocity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSwitchOnVelocity(short newSwitchOnVelocity)
  {
    short oldSwitchOnVelocity = switchOnVelocity;
    switchOnVelocity = newSwitchOnVelocity;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_DC_CONFIGURATION__SWITCH_ON_VELOCITY, oldSwitchOnVelocity, switchOnVelocity));
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
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY:
        return getVelocity();
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION:
        return getAcceleration();
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY:
        return getPwmFrequency();
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE:
        return getDriveMode();
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__SWITCH_ON_VELOCITY:
        return getSwitchOnVelocity();
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
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY:
        setVelocity((Short)newValue);
        return;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION:
        setAcceleration((Integer)newValue);
        return;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY:
        setPwmFrequency((Integer)newValue);
        return;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE:
        setDriveMode((Integer)newValue);
        return;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__SWITCH_ON_VELOCITY:
        setSwitchOnVelocity((Short)newValue);
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
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY:
        setVelocity(VELOCITY_EDEFAULT);
        return;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION:
        setAcceleration(ACCELERATION_EDEFAULT);
        return;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY:
        setPwmFrequency(PWM_FREQUENCY_EDEFAULT);
        return;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE:
        setDriveMode(DRIVE_MODE_EDEFAULT);
        return;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__SWITCH_ON_VELOCITY:
        setSwitchOnVelocity(SWITCH_ON_VELOCITY_EDEFAULT);
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
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__VELOCITY:
        return velocity != VELOCITY_EDEFAULT;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__ACCELERATION:
        return acceleration != ACCELERATION_EDEFAULT;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__PWM_FREQUENCY:
        return pwmFrequency != PWM_FREQUENCY_EDEFAULT;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__DRIVE_MODE:
        return driveMode != DRIVE_MODE_EDEFAULT;
      case ModelPackage.TF_BRICK_DC_CONFIGURATION__SWITCH_ON_VELOCITY:
        return switchOnVelocity != SWITCH_ON_VELOCITY_EDEFAULT;
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
    result.append(", pwmFrequency: ");
    result.append(pwmFrequency);
    result.append(", driveMode: ");
    result.append(driveMode);
    result.append(", switchOnVelocity: ");
    result.append(switchOnVelocity);
    result.append(')');
    return result.toString();
  }

} //TFBrickDCConfigurationImpl
