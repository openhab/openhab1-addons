/**
 */
package org.openhab.binding.tinkerforge.internal.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>NFC Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.NFCConfiguration#isClearValuesOnError <em>Clear Values On
 * Error</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.NFCConfiguration#isResetOldValues <em>Reset Old
 * Values</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.NFCConfiguration#isTriggeredScan <em>Triggered
 * Scan</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.NFCConfiguration#getDelayAfterScan <em>Delay After
 * Scan</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getNFCConfiguration()
 * @model
 * @generated
 */
public interface NFCConfiguration extends TFConfig {
    /**
     * Returns the value of the '<em><b>Clear Values On Error</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Clear Values On Error</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Clear Values On Error</em>' attribute.
     * @see #setClearValuesOnError(boolean)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getNFCConfiguration_ClearValuesOnError()
     * @model default="false" unique="false"
     * @generated
     */
    boolean isClearValuesOnError();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.NFCConfiguration#isClearValuesOnError <em>Clear Values On
     * Error</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Clear Values On Error</em>' attribute.
     * @see #isClearValuesOnError()
     * @generated
     */
    void setClearValuesOnError(boolean value);

    /**
     * Returns the value of the '<em><b>Reset Old Values</b></em>' attribute.
     * The default value is <code>"true"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Reset Old Values</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Reset Old Values</em>' attribute.
     * @see #setResetOldValues(boolean)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getNFCConfiguration_ResetOldValues()
     * @model default="true" unique="false"
     * @generated
     */
    boolean isResetOldValues();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.NFCConfiguration#isResetOldValues
     * <em>Reset Old Values</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Reset Old Values</em>' attribute.
     * @see #isResetOldValues()
     * @generated
     */
    void setResetOldValues(boolean value);

    /**
     * Returns the value of the '<em><b>Triggered Scan</b></em>' attribute.
     * The default value is <code>"false"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Triggered Scan</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Triggered Scan</em>' attribute.
     * @see #setTriggeredScan(boolean)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getNFCConfiguration_TriggeredScan()
     * @model default="false" unique="false"
     * @generated
     */
    boolean isTriggeredScan();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.NFCConfiguration#isTriggeredScan
     * <em>Triggered Scan</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Triggered Scan</em>' attribute.
     * @see #isTriggeredScan()
     * @generated
     */
    void setTriggeredScan(boolean value);

    /**
     * Returns the value of the '<em><b>Delay After Scan</b></em>' attribute.
     * The default value is <code>"3000"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Delay After Scan</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Delay After Scan</em>' attribute.
     * @see #setDelayAfterScan(int)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getNFCConfiguration_DelayAfterScan()
     * @model default="3000" unique="false"
     * @generated
     */
    int getDelayAfterScan();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.NFCConfiguration#getDelayAfterScan
     * <em>Delay After Scan</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Delay After Scan</em>' attribute.
     * @see #getDelayAfterScan()
     * @generated
     */
    void setDelayAfterScan(int value);

} // NFCConfiguration
