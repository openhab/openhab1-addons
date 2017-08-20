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
import eu.aleon.aleoncean.packet.radio.RadioPacketMSC;
import eu.aleon.aleoncean.packet.radio.RadioPacketUTE;
import eu.aleon.aleoncean.packet.radio.RadioPacketVLD;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataMSC;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataUTE;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataUTEFactory;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataUTEQuery;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataUTEResponse;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.DefaultState;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.DimTimer;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.DimValue;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.IOChannel;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.LocalControl;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.MeasurementMode;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.OverCurrentShutDown;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.PowerFailure;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.ReportMeasurement;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.ResetMeasurement;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.ResetOverCurrentShutDown;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.TaughtInDevices;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.Unit;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserDataEEPD201;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserDataEEPD201CMD01;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserDataEEPD201CMD02;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserDataEEPD201CMD03;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserDataEEPD201CMD04;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserDataEEPD201CMD05;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserDataEEPD201CMD06;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserDataEEPD201CMD07;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserDataEEPD201Factory;
import eu.aleon.aleoncean.packet.radio.userdata.eepd201.UserInterfaceIndication;
import eu.aleon.aleoncean.rxtx.ESP3Connector;
import eu.aleon.aleoncean.util.Bits;
import eu.aleon.aleoncean.util.ThreadUtil;

