/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.openhab.binding.tinkerforge.internal.types.HSBValue;
import org.openhab.binding.tinkerforge.internal.types.OnOffValue;
import org.openhab.core.library.types.HSBType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Switchable Color Actor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.SwitchableColorActor#getSwitchState <em>Switch
 * State</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getSwitchableColorActor()
 * @model interface="true" abstract="true"
 *        superTypes="org.openhab.binding.tinkerforge.internal.model.MSensor&lt;org.openhab.binding.tinkerforge.internal.model.HSBValue&gt;"
 * @generated
 */
public interface SwitchableColorActor extends MSensor<HSBValue> {

    /**
     * Returns the value of the '<em><b>Switch State</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Switch State</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Switch State</em>' attribute.
     * @see #setSwitchState(OnOffValue)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getSwitchableColorActor_SwitchState()
     * @model unique="false" dataType="org.openhab.binding.tinkerforge.internal.model.SwitchState"
     * @generated
     */
    OnOffValue getSwitchState();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.SwitchableColorActor#getSwitchState
     * <em>Switch State</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Switch State</em>' attribute.
     * @see #getSwitchState()
     * @generated
     */
    void setSwitchState(OnOffValue value);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model stateDataType="org.openhab.binding.tinkerforge.internal.model.SwitchState" stateUnique="false"
     * @generated
     */
    void turnSwitch(OnOffValue state);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model colorDataType="org.openhab.binding.tinkerforge.internal.model.HSBType" colorUnique="false"
     * @generated
     */
    void setSelectedColor(HSBType color);
} // SwitchableColorActor
