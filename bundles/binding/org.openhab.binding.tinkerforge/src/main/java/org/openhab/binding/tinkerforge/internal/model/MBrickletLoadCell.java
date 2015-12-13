/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletLoadCell;

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Load Cell</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLoadCell#getDeviceType <em>Device Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLoadCell()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletLoadCell> org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder<org.openhab.binding.tinkerforge.internal.model.LoadCellDevice>"
 * @generated
 */
public interface MBrickletLoadCell extends MDevice<BrickletLoadCell>, MSubDeviceHolder<LoadCellDevice>
{
  /**
   * Returns the value of the '<em><b>Device Type</b></em>' attribute.
   * The default value is <code>"bricklet_loadcell"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Device Type</em>' attribute.
   * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLoadCell_DeviceType()
   * @model default="bricklet_loadcell" unique="false" changeable="false"
   * @generated
   */
  String getDeviceType();

} // MBrickletLoadCell
