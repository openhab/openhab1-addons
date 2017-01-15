/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletCO2;

import java.math.BigDecimal;

import org.openhab.binding.tinkerforge.internal.types.DecimalValue;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet CO2</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletCO2#getDeviceType <em>Device Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletCO2#getThreshold <em>Threshold</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletCO2()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice
 *        <org.openhab.binding.tinkerforge.internal.model.TinkerBrickletCO2>
 *        org.openhab.binding.tinkerforge.internal.model.MSensor
 *        <org.openhab.binding.tinkerforge.internal.model.MDecimalValue>
 *        org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer
 *        <org.openhab.binding.tinkerforge.internal.model.TFBaseConfiguration>
 *        org.openhab.binding.tinkerforge.internal.model.CallbackListener"
 * @generated
 */
public interface MBrickletCO2
        extends MDevice<BrickletCO2>, MSensor<DecimalValue>, MTFConfigConsumer<TFBaseConfiguration>, CallbackListener {
    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_co2"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletCO2_DeviceType()
     * @model default="bricklet_co2" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Returns the value of the '<em><b>Threshold</b></em>' attribute.
     * The default value is <code>"0"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Threshold</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Threshold</em>' attribute.
     * @see #setThreshold(BigDecimal)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletCO2_Threshold()
     * @model default="0" unique="false"
     * @generated
     */
    BigDecimal getThreshold();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.MBrickletCO2#getThreshold
     * <em>Threshold</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Threshold</em>' attribute.
     * @see #getThreshold()
     * @generated
     */
    void setThreshold(BigDecimal value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model annotation="http://www.eclipse.org/emf/2002/GenModel body=''"
     * @generated
     */
    void init();

} // MBrickletCO2
