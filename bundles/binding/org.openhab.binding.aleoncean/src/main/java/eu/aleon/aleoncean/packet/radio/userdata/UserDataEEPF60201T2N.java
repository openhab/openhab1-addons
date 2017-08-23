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

import eu.aleon.aleoncean.values.RockerSwitchButton;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPF60201T2N extends UserDataEEPF602LightAndBlindControlT2N {

    public static final RockerSwitchButton DIM_A_UP = RockerSwitchButton.AO;
    public static final RockerSwitchButton DIM_A_DOWN = RockerSwitchButton.AI;
    public static final RockerSwitchButton DIM_B_UP = RockerSwitchButton.BO;
    public static final RockerSwitchButton DIM_B_DOWN = RockerSwitchButton.BI;

    public UserDataEEPF60201T2N() {
    }

    public UserDataEEPF60201T2N(final byte[] eepData) {
        super(eepData);
    }

    @Override
    public boolean dimLightDownA() {
        return isButtonAI();
    }

    @Override
    public boolean dimLightUpA() {
        return isButtonAO();
    }

    @Override
    public boolean dimLightDownB() {
        return isButtonBI();
    }

    @Override
    public boolean dimLightUpB() {
        return isButtonBO();
    }
}
