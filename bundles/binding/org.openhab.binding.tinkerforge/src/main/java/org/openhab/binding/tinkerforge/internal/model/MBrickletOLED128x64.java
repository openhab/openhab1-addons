/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletOLED128x64;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet OLED12 8x64</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#getDeviceType <em>Device Type</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#getPositionPrefix
 * <em>Position Prefix</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#getPositionSuffix
 * <em>Position Suffix</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#getContrast <em>Contrast</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#isInvert <em>Invert</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64()
 * @model superTypes=
 *        "org.openhab.binding.tinkerforge.internal.model.MDevice<org.openhab.binding.tinkerforge.internal.model.TinkerBrickletOLED128x64> org.openhab.binding.tinkerforge.internal.model.MTextActor org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer<org.openhab.binding.tinkerforge.internal.model.BrickletOLEDConfiguration>"
 * @generated
 */
public interface MBrickletOLED128x64
        extends MDevice<BrickletOLED128x64>, MTextActor, MTFConfigConsumer<BrickletOLEDConfiguration> {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_oled128x64"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64_DeviceType()
     * @model default="bricklet_oled128x64" unique="false" changeable="false"
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
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64_PositionPrefix()
     * @model default="TFNUM<" unique="false"
     * @generated
     */
    String getPositionPrefix();

    /**
     * Sets the value of the '
     * {@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#getPositionPrefix
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
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64_PositionSuffix()
     * @model default=">" unique="false"
     * @generated
     */
    String getPositionSuffix();

    /**
     * Sets the value of the '
     * {@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#getPositionSuffix
     * <em>Position Suffix</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Position Suffix</em>' attribute.
     * @see #getPositionSuffix()
     * @generated
     */
    void setPositionSuffix(String value);

    /**
     * Returns the value of the '<em><b>Contrast</b></em>' attribute.
     * The default value is <code>"143"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Contrast</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Contrast</em>' attribute.
     * @see #setContrast(short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64_Contrast()
     * @model default="143" unique="false"
     * @generated
     */
    short getContrast();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#getContrast
     * <em>Contrast</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Contrast</em>' attribute.
     * @see #getContrast()
     * @generated
     */
    void setContrast(short value);

    /**
     * Returns the value of the '<em><b>Invert</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Invert</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Invert</em>' attribute.
     * @see #setInvert(boolean)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletOLED128x64_Invert()
     * @model default="false" unique="false"
     * @generated
     */
    boolean isInvert();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletOLED128x64#isInvert
     * <em>Invert</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Invert</em>' attribute.
     * @see #isInvert()
     * @generated
     */
    void setInvert(boolean value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model
     * @generated
     */
    void clear();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model columnFromUnique="false" columnToUnique="false" rowFromUnique="false" rowToUnique="false"
     * @generated
     */
    void clear(short columnFrom, short columnTo, short rowFrom, short rowTo);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model lineUnique="false" positionUnique="false" textUnique="false"
     * @generated
     */
    void writeLine(short line, short position, String text);

} // MBrickletOLED128x64
