/**
 */
package org.openhab.binding.tinkerforge.internal.model;

import org.eclipse.emf.ecore.EObject;

import org.openhab.binding.tinkerforge.internal.tools.NDEFRecord;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>MNFCNDEF Record Listener</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getMNFCNDEFRecordListener()
 * @model interface="true" abstract="true"
 * @generated
 */
public interface MNFCNDEFRecordListener extends EObject {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @model recordDataType="org.openhab.binding.tinkerforge.internal.model.NDEFRecord" recordUnique="false"
     * @generated
     */
    void handleNDEFRecord(NDEFRecord record);

} // MNFCNDEFRecordListener
