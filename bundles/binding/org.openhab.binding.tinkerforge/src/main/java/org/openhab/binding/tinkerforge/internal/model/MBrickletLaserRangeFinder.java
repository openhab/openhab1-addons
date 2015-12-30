/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletLaserRangeFinder;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Laser Range Finder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder#getDeviceType <em>Device Type</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder#getDistanceAverageLength <em>Distance Average Length</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder#getVelocityAverageLength <em>Velocity Average Length</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder#getMode <em>Mode</em>}</li>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder#getEnableLaserOnStartup <em>Enable Laser On Startup</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLaserRangeFinder()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletLaserRangeFinder> org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.LaserRangeFinderDevice> org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.LaserRangeFinderConfiguration>"
 * @generated
 */
public interface MBrickletLaserRangeFinder extends MDevice<BrickletLaserRangeFinder>, MSubDeviceHolder<LaserRangeFinderDevice>, MTFConfigConsumer<LaserRangeFinderConfiguration>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_laser_range_finder"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLaserRangeFinder_DeviceType()
   * @model default="bricklet_laser_range_finder" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

  /**
   * Returns the value of the '<em><b>Distance Average Length</b></em>' attribute.
   * The default value is <code>"10"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Distance Average Length</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Distance Average Length</em>' attribute.
   * @see #setDistanceAverageLength(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLaserRangeFinder_DistanceAverageLength()
   * @model default="10" unique="false"
   * @generated
   */
  short getDistanceAverageLength();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder#getDistanceAverageLength <em>Distance Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Distance Average Length</em>' attribute.
   * @see #getDistanceAverageLength()
   * @generated
   */
  void setDistanceAverageLength(short value);

  /**
   * Returns the value of the '<em><b>Velocity Average Length</b></em>' attribute.
   * The default value is <code>"10"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Velocity Average Length</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Velocity Average Length</em>' attribute.
   * @see #setVelocityAverageLength(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLaserRangeFinder_VelocityAverageLength()
   * @model default="10" unique="false"
   * @generated
   */
  short getVelocityAverageLength();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder#getVelocityAverageLength <em>Velocity Average Length</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Velocity Average Length</em>' attribute.
   * @see #getVelocityAverageLength()
   * @generated
   */
  void setVelocityAverageLength(short value);

  /**
   * Returns the value of the '<em><b>Mode</b></em>' attribute.
   * The default value is <code>"0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Mode</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Mode</em>' attribute.
   * @see #setMode(short)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLaserRangeFinder_Mode()
   * @model default="0" unique="false"
   * @generated
   */
  short getMode();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder#getMode <em>Mode</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Mode</em>' attribute.
   * @see #getMode()
   * @generated
   */
  void setMode(short value);

  /**
   * Returns the value of the '<em><b>Enable Laser On Startup</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Enable Laser On Startup</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Enable Laser On Startup</em>' attribute.
   * @see #setEnableLaserOnStartup(Boolean)
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLaserRangeFinder_EnableLaserOnStartup()
   * @model default="true" unique="false"
   * @generated
   */
  Boolean getEnableLaserOnStartup();

  /**
   * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLaserRangeFinder#getEnableLaserOnStartup <em>Enable Laser On Startup</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Enable Laser On Startup</em>' attribute.
   * @see #getEnableLaserOnStartup()
   * @generated
   */
  void setEnableLaserOnStartup(Boolean value);

} // MBrickletLaserRangeFinder
