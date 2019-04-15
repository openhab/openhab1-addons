/**
 */
package org.openhab.binding.tinkerforge.internal.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MLCD12 8x64 Backlight</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MLCD128x64Backlight#getDeviceType <em>Device
 * Type</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMLCD128x64Backlight()
 * @model
 * @generated
 */
public interface MLCD128x64Backlight extends MInSwitchActor, MLCD128x64SubDevice {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"backlight"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMLCD128x64Backlight_DeviceType()
     * @model default="backlight" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

} // MLCD128x64Backlight
