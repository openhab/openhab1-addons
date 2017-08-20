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
 *    Stephan Meyer - make it an abstract class
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
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPA504;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 * Abstract base class for EEP A504.. 
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 * @author Stephan Meyer {@literal <smeyersdev@gmail.com>}
 */
public abstract class RemoteDeviceEEPA504 extends StandardDevice implements RemoteDevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPA504.class);

    private Double humidity;
    private Double temperature;

    public RemoteDeviceEEPA504(final ESP3Connector conn,
                                 final EnOceanId addressRemote,
                                 final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }
    
    protected abstract UserDataEEPA504 createUserData(final RadioPacket4BS packet);

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(final DeviceParameterUpdatedInitiation initiation, final Double humidity) {
        final Double oldHumidity = this.humidity;
        this.humidity = humidity;
        fireParameterChanged(DeviceParameter.HUMIDITY_PERCENT, initiation, oldHumidity, humidity);
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(final DeviceParameterUpdatedInitiation initiation, final Double temperature) {
        final Double oldTemperature = this.temperature;
        this.temperature = temperature;
        fireParameterChanged(DeviceParameter.TEMPERATURE_CELSIUS, initiation, oldTemperature, temperature);
    }

    protected void parseRadioPacket4BS(final RadioPacket4BS packet) {
        if (packet.isTeachIn()) {
            LOGGER.debug("Ignore teach-in packets.");
            return;
        }

        final UserDataEEPA504 userData = this.createUserData(packet);
        try {
            setHumidity(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getHumidity());
        } catch (final UserDataScaleValueException ex) {
            LOGGER.warn("Received humidity is invalid.");
        }

        try {
            setTemperature(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getTemperature());
        } catch (final UserDataScaleValueException ex) {
            LOGGER.warn("Received temperature is invalid.");
        }
    }

    @Override
    protected void fillParameters(final Set<DeviceParameter> params) {
        params.add(DeviceParameter.HUMIDITY_PERCENT);
        params.add(DeviceParameter.TEMPERATURE_CELSIUS);
    }

    @Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case HUMIDITY_PERCENT:
                return getHumidity();
            case TEMPERATURE_CELSIUS:
                return getTemperature();
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
