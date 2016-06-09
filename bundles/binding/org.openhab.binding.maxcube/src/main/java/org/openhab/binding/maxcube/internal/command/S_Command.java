/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal.command;

import org.apache.commons.codec.binary.Base64;
import org.openhab.binding.maxcube.internal.Utils;
import org.openhab.binding.maxcube.internal.message.ThermostatModeType;

/**
 * {@link S_Command} for setting MAX! thermostat temperature & mode.
 *
 * @author Andreas Heil (info@aheil.de)
 * @author Marcel Verpaalen - OH2 update + simplification
 * @since 1.4.0
 */
public class S_Command extends CubeCommand {

    private String baseStringS = "000040000000"; // for single devices
    private String baseStringG = "000440000000"; // for group/room devices

    private boolean[] bits = null;

    private String rfAddress = null;
    private int roomId = -1;

    /**
     * Creates a new instance of the MAX! protocol S command.
     *
     * @param rfAddress
     *            the RF address the command is for
     * @param roomId
     *            the room ID the RF address is mapped to
     * @param setpointTemperature
     *            the desired setpoint temperature for the device.
     */
    public S_Command(String rfAddress, int roomId, ThermostatModeType mode, double setpointTemperature) {
        this.rfAddress = rfAddress;
        this.roomId = roomId;

        // Temperature setpoint, Temp uses 6 bits (bit 0:5),
        // 20 deg C = bits 101000 = dec 40/2 = 20 deg C,
        // you need 8 bits to send so add the 2 bits below (sample 10101000 = hex A8)
        // bit 0,1 = 00 = Auto weekprog (no temp is needed)

        int setpointValue = (int) (setpointTemperature * 2);
        bits = Utils.getBits(setpointValue);

        // default to perm setting
        // AB => bit mapping
        // 01 = Permanent
        // 10 = Temporarily
        // 11 = Boost

        switch (mode) {
            case MANUAL:
                bits[7] = false; // A (MSB)
                bits[6] = true; // B
                break;
            case AUTOMATIC:
                bits[7] = false; // A (MSB)
                bits[6] = false; // B
                break;
            case BOOST:
                bits[7] = true; // A (MSB)
                bits[6] = true; // B
                break;
            case VACATION:
                // not implemented needs time
            default:
                // no further modes supported
        }
    }

    /**
     * Returns the Base64 encoded command string to be sent via the MAX!
     * protocol.
     *
     * @return the string representing the command
     */
    @Override
    public String getCommandString() {

        String baseString = "";
        if (roomId == 0) {
            baseString = baseStringS;
        } else {
            baseString = baseStringG;
        }

        String commandString = baseString + rfAddress + Utils.toHex(roomId) + Utils.toHex(bits);

        String encodedString = Base64.encodeBase64String(Utils.hexStringToByteArray(commandString));

        return "s:" + encodedString + '\r' + '\n';
    }

    @Override
    public String getReturnStrings() {
        return "S:";
    }
}
