/**
 */
package org.openhab.binding.tinkerforge.internal.model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TF Brick Stepper Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getMaxVelocity <em>Max
 * Velocity</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getAcceleration
 * <em>Acceleration</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getDeacceleration
 * <em>Deacceleration</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getMotorCurrent <em>Motor
 * Current</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getStepMode <em>Step Mode</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getDecay <em>Decay</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#isSyncRect <em>Sync Rect</em>}
 * </li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getTimeBase <em>Time Base</em>}
 * </li>
 * </ul>
 *
 * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickStepperConfiguration()
 * @model
 * @generated
 */
public interface TFBrickStepperConfiguration extends TFBaseConfiguration {
    /**
     * Returns the value of the '<em><b>Max Velocity</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Max Velocity</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Max Velocity</em>' attribute.
     * @see #setMaxVelocity(int)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickStepperConfiguration_MaxVelocity()
     * @model unique="false"
     * @generated
     */
    int getMaxVelocity();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getMaxVelocity <em>Max
     * Velocity</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Max Velocity</em>' attribute.
     * @see #getMaxVelocity()
     * @generated
     */
    void setMaxVelocity(int value);

    /**
     * Returns the value of the '<em><b>Acceleration</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Acceleration</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Acceleration</em>' attribute.
     * @see #setAcceleration(int)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickStepperConfiguration_Acceleration()
     * @model unique="false"
     * @generated
     */
    int getAcceleration();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getAcceleration
     * <em>Acceleration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Acceleration</em>' attribute.
     * @see #getAcceleration()
     * @generated
     */
    void setAcceleration(int value);

    /**
     * Returns the value of the '<em><b>Deacceleration</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Deacceleration</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Deacceleration</em>' attribute.
     * @see #setDeacceleration(int)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickStepperConfiguration_Deacceleration()
     * @model unique="false"
     * @generated
     */
    int getDeacceleration();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getDeacceleration
     * <em>Deacceleration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Deacceleration</em>' attribute.
     * @see #getDeacceleration()
     * @generated
     */
    void setDeacceleration(int value);

    /**
     * Returns the value of the '<em><b>Motor Current</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Motor Current</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Motor Current</em>' attribute.
     * @see #setMotorCurrent(int)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickStepperConfiguration_MotorCurrent()
     * @model unique="false"
     * @generated
     */
    int getMotorCurrent();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getMotorCurrent <em>Motor
     * Current</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Motor Current</em>' attribute.
     * @see #getMotorCurrent()
     * @generated
     */
    void setMotorCurrent(int value);

    /**
     * Returns the value of the '<em><b>Step Mode</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Step Mode</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Step Mode</em>' attribute.
     * @see #setStepMode(short)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickStepperConfiguration_StepMode()
     * @model unique="false"
     * @generated
     */
    short getStepMode();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getStepMode <em>Step
     * Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Step Mode</em>' attribute.
     * @see #getStepMode()
     * @generated
     */
    void setStepMode(short value);

    /**
     * Returns the value of the '<em><b>Decay</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Decay</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Decay</em>' attribute.
     * @see #setDecay(int)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickStepperConfiguration_Decay()
     * @model unique="false"
     * @generated
     */
    int getDecay();

    /**
     * Sets the value of the '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getDecay
     * <em>Decay</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Decay</em>' attribute.
     * @see #getDecay()
     * @generated
     */
    void setDecay(int value);

    /**
     * Returns the value of the '<em><b>Sync Rect</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Sync Rect</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Sync Rect</em>' attribute.
     * @see #setSyncRect(boolean)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickStepperConfiguration_SyncRect()
     * @model unique="false"
     * @generated
     */
    boolean isSyncRect();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#isSyncRect <em>Sync
     * Rect</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Sync Rect</em>' attribute.
     * @see #isSyncRect()
     * @generated
     */
    void setSyncRect(boolean value);

    /**
     * Returns the value of the '<em><b>Time Base</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Time Base</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Time Base</em>' attribute.
     * @see #setTimeBase(long)
     * @see org.openhab.binding.tinkerforge.internal.model.ModelPackage#getTFBrickStepperConfiguration_TimeBase()
     * @model unique="false"
     * @generated
     */
    long getTimeBase();

    /**
     * Sets the value of the
     * '{@link org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration#getTimeBase <em>Time
     * Base</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Time Base</em>' attribute.
     * @see #getTimeBase()
     * @generated
     */
    void setTimeBase(long value);

} // TFBrickStepperConfiguration
