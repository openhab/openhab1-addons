/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.openhab.binding.tinkerforge.internal.model.LaserRangeFinderConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Laser Range Finder Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LaserRangeFinderConfigurationImpl#getDistanceAverageLength <em>Distance Average Length</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LaserRangeFinderConfigurationImpl#getVelocityAverageLength <em>Velocity Average Length</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LaserRangeFinderConfigurationImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LaserRangeFinderConfigurationImpl#getEnableLaserOnStartup <em>Enable Laser On Startup</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LaserRangeFinderConfigurationImpl extends MinimalEObjectImpl.Container implements LaserRangeFinderConfiguration
{
  /**
   * The default value of the '{@link #getDistanceAverageLength() <em>Distance Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDistanceAverageLength()
   * @generated
   * @ordered
   */
  protected static final short DISTANCE_AVERAGE_LENGTH_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getDistanceAverageLength() <em>Distance Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDistanceAverageLength()
   * @generated
   * @ordered
   */
  protected short distanceAverageLength = DISTANCE_AVERAGE_LENGTH_EDEFAULT;

  /**
   * The default value of the '{@link #getVelocityAverageLength() <em>Velocity Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocityAverageLength()
   * @generated
   * @ordered
   */
  protected static final short VELOCITY_AVERAGE_LENGTH_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getVelocityAverageLength() <em>Velocity Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVelocityAverageLength()
   * @generated
   * @ordered
   */
  protected short velocityAverageLength = VELOCITY_AVERAGE_LENGTH_EDEFAULT;

  /**
   * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMode()
   * @generated
   * @ordered
   */
  protected static final short MODE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMode()
   * @generated
   * @ordered
   */
  protected short mode = MODE_EDEFAULT;

  /**
   * The default value of the '{@link #getEnableLaserOnStartup() <em>Enable Laser On Startup</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnableLaserOnStartup()
   * @generated
   * @ordered
   */
  protected static final Boolean ENABLE_LASER_ON_STARTUP_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getEnableLaserOnStartup() <em>Enable Laser On Startup</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnableLaserOnStartup()
   * @generated
   * @ordered
   */
  protected Boolean enableLaserOnStartup = ENABLE_LASER_ON_STARTUP_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LaserRangeFinderConfigurationImpl()
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
    return ModelPackage.Literals.LASER_RANGE_FINDER_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getDistanceAverageLength()
  {
    return distanceAverageLength;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDistanceAverageLength(short newDistanceAverageLength)
  {
    short oldDistanceAverageLength = distanceAverageLength;
    distanceAverageLength = newDistanceAverageLength;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__DISTANCE_AVERAGE_LENGTH, oldDistanceAverageLength, distanceAverageLength));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getVelocityAverageLength()
  {
    return velocityAverageLength;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVelocityAverageLength(short newVelocityAverageLength)
  {
    short oldVelocityAverageLength = velocityAverageLength;
    velocityAverageLength = newVelocityAverageLength;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__VELOCITY_AVERAGE_LENGTH, oldVelocityAverageLength, velocityAverageLength));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getMode()
  {
    return mode;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMode(short newMode)
  {
    short oldMode = mode;
    mode = newMode;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__MODE, oldMode, mode));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Boolean getEnableLaserOnStartup()
  {
    return enableLaserOnStartup;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEnableLaserOnStartup(Boolean newEnableLaserOnStartup)
  {
    Boolean oldEnableLaserOnStartup = enableLaserOnStartup;
    enableLaserOnStartup = newEnableLaserOnStartup;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__ENABLE_LASER_ON_STARTUP, oldEnableLaserOnStartup, enableLaserOnStartup));
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
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__DISTANCE_AVERAGE_LENGTH:
        return getDistanceAverageLength();
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__VELOCITY_AVERAGE_LENGTH:
        return getVelocityAverageLength();
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__MODE:
        return getMode();
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__ENABLE_LASER_ON_STARTUP:
        return getEnableLaserOnStartup();
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
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__DISTANCE_AVERAGE_LENGTH:
        setDistanceAverageLength((Short)newValue);
        return;
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__VELOCITY_AVERAGE_LENGTH:
        setVelocityAverageLength((Short)newValue);
        return;
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__MODE:
        setMode((Short)newValue);
        return;
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__ENABLE_LASER_ON_STARTUP:
        setEnableLaserOnStartup((Boolean)newValue);
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
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__DISTANCE_AVERAGE_LENGTH:
        setDistanceAverageLength(DISTANCE_AVERAGE_LENGTH_EDEFAULT);
        return;
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__VELOCITY_AVERAGE_LENGTH:
        setVelocityAverageLength(VELOCITY_AVERAGE_LENGTH_EDEFAULT);
        return;
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__MODE:
        setMode(MODE_EDEFAULT);
        return;
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__ENABLE_LASER_ON_STARTUP:
        setEnableLaserOnStartup(ENABLE_LASER_ON_STARTUP_EDEFAULT);
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
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__DISTANCE_AVERAGE_LENGTH:
        return distanceAverageLength != DISTANCE_AVERAGE_LENGTH_EDEFAULT;
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__VELOCITY_AVERAGE_LENGTH:
        return velocityAverageLength != VELOCITY_AVERAGE_LENGTH_EDEFAULT;
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__MODE:
        return mode != MODE_EDEFAULT;
      case ModelPackage.LASER_RANGE_FINDER_CONFIGURATION__ENABLE_LASER_ON_STARTUP:
        return ENABLE_LASER_ON_STARTUP_EDEFAULT == null ? enableLaserOnStartup != null : !ENABLE_LASER_ON_STARTUP_EDEFAULT.equals(enableLaserOnStartup);
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
    result.append(" (distanceAverageLength: ");
    result.append(distanceAverageLength);
    result.append(", velocityAverageLength: ");
    result.append(velocityAverageLength);
    result.append(", mode: ");
    result.append(mode);
    result.append(", enableLaserOnStartup: ");
    result.append(enableLaserOnStartup);
    result.append(')');
    return result.toString();
  }

} //LaserRangeFinderConfigurationImpl
