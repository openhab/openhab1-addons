/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletLoadCellV2;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Load Cell V2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLoadCellV2#getDeviceType <em>Device
 * Type</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLoadCellV2()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice&lt;org.openhab.binding.tinkerforge.internal.model.TinkerBrickletLoadCellV2&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder&lt;org.openhab.binding.tinkerforge.internal.model.LoadCellDeviceV2&gt;"
 * @generated
 */
public interface MBrickletLoadCellV2 extends MDevice<BrickletLoadCellV2>, MSubDeviceHolder<LoadCellDeviceV2> {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_loadcell"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLoadCellV2_DeviceType()
     * @model default="bricklet_loadcell" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

} // MBrickletLoadCellV2
