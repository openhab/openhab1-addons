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
package eu.aleon.aleoncean.packet.radio.userdata;

import eu.aleon.aleoncean.packet.radio.RadioPacketRPS.NUState;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS.T21State;
import eu.aleon.aleoncean.values.EnergyBow;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPF602LightAndBlindControlT2U extends UserDataRPS {

    private static final int POS_ENERGY_BOW_PRESSED = 4;

    public UserDataEEPF602LightAndBlindControlT2U() {
        super(T21State.PTM_TYPE2, NUState.UNASSIGNEDMESSAGE);
    }

    public UserDataEEPF602LightAndBlindControlT2U(final byte[] eepData) {
        super(eepData, T21State.PTM_TYPE2, NUState.UNASSIGNEDMESSAGE);
    }

    public enum PressedButtons {

        NO_BUTTON,
        THREE_OR_FOUR_BUTTONS;

    }

    public PressedButtons getNumberOfPressedButtons() throws UserDataScaleValueException {
        final int range = (int) getDataRange(0, 7, 0, 5);

        PressedButtons buttons = null;
        switch (range) {
            case 0:
                buttons = PressedButtons.NO_BUTTON;
                break;
            case 3:
                buttons = PressedButtons.THREE_OR_FOUR_BUTTONS;
                break;
            default:
                break;
        }
        if (buttons == null) {
            throw new UserDataScaleValueException(String.format("Number of pressed buttons invalid (%d).", range));
        }

        return buttons;
    }

    public void setNumberOfPressedButtons(final PressedButtons buttons) {
        long value;
        if (buttons == PressedButtons.NO_BUTTON) {
            value = 0;
        } else {
            value = 3;
        }

        setDataRange(value, 0, 7, 0, 5);
    }

    public EnergyBow getEnergyBowPressed() {
        return getDataBit(0, POS_ENERGY_BOW_PRESSED) == 1 ? EnergyBow.PRESSED : EnergyBow.RELEASED;
    }

    public void setEnergyBowPressed(final EnergyBow energyBow) {
        setDataBit(0, POS_ENERGY_BOW_PRESSED, energyBow == EnergyBow.PRESSED);
    }

}
