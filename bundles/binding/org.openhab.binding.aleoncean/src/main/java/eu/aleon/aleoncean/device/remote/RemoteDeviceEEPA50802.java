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
import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataEEPA50802;
import eu.aleon.aleoncean.rxtx.ESP3Connector;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RemoteDeviceEEPA50802 extends StandardDevice implements RemoteDevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPA50802.class);

    private Double supplyVoltage;
    private Double illumination;
    private Double temperature;
    private Boolean motion;
    private Boolean occupancyButtonPressed;

    public RemoteDeviceEEPA50802(final ESP3Connector conn, final EnOceanId addressRemote, final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }

    public Double getSupplyVoltage() {
        return supplyVoltage;
    }

    public void setSupplyVoltage(final DeviceParameterUpdatedInitiation initiation, final Double supplyVoltage) {
        final Double oldSupplyVoltage = this.supplyVoltage;
        this.supplyVoltage = supplyVoltage;
        fireParameterChanged(DeviceParameter.SUPPLY_VOLTAGE_V, initiation, oldSupplyVoltage, supplyVoltage);
    }

    public Double getIllumination() {
        return illumination;
    }

    public void setIllumination(final DeviceParameterUpdatedInitiation initiation, final Double illumination) {
        final Double oldIllumination = this.illumination;
        this.illumination = illumination;
        fireParameterChanged(DeviceParameter.ILLUMINATION_LUX, initiation, oldIllumination, illumination);
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(final DeviceParameterUpdatedInitiation initiation, final Double temperature) {
        final Double oldTemperature = this.temperature;
        this.temperature = temperature;
        fireParameterChanged(DeviceParameter.TEMPERATURE_CELSIUS, initiation, oldTemperature, temperature);
    }

    public Boolean isMotion() {
        return motion;
    }

    public void setMotion(final DeviceParameterUpdatedInitiation initiation, final Boolean motion) {
        final Boolean oldMotion = this.motion;
        this.motion = motion;
        fireParameterChanged(DeviceParameter.MOTION, initiation, oldMotion, motion);
    }

    public Boolean isOccupancyButtonPressed() {
        return occupancyButtonPressed;
    }

    public void setOccupancyButtonPressed(final DeviceParameterUpdatedInitiation initiation, final Boolean occupancyButtonPressed) {
        final Boolean oldOccupancyButtonPressed = this.occupancyButtonPressed;
        this.occupancyButtonPressed = occupancyButtonPressed;
        fireParameterChanged(DeviceParameter.OCCUPANCY_BUTTON, initiation, oldOccupancyButtonPressed, occupancyButtonPressed);
    }

    protected void parseRadioPacket4BS(final RadioPacket4BS packet) {
        if (packet.isTeachIn()) {
            LOGGER.debug("Ignore teach-in packets.");
            return;
        }

        final UserDataEEPA50802 userData = new UserDataEEPA50802(packet.getUserDataRaw());
        setSupplyVoltage(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getSupplyVoltage());
        setIllumination(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getIllumination());
        setTemperature(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getTemperature());
        setMotion(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isPIRStatusOn());
        setOccupancyButtonPressed(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isOccupancyButtonPressed());
    }

    @Override
    protected void fillParameters(final Set<DeviceParameter> params) {
        params.add(DeviceParameter.SUPPLY_VOLTAGE_V);
        params.add(DeviceParameter.ILLUMINATION_LUX);
        params.add(DeviceParameter.TEMPERATURE_CELSIUS);
        params.add(DeviceParameter.MOTION);
        params.add(DeviceParameter.OCCUPANCY_BUTTON);
    }

    @Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case SUPPLY_VOLTAGE_V:
                return getSupplyVoltage();
            case ILLUMINATION_LUX:
                return getIllumination();
            case TEMPERATURE_CELSIUS:
                return getTemperature();
            case MOTION:
                return isMotion();
            case OCCUPANCY_BUTTON:
                return isOccupancyButtonPressed();
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
