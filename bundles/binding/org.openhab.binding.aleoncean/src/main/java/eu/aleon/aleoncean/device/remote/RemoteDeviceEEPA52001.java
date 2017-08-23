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
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.packet.radio.userdata.eepa520.Function;
import eu.aleon.aleoncean.packet.radio.userdata.eepa520.SetPointSelection;
import eu.aleon.aleoncean.packet.radio.userdata.eepa520.UserDataEEPA52001FromActuator;
import eu.aleon.aleoncean.packet.radio.userdata.eepa520.UserDataEEPA52001ToActuator;
import eu.aleon.aleoncean.packet.radio.userdata.teachin4bs.EEPResult;
import eu.aleon.aleoncean.packet.radio.userdata.teachin4bs.LearnResult;
import eu.aleon.aleoncean.packet.radio.userdata.teachin4bs.LearnStatus;
import eu.aleon.aleoncean.packet.radio.userdata.teachin4bs.UserData4BSTeachInVariant3;
import eu.aleon.aleoncean.rxtx.ESP3Connector;
import eu.aleon.aleoncean.values.LearnType4BS;

/**
 * A remote device using the EEP A5-20-01.
 *
 * Device parameters (user action):
 * - POSITION_PERCENT (r): The position that was last received from the actuator.
 * - TEMPERATURE_CELSIUS (r): The temperature that was last received from the actuator.
 * - SETPOINT_POSITION_PERCENT (rw): The position that should be set (if TEMPERATURE_CONTROL_ENABLE is false).
 * - SETPOINT_TEMPERATURE_CELSIUS (rw): The temperature that should be set (if TEMPERATURE_CONTROL_ENABLE is true).
 * - TEMPERATURE_CONTROL_ENABLE (rw): Flag if the set point is controlled by temperature (or position).
 * - TEMPERATURE_CONTROL_CUR_TEMP (rw): The current temperature used for proportional–integral controller (used if TEMPERATURE_CONTROL_ENABLE is true).
 *
 * Temporary used (only for internal use / debugging);
 * DO NOT USE, could be changed with every build:
 * - TMP_RECV_SERVICE_ON,
 * - TMP_RECV_ENERGY_INPUT_ENABLED,
 * - TMP_RECV_ENERGY_STORAGE_SUFFICIENT,
 * - TMP_RECV_CHANGE_BATTERY,
 * - TMP_RECV_COVER_OPEN,
 * - TMP_RECV_TEMPERATURE_SENSOR_FAILURE,
 * - TMP_RECV_WINDOW_OPEN,
 * - TMP_RECV_ACTUATOR_OBSTRUCTED,
 * - TMP_SEND_RUN_INIT_SEQUENCE,
 * - TMP_SEND_LIFT_SET,
 * - TMP_SEND_VALVE_OPEN,
 * - TMP_SEND_VALVE_CLOSED,
 * - TMP_SEND_REDUCED_ENERGY_CONSUMPTION
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RemoteDeviceEEPA52001 extends StandardDevice implements RemoteDevice {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteDeviceEEPA52001.class);

    private static final int DFL_SEND_SETPOINT_POSITION = 50;
    private static final double DFL_SEND_SETPOINT_TEMPERATURE = 20.0;
    private static final double DFL_SEND_TEMPERATURE_ROOM = 20.0;
    private static final boolean DFL_SEND_TEMPERATURE_CONTROLLED = false;
    private static final boolean DFL_SEND_RUN_INIT_SEQUENCE = false;
    private static final boolean DFL_SEND_LIFT_SET = false;
    private static final boolean DFL_SEND_VALVE_OPEN = false;
    private static final boolean DFL_SEND_VALVE_CLOSED = false;
    private static final boolean DFL_SEND_REDUCED_ENERGY_CONSUMPTION = false;

    private Integer recvPosition;
    private Boolean recvServiceOn;
    private Boolean recvEnergyInputEnabled;
    private Boolean recvEnergyStorageSufficient;
    private Boolean recvChangeBattery;
    private Boolean recvCoverOpen;
    private Boolean recvTemperatureSensorFailure;
    private Boolean recvWindowOpen;
    private Boolean recvActuatorObstructed;
    private Double recvTemperature;

    private Integer sendSetPointPosition;
    private Double sendSetPointTemperature;
    private Double sendTemperatureRoom;
    private Boolean sendRunInitSequence;
    private Boolean sendLiftSet;
    private Boolean sendValveOpen;
    private Boolean sendValveClosed;
    private Boolean sendReducedEnergyConsumption;
    private Boolean sendTemperatureControlled;

    public RemoteDeviceEEPA52001(final ESP3Connector conn,
                                 final EnOceanId addressRemote,
                                 final EnOceanId addressLocal) {
        super(conn, addressRemote, addressLocal);
    }

    public Integer getRecvPosition() {
        return recvPosition;
    }

    public void setRecvPosition(final DeviceParameterUpdatedInitiation initiation,
                                final Integer recvPosition) {
        final Integer old = this.recvPosition;
        this.recvPosition = recvPosition;
        fireParameterChanged(DeviceParameter.POSITION_PERCENT, initiation, old, recvPosition);
    }

    public Boolean getRecvServiceOn() {
        return recvServiceOn;
    }

    public void setRecvServiceOn(final DeviceParameterUpdatedInitiation initiation,
                                 final Boolean recvServiceOn) {
        final Boolean old = this.recvServiceOn;
        this.recvServiceOn = recvServiceOn;
        fireParameterChanged(DeviceParameter.TMP_RECV_SERVICE_ON, initiation, old, recvServiceOn);
    }

    public Boolean getRecvEnergyInputEnabled() {
        return recvEnergyInputEnabled;
    }

    public void setRecvEnergyInputEnabled(final DeviceParameterUpdatedInitiation initiation,
                                          final Boolean recvEnergyInputEnabled) {
        final Boolean old = this.recvEnergyInputEnabled;
        this.recvEnergyInputEnabled = recvEnergyInputEnabled;
        fireParameterChanged(DeviceParameter.TMP_RECV_ENERGY_INPUT_ENABLED, initiation, old, recvEnergyInputEnabled);
    }

    public Boolean getRecvEnergyStorageSufficient() {
        return recvEnergyStorageSufficient;
    }

    public void setRecvEnergyStorageSufficient(final DeviceParameterUpdatedInitiation initiation,
                                               final Boolean recvEnergyStorageSufficient) {
        final Boolean old = this.recvEnergyStorageSufficient;
        this.recvEnergyStorageSufficient = recvEnergyStorageSufficient;
        fireParameterChanged(DeviceParameter.TMP_RECV_ENERGY_STORAGE_SUFFICIENT, initiation, old, recvEnergyStorageSufficient);
    }

    public Boolean getRecvChangeBattery() {
        return recvChangeBattery;
    }

    public void setRecvChangeBattery(final DeviceParameterUpdatedInitiation initiation,
                                     final Boolean recvChangeBattery) {
        final Boolean old = this.recvChangeBattery;
        this.recvChangeBattery = recvChangeBattery;
        fireParameterChanged(DeviceParameter.TMP_RECV_CHANGE_BATTERY, initiation, old, recvChangeBattery);
    }

    public Boolean getRecvCoverOpen() {
        return recvCoverOpen;
    }

    public void setRecvCoverOpen(final DeviceParameterUpdatedInitiation initiation,
                                 final Boolean recvCoverOpen) {
        final Boolean old = this.recvCoverOpen;
        this.recvCoverOpen = recvCoverOpen;
        fireParameterChanged(DeviceParameter.TMP_RECV_COVER_OPEN, initiation, old, recvCoverOpen);
    }

    public Boolean getRecvTemperatureSensorFailure() {
        return recvTemperatureSensorFailure;
    }

    public void setRecvTemperatureSensorFailure(final DeviceParameterUpdatedInitiation initiation,
                                                final Boolean recvTemperatureSensorFailure) {
        final Boolean old = this.recvTemperatureSensorFailure;
        this.recvTemperatureSensorFailure = recvTemperatureSensorFailure;
        fireParameterChanged(DeviceParameter.TMP_RECV_TEMPERATURE_SENSOR_FAILURE, initiation, old, recvTemperatureSensorFailure);
    }

    public Boolean getRecvWindowOpen() {
        return recvWindowOpen;
    }

    public void setRecvWindowOpen(final DeviceParameterUpdatedInitiation initiation,
                                  final Boolean recvWindowOpen) {
        final Boolean old = this.recvWindowOpen;
        this.recvWindowOpen = recvWindowOpen;
        fireParameterChanged(DeviceParameter.TMP_RECV_WINDOW_OPEN, initiation, old, recvWindowOpen);
    }

    public Boolean getRecvActuatorObstructed() {
        return recvActuatorObstructed;
    }

    public void setRecvActuatorObstructed(final DeviceParameterUpdatedInitiation initiation,
                                          final Boolean recvActuatorObstructed) {
        final Boolean old = this.recvActuatorObstructed;
        this.recvActuatorObstructed = recvActuatorObstructed;
        fireParameterChanged(DeviceParameter.TMP_RECV_ACTUATOR_OBSTRUCTED, initiation, old, recvActuatorObstructed);
    }

    public Double getRecvTemperature() {
        return recvTemperature;
    }

    public void setRecvTemperature(final DeviceParameterUpdatedInitiation initiation,
                                   final Double recvTemperature) {
        final Double old = this.recvTemperature;
        this.recvTemperature = recvTemperature;
        fireParameterChanged(DeviceParameter.TEMPERATURE_CELSIUS, initiation, old, recvTemperature);
    }

    public Integer getSendSetPointPosition() {
        return sendSetPointPosition;
    }

    public void setSendSetPointPosition(final DeviceParameterUpdatedInitiation initiation,
                                        final Integer sendSetPointPosition) {
        final Integer old = this.sendSetPointPosition;
        this.sendSetPointPosition = sendSetPointPosition;
        fireParameterChanged(DeviceParameter.SETPOINT_POSITION_PERCENT, initiation, old, sendSetPointPosition);
    }

    public Double getSendSetPointTemperature() {
        return sendSetPointTemperature;
    }

    public void setSendSetPointTemperature(final DeviceParameterUpdatedInitiation initiation,
                                           final Double sendSetPointTemperature) {
        final Double old = this.sendSetPointTemperature;
        this.sendSetPointTemperature = sendSetPointTemperature;
        fireParameterChanged(DeviceParameter.SETPOINT_TEMPERATURE_CELSIUS, initiation, old, sendSetPointTemperature);
    }

    public Double getSendTemperatureRoom() {
        return sendTemperatureRoom;
    }

    public void setSendTemperatureRoom(final DeviceParameterUpdatedInitiation initiation,
                                       final Double sendTemperatureRoom) {
        final Double old = this.sendTemperatureRoom;
        this.sendTemperatureRoom = sendTemperatureRoom;
        fireParameterChanged(DeviceParameter.TEMPERATURE_CONTROL_CUR_TEMP, initiation, old, sendTemperatureRoom);
    }

    public Boolean getSendRunInitSequence() {
        return sendRunInitSequence;
    }

    public void setSendRunInitSequence(final DeviceParameterUpdatedInitiation initiation,
                                       final Boolean sendRunInitSequence) {
        final Boolean old = this.sendRunInitSequence;
        this.sendRunInitSequence = sendRunInitSequence;
        fireParameterChanged(DeviceParameter.TMP_SEND_RUN_INIT_SEQUENCE, initiation, old, sendRunInitSequence);
    }

    public Boolean getSendLiftSet() {
        return sendLiftSet;
    }

    public void setSendLiftSet(final DeviceParameterUpdatedInitiation initiation,
                               final Boolean sendLiftSet) {
        final Boolean old = this.sendLiftSet;
        this.sendLiftSet = sendLiftSet;
        fireParameterChanged(DeviceParameter.TMP_SEND_LIFT_SET, initiation, old, sendLiftSet);
    }

    public Boolean getSendValveOpen() {
        return sendValveOpen;
    }

    public void setSendValveOpen(final DeviceParameterUpdatedInitiation initiation,
                                 final Boolean sendValveOpen) {
        final Boolean old = this.sendValveOpen;
        this.sendValveOpen = sendValveOpen;
        fireParameterChanged(DeviceParameter.TMP_SEND_VALVE_OPEN, initiation, old, sendValveOpen);
    }

    public Boolean getSendValveClosed() {
        return sendValveClosed;
    }

    public void setSendValveClosed(final DeviceParameterUpdatedInitiation initiation,
                                   final Boolean sendValveClosed) {
        final Boolean old = this.sendValveClosed;
        this.sendValveClosed = sendValveClosed;
        fireParameterChanged(DeviceParameter.TMP_SEND_VALVE_CLOSED, initiation, old, sendValveClosed);
    }

    public Boolean getSendReducedEnergyConsumption() {
        return sendReducedEnergyConsumption;
    }

    public void setSendReducedEnergyConsumption(final DeviceParameterUpdatedInitiation initiation,
                                                final Boolean sendReducedEnergyConsumption) {
        final Boolean old = this.sendReducedEnergyConsumption;
        this.sendReducedEnergyConsumption = sendReducedEnergyConsumption;
        fireParameterChanged(DeviceParameter.TMP_SEND_REDUCED_ENERGY_CONSUMPTION, initiation, old, sendReducedEnergyConsumption);
    }

    public Boolean getSendTemperatureControlled() {
        return sendTemperatureControlled;
    }

    public void setSendTemperatureControlled(final DeviceParameterUpdatedInitiation initiation,
                                             final Boolean sendTemperatureControlled) {
        final Boolean old = this.sendTemperatureControlled;
        this.sendTemperatureControlled = sendTemperatureControlled;
        fireParameterChanged(DeviceParameter.TEMPERATURE_CONTROL_ENABLE, initiation, old, sendTemperatureControlled);
    }

    private void setDefault(final boolean onlyIfUnset) {
        if (!onlyIfUnset || getSendSetPointPosition() == null) {
            setSendSetPointPosition(DeviceParameterUpdatedInitiation.INTERNAL_LOGIC, DFL_SEND_SETPOINT_POSITION);
        }

        if (!onlyIfUnset || getSendSetPointTemperature() == null) {
            setSendSetPointTemperature(DeviceParameterUpdatedInitiation.INTERNAL_LOGIC, DFL_SEND_SETPOINT_TEMPERATURE);
        }

        if (!onlyIfUnset || getSendTemperatureControlled() == null) {
            setSendTemperatureControlled(DeviceParameterUpdatedInitiation.INTERNAL_LOGIC, DFL_SEND_TEMPERATURE_CONTROLLED);
        }

        if (!onlyIfUnset || getSendTemperatureRoom() == null) {
            setSendTemperatureRoom(DeviceParameterUpdatedInitiation.INTERNAL_LOGIC, DFL_SEND_TEMPERATURE_ROOM);
        }

        if (!onlyIfUnset || getSendRunInitSequence() == null) {
            setSendRunInitSequence(DeviceParameterUpdatedInitiation.INTERNAL_LOGIC, DFL_SEND_RUN_INIT_SEQUENCE);
        }

        if (!onlyIfUnset || getSendLiftSet() == null) {
            setSendLiftSet(DeviceParameterUpdatedInitiation.INTERNAL_LOGIC, DFL_SEND_LIFT_SET);
        }

        if (!onlyIfUnset || getSendValveOpen() == null) {
            setSendValveOpen(DeviceParameterUpdatedInitiation.INTERNAL_LOGIC, DFL_SEND_VALVE_OPEN);
        }

        if (!onlyIfUnset || getSendValveClosed() == null) {
            setSendValveClosed(DeviceParameterUpdatedInitiation.INTERNAL_LOGIC, DFL_SEND_VALVE_CLOSED);
        }

        if (!onlyIfUnset || getSendReducedEnergyConsumption() == null) {
            setSendReducedEnergyConsumption(DeviceParameterUpdatedInitiation.INTERNAL_LOGIC, DFL_SEND_REDUCED_ENERGY_CONSUMPTION);
        }
    }

    private void sendPacket() {
        setDefault(true);

        // Try to build and send packet.
        try {
            final UserDataEEPA52001ToActuator userData = new UserDataEEPA52001ToActuator();
            userData.setTeachIn(false);

            if (getSendTemperatureControlled()) {
                userData.setSetPointSelection(SetPointSelection.TEMPERATURE);
                userData.setTemperatureSetpoint(getSendSetPointTemperature());
            } else {
                userData.setSetPointSelection(SetPointSelection.VALVE_POSITION);
                userData.setValvePosition(getSendSetPointPosition());
            }

            userData.setCurrentTemperature(getSendTemperatureRoom());
            userData.setRunInitSequence(getSendRunInitSequence());
            userData.setLiftSet(getSendLiftSet());
            userData.setValveOpen(getSendValveOpen());
            userData.setValveClosed(getSendValveClosed());
            userData.setEnergyConsumotionReduced(getSendReducedEnergyConsumption());
            userData.setSetPointInverse(false);
            userData.setFunction(Function.RCU);
            send(userData);
        } catch (final UserDataScaleValueException ex) {
            LOGGER.warn("Fill user data failed.\n{}", ex);
            return;
        } catch (final NullPointerException ex) {
            LOGGER.warn("Do not send data, because a null value is not handled.", ex);
            return;
        }
    }

    private void handleIncomingData(final RadioPacket4BS packet) {
        // Send packet data as soon as possible.
        sendPacket();

        // Handle incoming user data.
        final UserDataEEPA52001FromActuator userData = new UserDataEEPA52001FromActuator(packet.getUserDataRaw());
        try {
            setRecvPosition(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getCurrentValue());
            setRecvServiceOn(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isServiceOn());
            setRecvEnergyInputEnabled(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isEnergyInputEnabled());
            setRecvEnergyStorageSufficient(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isEnergyStorageSufficientlyCharged());
            setRecvChangeBattery(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isBatteryCapacityLow());
            setRecvCoverOpen(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isCoverOpen());
            setRecvTemperatureSensorFailure(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isTemperatureSensorOutOfRange());
            setRecvWindowOpen(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isWindowOpen());
            setRecvActuatorObstructed(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.isActuatorObstructed());
            setRecvTemperature(DeviceParameterUpdatedInitiation.RADIO_PACKET, userData.getTemperature());
        } catch (final UserDataScaleValueException ex) {
            LOGGER.warn("Received position is invalid.");
        }
    }

    private void handleIncomingTeachIn(final RadioPacket4BS packet) {
        final UserData4BSTeachInVariant3 receivedUserData = new UserData4BSTeachInVariant3(packet.getUserDataRaw());

        final UserData4BSTeachInVariant3 userData = new UserData4BSTeachInVariant3();
        userData.setFunc(receivedUserData.getFunc());
        userData.setType(receivedUserData.getType());
        userData.setManufacturerId(receivedUserData.getManufacturerId());
        userData.setLearnType(LearnType4BS.WITH_EEP_NUM_WITH_MANU_ID);
        userData.setEEPResult(EEPResult.EEP_SUPPORTED);
        userData.setLearnResult(LearnResult.SENDER_ID_STORED);
        userData.setLearnStatus(LearnStatus.RESPONSE);
        userData.setTeachIn(true);
        send(userData);
    }

    protected void parseRadioPacket4BS(final RadioPacket4BS packet) {
        if (packet.isTeachIn()) {
            handleIncomingTeachIn(packet);
        } else {
            handleIncomingData(packet);
        }
    }

    @Override
    protected void fillParameters(final Set<DeviceParameter> params) {
        params.add(DeviceParameter.POSITION_PERCENT);
        params.add(DeviceParameter.TMP_RECV_SERVICE_ON);
        params.add(DeviceParameter.TMP_RECV_ENERGY_INPUT_ENABLED);
        params.add(DeviceParameter.TMP_RECV_ENERGY_STORAGE_SUFFICIENT);
        params.add(DeviceParameter.TMP_RECV_CHANGE_BATTERY);
        params.add(DeviceParameter.TMP_RECV_COVER_OPEN);
        params.add(DeviceParameter.TMP_RECV_TEMPERATURE_SENSOR_FAILURE);
        params.add(DeviceParameter.TMP_RECV_WINDOW_OPEN);
        params.add(DeviceParameter.TMP_RECV_ACTUATOR_OBSTRUCTED);
        params.add(DeviceParameter.TEMPERATURE_CELSIUS);
        params.add(DeviceParameter.SETPOINT_POSITION_PERCENT);
        params.add(DeviceParameter.SETPOINT_TEMPERATURE_CELSIUS);
        params.add(DeviceParameter.TEMPERATURE_CONTROL_CUR_TEMP);
        params.add(DeviceParameter.TMP_SEND_RUN_INIT_SEQUENCE);
        params.add(DeviceParameter.TMP_SEND_LIFT_SET);
        params.add(DeviceParameter.TMP_SEND_VALVE_OPEN);
        params.add(DeviceParameter.TMP_SEND_VALVE_CLOSED);
        params.add(DeviceParameter.TMP_SEND_REDUCED_ENERGY_CONSUMPTION);
        params.add(DeviceParameter.TEMPERATURE_CONTROL_ENABLE);
    }

    @Override
    public Object getByParameter(final DeviceParameter parameter) throws IllegalDeviceParameterException {
        switch (parameter) {
            case POSITION_PERCENT:
                return getRecvPosition();
            case TMP_RECV_SERVICE_ON:
                return getRecvServiceOn();
            case TMP_RECV_ENERGY_INPUT_ENABLED:
                return getRecvEnergyInputEnabled();
            case TMP_RECV_ENERGY_STORAGE_SUFFICIENT:
                return getRecvEnergyStorageSufficient();
            case TMP_RECV_CHANGE_BATTERY:
                return getRecvChangeBattery();
            case TMP_RECV_COVER_OPEN:
                return getRecvCoverOpen();
            case TMP_RECV_TEMPERATURE_SENSOR_FAILURE:
                return getRecvTemperatureSensorFailure();
            case TMP_RECV_WINDOW_OPEN:
                return getRecvWindowOpen();
            case TMP_RECV_ACTUATOR_OBSTRUCTED:
                return getRecvActuatorObstructed();
            case TEMPERATURE_CELSIUS:
                return getRecvTemperature();
            case SETPOINT_POSITION_PERCENT:
                return getSendSetPointPosition();
            case SETPOINT_TEMPERATURE_CELSIUS:
                return getSendSetPointTemperature();
            case TEMPERATURE_CONTROL_CUR_TEMP:
                return getSendTemperatureRoom();
            case TMP_SEND_RUN_INIT_SEQUENCE:
                return getSendRunInitSequence();
            case TMP_SEND_LIFT_SET:
                return getSendLiftSet();
            case TMP_SEND_VALVE_OPEN:
                return getSendValveOpen();
            case TMP_SEND_VALVE_CLOSED:
                return getSendValveClosed();
            case TMP_SEND_REDUCED_ENERGY_CONSUMPTION:
                return getSendReducedEnergyConsumption();
            case TEMPERATURE_CONTROL_ENABLE:
                return getSendTemperatureControlled();
            default:
                return super.getByParameter(parameter);
        }
    }

    @Override
    public void setByParameter(final DeviceParameter parameter, final Object value)
            throws IllegalDeviceParameterException {
        assert DeviceParameter.getSupportedClass(parameter).isAssignableFrom(value.getClass());
        switch (parameter) {
            case SETPOINT_POSITION_PERCENT:
                setSendSetPointPosition(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Integer) value);
                break;
            case SETPOINT_TEMPERATURE_CELSIUS:
                setSendSetPointTemperature(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Double) value);
                break;
            case TEMPERATURE_CONTROL_CUR_TEMP:
                setSendTemperatureRoom(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Double) value);
                break;
            case TMP_SEND_RUN_INIT_SEQUENCE:
                setSendRunInitSequence(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Boolean) value);
                break;
            case TMP_SEND_LIFT_SET:
                setSendLiftSet(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Boolean) value);
                break;
            case TMP_SEND_VALVE_OPEN:
                setSendValveOpen(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Boolean) value);
                break;
            case TMP_SEND_VALVE_CLOSED:
                setSendValveClosed(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Boolean) value);
                break;
            case TMP_SEND_REDUCED_ENERGY_CONSUMPTION:
                setSendReducedEnergyConsumption(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Boolean) value);
                break;
            case TEMPERATURE_CONTROL_ENABLE:
                setSendTemperatureControlled(DeviceParameterUpdatedInitiation.SET_PARAMETER, (Boolean) value);
                break;
            default:
                super.setByParameter(parameter, value);
                break;
        }
    }

}
