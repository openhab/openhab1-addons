/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletLCD128x64;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet LCD12 8x64</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD128x64#getDeviceType <em>Device Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD128x64#getPositionPrefix <em>Position
 * Prefix</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD128x64#getPositionSuffix <em>Position
 * Suffix</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD128x64()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice&lt;org.openhab.binding.tinkerforge.internal.model.MTinkerBrickletLCD128x64&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MTextActor
 *        org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder&lt;org.openhab.binding.tinkerforge.internal.model.MLCD128x64SubDevice&gt;"
 * @generated
 */
public interface MBrickletLCD128x64
        extends MDevice<BrickletLCD128x64>, MTextActor, MSubDeviceHolder<MLCD128x64SubDevice> {

    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_LCD128x64"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD128x64_DeviceType()
     * @model default="bricklet_LCD128x64" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Returns the value of the '<em><b>Position Prefix</b></em>' attribute.
     * The default value is <code>"TFNUM<"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Position Prefix</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Position Prefix</em>' attribute.
     * @see #setPositionPrefix(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD128x64_PositionPrefix()
     * @model default="TFNUM&lt;" unique="false"
     * @generated
     */
    String getPositionPrefix();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD128x64#getPositionPrefix
     * <em>Position Prefix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Position Prefix</em>' attribute.
     * @see #getPositionPrefix()
     * @generated
     */
    void setPositionPrefix(String value);

    /**
     * Returns the value of the '<em><b>Position Suffix</b></em>' attribute.
     * The default value is <code>">"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Position Suffix</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Position Suffix</em>' attribute.
     * @see #setPositionSuffix(String)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletLCD128x64_PositionSuffix()
     * @model default="&gt;" unique="false"
     * @generated
     */
    String getPositionSuffix();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletLCD128x64#getPositionSuffix
     * <em>Position Suffix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Position Suffix</em>' attribute.
     * @see #getPositionSuffix()
     * @generated
     */
    void setPositionSuffix(String value);
} // MBrickletLCD128x64
