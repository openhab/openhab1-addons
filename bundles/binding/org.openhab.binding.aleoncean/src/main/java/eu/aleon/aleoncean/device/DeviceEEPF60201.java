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

import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPF60201T2N;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPF60201T2U;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPF602LightAndBlindControlT2N;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPF602LightAndBlindControlT2U;
import eu.aleon.aleoncean.rxtx.ESP3Connector;
import eu.aleon.aleoncean.values.RockerSwitchButton;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class DeviceEEPF60201 extends DeviceRPSLightAndBlindControlAppStyle {

    public DeviceEEPF60201(final ESP3Connector conn,
                           final EnOceanId addressRemote,
                           final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }

    @Override
    protected UserDataEEPF602LightAndBlindControlT2N newPacketDataT2N() {
        return new UserDataEEPF60201T2N();
    }

    @Override
    protected UserDataEEPF602LightAndBlindControlT2U newPacketDataT2U() {
        return new UserDataEEPF60201T2U();
    }

    @Override
    protected RockerSwitchButton getDimUpA() {
        return UserDataEEPF60201T2N.DIM_A_UP;
    }

    @Override
    protected RockerSwitchButton getDimDownA() {
        return UserDataEEPF60201T2N.DIM_A_DOWN;
    }

    @Override
    protected RockerSwitchButton getDimUpB() {
        return UserDataEEPF60201T2N.DIM_B_UP;
    }

    @Override
    protected RockerSwitchButton getDimDownB() {
        return UserDataEEPF60201T2N.DIM_B_DOWN;
    }

}
