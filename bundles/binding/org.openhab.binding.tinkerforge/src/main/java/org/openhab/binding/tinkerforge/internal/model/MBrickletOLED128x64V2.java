/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletOLED128x64V2;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet OLED12 8x64 V2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64V2#getDeviceType <em>Device
 * Type</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64V2()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.OLEDBricklet
 *        org.openhab.binding.tinkerforge.internal.model.MDevice&lt;org.openhab.binding.tinkerforge.internal.model.TinkerBrickletOLED128x64V2&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MTextActor
 *        org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer&lt;org.openhab.binding.tinkerforge.internal.model.BrickletOLEDConfiguration&gt;"
 * @generated
 */
public interface MBrickletOLED128x64V2
        extends OLEDBricklet, MDevice<BrickletOLED128x64V2>, MTextActor, MTFConfigConsumer<BrickletOLEDConfiguration> {

    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_oled128x64v2"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64V2_DeviceType()
     * @model default="bricklet_oled128x64v2" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();
} // MBrickletOLED128x64V2
