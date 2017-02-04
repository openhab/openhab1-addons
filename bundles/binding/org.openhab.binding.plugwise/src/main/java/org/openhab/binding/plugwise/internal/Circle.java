/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.internal;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.openhab.binding.plugwise.PlugwiseCommandType;
import org.openhab.binding.plugwise.protocol.AcknowledgeMessage;
import org.openhab.binding.plugwise.protocol.CalibrationRequestMessage;
import org.openhab.binding.plugwise.protocol.CalibrationResponseMessage;
import org.openhab.binding.plugwise.protocol.ClockGetRequestMessage;
import org.openhab.binding.plugwise.protocol.ClockGetResponseMessage;
import org.openhab.binding.plugwise.protocol.InformationRequestMessage;
import org.openhab.binding.plugwise.protocol.InformationResponseMessage;
import org.openhab.binding.plugwise.protocol.Message;
import org.openhab.binding.plugwise.protocol.PowerBufferRequestMessage;
import org.openhab.binding.plugwise.protocol.PowerBufferResponseMessage;
import org.openhab.binding.plugwise.protocol.PowerChangeRequestMessage;
import org.openhab.binding.plugwise.protocol.PowerInformationRequestMessage;
import org.openhab.binding.plugwise.protocol.PowerInformationResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that represents a Plugwise Circle device
 *
 * Circles maintain current energy usage by counting 'pulses' in a one or eight-second interval. Furthermore, they
 * store hourly energy usage as well in a buffer. Each entry in the buffer contains usage for the last 4 full hours
 * of consumption. In order to convert pulses to power (Watt) or KWh you need to apply a formula that uses some
 * calibration information.
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class Circle extends PlugwiseDevice {

    private static Logger logger = LoggerFactory.getLogger(Circle.class);

    private static final double PULSES_PER_KW_SECOND = 468.9385193;

    private static final double PULSES_PER_W_SECOND = (PULSES_PER_KW_SECOND / 1000);

    private static final int POWER_STATE_RETRIES = 3;

    private class PendingPowerStateChange {

        final boolean state;
        int retries;

        PendingPowerStateChange(boolean state) {
            this.state = state;
        }

        public String getStateAsString() {
            return state ? "ON" : "OFF";
        }
    }

    protected Stick stick;

    // Calibration data, required to calculate energy consumption
    protected boolean calibrated;
    protected double gainA;
    protected double gainB;
    protected double offsetTotal;
    protected double offsetNoise;

    // System variables as kept/maintained by the Circle hardware
    protected DateTime stamp;
    protected LocalTime systemClock;
    protected int recentLogAddress;
    protected int previousLogAddress = 0;
    protected boolean powerState;
    protected int hertz;
    protected String hardwareVersion;
    protected DateTime firmwareVersion;
    protected Energy one;
    protected Energy lastHour;

    // Pending power state changes are tracked for retries and temporarily
    // ignoring an outdated result of an InformationJob
    protected PendingPowerStateChange pendingPowerStateChange;

    public Circle(String mac, Stick stick, String friendly) {
        super(mac, DeviceType.Circle, friendly);
        this.stick = stick;
    }

    public boolean getPowerState() {
        return powerState;
    }

    public boolean setPowerState(String state) {
        if (state != null) {
            if (state.equals("ON") || state.equals("OPEN") || state.equals("UP")) {
                pendingPowerStateChange = new PendingPowerStateChange(true);
                return setPowerState(true);
            } else if (state.equals("OFF") || state.equals("CLOSED") || state.equals("DOWN")) {
                pendingPowerStateChange = new PendingPowerStateChange(false);
                return setPowerState(false);
            }
        }
        return true;
    }

    public boolean setPowerState(boolean state) {
        stick.sendPriorityMessage(new PowerChangeRequestMessage(MAC, state));
        stick.sendPriorityMessage(new InformationRequestMessage(MAC));
        return true;
    }

    public LocalTime getSystemClock() {
        if (systemClock != null) {
            return systemClock;
        } else {
            updateSystemClock();
            return null;
        }
    }

    public void updateSystemClock() {
        ClockGetRequestMessage message = new ClockGetRequestMessage(MAC);
        stick.sendMessage(message);
    }

    public void updateInformation() {
        InformationRequestMessage message = new InformationRequestMessage(MAC);
        stick.sendMessage(message);
    }

    public Stick getStick() {
        return stick;
    }

    public void calibrate() {
        CalibrationRequestMessage message = new CalibrationRequestMessage(MAC, "");
        stick.sendMessage(message);
    }

    public void updateCurrentEnergy() {
        PowerInformationRequestMessage message = new PowerInformationRequestMessage(MAC);
        stick.sendMessage(message);
    }

    public void updateEnergy(boolean completeHistory) {
        if (!completeHistory) {
            // fetch only the last available buffer
            previousLogAddress = recentLogAddress - 1;
        } else {
            previousLogAddress = 0;
        }
        while (previousLogAddress <= recentLogAddress) {
            PowerBufferRequestMessage message = new PowerBufferRequestMessage(MAC, previousLogAddress);
            previousLogAddress = previousLogAddress + 1;
            stick.sendMessage(message);
        }
    }

    public double getCurrentWatt() {
        if (one != null) {
            return pulseToWatt(one);
        } else {
            return 0;
        }
    }

    private double correctPulses(double pulses) {
        double correctedPulses = Math.pow(pulses + offsetNoise, 2) * gainB + (pulses + offsetNoise) * gainA
                + offsetTotal;
        if ((pulses > 0 && correctedPulses < 0) || (pulses < 0 && correctedPulses > 0)) {
            return 0;
        }
        return correctedPulses;
    }

    private double pulseToWatt(Energy energy) {
        double averagePulses = energy.getPulses() / energy.getInterval();
        return correctPulses(averagePulses) / PULSES_PER_W_SECOND;
    }

    private double pulseTokWh(Energy energy) {
        return pulseToWatt(energy) * energy.getInterval() / (3600 * 1000);
    }

    @Override
    public boolean processMessage(Message message) {
        if (message != null) {

            switch (message.getType()) {
                case CLOCK_GET_RESPONSE:

                    systemClock = ((ClockGetResponseMessage) message).getTime();

                    DateTimeFormatter sc = DateTimeFormat.forPattern("HH:mm:ss");
                    postUpdate(MAC, PlugwiseCommandType.CURRENTCLOCK, sc.print(systemClock));

                    return true;

                case DEVICE_CALIBRATION_RESPONSE:

                    gainA = ((CalibrationResponseMessage) message).getGainA();
                    gainB = ((CalibrationResponseMessage) message).getGainB();
                    offsetTotal = ((CalibrationResponseMessage) message).getOffsetTotal();
                    offsetNoise = ((CalibrationResponseMessage) message).getOffsetNoise();
                    calibrated = true;

                    return true;

                case DEVICE_INFORMATION_RESPONSE:

                    stamp = new DateTime(((InformationResponseMessage) message).getYear(),
                            ((InformationResponseMessage) message).getMonth(), 1, 0, 0)
                                    .plusMinutes(((InformationResponseMessage) message).getMinutes());
                    recentLogAddress = ((InformationResponseMessage) message).getLogAddress();
                    powerState = ((InformationResponseMessage) message).getPowerState();
                    hertz = ((InformationResponseMessage) message).getHertz();
                    hardwareVersion = ((InformationResponseMessage) message).getHardwareVersion();

                    if (pendingPowerStateChange != null) {
                        if (powerState == pendingPowerStateChange.state) {
                            pendingPowerStateChange = null;
                        } else {
                            // power state change message may be lost or an InformationJob may have queried the power
                            // state just before the power state change message arrived
                            if (pendingPowerStateChange.retries < POWER_STATE_RETRIES) {
                                pendingPowerStateChange.retries++;
                                logger.warn("Retrying to switch {} {} {} (retry #{})", type.name().toString(),
                                        this.getName(), pendingPowerStateChange.getStateAsString(),
                                        pendingPowerStateChange.retries);
                                setPowerState(pendingPowerStateChange.state);
                            } else {
                                logger.warn("Failed to switch {} {} {} after {} retries", type.name().toString(),
                                        this.getName(), pendingPowerStateChange.getStateAsString(),
                                        pendingPowerStateChange.retries);
                                pendingPowerStateChange = null;
                            }
                        }
                    }

                    if (pendingPowerStateChange == null) {
                        postUpdate(MAC, PlugwiseCommandType.CURRENTSTATE, powerState);
                    }

                    if (lastHour == null) {
                        updateEnergy(false);
                    }

                    return true;

                case POWER_INFORMATION_RESPONSE:

                    if (!calibrated) {
                        logger.debug(
                                "{} with name: {} and MAC address: {} received power information without "
                                        + "being calibrated, calibrating and skipping response",
                                getType().name(), name, MAC);
                        calibrate();
                        return true;
                    }
                    one = ((PowerInformationResponseMessage) message).getOneSecond();
                    double watt = pulseToWatt(one);
                    if (watt > 10000) {
                        logger.debug("{} with name: {} and MAC address: {} is in a kind of error state, "
                                + "skipping power information response", type.name(), name, MAC);
                        return true;
                    }
                    postUpdate(MAC, PlugwiseCommandType.CURRENTPOWER, watt);
                    postUpdate(MAC, PlugwiseCommandType.CURRENTPOWERSTAMP, one.getTime());
                    return true;

                case POWER_BUFFER_RESPONSE:

                    if (!calibrated) {
                        return true;
                    }

                    // get the most recent energy consumption
                    Energy mostRecentEnergy = null;
                    for (int i = 0; i < 4; i++) {
                        Energy energy = ((PowerBufferResponseMessage) message).getEnergy(i);
                        if (energy != null) {
                            mostRecentEnergy = energy;
                        }
                    }

                    if (mostRecentEnergy != null) {
                        // when the current time is '11:44:55.888', the last hour energy has time '10:00:00.000'
                        boolean isLastHour = mostRecentEnergy.getTime().isAfter(DateTime.now().minusHours(2));
                        if (isLastHour) {
                            lastHour = mostRecentEnergy;
                            postUpdate(MAC, PlugwiseCommandType.LASTHOURCONSUMPTION, pulseTokWh(lastHour));
                            postUpdate(MAC, PlugwiseCommandType.LASTHOURCONSUMPTIONSTAMP, lastHour.getTime());
                        }
                    }

                    return true;

                case ACKNOWLEDGEMENT:
                    if (((AcknowledgeMessage) message).isExtended()) {
                        switch (((AcknowledgeMessage) message).getExtensionCode()) {

                            case ON:
                                postUpdate(MAC, PlugwiseCommandType.CURRENTSTATE,
                                        ((AcknowledgeMessage) message).isOn());
                                break;

                            case OFF:
                                postUpdate(MAC, PlugwiseCommandType.CURRENTSTATE,
                                        ((AcknowledgeMessage) message).isOff());
                                break;

                            default:
                                return stick.processMessage(message);
                        }
                    }
                    return true;

                default:
                    // Let's have the Stick a go at this message
                    return stick.processMessage(message);
            }

        } else {
            return false;
        }
    }

    @Override
    public boolean postUpdate(String MAC, PlugwiseCommandType type, Object value) {
        if (MAC != null && type != null && value != null) {
            stick.postUpdate(MAC, type, value);
            return true;
        } else {
            return false;
        }

    }

}
