/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletAnalogOutV2;
import java.math.BigDecimal;
import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet Analog Out V2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogOutV2#getDeviceType <em>Device Type</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogOutV2#getMinValueDevice <em>Min Value
 * Device</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogOutV2#getMaxValueDevice <em>Max Value
 * Device</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAnalogOutV2()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor
 *        <org.openhab.binding.tinkerforge.internal.model.MDecimalValue>
 *        org.openhab.binding.tinkerforge.internal.model.MDevice
 *        <org.openhab.binding.tinkerforge.internal.model.TinkerBrickletAnalogOutV2>
 *        org.openhab.binding.tinkerforge.internal.model.SetPointActor
 *        <org.openhab.binding.tinkerforge.internal.model.DimmableConfiguration>"
 * @generated
 */
public interface MBrickletAnalogOutV2
        extends MSensor<DecimalValue>, MDevice<BrickletAnalogOutV2>, SetPointActor<DimmableConfiguration> {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_analog_out_v2"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAnalogOutV2_DeviceType()
     * @model default="bricklet_analog_out_v2" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Returns the value of the '<em><b>Min Value Device</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Min Value Device</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Min Value Device</em>' attribute.
     * @see #setMinValueDevice(BigDecimal)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAnalogOutV2_MinValueDevice()
     * @model default="0" unique="false"
     * @generated
     */
    BigDecimal getMinValueDevice();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogOutV2#getMinValueDevice <em>Min Value
     * Device</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Min Value Device</em>' attribute.
     * @see #getMinValueDevice()
     * @generated
     */
    void setMinValueDevice(BigDecimal value);

    /**
     * Returns the value of the '<em><b>Max Value Device</b></em>' attribute.
     * The default value is <code>"12000"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Value Device</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Max Value Device</em>' attribute.
     * @see #setMaxValueDevice(BigDecimal)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletAnalogOutV2_MaxValueDevice()
     * @model default="12000" unique="false"
     * @generated
     */
    BigDecimal getMaxValueDevice();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletAnalogOutV2#getMaxValueDevice <em>Max Value
     * Device</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Max Value Device</em>' attribute.
     * @see #getMaxValueDevice()
     * @generated
     */
    void setMaxValueDevice(BigDecimal value);

} // MBrickletAnalogOutV2
