/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Load Cell Weight</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.LoadCellWeight#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.LoadCellWeight#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.LoadCellWeight#getMovingAverage <em>Moving Average</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLoadCellWeight()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.LoadCellDevice org.openhab.binding.tinkerforge.internal.model.MSensor<org.openhab.binding.tinkerforge.internal.model.MDecimalValue> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.LoadCellConfiguration> org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface LoadCellWeight extends LoadCellDevice, MSensor<DecimalValue>, MTFConfigConsumer<LoadCellConfiguration>, CallbackListener
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"loadcell_weight"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLoadCellWeight_DeviceType()
   * @model default="loadcell_weight" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Threshold</b></em>' attribute.
   * The default value is <code>"0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Threshold</em>' attribute.
   * @see #setThreshold(BigDecimal)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLoadCellWeight_Threshold()
   * @model default="0" unique="false"
   * @generated
   */
  BigDecimal getThreshold();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.LoadCellWeight#getThreshold <em>Threshold</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Threshold</em>' attribute.
   * @see #getThreshold()
   * @generated
   */
  void setThreshold(BigDecimal value);

  /**
   * Returns the value of the '<em><b>Moving Average</b></em>' attribute.
   * The default value is <code>"4"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Moving Average</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Moving Average</em>' attribute.
   * @see #setMovingAverage(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getLoadCellWeight_MovingAverage()
   * @model default="4" unique="false"
   * @generated
   */
  short getMovingAverage();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.LoadCellWeight#getMovingAverage <em>Moving Average</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Moving Average</em>' attribute.
   * @see #getMovingAverage()
   * @generated
   */
  void setMovingAverage(short value);

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
   * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
   * @generated
   */
  void tare();

} // LoadCellWeight