/**
 * Abstract communication with a device using EEP D20108.
 *
 * Tested with device(s):
 * - Telefunken FS1
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RemoteDeviceEEPD20108 extends StandardDevice implements RemoteDevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPD20108.class);

    private Double energy;
    private Double power;
    private Boolean on;

    public RemoteDeviceEEPD20108(final ESP3Connector conn, final EnOceanId addressRemote, final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(final DeviceParameterUpdatedInitiation initiation, final Double energy) {
        final Double oldEnergy = this.energy;
        this.energy = energy;
        fireParameterChanged(DeviceParameter.ENERGY_WS, initiation, oldEnergy, energy);
    }

    public Double getPower() {
        return power;
    }

    public void setPower(final DeviceParameterUpdatedInitiation initiation, final Double power) {
        final Double oldPower = this.power;
        this.power = power;
        fireParameterChanged(DeviceParameter.POWER_W, initiation, oldPower, power);
    }

    public Boolean isOn() {
        return on;
    }

    public void setOn(final DeviceParameterUpdatedInitiation initiation, final Boolean on) {
        final Boolean oldOn = this.on;
        this.on = on;
        fireParameterChanged(DeviceParameter.SWITCH, initiation, oldOn, on);
    }

    public void switchOnOff(final DeviceParameterUpdatedInitiation initiation, final boolean on) {
        final UserDataEEPD201CMD01 userData = new UserDataEEPD201CMD01();
        userData.setDimValue(DimValue.SWITCH_TO_NEW_OUT_VALUE);
        userData.setIOChannel(IOChannel.OUTPUT_CHANNEL_00);
        userData.setOutputValueOnOff(on);
        send(userData);
        setOn(initiation, on);
    }

    public void sendTeachInResponse(final UserDataUTEQuery teachInQuery) {
        final RadioPacketMSC msc = new RadioPacketMSC();
        msc.setDestinationId(getAddressRemote());
        msc.setSenderId(getAddressLocal());
        msc.setStatus((byte) 0x00);

        final UserDataUTEResponse outUserData = new UserDataUTEResponse(teachInQuery);
        outUserData.setResponseType(UserDataUTEResponse.ResponseType.ACCEPTED_TEACH_IN);
        final RadioPacketUTE ute = outUserData.generateRadioPacket();
        ute.setDestinationId(getAddressRemote());
        ute.setSenderId(getAddressLocal());
        send(ute);
        ThreadUtil.sleep(20);

        switchOnOff(DeviceParameterUpdatedInitiation.RADIO_PACKET, false);
        ThreadUtil.sleep(20);

        sendConfiguration();
        ThreadUtil.sleep(20);

        msc.setUserDataRaw(new byte[] { (byte) 0x02, (byte) 0x50, (byte) 0x00, (byte) 0x00 });
        send(msc);
        ThreadUtil.sleep(20);

        querySwitchOnOff();
        ThreadUtil.sleep(20);
    }

    public void testTelefunkenFS1MSC() {
        /**
         * Let's do some reverse engineering of the MSC package send to the FS1.
         *
         * The data part (after the choice / R-ORG) consists of four bytes.
         * MSC packages contains 1,5 bytes Manufacturer ID.
         * So, there are 2,5 bytes / 5 nibbles to code data.
         *
         * It seems, that the MSC is used to control the status and the info LED.
         *
         * data byte: DB0-------------------- DB1-------------------- DB2-------------------- DB3--------------------
         * bit:...... 07 06 05 04 03 02 01 00 07 06 05 04 03 02 01 00 07 06 05 04 03 02 01 00 07 06 05 04 03 02 01 00
         * nibble:... N0--------- N1--------- N2--------- N3----------N4----------N5--------- N6--------- N7---------
         * offset:... 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31
         *
         * DB0,7 - DB1,4: manufacturer ID
         *
         * Tested with FS1 switched OFF:
         * ===
         * DB2,7 - DB2,6: control the info LED color
         * - 0b00 = off
         * - 0b01 = green
         * - 0b10 = yellow
         * - 0b11 = red
         * DB3,0: control the status LED color
         * - 0b0 = off
         * - 0b1 = yellow
         */
        final byte[] data = new byte[4];

        /* Telefunken smart building manufacturer ID */
        final int manu = 0x0025;

        // Fill manufacturer ID
        data[0] = (manu & 0x0FF0) >> 4;
        data[1] = (manu & 0x000F) << 4;
        /*
         * data[1] |= (x & 0x0F0000) >> 16;
         * data[2] = (byte) ((x & 0x00FF00) >> 8);
         * data[3] = (byte) ((x & 0x00000FF));
         */

        // Set the info LED to color red.
        Bits.setBitsOfBytes(3, data, 16, 2);

        // Set the status LED to color yellow.
        Bits.setBitsOfBytes(1, data, 31, 1);

        send(new UserDataMSC(data));
    }

    private void querySwitchOnOff() {
        final UserDataEEPD201CMD03 userData = new UserDataEEPD201CMD03();
        userData.setIOChannel(IOChannel.OUTPUT_CHANNEL_00);
        send(userData);
    }

    private void sendConfiguration() {
        configureSwitch();
        ThreadUtil.sleep(20);

        configurePowerMeasurement();
        ThreadUtil.sleep(20);

        configureEnergyMeasurement();
    }

    private void configureSwitch() {
        // Configure the one channel of the actuator.
        final UserDataEEPD201CMD02 ud = new UserDataEEPD201CMD02();
        ud.setTaughtInDevices(TaughtInDevices.DISABLE);
        ud.setOverCurrentShutDown(OverCurrentShutDown.AUTOMATIC_RESTART);
        ud.setResetOverCurrentShutDown(ResetOverCurrentShutDown.NOT_ACTIVE);
        ud.setLocalControl(LocalControl.ENABLE);
        ud.setIOChannel(IOChannel.OUTPUT_CHANNEL_00);
        ud.setDimTimer1(DimTimer.NOT_USED);
        ud.setDimTimer2(DimTimer.NOT_USED);
        ud.setDimTimer3(DimTimer.TIMER_4_0_SEC);
        ud.setUserInterfaceIndication(UserInterfaceIndication.DAY_OPERATION);
        ud.setPowerFailure(PowerFailure.DISABLE_DETECTION_OR_DETECTION_NOT_SUPPORTED);
        ud.setDefaultState(DefaultState.REMEMBER_PREVIOUS_STATE);
        LOGGER.trace("Send configuration ({} => {}): switch", getAddressLocal(), getAddressRemote());
        send(ud);
    }

    private void configureEnergyMeasurement() {
        try {
            final UserDataEEPD201CMD05 ud = new UserDataEEPD201CMD05();
            ud.setReportMeasurement(ReportMeasurement.QUERY_AND_AUTO_REPORTING);
            ud.setResetMeasurement(ResetMeasurement.NOT_ACTIVE);
            ud.setMeasurementMode(MeasurementMode.ENERGY);
            ud.setIOChannel(IOChannel.OUTPUT_CHANNEL_00);
            ud.setUnit(Unit.ENERGY_WH);
            ud.setMeasurementDeltaToBeReported(1);
            ud.setMaximumTimeBetweenTwoSubsequentActuator(130);
            ud.setMinimumTimeBetweenTwoSubsequentActuator(15);
            LOGGER.trace("Send configuration ({} => {}): energy measurement", getAddressLocal(), getAddressRemote());
            send(ud);
        } catch (final UserDataScaleValueException ex) {
            LOGGER.warn("Something went wrong on configure energy measurement.", ex);
        }
    }

    private void configurePowerMeasurement() {
        try {
            final UserDataEEPD201CMD05 ud = new UserDataEEPD201CMD05();
            ud.setReportMeasurement(ReportMeasurement.QUERY_AND_AUTO_REPORTING);
            ud.setResetMeasurement(ResetMeasurement.NOT_ACTIVE);
            ud.setMeasurementMode(MeasurementMode.POWER);
            ud.setIOChannel(IOChannel.OUTPUT_CHANNEL_00);
            ud.setUnit(Unit.POWER_W);
            ud.setMeasurementDeltaToBeReported(15);
            ud.setMaximumTimeBetweenTwoSubsequentActuator(130);
            ud.setMinimumTimeBetweenTwoSubsequentActuator(15);
            LOGGER.trace("Send configuration ({} => {}): power measurement", getAddressLocal(), getAddressRemote());
            send(ud);
        } catch (final UserDataScaleValueException ex) {
            LOGGER.warn("Something went wrong on configure power measurement.", ex);
        }
    }

    public void handleIncomingEnergyValue(final double wattSeconds) {
        LOGGER.debug("{} - Received new energy value: {} Ws", getAddressRemote(), wattSeconds);
        setEnergy(DeviceParameterUpdatedInitiation.RADIO_PACKET, wattSeconds);
    }

    public void handleIncomingPowerValue(final double watt) {
        LOGGER.debug("{} - Received new power value: {} W", getAddressRemote(), watt);
        setPower(DeviceParameterUpdatedInitiation.RADIO_PACKET, watt);
    }

    public void handleIncomingOutputValue(final boolean on) {
        LOGGER.debug("{} - Received new output value: {}", getAddressRemote(), on);
        setOn(DeviceParameterUpdatedInitiation.RADIO_PACKET, on);
    }

    public void handleIncomingActuatorMeasurementResponse(final UserDataEEPD201CMD07 userData) {
        if (userData.getIOChannel() != IOChannel.OUTPUT_CHANNEL_00) {
            LOGGER.warn("Received measurement response from invalid channel ({}).", userData.getIOChannel());
            return;
        }

        switch (userData.getUnit()) {
            case ENERGY_WS:
                handleIncomingEnergyValue(userData.getMeasurementValue());
                break;
            case ENERGY_WH:
                handleIncomingEnergyValue(userData.getMeasurementValue() * 3600);
                break;
            case ENERGY_KWH:
                handleIncomingEnergyValue(userData.getMeasurementValue() * 3600 * 1000);
                break;

            case POWER_W:
                handleIncomingPowerValue(userData.getMeasurementValue());
                break;
            case POWER_KW:
                handleIncomingPowerValue(userData.getMeasurementValue() * 1000);
                break;

            default:
                LOGGER.warn("The unit ({}) of the incoming measurement response is unknown.", userData.getUnit());
                break;
        }
    }

    public void handleIncomingActuatorStatusResponse(final UserDataEEPD201CMD04 userData) {
        try {
            if (userData.getIOChannel() != IOChannel.OUTPUT_CHANNEL_00) {
                LOGGER.warn("Received status response from invalid channel ({}).", userData.getIOChannel());
                return;
            }

            handleIncomingOutputValue(userData.getOutputValueOnOff());
        } catch (final UserDataScaleValueException ex) {
            LOGGER.warn("Something went wrong on status response handling.", ex);
        }
    }

    private void parseRadioPacketVLDCmdShouldBeSent(final UserDataEEPD201 userData) {
        LOGGER.warn("This command (0x%02X) should be sent to an actuator... Skip it.", userData.getCmd());
    }

    protected void parseRadioPacketVLD(final RadioPacketVLD packet) {
        // if (!packet.getSenderId().equals(getAddressRemote())) {
        // LOGGER.warn("Got a package that sender ID does not fit (senderId={}, expected={}).", packet.getSenderId(),
        // getAddressRemote());
        // return;
        // }

        final UserDataEEPD201 userData = UserDataEEPD201Factory.createFromUserDataRaw(packet.getUserDataRaw());

        if (userData instanceof UserDataEEPD201CMD01) {
            parseRadioPacketVLDCmdShouldBeSent(userData);
        } else if (userData instanceof UserDataEEPD201CMD02) {
            parseRadioPacketVLDCmdShouldBeSent(userData);
        } else if (userData instanceof UserDataEEPD201CMD03) {
            parseRadioPacketVLDCmdShouldBeSent(userData);
        } else if (userData instanceof UserDataEEPD201CMD04) {
            handleIncomingActuatorStatusResponse((UserDataEEPD201CMD04) userData);
        } else if (userData instanceof UserDataEEPD201CMD05) {
            parseRadioPacketVLDCmdShouldBeSent(userData);
        } else if (userData instanceof UserDataEEPD201CMD06) {
            parseRadioPacketVLDCmdShouldBeSent(userData);
        } else if (userData instanceof UserDataEEPD201CMD07) {
            handleIncomingActuatorMeasurementResponse((UserDataEEPD201CMD07) userData);
        } else {
            LOGGER.warn("Unexpected user data received (CMD=0x%02X).", userData.getCmd());
        }
    }

    protected void parseRadioPacketUTE(final RadioPacketUTE packet) {
        // if (!packet.getSenderId().equals(getAddressRemote())) {
        // LOGGER.warn("Got a package that sender ID does not fit (senderId={}, expected={}).", packet.getSenderId(),
        // getAddressRemote());
        // return;
        // }

        final UserDataUTE userData = UserDataUTEFactory.createFromUserDataRaw(packet.getUserDataRaw());
        if (userData instanceof UserDataUTEQuery) {
            sendTeachInResponse((UserDataUTEQuery) userData);
        } else {
            LOGGER.warn("Got a UTE message, that is not a query.");
        }
    }

    @Override
    protected void fillParameters(final Set<DeviceParameter> params) {
        params.add(DeviceParameter.ENERGY_WS);
        params.add(DeviceParameter.POWER_W);
        params.add(DeviceParameter.SWITCH);
        params.add(DeviceParameter.TMP_SEND_CONFIGURATION);
    }

    @Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case ENERGY_WS:
                return getEnergy();
            case POWER_W:
                return getPower();
            case SWITCH:
                return isOn();
            default:
                return super.getByParameter(parameter);
        }
    }

    @Override
    public void setByParameter(final DeviceParameter parameter, final Object value)
            throws IllegalDeviceParameterException {
        assert DeviceParameter.getSupportedClass(parameter).isAssignableFrom(value.getClass());
        switch (parameter) {
            case SWITCH:
                switchOnOff(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Boolean) value);
                break;
            case TMP_SEND_CONFIGURATION:
                if (value instanceof Boolean && (Boolean) value) {
                    sendConfiguration();
                }
                break;
            default:
                super.setByParameter(parameter, value);
                break;
        }
    }
}
