/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.openhab.binding.tinkerforge.internal.model.LoadCellConfiguration;
import org.openhab.binding.tinkerforge.internal.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Load Cell Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.LoadCellConfigurationImpl#getMovingAverage <em>Moving Average</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LoadCellConfigurationImpl extends TFBaseConfigurationImpl implements LoadCellConfiguration
{
  /**
   * The default value of the '{@link #getMovingAverage() <em>Moving Average</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMovingAverage()
   * @generated
   * @ordered
   */
  protected static final short MOVING_AVERAGE_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getMovingAverage() <em>Moving Average</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMovingAverage()
   * @generated
   * @ordered
   */
  protected short movingAverage = MOVING_AVERAGE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LoadCellConfigurationImpl()
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
    return ModelPackage.Literals.LOAD_CELL_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public short getMovingAverage()
  {
    return movingAverage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMovingAverage(short newMovingAverage)
  {
    short oldMovingAverage = movingAverage;
    movingAverage = newMovingAverage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.LOAD_CELL_CONFIGURATION__MOVING_AVERAGE, oldMovingAverage, movingAverage));
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
      case ModelPackage.LOAD_CELL_CONFIGURATION__MOVING_AVERAGE:
        return getMovingAverage();
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
      case ModelPackage.LOAD_CELL_CONFIGURATION__MOVING_AVERAGE:
        setMovingAverage((Short)newValue);
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
      case ModelPackage.LOAD_CELL_CONFIGURATION__MOVING_AVERAGE:
        setMovingAverage(MOVING_AVERAGE_EDEFAULT);
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
      case ModelPackage.LOAD_CELL_CONFIGURATION__MOVING_AVERAGE:
        return movingAverage != MOVING_AVERAGE_EDEFAULT;
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
    result.append(" (movingAverage: ");
    result.append(movingAverage);
    result.append(')');
    return result.toString();
  }

} //LoadCellConfigurationImpl
