/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.device;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPF602LightAndBlindControlT2N;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPF602LightAndBlindControlT2U;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.rxtx.ESP3Connector;
import eu.aleon.aleoncean.values.EnergyBow;
import eu.aleon.aleoncean.values.RockerSwitchAction;
import eu.aleon.aleoncean.values.RockerSwitchButton;
import eu.aleon.aleoncean.values.RockerSwitchState;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public abstract class DeviceRPSLightAndBlindControlAppStyle extends StandardDevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceRPSLightAndBlindControlAppStyle.class);

    private final RockerSwitchState state = new RockerSwitchState();

    private RockerSwitchAction actionA;
    private RockerSwitchAction actionB;

    protected abstract UserDataEEPF602LightAndBlindControlT2N newPacketDataT2N();

    protected abstract UserDataEEPF602LightAndBlindControlT2U newPacketDataT2U();

    protected abstract RockerSwitchButton getDimUpA();

    protected abstract RockerSwitchButton getDimDownA();

    protected abstract RockerSwitchButton getDimUpB();

    protected abstract RockerSwitchButton getDimDownB();

    public DeviceRPSLightAndBlindControlAppStyle(final ESP3Connector conn,
                                                 final EnOceanId addressRemote,
                                                 final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }

    public void setButton(final RockerSwitchButton button, final EnergyBow energyBow) {
        final UserDataEEPF602LightAndBlindControlT2N t2n = newPacketDataT2N();
        t2n.setRocker1stAction(button);
        t2n.set2ndActionValid(false);
        t2n.setEnergyBowPressed(energyBow);

        send(t2n);
    }

    public void setButtons(final RockerSwitchButton button1, final RockerSwitchButton button2, final EnergyBow energyBow) {
        final UserDataEEPF602LightAndBlindControlT2N t2n = newPacketDataT2N();
        t2n.setRocker1stAction(button1);
        t2n.setRocker2ndAction(button2);
        t2n.set2ndActionValid(true);
        t2n.setEnergyBowPressed(energyBow);

        send(t2n);
    }

    public void releaseButtons() {
        final UserDataEEPF602LightAndBlindControlT2U t2u = newPacketDataT2U();
        t2u.setEnergyBowPressed(EnergyBow.RELEASED);
        t2u.setNumberOfPressedButtons(UserDataEEPF602LightAndBlindControlT2U.PressedButtons.NO_BUTTON);

        send(t2u);
    }

    protected boolean isDimUp(final RockerSwitchButton button) {
        return button == getDimUpA() || button == getDimUpB();
    }

    private boolean buttonChanged(final boolean onlyIfLastIsKnown,
                                  final Boolean curStateButtonPressed,
                                  final Boolean newStateButtonPressed) {
        return newStateButtonPressed != null
               && (!onlyIfLastIsKnown || curStateButtonPressed != null)
               && !newStateButtonPressed.equals(curStateButtonPressed);
    }

    private void buttonFireChange(final RockerSwitchButton button,
                                  final Boolean newState) {
        final DeviceParameterUpdatedInitiation initiation = DeviceParameterUpdatedInitiation.RADIO_PACKET;

        // Fire the action for the specific button side (A/B).
        // Use DIM, because this is different for the two RPS profiles.
        DeviceParameter dimParam;
        RockerSwitchAction dimAction;
        RockerSwitchAction dimActionOld;
        boolean dimUp;
        if (button == RockerSwitchButton.AI || button == RockerSwitchButton.AO) {
            dimParam = DeviceParameter.BUTTON_DIM_A;
            dimUp = getDimUpA().equals(button);
        } else {
            dimParam = DeviceParameter.BUTTON_DIM_B;
            dimUp = getDimUpB().equals(button);
        }
        if (dimUp) {
            dimAction = newState ? RockerSwitchAction.DIM_UP_PRESSED : RockerSwitchAction.DIM_UP_RELEASED;
        } else {
            dimAction = newState ? RockerSwitchAction.DIM_DOWN_PRESSED : RockerSwitchAction.DIM_DOWN_RELEASED;
        }
        if (button == RockerSwitchButton.AI || button == RockerSwitchButton.AO) {
            dimActionOld = actionA;
            actionA = dimAction;
        } else {
            dimActionOld = actionB;
            actionB = dimAction;
        }

        parameterChangedSupport.fireParameterUpdated(dimParam, initiation, dimActionOld, dimAction);
    }

    private boolean handleNewStateButton(final RockerSwitchButton button,
                                         final RockerSwitchState newState,
                                         final boolean onlyIfLastIsKnown) {
        final Boolean curStateButtonPressed = state.get(button);
        final Boolean newStateButtonPressed = newState.get(button);

        if (buttonChanged(onlyIfLastIsKnown, curStateButtonPressed, newStateButtonPressed)) {
            state.set(button, newStateButtonPressed);
            buttonFireChange(button, newStateButtonPressed);
            return true;
        } else {
            return false;
        }

    }

    /**
     * This function handle the state change of a rocker switch.
     *
     * @param newState          The current state that was created by package inspection.
     * @param onlyIfLastIsKnown Save the current state only, if the last state was known.
     */
    private void handleNewState(final RockerSwitchState newState,
                                final boolean onlyIfLastIsKnown) {
        try {
            boolean changed = false;

            final RockerSwitchState oldState = state.clone();

            changed |= handleNewStateButton(RockerSwitchButton.AI, newState, onlyIfLastIsKnown);
            changed |= handleNewStateButton(RockerSwitchButton.AO, newState, onlyIfLastIsKnown);
            changed |= handleNewStateButton(RockerSwitchButton.BI, newState, onlyIfLastIsKnown);
            changed |= handleNewStateButton(RockerSwitchButton.BO, newState, onlyIfLastIsKnown);

            if (changed) {
                final List<RockerSwitchButton> pressed = new LinkedList<>();
                final List<RockerSwitchButton> released = new LinkedList<>();

                RockerSwitchState.getChanges(oldState,
                                             state,
                                             pressed, released);

                parameterChangedSupport.fireParameterUpdated(null, null, oldState, state);
            }

        } catch (final CloneNotSupportedException ex) {
            LOGGER.debug("Catched exception: {}", ex);
        }
    }

    void parseT2N(final RadioPacketRPS packet) {
        final UserDataEEPF602LightAndBlindControlT2N data = newPacketDataT2N();
        data.setUserData(packet.getUserDataRaw());
        final RockerSwitchState newState = new RockerSwitchState();
        final boolean pressed = data.getEnergyBowPressed() == EnergyBow.PRESSED;

        try {
            newState.set(data.getRocker1stAction(), pressed);
            if (data.is2ndActionValid()) {
                newState.set(data.getRocker2ndAction(), pressed);
            }
            handleNewState(newState, false);
        } catch (final UserDataScaleValueException ex) {
            LOGGER.debug("Catched exception: {}", ex);
        }
    }

    void parseT2U(final RadioPacketRPS packet) {
        final UserDataEEPF602LightAndBlindControlT2U data = newPacketDataT2U();
        data.setUserData(packet.getUserDataRaw());

        try {
            if (data.getNumberOfPressedButtons() == UserDataEEPF602LightAndBlindControlT2U.PressedButtons.NO_BUTTON) {
                final RockerSwitchState newState = new RockerSwitchState(Boolean.FALSE);
                //handleNewState(newState, true);
                handleNewState(newState, false);
            }
        } catch (final UserDataScaleValueException ex) {
            LOGGER.debug("Catched exception: {}", ex);
        }

    }

    void parseT2(final RadioPacketRPS packet) {
        switch (packet.getNUState()) {
            case NORMALMESSAGE:
                parseT2N(packet);
                break;
            case UNASSIGNEDMESSAGE:
                parseT2U(packet);
                break;
            default:
                break;
        }
    }

    @Override
    protected void parseRadioPacketRPS(final RadioPacketRPS packet) {
        switch (packet.getT21State()) {
            case PTM_TYPE2:
                parseT2(packet);
                break;
            default:
                break;
        }
    }

    @Override
    public String toString() {
        return "DeviceRPSLightAndBlindControlAppStyle{" + "state=" + state + '}';
    }

    @Override
    protected void fillParameters(final Set<DeviceParameter> params) {
        params.add(DeviceParameter.BUTTON_DIM_A);
        params.add(DeviceParameter.BUTTON_DIM_B);
    }

    @Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case BUTTON_DIM_A:
                return actionA;
            case BUTTON_DIM_B:
                return actionB;
            default:
                return super.getByParameter(parameter);
        }
    }

    private void setByParameterButtonDim(final RockerSwitchButton buttonUp,
                                         final RockerSwitchButton buttonDown,
                                         final RockerSwitchAction action) {
        EnergyBow energyBow;
        RockerSwitchButton button;

        switch (action) {
            case DIM_UP_PRESSED:
                button = buttonUp;
                energyBow = EnergyBow.PRESSED;
                break;
            case DIM_UP_RELEASED:
                button = buttonUp;
                energyBow = EnergyBow.RELEASED;
                break;
            case DIM_DOWN_PRESSED:
                button = buttonDown;
                energyBow = EnergyBow.PRESSED;
                break;
            case DIM_DOWN_RELEASED:
                button = buttonDown;
                energyBow = EnergyBow.RELEASED;
                break;
            default:
                LOGGER.warn("Unhandled rocker action ({}).", action);
                return;
        }

        setButton(button, energyBow);
    }

    @Override
    public void setByParameter(final DeviceParameter parameter, final Object value) throws IllegalDeviceParameterException {
        assert DeviceParameter.getSupportedClass(parameter).isAssignableFrom(value.getClass());
        if (this instanceof LocalDevice) {
            switch (parameter) {
                case BUTTON_DIM_A:
                    setByParameterButtonDim(getDimUpA(), getDimDownA(), (RockerSwitchAction) value);
                    break;
                case BUTTON_DIM_B:
                    setByParameterButtonDim(getDimUpB(), getDimDownB(), (RockerSwitchAction) value);
                    break;
                default:
                    super.setByParameter(parameter, value);
                    break;
            }
        } else {
            super.setByParameter(parameter, value);
        }
    }

}
