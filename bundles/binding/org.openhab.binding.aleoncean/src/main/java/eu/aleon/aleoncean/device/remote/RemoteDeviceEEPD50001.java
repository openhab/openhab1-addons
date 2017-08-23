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
package eu.aleon.aleoncean.device.remote;

import java.util.Set;

import eu.aleon.aleoncean.device.DeviceParameter;
import eu.aleon.aleoncean.device.DeviceParameterUpdatedInitiation;
import eu.aleon.aleoncean.device.IllegalDeviceParameterException;
import eu.aleon.aleoncean.device.RemoteDevice;
import eu.aleon.aleoncean.device.StandardDevice;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.radio.RadioPacket1BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPD50001;
import eu.aleon.aleoncean.rxtx.ESP3Connector;
import eu.aleon.aleoncean.values.OpenClosed;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RemoteDeviceEEPD50001 extends StandardDevice implements RemoteDevice {

    private OpenClosed openClose;

    public RemoteDeviceEEPD50001(ESP3Connector conn, EnOceanId addressRemote, EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }

    public OpenClosed getOpenClose() {
        return openClose;
    }

    public void setOpenClose(final DeviceParameterUpdatedInitiation initiation, final OpenClosed openClose) {
        final OpenClosed oldOpenClose = this.openClose;
        this.openClose = openClose;
        fireParameterChanged(DeviceParameter.OPEN_CLOSED, initiation, oldOpenClose, openClose);
    }

    @Override
    protected void parseRadioPacket1BS(RadioPacket1BS packet) {
        final UserDataEEPD50001 userData = new UserDataEEPD50001(packet.getUserDataRaw());

        if (userData.isContactClosed()) {
            setOpenClose(DeviceParameterUpdatedInitiation.RADIO_PACKET, OpenClosed.CLOSED);
        } else {
            setOpenClose(DeviceParameterUpdatedInitiation.RADIO_PACKET, OpenClosed.OPEN);
        }
    }

    @Override
    public Object getByParameter(DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case OPEN_CLOSED:
                return getOpenClose();
            default:
                return super.getByParameter(parameter);
        }
    }

    @Override
    protected void fillParameters(Set<DeviceParameter> params) {
        params.add(DeviceParameter.OPEN_CLOSED);
    }

}
