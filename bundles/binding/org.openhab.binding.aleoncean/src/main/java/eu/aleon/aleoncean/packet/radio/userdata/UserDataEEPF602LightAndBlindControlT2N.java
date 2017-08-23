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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS.NUState;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS.T21State;
import eu.aleon.aleoncean.util.MapUtil;
import eu.aleon.aleoncean.values.EnergyBow;
import eu.aleon.aleoncean.values.RockerSwitchButton;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public abstract class UserDataEEPF602LightAndBlindControlT2N extends UserDataRPS {

    private static final Map<Integer, RockerSwitchButton> ROCKER_ACTION_MAP;

    static {
        final Map<Integer, RockerSwitchButton> map = new HashMap<>();
        map.put(0, RockerSwitchButton.AI);
        map.put(1, RockerSwitchButton.AO);
        map.put(2, RockerSwitchButton.BI);
        map.put(3, RockerSwitchButton.BO);
        ROCKER_ACTION_MAP = Collections.unmodifiableMap(map);
    }

    private static final int POS_ENERGY_BOW_PRESSED = 4;

    private static final int POS_IS_2ND_ACTION_VALID = 0;

    public UserDataEEPF602LightAndBlindControlT2N() {
        super(T21State.PTM_TYPE2, NUState.NORMALMESSAGE);
    }

    public UserDataEEPF602LightAndBlindControlT2N(final byte[] eepData) {
        super(eepData, T21State.PTM_TYPE2, NUState.NORMALMESSAGE);
    }

    public RockerSwitchButton getRocker1stAction() throws UserDataScaleValueException {
        final int range = (int) getDataRange(0, 7, 0, 5);
        final RockerSwitchButton buttonState = ROCKER_ACTION_MAP.get(range);
        if (buttonState == null) {
            throw new UserDataScaleValueException(String.format("Button state unknown (%d).", range));
        }
        return buttonState;
    }

    public void setRocker1stAction(final RockerSwitchButton button) {
        final Integer range = MapUtil.getKeyFromValue(ROCKER_ACTION_MAP, button);
        setDataRange(range.longValue(), 0, 7, 0, 5);
    }

    public EnergyBow getEnergyBowPressed() {
        return getDataBit(0, POS_ENERGY_BOW_PRESSED) == 1 ? EnergyBow.PRESSED : EnergyBow.RELEASED;
    }

    public void setEnergyBowPressed(final EnergyBow energyBow) {
        setDataBit(0, POS_ENERGY_BOW_PRESSED, energyBow == EnergyBow.PRESSED);
    }

    public RockerSwitchButton getRocker2ndAction() throws UserDataScaleValueException {
        final int range = (int) getDataRange(0, 3, 0, 1);
        final RockerSwitchButton buttonState = ROCKER_ACTION_MAP.get(range);
        if (buttonState == null) {
            throw new UserDataScaleValueException(String.format("Button state unknown (%d).", range));
        }
        return buttonState;
    }

    public void setRocker2ndAction(final RockerSwitchButton button) {
        final Integer range = MapUtil.getKeyFromValue(ROCKER_ACTION_MAP, button);
        setDataRange(range.longValue(), 0, 3, 0, 1);
    }

    public boolean is2ndActionValid() {
        return getDataBit(0, POS_IS_2ND_ACTION_VALID) == 1;
    }

    public void set2ndActionValid(final boolean valid) {
        setDataBit(0, POS_IS_2ND_ACTION_VALID, valid);
    }

    public boolean isButtonAI() {
        try {
            return RockerSwitchButton.AI == getRocker1stAction() || (is2ndActionValid() && RockerSwitchButton.AI == getRocker2ndAction());
        } catch (final UserDataScaleValueException ex) {
            return false;
        }
    }

    public boolean isButtonAO() {
        try {
            return RockerSwitchButton.AO == getRocker1stAction() || (is2ndActionValid() && RockerSwitchButton.AO == getRocker2ndAction());
        } catch (final UserDataScaleValueException ex) {
            return false;
        }
    }

    public boolean isButtonBI() {
        try {
            return RockerSwitchButton.BI == getRocker1stAction() || (is2ndActionValid() && RockerSwitchButton.BI == getRocker2ndAction());
        } catch (final UserDataScaleValueException ex) {
            return false;
        }
    }

    public boolean isButtonBO() {
        try {
            return RockerSwitchButton.BO == getRocker1stAction() || (is2ndActionValid() && RockerSwitchButton.BO == getRocker2ndAction());
        } catch (final UserDataScaleValueException ex) {
            return false;
        }
    }

    public boolean switchLightOnA() {
        return isButtonAI();
    }

    public boolean switchLightOffA() {
        return isButtonAO();
    }

    public boolean switchLightOnB() {
        return isButtonBI();
    }

    public boolean switchLightOffB() {
        return isButtonBO();
    }

    public abstract boolean dimLightDownA();

    public abstract boolean dimLightUpA();

    public abstract boolean dimLightDownB();

    public abstract boolean dimLightUpB();

    public boolean moveBlindClosedA() {
        return dimLightDownA();
    }

    public boolean moveBlindOpenA() {
        return dimLightUpA();
    }

    public boolean moveBlindClosedB() {
        return dimLightDownB();
    }

    public boolean moveBlindOpenB() {
        return dimLightUpB();
    }

}
