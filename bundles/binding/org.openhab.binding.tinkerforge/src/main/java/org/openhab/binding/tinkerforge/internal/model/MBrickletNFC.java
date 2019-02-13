/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import com.tinkerforge.BrickletNFC;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MBricklet NFC</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletNFC#getDeviceType <em>Device Type</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletNFC#getTextSubId <em>Text Sub Id</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletNFC#getUriSubId <em>Uri Sub Id</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletNFC#getIdSubId <em>Id Sub Id</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.MBrickletNFC#getTriggerSubId <em>Trigger Sub Id</em>}</li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletNFC()
 * @model superTypes="org.openhab.binding.tinkerforge.internal.model.MDevice&lt;org.openhab.binding.tinkerforge.internal.model.TinkerBrickletNFC&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MSubDeviceHolder&lt;org.openhab.binding.tinkerforge.internal.model.MNFCSubDevice&gt;
 *        org.openhab.binding.tinkerforge.internal.model.MTFConfigConsumer&lt;org.openhab.binding.tinkerforge.internal.model.NFCConfiguration&gt;"
 * @generated
 */
public interface MBrickletNFC
        extends MDevice<BrickletNFC>, MSubDeviceHolder<MNFCSubDevice>, MTFConfigConsumer<NFCConfiguration> {

    /**
     * Returns the value of the '<em><b>Device Type</b></em>' attribute.
     * The default value is <code>"bricklet_nfc"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Device Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Device Type</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletNFC_DeviceType()
     * @model default="bricklet_nfc" unique="false" changeable="false"
     * @generated
     */
    String getDeviceType();

    /**
     * Returns the value of the '<em><b>Text Sub Id</b></em>' attribute.
     * The default value is <code>"text"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Text Sub Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Text Sub Id</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletNFC_TextSubId()
     * @model default="text" unique="false" changeable="false"
     * @generated
     */
    String getTextSubId();

    /**
     * Returns the value of the '<em><b>Uri Sub Id</b></em>' attribute.
     * The default value is <code>"uri"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Uri Sub Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Uri Sub Id</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletNFC_UriSubId()
     * @model default="uri" unique="false" changeable="false"
     * @generated
     */
    String getUriSubId();

    /**
     * Returns the value of the '<em><b>Id Sub Id</b></em>' attribute.
     * The default value is <code>"id"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id Sub Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Id Sub Id</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletNFC_IdSubId()
     * @model default="id" unique="false" changeable="false"
     * @generated
     */
    String getIdSubId();

    /**
     * Returns the value of the '<em><b>Trigger Sub Id</b></em>' attribute.
     * The default value is <code>"trigger"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Trigger Sub Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Trigger Sub Id</em>' attribute.
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMBrickletNFC_TriggerSubId()
     * @model default="trigger" unique="false" changeable="false"
     * @generated
     */
    String getTriggerSubId();

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model listenerUnique="false"
     * @generated
     */
    void addNDEFRecordListener(MNFCNDEFRecordListener listener);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model listenerUnique="false"
     * @generated
     */
    void removeNDEFRecordListener(MNFCNDEFRecordListener listener);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model listenerUnique="false"
     * @generated
     */
    void addTagInfoListener(MNFCTagInfoListener listener);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model listenerUnique="false"
     * @generated
     */
    void removeTagInfoListener(MNFCTagInfoListener listener);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model unique="false"
     * @generated
     */
    boolean triggerScan();
} // MBrickletNFC
