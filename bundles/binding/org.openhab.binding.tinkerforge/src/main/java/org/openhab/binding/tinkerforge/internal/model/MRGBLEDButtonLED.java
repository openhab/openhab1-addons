/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.core.library.types.HSBType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MRGBLED Button LED</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MRGBLEDButtonLED#getDeviceType <em>Device Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MRGBLEDButtonLED#getLastSelectedColor <em>Last Selected
 * Color</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMRGBLEDButtonLED()
 * @model
 * @generated
 */
public interface MRGBLEDButtonLED extends SwitchableColorActor, MSubDevice<MBrickletRGBLEDButton> {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"led"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMRGBLEDButtonLED_DeviceType()
     * @model default="led" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Returns the value of the '<em><b>Last Selected Color</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Last Selected Color</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Last Selected Color</em>' attribute.
     * @see #setLastSelectedColor(HSBType)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMRGBLEDButtonLED_LastSelectedColor()
     * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.HSBType"
     * @generated
     */
    HSBType getLastSelectedColor();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.MRGBLEDButtonLED#getLastSelectedColor <em>Last Selected
     * Color</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Last Selected Color</em>' attribute.
     * @see #getLastSelectedColor()
     * @generated
     */
    void setLastSelectedColor(HSBType value);

} // MRGBLEDButtonLED
