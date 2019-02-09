/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletRGBLEDButton;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet RGBLED Button</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletRGBLEDButton#getDeviceType <em>Device
 * Type</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletRGBLEDButton()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor&lt;org.openhab.binding.tinkerforge.internal.model.SwitchState&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MDevice&lt;org.openhab.binding.tinkerforge.internal.model.TinkerBrickletRGBLEDButton&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder&lt;org.openhab.binding.tinkerforge.internal.model.MRGBLEDButtonLED&gt;"
 * @generated
 */
public interface MBrickletRGBLEDButton
        extends MSensor<OnOffValue>, MDevice<BrickletRGBLEDButton>, MSubDeviceHolder<MRGBLEDButtonLED> {

    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"button"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletRGBLEDButton_DeviceType()
     * @model default="button" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

} // MBrickletRGBLEDButton
