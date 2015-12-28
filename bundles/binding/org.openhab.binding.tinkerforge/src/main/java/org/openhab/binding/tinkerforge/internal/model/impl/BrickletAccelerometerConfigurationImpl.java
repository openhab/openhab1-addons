/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.openhab.binding.tinkerforge.internal.model.BrickletAccelerometerConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bricklet Accelerometer Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletAccelerometerConfigurationImpl#getDataRate <em>Data Rate</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletAccelerometerConfigurationImpl#getFullScale <em>Full Scale</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.BrickletAccelerometerConfigurationImpl#getFilterBandwidth <em>Filter Bandwidth</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BrickletAccelerometerConfigurationImpl extends MinimalEObjectImpl.Container implements BrickletAccelerometerConfiguration
{
  /**
   * The default value of the '{@link #getDataRate() <em>Data Rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDataRate()
   * @generated
   * @ordered
   */
  protected static final Short DATA_RATE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDataRate() <em>Data Rate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDataRate()
   * @generated
   * @ordered
   */
  protected Short dataRate = DATA_RATE_EDEFAULT;

  /**
   * The default value of the '{@link #getFullScale() <em>Full Scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFullScale()
   * @generated
   * @ordered
   */
  protected static final Short FULL_SCALE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFullScale() <em>Full Scale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFullScale()
   * @generated
   * @ordered
   */
  protected Short fullScale = FULL_SCALE_EDEFAULT;

  /**
   * The default value of the '{@link #getFilterBandwidth() <em>Filter Bandwidth</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilterBandwidth()
   * @generated
   * @ordered
   */
  protected static final Short FILTER_BANDWIDTH_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFilterBandwidth() <em>Filter Bandwidth</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilterBandwidth()
   * @generated
   * @ordered
   */
  protected Short filterBandwidth = FILTER_BANDWIDTH_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected BrickletAccelerometerConfigurationImpl()
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
    return ModelPackage.Literals.BRICKLET_ACCELEROMETER_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getDataRate()
  {
    return dataRate;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDataRate(Short newDataRate)
  {
    Short oldDataRate = dataRate;
    dataRate = newDataRate;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__DATA_RATE, oldDataRate, dataRate));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getFullScale()
  {
    return fullScale;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFullScale(Short newFullScale)
  {
    Short oldFullScale = fullScale;
    fullScale = newFullScale;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FULL_SCALE, oldFullScale, fullScale));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Short getFilterBandwidth()
  {
    return filterBandwidth;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFilterBandwidth(Short newFilterBandwidth)
  {
    Short oldFilterBandwidth = filterBandwidth;
    filterBandwidth = newFilterBandwidth;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FILTER_BANDWIDTH, oldFilterBandwidth, filterBandwidth));
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
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__DATA_RATE:
        return getDataRate();
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FULL_SCALE:
        return getFullScale();
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FILTER_BANDWIDTH:
        return getFilterBandwidth();
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
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__DATA_RATE:
        setDataRate((Short)newValue);
        return;
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FULL_SCALE:
        setFullScale((Short)newValue);
        return;
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FILTER_BANDWIDTH:
        setFilterBandwidth((Short)newValue);
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
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__DATA_RATE:
        setDataRate(DATA_RATE_EDEFAULT);
        return;
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FULL_SCALE:
        setFullScale(FULL_SCALE_EDEFAULT);
        return;
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FILTER_BANDWIDTH:
        setFilterBandwidth(FILTER_BANDWIDTH_EDEFAULT);
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
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__DATA_RATE:
        return DATA_RATE_EDEFAULT == null ? dataRate != null : !DATA_RATE_EDEFAULT.equals(dataRate);
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FULL_SCALE:
        return FULL_SCALE_EDEFAULT == null ? fullScale != null : !FULL_SCALE_EDEFAULT.equals(fullScale);
      case ModelPackage.BRICKLET_ACCELEROMETER_CONFIGURATION__FILTER_BANDWIDTH:
        return FILTER_BANDWIDTH_EDEFAULT == null ? filterBandwidth != null : !FILTER_BANDWIDTH_EDEFAULT.equals(filterBandwidth);
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
    result.append(" (dataRate: ");
    result.append(dataRate);
    result.append(", fullScale: ");
    result.append(fullScale);
    result.append(", filterBandwidth: ");
    result.append(filterBandwidth);
    result.append(')');
    return result.toString();
  }

} //BrickletAccelerometerConfigurationImpl
