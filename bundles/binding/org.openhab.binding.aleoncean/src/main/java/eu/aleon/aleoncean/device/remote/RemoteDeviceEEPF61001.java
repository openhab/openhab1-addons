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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.aleon.aleoncean.device.DeviceParameter;
import eu.aleon.aleoncean.device.DeviceParameterUpdatedInitiation;
import eu.aleon.aleoncean.device.IllegalDeviceParameterException;
import eu.aleon.aleoncean.device.RemoteDevice;
import eu.aleon.aleoncean.device.StandardDevice;
import eu.aleon.aleoncean.packet.EnOceanId;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPF61001Factory;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPF61001T2U;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataRPS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.rxtx.ESP3Connector;
import eu.aleon.aleoncean.values.WindowHandlePosition;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RemoteDeviceEEPF61001 extends StandardDevice implements RemoteDevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPF61001.class);

    private WindowHandlePosition windowHandlePosition;

    public RemoteDeviceEEPF61001(final ESP3Connector conn, final EnOceanId addressRemote, final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }

    public WindowHandlePosition getWindowHandlePosition() {
        return windowHandlePosition;
    }

    public void setWindowHandlePosition(final DeviceParameterUpdatedInitiation initiation, final WindowHandlePosition windowHandlePosition) {
        final WindowHandlePosition oldWindowHandlePosition = this.windowHandlePosition;
        this.windowHandlePosition = windowHandlePosition;
        fireParameterChanged(DeviceParameter.WINDOW_HANDLE_POSITION, initiation, oldWindowHandlePosition, windowHandlePosition);
    }

    public void parseT2U(final UserDataEEPF61001T2U userData) {
        try {
            setWindowHandlePosition(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getWindowHandlePosition());
        } catch (final UserDataScaleValueException ex) {
            LOGGER.warn("Received not parsable window handle position.");
        }
    }

    @Override
    protected void parseRadioPacketRPS(final RadioPacketRPS packet) {
        final UserDataRPS userData = UserDataEEPF61001Factory.getPacketData(packet);
        if (userData instanceof UserDataEEPF61001T2U) {
            parseT2U((UserDataEEPF61001T2U) userData);
        } else {
            LOGGER.debug("Cannot handle user data.");
        }
    }

    @Override
    protected void fillParameters(final Set<DeviceParameter> params) {
        params.add(DeviceParameter.WINDOW_HANDLE_POSITION);
    }

    @Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case WINDOW_HANDLE_POSITION:
                return getWindowHandlePosition();
            default:
                return super.getByParameter(parameter);
        }
    }

    @Override
    public void setByParameter(final DeviceParameter parameter, final Object value) throws IllegalDeviceParameterException {
        assert DeviceParameter.getSupportedClass(parameter).isAssignableFrom(value.getClass());
        super.setByParameter(parameter, value);
    }

}
