/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFTemperatureConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TF Temperature Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFTemperatureConfigurationImpl#isSlowI2C <em>Slow I2C</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TFTemperatureConfigurationImpl extends TFBaseConfigurationImpl implements TFTemperatureConfiguration
{
  /**
   * The default value of the '{@link #isSlowI2C() <em>Slow I2C</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSlowI2C()
   * @generated
   * @ordered
   */
  protected static final boolean SLOW_I2C_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isSlowI2C() <em>Slow I2C</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isSlowI2C()
   * @generated
   * @ordered
   */
  protected boolean slowI2C = SLOW_I2C_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TFTemperatureConfigurationImpl()
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
    return ModelPackage.Literals.TF_TEMPERATURE_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isSlowI2C()
  {
    return slowI2C;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSlowI2C(boolean newSlowI2C)
  {
    boolean oldSlowI2C = slowI2C;
    slowI2C = newSlowI2C;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_TEMPERATURE_CONFIGURATION__SLOW_I2C, oldSlowI2C, slowI2C));
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
      case ModelPackage.TF_TEMPERATURE_CONFIGURATION__SLOW_I2C:
        return isSlowI2C();
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
      case ModelPackage.TF_TEMPERATURE_CONFIGURATION__SLOW_I2C:
        setSlowI2C((Boolean)newValue);
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
      case ModelPackage.TF_TEMPERATURE_CONFIGURATION__SLOW_I2C:
        setSlowI2C(SLOW_I2C_EDEFAULT);
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
      case ModelPackage.TF_TEMPERATURE_CONFIGURATION__SLOW_I2C:
        return slowI2C != SLOW_I2C_EDEFAULT;
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
    result.append(" (slowI2C: ");
    result.append(slowI2C);
    result.append(')');
    return result.toString();
  }

} //TFTemperatureConfigurationImpl
