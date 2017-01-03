/**
 */
package org.openhab.binding.tinkerforge.internal.model.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.openhab.binding.tinkerforge.internal.model.ModelPackage;
import org.openhab.binding.tinkerforge.internal.model.TFBrickStepperConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TF Brick Stepper Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickStepperConfigurationImpl#getMaxVelocity <em>Max
 * Velocity</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickStepperConfigurationImpl#getAcceleration
 * <em>Acceleration</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickStepperConfigurationImpl#getDeacceleration
 * <em>Deacceleration</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickStepperConfigurationImpl#getMotorCurrent
 * <em>Motor Current</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickStepperConfigurationImpl#getStepMode <em>Step
 * Mode</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickStepperConfigurationImpl#getDecay
 * <em>Decay</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickStepperConfigurationImpl#isSyncRect <em>Sync
 * Rect</em>}</li>
 * <li>{@link org.openhab.binding.tinkerforge.internal.model.impl.TFBrickStepperConfigurationImpl#getTimeBase <em>Time
 * Base</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TFBrickStepperConfigurationImpl extends TFBaseConfigurationImpl implements TFBrickStepperConfiguration {
    /**
     * The default value of the '{@link #getMaxVelocity() <em>Max Velocity</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMaxVelocity()
     * @generated
     * @ordered
     */
    protected static final int MAX_VELOCITY_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getMaxVelocity() <em>Max Velocity</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMaxVelocity()
     * @generated
     * @ordered
     */
    protected int maxVelocity = MAX_VELOCITY_EDEFAULT;

    /**
     * The default value of the '{@link #getAcceleration() <em>Acceleration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getAcceleration()
     * @generated
     * @ordered
     */
    protected static final int ACCELERATION_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getAcceleration() <em>Acceleration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getAcceleration()
     * @generated
     * @ordered
     */
    protected int acceleration = ACCELERATION_EDEFAULT;

    /**
     * The default value of the '{@link #getDeacceleration() <em>Deacceleration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDeacceleration()
     * @generated
     * @ordered
     */
    protected static final int DEACCELERATION_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getDeacceleration() <em>Deacceleration</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDeacceleration()
     * @generated
     * @ordered
     */
    protected int deacceleration = DEACCELERATION_EDEFAULT;

    /**
     * The default value of the '{@link #getMotorCurrent() <em>Motor Current</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMotorCurrent()
     * @generated
     * @ordered
     */
    protected static final int MOTOR_CURRENT_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getMotorCurrent() <em>Motor Current</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMotorCurrent()
     * @generated
     * @ordered
     */
    protected int motorCurrent = MOTOR_CURRENT_EDEFAULT;

    /**
     * The default value of the '{@link #getStepMode() <em>Step Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getStepMode()
     * @generated
     * @ordered
     */
    protected static final short STEP_MODE_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getStepMode() <em>Step Mode</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getStepMode()
     * @generated
     * @ordered
     */
    protected short stepMode = STEP_MODE_EDEFAULT;

    /**
     * The default value of the '{@link #getDecay() <em>Decay</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDecay()
     * @generated
     * @ordered
     */
    protected static final int DECAY_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getDecay() <em>Decay</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getDecay()
     * @generated
     * @ordered
     */
    protected int decay = DECAY_EDEFAULT;

    /**
     * The default value of the '{@link #isSyncRect() <em>Sync Rect</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isSyncRect()
     * @generated
     * @ordered
     */
    protected static final boolean SYNC_RECT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isSyncRect() <em>Sync Rect</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isSyncRect()
     * @generated
     * @ordered
     */
    protected boolean syncRect = SYNC_RECT_EDEFAULT;

    /**
     * The default value of the '{@link #getTimeBase() <em>Time Base</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getTimeBase()
     * @generated
     * @ordered
     */
    protected static final long TIME_BASE_EDEFAULT = 0L;

    /**
     * The cached value of the '{@link #getTimeBase() <em>Time Base</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getTimeBase()
     * @generated
     * @ordered
     */
    protected long timeBase = TIME_BASE_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected TFBrickStepperConfigurationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ModelPackage.Literals.TF_BRICK_STEPPER_CONFIGURATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getMaxVelocity() {
        return maxVelocity;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setMaxVelocity(int newMaxVelocity) {
        int oldMaxVelocity = maxVelocity;
        maxVelocity = newMaxVelocity;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MAX_VELOCITY, oldMaxVelocity, maxVelocity));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getAcceleration() {
        return acceleration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setAcceleration(int newAcceleration) {
        int oldAcceleration = acceleration;
        acceleration = newAcceleration;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__ACCELERATION, oldAcceleration, acceleration));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getDeacceleration() {
        return deacceleration;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setDeacceleration(int newDeacceleration) {
        int oldDeacceleration = deacceleration;
        deacceleration = newDeacceleration;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DEACCELERATION, oldDeacceleration, deacceleration));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getMotorCurrent() {
        return motorCurrent;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setMotorCurrent(int newMotorCurrent) {
        int oldMotorCurrent = motorCurrent;
        motorCurrent = newMotorCurrent;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MOTOR_CURRENT, oldMotorCurrent, motorCurrent));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public short getStepMode() {
        return stepMode;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setStepMode(short newStepMode) {
        short oldStepMode = stepMode;
        stepMode = newStepMode;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__STEP_MODE, oldStepMode, stepMode));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getDecay() {
        return decay;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setDecay(int newDecay) {
        int oldDecay = decay;
        decay = newDecay;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DECAY,
                    oldDecay, decay));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSyncRect() {
        return syncRect;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setSyncRect(boolean newSyncRect) {
        boolean oldSyncRect = syncRect;
        syncRect = newSyncRect;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__SYNC_RECT, oldSyncRect, syncRect));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public long getTimeBase() {
        return timeBase;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setTimeBase(long newTimeBase) {
        long oldTimeBase = timeBase;
        timeBase = newTimeBase;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET,
                    ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__TIME_BASE, oldTimeBase, timeBase));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MAX_VELOCITY:
                return getMaxVelocity();
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__ACCELERATION:
                return getAcceleration();
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DEACCELERATION:
                return getDeacceleration();
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MOTOR_CURRENT:
                return getMotorCurrent();
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__STEP_MODE:
                return getStepMode();
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DECAY:
                return getDecay();
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__SYNC_RECT:
                return isSyncRect();
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__TIME_BASE:
                return getTimeBase();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MAX_VELOCITY:
                setMaxVelocity((Integer) newValue);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__ACCELERATION:
                setAcceleration((Integer) newValue);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DEACCELERATION:
                setDeacceleration((Integer) newValue);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MOTOR_CURRENT:
                setMotorCurrent((Integer) newValue);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__STEP_MODE:
                setStepMode((Short) newValue);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DECAY:
                setDecay((Integer) newValue);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__SYNC_RECT:
                setSyncRect((Boolean) newValue);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__TIME_BASE:
                setTimeBase((Long) newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MAX_VELOCITY:
                setMaxVelocity(MAX_VELOCITY_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__ACCELERATION:
                setAcceleration(ACCELERATION_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DEACCELERATION:
                setDeacceleration(DEACCELERATION_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MOTOR_CURRENT:
                setMotorCurrent(MOTOR_CURRENT_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__STEP_MODE:
                setStepMode(STEP_MODE_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DECAY:
                setDecay(DECAY_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__SYNC_RECT:
                setSyncRect(SYNC_RECT_EDEFAULT);
                return;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__TIME_BASE:
                setTimeBase(TIME_BASE_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MAX_VELOCITY:
                return maxVelocity != MAX_VELOCITY_EDEFAULT;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__ACCELERATION:
                return acceleration != ACCELERATION_EDEFAULT;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DEACCELERATION:
                return deacceleration != DEACCELERATION_EDEFAULT;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__MOTOR_CURRENT:
                return motorCurrent != MOTOR_CURRENT_EDEFAULT;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__STEP_MODE:
                return stepMode != STEP_MODE_EDEFAULT;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__DECAY:
                return decay != DECAY_EDEFAULT;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__SYNC_RECT:
                return syncRect != SYNC_RECT_EDEFAULT;
            case ModelPackage.TF_BRICK_STEPPER_CONFIGURATION__TIME_BASE:
                return timeBase != TIME_BASE_EDEFAULT;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (maxVelocity: ");
        result.append(maxVelocity);
        result.append(", acceleration: ");
        result.append(acceleration);
        result.append(", deacceleration: ");
        result.append(deacceleration);
        result.append(", motorCurrent: ");
        result.append(motorCurrent);
        result.append(", stepMode: ");
        result.append(stepMode);
        result.append(", decay: ");
        result.append(decay);
        result.append(", syncRect: ");
        result.append(syncRect);
        result.append(", timeBase: ");
        result.append(timeBase);
        result.append(')');
        return result.toString();
    }

} // TFBrickStepperConfigurationImpl
